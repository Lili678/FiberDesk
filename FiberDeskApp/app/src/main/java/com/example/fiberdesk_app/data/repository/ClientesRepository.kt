package com.example.fiberdesk_app.data.repository

import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.data.remote.ClientesApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

class ClientesRepository {
    
    suspend fun obtenerClientes(incluirArchivados: Boolean = false): Result<List<ClienteModel>> = withContext(Dispatchers.IO) {
        try {
            val clientes = ClientesApiClient.obtenerClientes(incluirArchivados)
            Result.Success(clientes)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al obtener clientes")
        }
    }
    
    suspend fun obtenerClientePorId(id: String): Result<ClienteModel> = withContext(Dispatchers.IO) {
        try {
            val cliente = ClientesApiClient.obtenerClientePorId(id)
            Result.Success(cliente)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al obtener cliente")
        }
    }
    
    suspend fun buscarClientes(query: String): Result<List<ClienteModel>> = withContext(Dispatchers.IO) {
        try {
            val clientes = ClientesApiClient.buscarClientes(query)
            Result.Success(clientes)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al buscar clientes")
        }
    }
    
    suspend fun crearCliente(cliente: CrearClienteRequest): Result<ClienteResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ClientesApiClient.crearCliente(cliente)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al crear cliente")
        }
    }
    
    suspend fun actualizarCliente(id: String, cliente: ActualizarClienteRequest): Result<ClienteResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ClientesApiClient.actualizarCliente(id, cliente)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al actualizar cliente")
        }
    }
    
    suspend fun obtenerInfoCompleta(id: String): Result<ClienteInfoCompleta> = withContext(Dispatchers.IO) {
        try {
            val info = ClientesApiClient.obtenerInfoCompleta(id)
            Result.Success(info)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al obtener información completa")
        }
    }
    
    suspend fun archivarCliente(id: String, archivar: Boolean = true): Result<ClienteResponse> = withContext(Dispatchers.IO) {
        try {
            val response = ClientesApiClient.archivarCliente(id, archivar)
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al archivar cliente")
        }
    }
    
    suspend fun eliminarCliente(id: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val mensaje = ClientesApiClient.eliminarCliente(id)
            Result.Success(mensaje)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Error al eliminar cliente")
        }
    }
}
