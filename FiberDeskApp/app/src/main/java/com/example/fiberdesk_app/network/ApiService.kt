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

    // Tickets endpoints
    @POST("tickets")
    suspend fun crearTicket(@Body ticket: com.example.fiberdesk_app.Ticket): Response<Map<String, Any>>

    @GET("tickets")
    suspend fun getTickets(): Response<List<com.example.fiberdesk_app.Ticket>>

    @GET("tickets/search")
    suspend fun searchTickets(@Query("query") query: String): Response<List<com.example.fiberdesk_app.Ticket>>

    @PUT("tickets/archive/{folio}")
    suspend fun archivarTicket(@Path("folio") folio: String): Response<Map<String, Any>>

    @PUT("tickets/unarchive/{folio}")
    suspend fun desarchivarTicket(@Path("folio") folio: String): Response<Map<String, Any>>

    @GET("tickets/archived")
    suspend fun getArchivedTickets(): Response<List<com.example.fiberdesk_app.Ticket>>

    @PUT("tickets/{folio}/estado")
    suspend fun actualizarEstadoTicket(
        @Path("folio") folio: String,
        @Body estado: Map<String, String>
    ): Response<Map<String, Any>>
}
