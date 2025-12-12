package com.example.fiberdesk_app.data.model

data class Instalacion(
    val _id: String? = null,
    val clienteId: String,
    val direccion: String,
    val estado: String = "Pendiente", // Pendiente, En Progreso, Completada
    val materiales: List<MaterialUsado> = emptyList(),
    val fecha: String? = null,
    val tecnicoAsignado: String? = null,
    val notas: String? = null
)

data class MaterialUsado(
    val materialId: String,
    val cantidad: Int,
    val nombre: String? = null
)
