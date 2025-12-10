package com.example.fiberdesk_app.data.remote

import com.example.fiberdesk_app.data.model.Material
import retrofit2.Response
import retrofit2.http.*

interface InventarioApi {
    @GET("materiales")
    suspend fun getMateriales(): List<Material>

}
