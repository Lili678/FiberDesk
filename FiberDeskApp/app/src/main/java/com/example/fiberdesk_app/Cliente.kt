package com.example.fiberdesk_app

import java.io.Serializable

data class Cliente(
    val nombre: String,
    val apellidos: String,
    val telefono: String,
    val correo: String,
    // Dirección
    val calle: String,
    val numExterior: String,
    val numInterior: String,
    val colonia: String,
    val municipio: String,
    val estado: String,
    val cp: String,
    // Coordenadas
    val latitud: Double,
    val longitud: Double
) : Serializable