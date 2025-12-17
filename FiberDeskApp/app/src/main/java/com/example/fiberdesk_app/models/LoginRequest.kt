package com.example.fiberdesk_app.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val correo: String,
    @SerializedName("contraseña")
    val contraseña: String
)
