package com.example.fiberdesk_app.network

import android.os.Build
import com.example.fiberdesk_app.BuildConfig

object NetworkConfig {
    
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
     * Obtiene la URL base configurada automáticamente según el entorno
     */
    fun getBaseUrl(): String {
        // Si BASE_URL no es "AUTO", usar el valor configurado
        if (BuildConfig.BASE_URL != "AUTO") {
            return BuildConfig.BASE_URL
        }
        
        // Detección automática: Emulador vs Dispositivo Físico
        val ip = if (isEmulator()) {
            "10.0.2.2" // IP especial para acceder al host desde el emulador
        } else {
            BuildConfig.LOCAL_IP // IP local del PC donde corre el backend
        }
        
        return "http://$ip:${BuildConfig.API_PORT}/api/"
    }
}
