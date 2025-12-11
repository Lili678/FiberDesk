package com.example.fiberdesk_app.data.repository

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.data.remote.RetrofitClient
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
            val response = api.obtenerPagos()
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al obtener pagos: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
    
    // Obtener pago por ID
    suspend fun obtenerPagoPorId(id: String): Result<Pago> = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerPagoPorId(id)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al obtener pago: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
    
    // Obtener pagos por usuario
    suspend fun obtenerPagosPorUsuario(usuarioId: String): Result<List<Pago>> = withContext(Dispatchers.IO) {
        try {
            val response = api.obtenerPagosPorUsuario(usuarioId)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al obtener pagos del usuario: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
    
    // Crear nuevo pago
    suspend fun crearPago(pago: CrearPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.crearPago(pago)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al crear pago: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
    
    // Actualizar pago
    suspend fun actualizarPago(id: String, pago: ActualizarPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = api.actualizarPago(id, pago)
            if (response.isSuccessful && response.body() != null) {
                Result.Success(response.body()!!)
            } else {
                Result.Error("Error al actualizar pago: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
    
    // Eliminar pago
    suspend fun eliminarPago(id: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = api.eliminarPago(id)
            if (response.isSuccessful) {
                val mensaje = response.body()?.get("mensaje") ?: "Pago eliminado exitosamente"
                Result.Success(mensaje)
            } else {
                Result.Error("Error al eliminar pago: ${response.code()}")
            }
        } catch (e: Exception) {
            Result.Error("Error de conexión: ${e.message}")
        }
    }
}