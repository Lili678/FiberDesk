package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object ClientesApiClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"
    
    private fun makeRequest(
        endpoint: String,
        method: String,
        body: String? = null
    ): String {
        val url = URL(BASE_URL + endpoint)
        val connection = url.openConnection() as HttpURLConnection
        
        connection.requestMethod = method
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Accept", "application/json")
        connection.connectTimeout = 30000
        connection.readTimeout = 30000
        
        if (body != null && (method == "POST" || method == "PUT" || method == "PATCH")) {
            connection.doOutput = true
            val writer = OutputStreamWriter(connection.outputStream)
            writer.write(body)
            writer.flush()
            writer.close()
        }
        
        val responseCode = connection.responseCode
        val reader = if (responseCode in 200..299) {
            BufferedReader(InputStreamReader(connection.inputStream))
        } else {
            BufferedReader(InputStreamReader(connection.errorStream))
        }
        
        val response = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            response.append(line)
        }
        reader.close()
        connection.disconnect()
        
        if (responseCode !in 200..299) {
            throw Exception("Error HTTP: $responseCode - ${response.toString()}")
        }
        
        return response.toString()
    }
    
    // Obtener todos los clientes
    fun obtenerClientes(incluirArchivados: Boolean = false): List<ClienteModel> {
        val query = if (incluirArchivados) "?incluirArchivados=true" else ""
        val response = makeRequest("api/clientes$query", "GET")
        val jsonArray = JSONArray(response)
        val clientes = mutableListOf<ClienteModel>()
        
        for (i in 0 until jsonArray.length()) {
            clientes.add(jsonToCliente(jsonArray.getJSONObject(i)))
        }
        
        return clientes
    }
    
    // Obtener cliente por ID
    fun obtenerClientePorId(id: String): ClienteModel {
        val response = makeRequest("api/clientes/$id", "GET")
        return jsonToCliente(JSONObject(response))
    }
    
    // Buscar clientes
    fun buscarClientes(query: String): List<ClienteModel> {
        val response = makeRequest("api/clientes/buscar?query=$query", "GET")
        val jsonArray = JSONArray(response)
        val clientes = mutableListOf<ClienteModel>()
        
        for (i in 0 until jsonArray.length()) {
            clientes.add(jsonToCliente(jsonArray.getJSONObject(i)))
        }
        
        return clientes
    }
    
    // Crear nuevo cliente
    fun crearCliente(cliente: CrearClienteRequest): ClienteResponse {
        val json = JSONObject().apply {
            put("Name", JSONObject().apply {
                put("FirstName", cliente.Name.FirstName)
                put("MiddleName", cliente.Name.MiddleName)
            })
            put("LastName", JSONObject().apply {
                put("PaternalLastName", cliente.LastName.PaternalLastName)
                put("MaternalLastName", cliente.LastName.MaternalLastName)
            })
            put("PhoneNumber", JSONArray(cliente.PhoneNumber))
            put("Email", cliente.Email)
            put("Location", JSONObject().apply {
                put("Address", JSONObject().apply {
                    put("Street", cliente.Location.Address.Street)
                    put("ExteriorNumber", cliente.Location.Address.ExteriorNumber)
                    put("InteriorNumber", cliente.Location.Address.InteriorNumber)
                    put("Neighborhood", cliente.Location.Address.Neighborhood)
                    put("City", cliente.Location.Address.City)
                    put("State", cliente.Location.Address.State)
                    put("ZipCode", cliente.Location.Address.ZipCode)
                })
                put("Coordinates", JSONObject().apply {
                    put("Latitude", cliente.Location.Coordinates.Latitude)
                    put("Longitude", cliente.Location.Coordinates.Longitude)
                })
            })
            if (cliente.Paquete != null) {
                put("Paquete", JSONObject().apply {
                    put("NombrePaquete", cliente.Paquete.NombrePaquete)
                    put("Velocidad", cliente.Paquete.Velocidad)
                    put("Precio", cliente.Paquete.Precio)
                })
            }
        }
        
        val response = makeRequest("api/clientes", "POST", json.toString())
        val jsonResponse = JSONObject(response)
        
        return ClienteResponse(
            mensaje = jsonResponse.getString("mensaje"),
            cliente = jsonToCliente(jsonResponse.getJSONObject("cliente"))
        )
    }
    
    // Actualizar cliente
    fun actualizarCliente(id: String, cliente: ActualizarClienteRequest): ClienteResponse {
        val json = JSONObject()
        
        cliente.Name?.let {
            json.put("Name", JSONObject().apply {
                put("FirstName", it.FirstName)
                put("MiddleName", it.MiddleName)
            })
        }
        cliente.LastName?.let {
            json.put("LastName", JSONObject().apply {
                put("PaternalLastName", it.PaternalLastName)
                put("MaternalLastName", it.MaternalLastName)
            })
        }
        cliente.PhoneNumber?.let { json.put("PhoneNumber", JSONArray(it)) }
        cliente.Email?.let { json.put("Email", it) }
        cliente.Location?.let { loc ->
            json.put("Location", JSONObject().apply {
                put("Address", JSONObject().apply {
                    put("Street", loc.Address.Street)
                    put("ExteriorNumber", loc.Address.ExteriorNumber)
                    put("InteriorNumber", loc.Address.InteriorNumber)
                    put("Neighborhood", loc.Address.Neighborhood)
                    put("City", loc.Address.City)
                    put("State", loc.Address.State)
                    put("ZipCode", loc.Address.ZipCode)
                })
                put("Coordinates", JSONObject().apply {
                    put("Latitude", loc.Coordinates.Latitude)
                    put("Longitude", loc.Coordinates.Longitude)
                })
            })
        }
        
        val response = makeRequest("api/clientes/$id", "PUT", json.toString())
        val jsonResponse = JSONObject(response)
        
        return ClienteResponse(
            mensaje = jsonResponse.getString("mensaje"),
            cliente = jsonToCliente(jsonResponse.getJSONObject("cliente"))
        )
    }
    
    // Obtener información completa
    fun obtenerInfoCompleta(id: String): ClienteInfoCompleta {
        val response = makeRequest("api/clientes/$id/completo", "GET")
        return jsonToInfoCompleta(JSONObject(response))
    }
    
    // Archivar cliente
    fun archivarCliente(id: String, archivar: Boolean = true): ClienteResponse {
        val json = JSONObject().apply {
            put("archivar", archivar)
        }
        
        val response = makeRequest("api/clientes/$id/archivar", "PATCH", json.toString())
        val jsonResponse = JSONObject(response)
        
        return ClienteResponse(
            mensaje = jsonResponse.getString("mensaje"),
            cliente = jsonToCliente(jsonResponse.getJSONObject("cliente"))
        )
    }
    
    // Eliminar cliente
    fun eliminarCliente(id: String): String {
        val response = makeRequest("api/clientes/$id", "DELETE")
        val jsonResponse = JSONObject(response)
        return jsonResponse.getString("mensaje")
    }
    
    // Convertir JSON a ClienteModel
    private fun jsonToCliente(json: JSONObject): ClienteModel {
        val name = json.getJSONObject("Name")
        val lastName = json.getJSONObject("LastName")
        val location = json.getJSONObject("Location")
        val address = location.getJSONObject("Address")
        val coordinates = location.getJSONObject("Coordinates")
        
        val phoneArray = json.getJSONArray("PhoneNumber")
        val phones = mutableListOf<String>()
        for (i in 0 until phoneArray.length()) {
            phones.add(phoneArray.getString(i))
        }
        
        val documentos = mutableListOf<Documento>()
        if (json.has("Documentos")) {
            val docsArray = json.getJSONArray("Documentos")
            for (i in 0 until docsArray.length()) {
                val doc = docsArray.getJSONObject(i)
                documentos.add(Documento(
                    Tipo = doc.getString("Tipo"),
                    Nombre = doc.getString("Nombre"),
                    URL = doc.getString("URL"),
                    FechaSubida = doc.optString("FechaSubida")
                ))
            }
        }
        
        val paquete = if (json.has("Paquete") && !json.isNull("Paquete")) {
            val paq = json.getJSONObject("Paquete")
            Paquete(
                NombrePaquete = paq.optString("NombrePaquete", ""),
                Velocidad = paq.optString("Velocidad", ""),
                Precio = paq.optDouble("Precio", 0.0),
                FechaContratacion = paq.optString("FechaContratacion")
            )
        } else null
        
        return ClienteModel(
            _id = json.optString("_id"),
            Name = Name(
                FirstName = name.getString("FirstName"),
                MiddleName = name.optString("MiddleName", "")
            ),
            LastName = LastName(
                PaternalLastName = lastName.getString("PaternalLastName"),
                MaternalLastName = lastName.optString("MaternalLastName", "")
            ),
            PhoneNumber = phones,
            Email = json.getString("Email"),
            Location = Location(
                Address = Address(
                    Street = address.optString("Street", ""),
                    ExteriorNumber = address.optString("ExteriorNumber", ""),
                    InteriorNumber = address.optString("InteriorNumber", ""),
                    Neighborhood = address.optString("Neighborhood", ""),
                    City = address.optString("City", ""),
                    State = address.optString("State", ""),
                    ZipCode = address.optString("ZipCode", "")
                ),
                Coordinates = Coordinates(
                    Latitude = coordinates.getDouble("Latitude"),
                    Longitude = coordinates.getDouble("Longitude")
                )
            ),
            Documentos = documentos,
            Paquete = paquete,
            Archived = json.optBoolean("Archived", false),
            CreateDate = json.optString("CreateDate"),
            UpdateDate = json.optString("UpdateDate")
        )
    }
    
    // Convertir JSON a ClienteInfoCompleta
    private fun jsonToInfoCompleta(json: JSONObject): ClienteInfoCompleta {
        val infoCliente = json.getJSONObject("InfoCliente")
        val phonesArray = infoCliente.getJSONArray("Telefonos")
        val phones = mutableListOf<String>()
        for (i in 0 until phonesArray.length()) {
            phones.add(phonesArray.getString(i))
        }
        
        return ClienteInfoCompleta(
            InfoCliente = InfoCliente(
                _id = infoCliente.getString("_id"),
                NombreCompleto = infoCliente.getString("NombreCompleto"),
                Email = infoCliente.getString("Email"),
                Telefonos = phones,
                CreateDate = infoCliente.getString("CreateDate")
            ),
            Documentos = emptyList(), // TODO: parsear documentos
            Ubicacion = Location(
                Address = Address(),
                Coordinates = Coordinates(0.0, 0.0)
            ), // TODO: parsear ubicación
            Paquete = null, // TODO: parsear paquete
            Pagos = emptyList(),
            Tickets = emptyList()
        )
    }
}
