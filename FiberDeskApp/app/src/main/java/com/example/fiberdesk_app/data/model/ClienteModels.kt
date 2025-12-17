package com.example.fiberdesk_app.data.model

import java.io.Serializable

// Modelo principal del Cliente (coincide con el backend)
data class ClienteModel(
    val _id: String? = null,
    val Name: Name,
    val LastName: LastName,
    val PhoneNumber: List<String>,
    val Email: String,
    val Location: Location,
    val Documentos: List<Documento> = emptyList(),
    val Paquete: Paquete? = null,
    val Archived: Boolean = false,
    val CreateDate: String? = null,
    val UpdateDate: String? = null
) : Serializable

data class Name(
    val FirstName: String,
    val MiddleName: String = ""
) : Serializable

data class LastName(
    val PaternalLastName: String,
    val MaternalLastName: String = ""
) : Serializable

data class Location(
    val Address: Address,
    val Coordinates: Coordinates
) : Serializable

data class Address(
    val Street: String = "",
    val ExteriorNumber: String = "",
    val InteriorNumber: String = "",
    val Neighborhood: String = "",
    val City: String = "",
    val State: String = "",
    val ZipCode: String = ""
) : Serializable

data class Coordinates(
    val Latitude: Double,
    val Longitude: Double
) : Serializable

data class Documento(
    val Tipo: String, // "INE", "Comprobante Domicilio", "Contrato", "Otro"
    val Nombre: String,
    val URL: String,
    val FechaSubida: String? = null
) : Serializable

data class Paquete(
    val NombrePaquete: String = "",
    val Velocidad: String = "",
    val Precio: Double = 0.0,
    val FechaContratacion: String? = null
) : Serializable

// Request para crear cliente
data class CrearClienteRequest(
    val Name: Name,
    val LastName: LastName,
    val PhoneNumber: List<String>,
    val Email: String,
    val Location: Location,
    val Paquete: Paquete? = null
)

// Request para actualizar cliente
data class ActualizarClienteRequest(
    val Name: Name? = null,
    val LastName: LastName? = null,
    val PhoneNumber: List<String>? = null,
    val Email: String? = null,
    val Location: Location? = null,
    val Paquete: Paquete? = null
)

// Response del servidor
data class ClienteResponse(
    val mensaje: String,
    val cliente: ClienteModel
)

// Información completa del cliente
data class ClienteInfoCompleta(
    val InfoCliente: InfoCliente,
    val Documentos: List<Documento>,
    val Ubicacion: Location,
    val Paquete: Paquete?,
    val Pagos: List<Any>, // TODO: cambiar por modelo de Pago cuando esté disponible
    val Tickets: List<Any> // TODO: cambiar por modelo de Ticket cuando esté disponible
)

data class InfoCliente(
    val _id: String,
    val NombreCompleto: String,
    val Email: String,
    val Telefonos: List<String>,
    val CreateDate: String
)

// Extensión para obtener nombre completo
fun ClienteModel.getNombreCompleto(): String {
    val middleName = if (Name.MiddleName.isNotBlank()) " ${Name.MiddleName}" else ""
    val maternalLastName = if (LastName.MaternalLastName.isNotBlank()) " ${LastName.MaternalLastName}" else ""
    return "${Name.FirstName}$middleName ${LastName.PaternalLastName}$maternalLastName"
}

// Extensión para obtener dirección completa
fun Location.getDireccionCompleta(): String {
    val parts = mutableListOf<String>()
    if (Address.Street.isNotBlank()) parts.add(Address.Street)
    if (Address.ExteriorNumber.isNotBlank()) parts.add("#${Address.ExteriorNumber}")
    if (Address.InteriorNumber.isNotBlank()) parts.add("Int. ${Address.InteriorNumber}")
    if (Address.Neighborhood.isNotBlank()) parts.add(Address.Neighborhood)
    if (Address.City.isNotBlank()) parts.add(Address.City)
    if (Address.State.isNotBlank()) parts.add(Address.State)
    if (Address.ZipCode.isNotBlank()) parts.add("CP ${Address.ZipCode}")
    return parts.joinToString(", ")
}
