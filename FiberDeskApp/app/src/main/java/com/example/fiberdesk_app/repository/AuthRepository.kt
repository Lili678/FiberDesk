package com.example.fiberdesk_app.repository

import com.example.fiberdesk_app.models.LoginRequest
import com.example.fiberdesk_app.models.LoginResponse
import com.example.fiberdesk_app.models.RegisterRequest
import com.example.fiberdesk_app.network.ApiClient

class AuthRepository {
    suspend fun login(correo: String, contrasena: String): LoginResponse? {
        return try {
            val request = LoginRequest(correo, contrasena)
            val response = ApiClient.apiService.login(request)
            
            if (response.isSuccessful) {
                response.body()
            } else {
                LoginResponse(
                    success = false,
                    message = "Error: ${response.code()} - ${response.message()}",
                    data = null
                )
            }
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Error de conexión: ${e.message}",
                data = null
            )
        }
    }

    suspend fun registro(correo: String, contraseña: String, nombre: String): LoginResponse? {
        return try {
            val request = RegisterRequest(correo, contraseña, nombre)
            val response = ApiClient.apiService.registro(request)
            
            if (response.isSuccessful) {
                response.body()
            } else {
                LoginResponse(
                    success = false,
                    message = "Error: ${response.code()} - ${response.message()}",
                    data = null
                )
            }
        } catch (e: Exception) {
            LoginResponse(
                success = false,
                message = "Error de conexión: ${e.message}",
                data = null
            )
        }
    }
}
