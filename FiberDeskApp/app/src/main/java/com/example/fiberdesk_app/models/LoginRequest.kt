package com.example.fiberdesk_app.models

import com.google.gson.annotations.SerializedName
data class LoginRequest(
    val correo: String,
    // En el JSON la clave es "contraseña" (ñ) — aquí la mapeamos a 'contrasena'
    @SerializedName("contraseña")
    val contrasena: String
)
