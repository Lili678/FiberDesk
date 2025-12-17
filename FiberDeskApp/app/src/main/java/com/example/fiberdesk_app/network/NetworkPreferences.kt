package com.example.fiberdesk_app.network

import android.content.Context
import android.content.SharedPreferences

object NetworkPreferences {
    private const val PREFS_NAME = "network_config"
    private const val KEY_SERVER_IP = "server_ip"
    private const val KEY_SERVER_PORT = "server_port"
    private const val KEY_AUTO_DETECT = "auto_detect"
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveServerIP(context: Context, ip: String) {
        getPrefs(context).edit().putString(KEY_SERVER_IP, ip).apply()
    }
    
    fun getServerIP(context: Context): String? {
        return getPrefs(context).getString(KEY_SERVER_IP, null)
    }
    
    fun saveServerPort(context: Context, port: String) {
        getPrefs(context).edit().putString(KEY_SERVER_PORT, port).apply()
    }
    
    fun getServerPort(context: Context): String {
        return getPrefs(context).getString(KEY_SERVER_PORT, "3000") ?: "3000"
    }
    
    fun setAutoDetect(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_AUTO_DETECT, enabled).apply()
    }
    
    fun isAutoDetectEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_AUTO_DETECT, true)
    }
    
    fun clearConfig(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
