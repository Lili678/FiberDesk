package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.Instalacion
import retrofit2.http.*

interface InstalacionApiService {

    @GET("instalaciones")
    suspend fun getInstalaciones(): List<Instalacion>

    @POST("instalaciones")
    suspend fun createInstalacion(
        @Body instalacion: Instalacion
    ): Instalacion

    @POST("instalaciones/{instalacionId}/usar-material")
    suspend fun usarMaterial(
        @Path("instalacionId") instalacionId: String,
        @Body body: UsarMaterialRequest
    ): Instalacion

    @POST("instalaciones/{instalacionId}/usar-materiales")
    suspend fun usarMateriales(
        @Path("instalacionId") instalacionId: String,
        @Body body: UsarMaterialesRequest
    ): Instalacion

    @DELETE("instalaciones/{instalacionId}/material/{materialId}")
    suspend fun removerMaterial(
        @Path("instalacionId") instalacionId: String,
        @Path("materialId") materialId: String
    ): Instalacion

    @PUT("instalaciones/{instalacionId}/estado")
    suspend fun updateEstado(
        @Path("instalacionId") instalacionId: String,
        @Body body: UpdateEstadoRequest
    ): Instalacion

    @DELETE("instalaciones/{instalacionId}")
    suspend fun deleteInstalacion(
        @Path("instalacionId") instalacionId: String
    )
}

/* ===== REQUESTS ===== */

data class UsarMaterialRequest(
    val materialId: String,
    val cantidad: Int
)

data class UsarMaterialesRequest(
    val materiales: List<MaterialItem>
)

data class MaterialItem(
    val materialId: String,
    val cantidad: Int
)

data class UpdateEstadoRequest(
    val estado: String
)
