package com.example.fiberdesk_app.repository

import com.example.fiberdesk_app.data.remote.RetrofitClient
import com.example.fiberdesk_app.data.remote.UsarMaterialRequest
import com.example.fiberdesk_app.data.remote.UpdateEstadoRequest
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.data.model.Instalacion

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

    // Instalación
    suspend fun getInstalaciones(): List<Instalacion> {
        return RetrofitClient.instalacionApi.getInstalaciones()
    }

    suspend fun createInstalacion(instalacion: Instalacion): Instalacion {
        return RetrofitClient.instalacionApi.createInstalacion(instalacion)
    }

    suspend fun usarMaterial(instalacionId: String, materialId: String, cantidad: Int): Instalacion {
        return RetrofitClient.instalacionApi.usarMaterial(
            instalacionId,
            UsarMaterialRequest(materialId, cantidad)
        )
    }

    suspend fun updateEstado(instalacionId: String, estado: String): Instalacion {
        return RetrofitClient.instalacionApi.updateEstado(
            instalacionId,
            UpdateEstadoRequest(estado)
        )
    }

    suspend fun deleteInstalacion(instalacionId: String) {
        return RetrofitClient.instalacionApi.deleteInstalacion(instalacionId)
    }
}

