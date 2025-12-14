package com.example.fiberdesk_app

import android.app.Application
import android.util.Log
import com.example.fiberdesk_app.network.NetworkConfig
import com.example.fiberdesk_app.network.NetworkPreferences
import com.example.fiberdesk_app.network.ServerDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FiberDeskApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Inicializar NetworkConfig con el contexto de la aplicación
        NetworkConfig.initialize(this)
        
        // Si está habilitada la detección automática, intentar detectar el servidor
        if (NetworkPreferences.isAutoDetectEnabled(this)) {
            detectServerOnStartup()
        }
    }
    
    private fun detectServerOnStartup() {
        // Solo intentar si estamos conectados a WiFi
        if (!ServerDetector.isConnectedToWiFi(this)) {
            Log.d("FiberDeskApp", "No hay conexión WiFi, usando configuración por defecto")
            return
        }
        
        // Intentar detectar el servidor en segundo plano
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val port = NetworkPreferences.getServerPort(this@FiberDeskApplication)
                val detectedIP = ServerDetector.detectServerIP(this@FiberDeskApplication, port)
                
                if (detectedIP != null) {
                    Log.d("FiberDeskApp", "Servidor detectado automáticamente en: $detectedIP")
                    NetworkConfig.updateServerIP(detectedIP)
                } else {
                    Log.d("FiberDeskApp", "No se pudo detectar el servidor automáticamente")
                }
            } catch (e: Exception) {
                Log.e("FiberDeskApp", "Error al detectar servidor", e)
            }
        }
    }
}
