package com.example.fiberdesk_app.models

import com.google.gson.annotations.SerializedName

data class Cliente(
    @SerializedName("_id") val id: String? = null,
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val correo: String?,
    val direccion: Direccion?
)

data class Direccion(
    val calle: String,
    val latitud: Double?,
    val longitud: Double?
)