package com.example.fiberdesk_app.data.model

data class Instalacion(
    val _id: String? = null,
    val cliente: String,
    val direccion: String,
    val estado: String = "pendiente", // pendiente, en_progreso, completada
    val materialesUsados: List<MaterialUsado>? = null,
    val fechaCreacion: String? = null,
    val fechaActualizacion: String? = null,
    val fechaCompletado: String? = null
)

data class MaterialUsado(
    val material: Material?,
    val cantidad: Int
)
