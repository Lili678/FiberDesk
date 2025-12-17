package com.example.fiberdesk_app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.util.concurrent.TimeUnit

object ServerDetector {
    private const val TAG = "ServerDetector"
    private const val TIMEOUT_MS = 2000
    
    /**
     * Obtiene la IP local del dispositivo en la red WiFi
     */
    fun getLocalIPAddress(context: Context): String? {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipAddress = wifiInfo.ipAddress
            
            if (ipAddress != 0) {
                return String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff,
                    ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff,
                    ipAddress shr 24 and 0xff
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error obteniendo IP local", e)
        }
        return null
    }
    
    /**
     * Extrae el prefijo de red de una IP (ejemplo: 192.168.1.x -> 192.168.1)
     */
    private fun getNetworkPrefix(ip: String): String? {
        val parts = ip.split(".")
        return if (parts.size == 4) {
            "${parts[0]}.${parts[1]}.${parts[2]}"
        } else null
    }
    
    /**
     * Verifica si el servidor está disponible en una IP específica
     */
    private suspend fun checkServerAt(ip: String, port: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("http://$ip:$port/api/health")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            connection.requestMethod = "GET"
            
            val responseCode = connection.responseCode
            connection.disconnect()
            
            val isAvailable = responseCode == 200 // Solo respuesta exitosa
            if (isAvailable) {
                Log.d(TAG, "Servidor encontrado en: http://$ip:$port")
            }
            return@withContext isAvailable
        } catch (e: Exception) {
            Log.d(TAG, "No se pudo conectar a $ip:$port - ${e.message}")
            return@withContext false
        }
    }
    
    /**
     * Detecta automáticamente la IP del servidor en la red local
     */
    suspend fun detectServerIP(context: Context, port: String = "3000"): String? = withContext(Dispatchers.IO) {
        Log.d(TAG, "Iniciando detección automática de servidor...")
        
        // Obtener IP local del dispositivo
        val localIP = getLocalIPAddress(context)
        if (localIP == null) {
            Log.e(TAG, "No se pudo obtener la IP local del dispositivo")
            return@withContext null
        }
        
        Log.d(TAG, "IP local del dispositivo: $localIP")
        val networkPrefix = getNetworkPrefix(localIP)
        
        if (networkPrefix == null) {
            Log.e(TAG, "No se pudo determinar el prefijo de red")
            return@withContext null
        }
        
        // Lista de IPs comunes para probar primero
        val commonIPs = listOf(
            "$networkPrefix.1",   // Gateway/Router común
            "$networkPrefix.100", // IP común para PCs
            "$networkPrefix.101",
            "$networkPrefix.2",
            "$networkPrefix.10",
            "$networkPrefix.254"
        )
        
        // Probar IPs comunes primero
        Log.d(TAG, "Probando IPs comunes en la red $networkPrefix.x")
        for (ip in commonIPs) {
            if (checkServerAt(ip, port)) {
                Log.d(TAG, "¡Servidor detectado en IP común: $ip!")
                return@withContext ip
            }
        }
        
        // Si no se encuentra en IPs comunes, escanear rango completo (opcional)
        Log.d(TAG, "Servidor no encontrado en IPs comunes. Escaneando rango completo...")
        val scanJobs = (1..254).map { i ->
            async {
                val ip = "$networkPrefix.$i"
                if (checkServerAt(ip, port)) ip else null
            }
        }
        
        val results = scanJobs.awaitAll().filterNotNull()
        
        return@withContext if (results.isNotEmpty()) {
            Log.d(TAG, "¡Servidor detectado en: ${results.first()}!")
            results.first()
        } else {
            Log.e(TAG, "No se pudo detectar el servidor en la red local")
            null
        }
    }
    
    /**
     * Verifica si hay conexión WiFi activa
     */
    fun isConnectedToWiFi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
    }
}
