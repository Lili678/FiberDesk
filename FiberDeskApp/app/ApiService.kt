package com.example.fiberdesk_app.network

import com.example.fiberdesk_app.Cliente
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // Endpoint para guardar un nuevo cliente
    // Se asume que tu backend escucha en: POST /api/clientes
    @POST("api/clientes")
    fun registrarCliente(@Body cliente: Cliente): Call<Void>

    // Ejemplo para obtener inventario (para tu Dashboard)
    // @GET("api/inventario")
    // fun obtenerInventario(): Call<List<Material>>
}