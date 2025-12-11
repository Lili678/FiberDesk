package com.example.fiberdesk_app.data.model

import com.google.gson.annotations.SerializedName

// Data class para Pago que coincide con el schema del backend
data class Pago(
    @SerializedName("_id")
    val _id: String? = null,
    
    @SerializedName("usuarioId")
    val usuarioId: String,
    
    @SerializedName("monto")
    val monto: Double,
    
    @SerializedName("abono")
    val abono: Double = 0.0,
    
    @SerializedName("metodoPago")
    val metodoPago: String, // efectivo, transferencia, tarjeta, cheque
    
    @SerializedName("fechaPago")
    val fechaPago: String, // ISO 8601 format
    
    @SerializedName("descripcion")
    val descripcion: String = "",
    
    @SerializedName("estado")
    val estado: String = "pendiente", // pendiente, pagado, parcial
    
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null
)

// Response del API cuando se crea un pago
data class PagoResponse(
    @SerializedName("mensaje")
    val mensaje: String,
    
    @SerializedName("pago")
    val pago: Pago
)

// Request para crear un pago
data class CrearPagoRequest(
    @SerializedName("usuarioId")
    val usuarioId: String,
    
    @SerializedName("monto")
    val monto: Double,
    
    @SerializedName("abono")
    val abono: Double,
    
    @SerializedName("metodoPago")
    val metodoPago: String,
    
    @SerializedName("fechaPago")
    val fechaPago: String,
    
    @SerializedName("descripcion")
    val descripcion: String
)

// Request para actualizar un pago
data class ActualizarPagoRequest(
    @SerializedName("monto")
    val monto: Double? = null,
    
    @SerializedName("abono")
    val abono: Double? = null,
    
    @SerializedName("metodoPago")
    val metodoPago: String? = null,
    
    @SerializedName("estado")
    val estado: String? = null,
    
    @SerializedName("descripcion")
    val descripcion: String? = null
)

// Enum para métodos de pago
enum class MetodoPago(val valor: String, val displayName: String) {
    EFECTIVO("efectivo", "Efectivo"),
    TRANSFERENCIA("transferencia", "Transferencia"),
    TARJETA("tarjeta", "Tarjeta"),
    CHEQUE("cheque", "Cheque");
    
    companion object {
        fun fromValor(valor: String): MetodoPago? {
            return values().find { it.valor == valor }
        }
    }
}

// Enum para estados de pago
enum class EstadoPago(val valor: String, val displayName: String) {
    PENDIENTE("pendiente", "Pendiente"),
    PAGADO("pagado", "Pagado"),
    PARCIAL("parcial", "Parcial");
    
    companion object {
        fun fromValor(valor: String): EstadoPago? {
            return values().find { it.valor == valor }
        }
    }
}