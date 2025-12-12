package com.example.fiberdesk_app.data.repository

import android.content.Context
import com.example.fiberdesk_app.data.local.LocalDataSource
import com.example.fiberdesk_app.data.remote.RetrofitClient
import com.example.fiberdesk_app.data.remote.UsarMaterialRequest
import com.example.fiberdesk_app.data.remote.UpdateEstadoRequest
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.data.model.Instalacion

class InventarioRepository(context: Context) {
    private val local = LocalDataSource(context)

    suspend fun getMateriales(): List<Material> {
        return try {
            val remote = RetrofitClient.inventarioApi.getMateriales()
            // cache locally
            remote.forEach { it._id?.let { _ -> local.saveMaterial(it) } }
            remote
        } catch (e: Exception) {
            // fallback to local cache
            local.getAllMaterials()
        }
    }

    suspend fun addMaterial(material: Material): Material {
        val created = RetrofitClient.inventarioApi.addMaterial(material)
        created._id?.let { local.saveMaterial(created) }
        return created
    }

    suspend fun updateMaterial(id: String, material: Material): Material {
        val updated = RetrofitClient.inventarioApi.updateMaterial(id, material)
        updated._id?.let { local.saveMaterial(updated) }
        return updated
    }

    suspend fun deleteMaterial(id: String) {
        RetrofitClient.inventarioApi.deleteMaterial(id)
        local.deleteMaterial(id)
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

