package com.example.fiberdesk_app.utils.extensions

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

// Extensión para mostrar/ocultar vistas
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

// Extensión para mostrar Toast desde Fragment
fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

// Extensión para formatear números como moneda
fun Double.toMoneyFormat(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(this)
}

fun Float.toMoneyFormat(): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "MX"))
    return format.format(this)
}

// Extensión para formatear fechas
fun String.toDisplayDate(): String {
    return try {
        val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdfISO.parse(this)
        sdfDisplay.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

fun String.toISODate(): String {
    return try {
        val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdfISO.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdfDisplay.parse(this)
        sdfISO.format(date ?: Date())
    } catch (e: Exception) {
        this
    }
}

// Extensión para validar strings
fun String?.isNotNullOrBlank(): Boolean {
    return !this.isNullOrBlank()
}

// Extensión para convertir String a Double de forma segura
fun String?.toDoubleOrZero(): Double {
    return this?.toDoubleOrNull() ?: 0.0
}


class extensions {
}