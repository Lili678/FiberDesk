package com.example.fiberdesk_app.network

import android.content.Context
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
     * Obtiene la URL base configurada
     * Prioridad: 1) IP guardada por usuario, 2) IP por defecto según entorno
     */
    fun getBaseUrl(): String {
        // Si ya tenemos una URL en caché, usarla
        cachedBaseUrl?.let { return it }
        
        val ctx = context
        val isEmulatorDevice = isEmulator()
        
        // Obtener configuración del usuario si está disponible
        val savedIP = if (ctx != null) {
            NetworkPreferences.getServerIP(ctx)
        } else {
            null
        }
        
        val port = if (ctx != null) {
            NetworkPreferences.getServerPort(ctx)
        } else {
            BuildConfig.API_PORT
        }
        
        val ip = when {
            // Si hay una IP guardada por el usuario, usarla
            !savedIP.isNullOrEmpty() -> {
                Log.d("NetworkConfig", "Usando IP configurada por el usuario: $savedIP")
                savedIP
            }
            // Si es emulador, usar IP especial
            isEmulatorDevice -> {
                Log.d("NetworkConfig", "Emulador detectado, usando 10.0.2.2")
                "10.0.2.2"
            }
            // Para dispositivos físicos, usar IP por defecto del BuildConfig
            else -> {
                Log.d("NetworkConfig", "Dispositivo físico, usando IP por defecto: ${BuildConfig.LOCAL_IP}")
                BuildConfig.LOCAL_IP
            }
        }
        
        val url = "http://$ip:$port/api/"
        Log.d("NetworkConfig", "Entorno: ${if (isEmulatorDevice) "EMULADOR" else "DISPOSITIVO FÍSICO"}")
        Log.d("NetworkConfig", "URL base configurada: $url")
        
        cachedBaseUrl = url
        return url
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
