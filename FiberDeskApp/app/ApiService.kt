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

    // Obtener la lista de clientes guardados
    @GET("api/clientes")
    fun obtenerClientes(): Call<List<Cliente>>
}