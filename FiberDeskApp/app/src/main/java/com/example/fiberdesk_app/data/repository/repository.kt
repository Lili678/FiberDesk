package com.example.fiberdesk_app.data.repository

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.data.remote.PagosApiClient
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
            val pagos = PagosApiClient.obtenerPagos()
            Result.Success(pagos)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
    
    // Obtener pago por ID
    suspend fun obtenerPagoPorId(id: String): Result<Pago> = withContext(Dispatchers.IO) {
        try {
            val pago = PagosApiClient.obtenerPagoPorId(id)
            Result.Success(pago)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
    
    // Obtener pagos por usuario
    suspend fun obtenerPagosPorUsuario(usuarioId: String): Result<List<Pago>> = withContext(Dispatchers.IO) {
        try {
            val pagos = PagosApiClient.obtenerPagosPorUsuario(usuarioId)
            Result.Success(pagos)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
    
    // Crear nuevo pago
    suspend fun crearPago(pago: CrearPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = PagosApiClient.crearPago(pago)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
    
    // Actualizar pago
    suspend fun actualizarPago(id: String, pago: ActualizarPagoRequest): Result<PagoResponse> = withContext(Dispatchers.IO) {
        try {
            val response = PagosApiClient.actualizarPago(id, pago)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
    
    // Eliminar pago
    suspend fun eliminarPago(id: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val mensaje = PagosApiClient.eliminarPago(id)
            Result.Success(mensaje)
        } catch (e: Exception) {
            Result.Error("Error: ${e.message}")
        }
    }
}