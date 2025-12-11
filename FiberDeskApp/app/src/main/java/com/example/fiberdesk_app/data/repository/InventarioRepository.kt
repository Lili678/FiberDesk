package com.example.fiberdesk_app.repository

import com.example.fiberdesk_app.data.remote.RetrofitClient
import com.example.fiberdesk_app.data.model.Material

class InventarioRepository {
    suspend fun getMateriales(): List<Material> {
        return RetrofitClient.inventarioApi.getMateriales()
    }

    suspend fun addMaterial(material: Material): Material {
        return RetrofitClient.inventarioApi.addMaterial(material)
    }

    suspend fun updateMaterial(id: String, material: Material): Material {
        return RetrofitClient.inventarioApi.updateMaterial(id, material)
    }

    suspend fun deleteMaterial(id: String) {
        return RetrofitClient.inventarioApi.deleteMaterial(id)
    }
}

