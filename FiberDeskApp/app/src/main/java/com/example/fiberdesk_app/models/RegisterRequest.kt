package com.example.fiberdesk_app.models

import com.google.gson.annotations.SerializedName
data class RegisterRequest(
    val correo: String,
    @SerializedName("contraseña")
    val contrasena: String,
    val nombre: String
)
