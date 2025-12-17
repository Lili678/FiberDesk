package com.example.fiberdesk_app.models
import com.google.gson.annotations.SerializedName
data class Usuario(
    val _id: String,
    val correo: String,
    val nombre: String,
    val fechaRegistro: String?,
    val activo: Boolean
)
