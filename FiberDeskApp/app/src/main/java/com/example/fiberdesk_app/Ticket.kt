package com.example.fiberdesk_app

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ticket(
    val folio: String,
    val cliente: String,
    val prioridad: String,
    val asunto: String,
    val tecnico: String,
    val creadoPor: String,
    val estado: String,
    val fecha: String,
    val descripcion: String,
    val archivado: Boolean = false
) : Parcelable
