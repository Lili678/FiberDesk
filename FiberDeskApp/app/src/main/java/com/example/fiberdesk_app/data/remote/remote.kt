package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

// Cliente HTTP simple sin dependencias externas
object PagosApiClient {
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
        
        if (body != null && (method == "POST" || method == "PUT")) {
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
    
    // Obtener todos los pagos
    fun obtenerPagos(): List<Pago> {
        val response = makeRequest("api/pagos", "GET")
        val jsonArray = JSONArray(response)
        val pagos = mutableListOf<Pago>()
        
        for (i in 0 until jsonArray.length()) {
            pagos.add(jsonToPago(jsonArray.getJSONObject(i)))
        }
        
        return pagos
    }
    
    // Obtener pago por ID
    fun obtenerPagoPorId(id: String): Pago {
        val response = makeRequest("api/pagos/$id", "GET")
        return jsonToPago(JSONObject(response))
    }
    
    // Obtener pagos por usuario
    fun obtenerPagosPorUsuario(usuarioId: String): List<Pago> {
        val response = makeRequest("api/pagos/usuario/$usuarioId", "GET")
        val jsonArray = JSONArray(response)
        val pagos = mutableListOf<Pago>()
        
        for (i in 0 until jsonArray.length()) {
            pagos.add(jsonToPago(jsonArray.getJSONObject(i)))
        }
        
        return pagos
    }
    
    // Crear nuevo pago
    fun crearPago(pago: CrearPagoRequest): PagoResponse {
        val json = JSONObject().apply {
            put("usuarioId", pago.usuarioId)
            put("monto", pago.monto)
            put("abono", pago.abono)
            put("metodoPago", pago.metodoPago)
            put("fechaPago", pago.fechaPago)
            put("descripcion", pago.descripcion)
            put("prioridad", pago.prioridad)
        }
        
        val response = makeRequest("api/pagos", "POST", json.toString())
        val jsonResponse = JSONObject(response)
        
        return PagoResponse(
            mensaje = jsonResponse.getString("mensaje"),
            pago = jsonToPago(jsonResponse.getJSONObject("pago"))
        )
    }
    
    // Actualizar pago
    fun actualizarPago(id: String, pago: ActualizarPagoRequest): PagoResponse {
        val json = JSONObject().apply {
            pago.monto?.let { put("monto", it) }
            pago.abono?.let { put("abono", it) }
            pago.metodoPago?.let { put("metodoPago", it) }
            pago.estado?.let { put("estado", it) }
            pago.descripcion?.let { put("descripcion", it) }
            pago.prioridad?.let { put("prioridad", it) }
        }
        
        val response = makeRequest("api/pagos/$id", "PUT", json.toString())
        val jsonResponse = JSONObject(response)
        
        return PagoResponse(
            mensaje = jsonResponse.getString("mensaje"),
            pago = jsonToPago(jsonResponse.getJSONObject("pago"))
        )
    }
    
    // Eliminar pago
    fun eliminarPago(id: String): String {
        val response = makeRequest("api/pagos/$id", "DELETE")
        val jsonResponse = JSONObject(response)
        return jsonResponse.getString("mensaje")
    }
    
    // Convertir JSON a Pago
    private fun jsonToPago(json: JSONObject): Pago {
        return Pago(
            _id = json.optString("_id"),
            usuarioId = json.getString("usuarioId"),
            monto = json.getDouble("monto"),
            abono = json.optDouble("abono", 0.0),
            metodoPago = json.getString("metodoPago"),
            fechaPago = json.getString("fechaPago"),
            descripcion = json.optString("descripcion", ""),
            estado = json.optString("estado", "pendiente"),
            prioridad = json.optString("prioridad", "medio"),
            createdAt = json.optString("createdAt"),
            updatedAt = json.optString("updatedAt")
        )
    }
}