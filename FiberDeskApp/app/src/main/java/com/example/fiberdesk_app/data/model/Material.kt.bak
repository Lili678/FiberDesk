package com.example.fiberdesk_app.data.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Material(
    @SerializedName("_id")
    val _id: String? = null,
    val nombre: String,
    val cantidad: Int,
    val descripcion: String? = null,
    @SerializedName("fechaRegistro")
    val fechaRegistro: String? = null
) {
    // Formato legible de la fecha
    fun formatFecha(): String {
        return if (fechaRegistro != null) {
            try {
                val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
                val date = isoFormat.parse(fechaRegistro.substring(0, minOf(19, fechaRegistro.length)))
                val displayFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                displayFormat.format(date ?: Date())
            } catch (e: Exception) {
                fechaRegistro
            }
        } else {
            ""
        }
    }
}
