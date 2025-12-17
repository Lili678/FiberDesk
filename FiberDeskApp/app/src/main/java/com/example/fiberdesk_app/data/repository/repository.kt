package com.example.fiberdesk_app.data.repository

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Sealed class para manejar estados de respuesta
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class PagosRepository {
    
    // Obtener todos los pagos
    suspend fun obtenerPagos(): Result<List<Pago>> = withContext(Dispatchers.IO) {
        try {
            val pagos = ApiClient.apiService.obtenerPagos()
            Result.Success(pagos)
        } catch (e: Exception) {
            Result.Error("Error al obtener pagos: ${e.message}")
        }
    }
    
    // Obtener pago por ID
    suspend fun obtenerPagoPorId(id: String): Result<Pago> = withContext(Dispatchers.IO) {
        try {
            val pago = ApiClient.apiService.obtenerPagoPorId(id)
            Result.Success(pago)
        } catch (e: Exception) {
            Result.Error("Error al obtener pago: ${e.message}")
        }
    }
    
    // Obtener pagos por usuario
    suspend fun obtenerPagosPorUsuario(usuarioId: String): Result<List<Pago>> = withContext(Dispatchers.IO) {
        try {
            val pagos = ApiClient.apiService.obtenerPagosPorUsuario(usuarioId)
            Result.Success(pagos)
        } catch (e: Exception) {
            Result.Error("Error al obtener pagos: ${e.message}")
        }
    }
    
    // Crear nuevo pago
    suspend fun crearPago(pago: CrearPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.crearPago(pago)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Error al crear pago: ${e.message}")
        }
    }
    
    // Actualizar pago
    suspend fun actualizarPago(id: String, pago: ActualizarPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.actualizarPago(id, pago)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Error al actualizar pago: ${e.message}")
        }
    }
    
    // Eliminar pago
    suspend fun eliminarPago(id: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.apiService.eliminarPago(id)
            val mensaje = response.body()?.get("mensaje") ?: "Pago eliminado"
            Result.Success(mensaje)
        } catch (e: Exception) {
            Result.Error("Error al eliminar pago: ${e.message}")
        }
    }
}

// Repositorio para Inventario e Instalaciones
class InventarioRepository(private val context: android.content.Context) {
    
    private val apiService = com.example.fiberdesk_app.network.ApiClient.apiService
    
    // Obtener materiales
    suspend fun getMateriales(): List<Material> = withContext(Dispatchers.IO) {
        apiService.getMateriales()
    }
    
    // Obtener instalaciones
    suspend fun getInstalaciones(): List<Instalacion> = withContext(Dispatchers.IO) {
        apiService.getInstalaciones()
    }
    
    // Crear instalación
    suspend fun createInstalacion(instalacion: Instalacion): Instalacion = withContext(Dispatchers.IO) {
        apiService.createInstalacion(instalacion)
    }
    
    // Usar material en instalación
    suspend fun usarMaterial(instalacionId: String, materialId: String, cantidad: Int) = withContext(Dispatchers.IO) {
        val materiales = mapOf(materialId to cantidad)
        apiService.usarMateriales(instalacionId, materiales)
    }
    
    // Usar múltiples materiales en instalación
    suspend fun usarMateriales(instalacionId: String, materiales: Map<String, Int>): Instalacion = withContext(Dispatchers.IO) {
        apiService.usarMateriales(instalacionId, materiales)
    }
    
    // Remover material de instalación
    suspend fun removerMaterial(instalacionId: String, materialId: String): Instalacion = withContext(Dispatchers.IO) {
        apiService.removerMaterial(instalacionId, materialId)
    }
    
    // Actualizar estado de instalación
    suspend fun updateEstado(instalacionId: String, estado: String): Instalacion = withContext(Dispatchers.IO) {
        apiService.updateEstadoInstalacion(instalacionId, mapOf("estado" to estado))
    }
    
    // Eliminar instalación
    suspend fun deleteInstalacion(instalacionId: String) = withContext(Dispatchers.IO) {
        apiService.deleteInstalacion(instalacionId)
    }
}