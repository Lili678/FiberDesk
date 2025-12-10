package com.example.fiberdesk_app.utils.constants

object Constants {
    // URL base del API - CAMBIAR POR TU IP LOCAL O SERVIDOR
    const val BASE_URL = "http://10.0.2.2:3000/" // Para emulador Android
    // const val BASE_URL = "http://192.168.1.X:3000/" // Para dispositivo físico (reemplazar X con tu IP)
    // const val BASE_URL = "https://tu-servidor.com/" // Para producción
    
    // Timeouts
    const val CONNECT_TIMEOUT = 30L
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L
    
    // SharedPreferences
    const val PREFS_NAME = "FiberDeskPrefs"
    const val KEY_USER_ID = "userId"
    const val KEY_USER_TOKEN = "userToken"
    
    // Request codes
    const val REQUEST_CODE_CREAR_PAGO = 1001
    const val REQUEST_CODE_EDITAR_PAGO = 1002
}