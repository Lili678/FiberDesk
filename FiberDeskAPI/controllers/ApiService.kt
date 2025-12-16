package com.example.fiberdesk_app.network

import com.example.fiberdesk_app.models.ApiResponse
import com.example.fiberdesk_app.models.Cliente
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("clientes")
    fun obtenerClientes(): Call<ApiResponse<List<Cliente>>>

    @POST("clientes")
    fun crearCliente(@Body cliente: Cliente): Call<ApiResponse<Cliente>>

    @GET("clientes/{id}")
    fun obtenerCliente(@Path("id") id: String): Call<ApiResponse<Cliente>>

    @PUT("clientes/{id}")
    fun actualizarCliente(@Path("id") id: String, @Body cliente: Cliente): Call<ApiResponse<Cliente>>

    @DELETE("clientes/{id}")
    fun eliminarCliente(@Path("id") id: String): Call<ApiResponse<Any>>
}