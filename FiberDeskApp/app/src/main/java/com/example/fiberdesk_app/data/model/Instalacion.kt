package com.example.fiberdesk_app.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Instalacion(
    @SerializedName("_id")
    val _id: String? = null,
    val cliente: String,
    val direccion: String,
    val estado: String = "pendiente", // pendiente, en_progreso, completada
    val materialesUsados: List<MaterialUsado> = emptyList(),
    @SerializedName("fechaCreacion")
    val fechaCreacion: String? = null,
    @SerializedName("fechaInicio")
    val fechaInicio: String? = null,
    @SerializedName("fechaCompletado")
    val fechaCompletado: String? = null
) : Parcelable

@Parcelize
data class MaterialUsado(
    val material: Material?,
    val cantidad: Int
) : Parcelable

