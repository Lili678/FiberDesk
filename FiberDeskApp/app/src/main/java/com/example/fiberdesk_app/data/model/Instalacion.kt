package com.example.fiberdesk_app.data.model

import com.google.gson.annotations.SerializedName

data class Instalacion(
    @SerializedName("_id")
    val _id: String? = null,
    val cliente: String,
    val direccion: String,
    val estado: String = "pendiente", // pendiente, en_progreso, completada
    val materialesUsados: List<MaterialUsado> = emptyList(),
    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,
    @SerializedName("fechaCompletado")
    val fechaCompletado: String? = null
)

data class MaterialUsado(
    val material: Material?,
    val cantidad: Int
)

