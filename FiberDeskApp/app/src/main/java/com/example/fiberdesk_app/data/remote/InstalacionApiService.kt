package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.Instalacion
import retrofit2.http.*

interface InstalacionApiService {
    @GET("instalaciones")
    suspend fun getInstalaciones(): List<Instalacion>

    @POST("instalaciones")
    suspend fun createInstalacion(@Body instalacion: Instalacion): Instalacion

    @PUT("instalaciones/{id}/material")
    suspend fun usarMaterial(
        @Path("id") instalacionId: String,
        @Body request: UsarMaterialRequest
    ): Instalacion

    @PUT("instalaciones/{id}/estado")
    suspend fun updateEstado(
        @Path("id") instalacionId: String,
        @Body request: UpdateEstadoRequest
    ): Instalacion

    @DELETE("instalaciones/{id}")
    suspend fun deleteInstalacion(@Path("id") instalacionId: String)
}

data class UsarMaterialRequest(
    val materialId: String,
    val cantidad: Int
)

data class UpdateEstadoRequest(
    val estado: String
)
