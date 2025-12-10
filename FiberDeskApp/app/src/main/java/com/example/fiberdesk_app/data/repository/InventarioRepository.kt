package com.example.fiberdesk_app.repository

import com.example.fiberdesk_app.data.remote.RetrofitClient
import com.example.fiberdesk_app.data.model.Material
import retrofit2.Response

class InventarioRepository {
    suspend fun getMateriales(): List<Material> {
        return RetrofitClient.inventarioApi.getMateriales()
    }
}

