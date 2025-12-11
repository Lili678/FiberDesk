package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.Material
import retrofit2.Response
import retrofit2.http.*

interface InventarioApi {
    @GET("materiales")
    suspend fun getMateriales(): List<Material>

    @POST("materiales")
    suspend fun addMaterial(@Body material: Material): Material

    @PUT("materiales/{id}")
    suspend fun updateMaterial(@Path("id") id: String, @Body material: Material): Material

    @DELETE("materiales/{id}")
    suspend fun deleteMaterial(@Path("id") id: String)

}
