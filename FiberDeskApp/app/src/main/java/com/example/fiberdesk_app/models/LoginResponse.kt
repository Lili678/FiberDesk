package com.example.fiberdesk_app.models
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val data: UsuarioData?
)
data class UsuarioData(
    val _id: String,
    val correo: String,
    val nombre: String,
    val token: String
)
