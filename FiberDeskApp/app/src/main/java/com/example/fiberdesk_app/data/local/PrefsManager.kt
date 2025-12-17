package com.example.fiberdesk_app.data.local

import android.content.Context

class PrefsManager(context: Context) {
    private val prefs = context.getSharedPreferences("fiberdesk_prefs", Context.MODE_PRIVATE)

    var lastSync: Long
        get() = prefs.getLong("last_sync", 0L)
        set(value) = prefs.edit().putLong("last_sync", value).apply()

    var useDarkTheme: Boolean
        get() = prefs.getBoolean("use_dark", false)
        set(value) = prefs.edit().putBoolean("use_dark", value).apply()

    fun clearAll() = prefs.edit().clear().apply()
}
