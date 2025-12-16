package com.example.fiberdesk_app.network

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.models.LoginRequest
import com.example.fiberdesk_app.models.LoginResponse
import com.example.fiberdesk_app.models.RegisterRequest
import com.example.fiberdesk_app.models.Usuario
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/registro")
    suspend fun registro(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("auth/me")
    suspend fun getPerfil(): Response<Usuario>

    // Inventario endpoints
    @GET("inventario")
    suspend fun getMateriales(): List<Material>

    @POST("inventario")
    suspend fun addMaterial(@Body material: Material): Material

    @PUT("inventario/{id}")
    suspend fun updateMaterial(@Path("id") id: String, @Body material: Material): Material

    @DELETE("inventario/{id}")
    suspend fun deleteMaterial(@Path("id") id: String): Response<Unit>

    // Pagos endpoints
    @GET("pagos")
    suspend fun obtenerPagos(): List<Pago>

    @GET("pagos/{id}")
    suspend fun obtenerPagoPorId(@Path("id") id: String): Pago

    @GET("pagos/usuario/{usuarioId}")
    suspend fun obtenerPagosPorUsuario(@Path("usuarioId") usuarioId: String): List<Pago>

    @POST("pagos")
    suspend fun crearPago(@Body pago: CrearPagoRequest): PagoResponse

    @PUT("pagos/{id}")
    suspend fun actualizarPago(@Path("id") id: String, @Body pago: ActualizarPagoRequest): PagoResponse

    @DELETE("pagos/{id}")
    suspend fun eliminarPago(@Path("id") id: String): Response<Map<String, String>>
}
