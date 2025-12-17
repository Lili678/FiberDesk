package com.example.fiberdesk_app.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.fiberdesk_app.BuildConfig

object NetworkConfig {
    
    private var context: Context? = null
    private var cachedBaseUrl: String? = null
    
    /**
     * Inicializa el NetworkConfig con el contexto de la aplicación
     */
    fun initialize(appContext: Context) {
        context = appContext.applicationContext
    }
    
    /**
     * Detecta si la app está corriendo en un emulador de Android
     */
    private fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk" == Build.PRODUCT)
    }
    
    /**
     * Detecta si el dispositivo está conectado por datos móviles
     */
    private fun isConnectedToMobileData(): Boolean {
        val ctx = context ?: return false
        try {
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } catch (e: Exception) {
            Log.e("NetworkConfig", "Error detectando datos móviles", e)
            return false
        }
    }
    
    /**
     * Detecta si el dispositivo está conectado por WiFi
     */
    private fun isConnectedToWiFi(): Boolean {
        val ctx = context ?: return false
        try {
            val connectivityManager = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } catch (e: Exception) {
            Log.e("NetworkConfig", "Error detectando WiFi", e)
            return false
        }
    }
    
    /**
     * Obtiene la URL base configurada con detección inteligente
     * 
     * LÓGICA:
     * 1. Datos móviles → Servidor remoto (si está configurado)
     * 2. WiFi → Servidor local
     * 3. Emulador → 10.0.2.2
     * 4. Manual → Lo que el usuario configure
     */
    fun getBaseUrl(): String {
        val ctx = context
        val isEmulatorDevice = isEmulator()
        val isMobileData = isConnectedToMobileData()
        val isWiFi = isConnectedToWiFi()
        
        // Obtener configuraciones guardadas
        val savedIP = ctx?.let { NetworkPreferences.getServerIP(it) }
        val remoteUrl = ctx?.let { NetworkPreferences.getRemoteUrl(it) }
        val useRemoteOnMobile = ctx?.let { NetworkPreferences.isUseRemoteOnMobileEnabled(it) } ?: true
        val port = ctx?.let { NetworkPreferences.getServerPort(it) } ?: BuildConfig.API_PORT
        val portInt = port.toIntOrNull() ?: 3000
        
        // ====== LÓGICA DE SELECCIÓN INTELIGENTE ======
        val url = when {
            // 1. DATOS MÓVILES + URL REMOTA CONFIGURADA
            isMobileData && useRemoteOnMobile && !remoteUrl.isNullOrEmpty() -> {
                Log.d("NetworkConfig", "📱 DATOS MÓVILES detectados → Usando servidor remoto")
                formatRemoteUrl(remoteUrl)
            }
            
            // 2. IP/URL MANUAL CONFIGURADA POR USUARIO (tiene prioridad)
            !savedIP.isNullOrEmpty() -> {
                if (savedIP.startsWith("http://") || savedIP.startsWith("https://")) {
                    Log.d("NetworkConfig", "🌐 Usando URL configurada manualmente: $savedIP")
                    formatRemoteUrl(savedIP)
                } else {
                    Log.d("NetworkConfig", "🏠 Usando IP local configurada: $savedIP")
                    val protocol = if (portInt == 443) "https" else "http"
                    val portSuffix = if (portInt == 80 || portInt == 443) "" else ":$port"
                    "$protocol://$savedIP$portSuffix/api/"
                }
            }
            
            // 3. EMULADOR
            isEmulatorDevice -> {
                Log.d("NetworkConfig", "🖥️ Emulador detectado → usando 10.0.2.2")
                "http://10.0.2.2:$port/api/"
            }
            
            // 4. WIFI (por defecto, usar IP local del BuildConfig)
            else -> {
                Log.d("NetworkConfig", "📶 WiFi → Usando IP local por defecto: ${BuildConfig.LOCAL_IP}")
                "http://${BuildConfig.LOCAL_IP}:$port/api/"
            }
        }
        
        // Log informativo
        val connectionType = when {
            isMobileData -> "DATOS MÓVILES 📱"
            isWiFi -> "WiFi 📶"
            else -> "DESCONOCIDA ❓"
        }
        
        Log.d("NetworkConfig", "=========================================")
        Log.d("NetworkConfig", "📡 Tipo de conexión: $connectionType")
        Log.d("NetworkConfig", "🖥️  Entorno: ${if (isEmulatorDevice) "EMULADOR" else "DISPOSITIVO FÍSICO"}")
        Log.d("NetworkConfig", "🔗 URL seleccionada: $url")
        Log.d("NetworkConfig", "=========================================")
        
        cachedBaseUrl = url
        return url
    }
    
    /**
     * Formatea una URL remota para asegurar que termine correctamente en /api/
     */
    private fun formatRemoteUrl(url: String): String {
        return when {
            url.endsWith("/api/") -> url
            url.endsWith("/api") -> "$url/"
            url.endsWith("/") -> "${url}api/"
            else -> "$url/api/"
        }
    }
    
    /**
     * Invalida el caché de URL para forzar recarga en la próxima llamada
     */
    fun invalidateCache() {
        cachedBaseUrl = null
        Log.d("NetworkConfig", "Caché de URL invalidado")
    }
    
    /**
     * Actualiza la IP del servidor y recarga la configuración
     */
    fun updateServerIP(newIP: String) {
        context?.let {
            NetworkPreferences.saveServerIP(it, newIP)
            invalidateCache()
            Log.d("NetworkConfig", "IP del servidor actualizada a: $newIP")
            // Notificar a ApiClient para que actualice también
            try {
                val apiClientClass = Class.forName("com.example.fiberdesk_app.network.ApiClient")
                val refreshMethod = apiClientClass.getDeclaredMethod("refreshBaseUrl")
                val apiClientInstance = apiClientClass.getDeclaredField("INSTANCE")
                apiClientInstance.isAccessible = true
                val instance = apiClientInstance.get(null)
                refreshMethod.invoke(instance)
            } catch (e: Exception) {
                Log.e("NetworkConfig", "Error al refrescar ApiClient", e)
            }
        }
    }
}
