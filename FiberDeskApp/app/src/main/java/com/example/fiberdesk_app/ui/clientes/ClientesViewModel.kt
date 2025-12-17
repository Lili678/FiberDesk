package com.example.fiberdesk_app.ui.clientes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.data.repository.ClientesRepository
import com.example.fiberdesk_app.data.repository.Result
import kotlinx.coroutines.launch

class ClientesViewModel : ViewModel() {
    
    private val repository = ClientesRepository()
    
    // Estado de carga
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Lista de clientes
    private val _clientes = MutableLiveData<List<ClienteModel>>()
    val clientes: LiveData<List<ClienteModel>> = _clientes
    
    // Cliente individual
    private val _cliente = MutableLiveData<ClienteModel>()
    val cliente: LiveData<ClienteModel> = _cliente
    
    // Información completa del cliente
    private val _infoCompleta = MutableLiveData<ClienteInfoCompleta>()
    val infoCompleta: LiveData<ClienteInfoCompleta> = _infoCompleta
    
    // Mensajes de error
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    
    // Mensaje de éxito
    private val _mensaje = MutableLiveData<String>()
    val mensaje: LiveData<String> = _mensaje
    
    // Cargar todos los clientes
    fun cargarClientes(incluirArchivados: Boolean = false) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.obtenerClientes(incluirArchivados)) {
                is Result.Success -> {
                    _clientes.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Buscar clientes
    fun buscarClientes(query: String) {
        if (query.isBlank()) {
            cargarClientes()
            return
        }
        
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.buscarClientes(query)) {
                is Result.Success -> {
                    _clientes.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Obtener cliente por ID
    fun obtenerClientePorId(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.obtenerClientePorId(id)) {
                is Result.Success -> {
                    _cliente.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Obtener información completa
    fun obtenerInfoCompleta(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.obtenerInfoCompleta(id)) {
                is Result.Success -> {
                    _infoCompleta.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Crear nuevo cliente
    fun crearCliente(cliente: CrearClienteRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.crearCliente(cliente)) {
                is Result.Success -> {
                    _mensaje.value = result.data.mensaje
                    _cliente.value = result.data.cliente
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Actualizar cliente
    fun actualizarCliente(id: String, cliente: ActualizarClienteRequest) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.actualizarCliente(id, cliente)) {
                is Result.Success -> {
                    _mensaje.value = result.data.mensaje
                    _cliente.value = result.data.cliente
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Archivar cliente
    fun archivarCliente(id: String, archivar: Boolean = true) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.archivarCliente(id, archivar)) {
                is Result.Success -> {
                    _mensaje.value = result.data.mensaje
                    _cliente.value = result.data.cliente
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Eliminar cliente
    fun eliminarCliente(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            when (val result = repository.eliminarCliente(id)) {
                is Result.Success -> {
                    _mensaje.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                is Result.Loading -> {}
            }
        }
    }
    
    // Limpiar mensajes
    fun limpiarMensajes() {
        _mensaje.value = null
        _error.value = null
    }
}
