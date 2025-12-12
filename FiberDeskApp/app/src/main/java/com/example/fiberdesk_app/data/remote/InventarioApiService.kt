package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.Material
import retrofit2.http.*

interface InventarioApiService {
    @GET("inventario/materiales")
    suspend fun getMateriales(): List<Material>

    @POST("inventario/materiales")
    suspend fun addMaterial(@Body material: Material): Material

    @PUT("inventario/materiales/{id}")
    suspend fun updateMaterial(@Path("id") id: String, @Body material: Material): Material

    @DELETE("inventario/materiales/{id}")
    suspend fun deleteMaterial(@Path("id") id: String)
}

