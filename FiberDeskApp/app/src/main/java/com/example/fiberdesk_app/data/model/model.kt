package com.example.fiberdesk_app.data.model

// Data class para Pago que coincide con el schema del backend
data class Pago(
    val _id: String? = null,
    val usuarioId: String,
    val monto: Double,
    val abono: Double = 0.0,
    val metodoPago: String, // efectivo, transferencia, tarjeta, cheque
    val fechaPago: String, // ISO 8601 format
    val descripcion: String = "",
    val estado: String = "pendiente", // pendiente, pagado, parcial
    val prioridad: String = "medio", // bajo, medio, alto, urgente
    val createdAt: String? = null,
    val updatedAt: String? = null
)

// Response del API cuando se crea un pago
data class PagoResponse(
    val mensaje: String,
    val pago: Pago
)

// Request para crear un pago
data class CrearPagoRequest(
    val usuarioId: String,
    val monto: Double,
    val abono: Double,
    val metodoPago: String,
    val fechaPago: String,
    val descripcion: String,
    val prioridad: String = "medio"
)

// Request para actualizar un pago
data class ActualizarPagoRequest(
    val monto: Double? = null,
    val abono: Double? = null,
    val metodoPago: String? = null,
    val estado: String? = null,
    val descripcion: String? = null,
    val prioridad: String? = null
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

// Enum para prioridades de pago
enum class Prioridad(val valor: String, val displayName: String, val color: String) {
    BAJO("bajo", "Bajo", "#4CAF50"),
    MEDIO("medio", "Medio", "#FFB300"),
    ALTO("alto", "Alto", "#FF6F00"),
    URGENTE("urgente", "Urgente", "#D32F2F");
    
    companion object {
        fun fromValor(valor: String): Prioridad? {
            return values().find { it.valor == valor }
        }
    }
}