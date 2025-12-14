package com.example.fiberdesk_app.ui.pagos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.data.repository.PagosRepository
import com.example.fiberdesk_app.data.repository.Result
import kotlinx.coroutines.launch

class PagosViewModel : ViewModel() {
    private val repository = PagosRepository()
    
    private val _pagos = MutableLiveData<List<Pago>>()
    val pagos: LiveData<List<Pago>> = _pagos
    
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _success = MutableLiveData<String?>()
    val success: LiveData<String?> = _success
    
    fun cargarPagos() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repository.obtenerPagos()) {
                is Result.Success -> {
                    _pagos.value = result.data
                    _error.value = null
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                is Result.Loading -> {}
            }
            _loading.value = false
        }
    }
    
    fun crearPago(request: CrearPagoRequest) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repository.crearPago(request)) {
                is Result.Success -> {
                    _success.value = "Pago creado exitosamente"
                    cargarPagos()
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                is Result.Loading -> {}
            }
            _loading.value = false
        }
    }
    
    fun actualizarPago(id: String, request: ActualizarPagoRequest) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repository.actualizarPago(id, request)) {
                is Result.Success -> {
                    _success.value = "Pago actualizado exitosamente"
                    cargarPagos()
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                is Result.Loading -> {}
            }
            _loading.value = false
        }
    }
    
    fun eliminarPago(id: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = repository.eliminarPago(id)) {
                is Result.Success -> {
                    _success.value = "Pago eliminado exitosamente"
                    cargarPagos()
                }
                is Result.Error -> {
                    _error.value = result.message
                }
                is Result.Loading -> {}
            }
            _loading.value = false
        }
    }
    
    fun calcularEstadisticas(): Map<String, Double> {
        val pagosList = _pagos.value ?: return mapOf(
            "total" to 0.0,
            "pendientes" to 0.0,
            "parciales" to 0.0,
            "pagados" to 0.0
        )
        
        return mapOf(
            "total" to pagosList.sumOf { it.monto },
            "pendientes" to pagosList.filter { it.estado == EstadoPago.PENDIENTE.valor }.sumOf { it.monto },
            "parciales" to pagosList.filter { it.estado == EstadoPago.PARCIAL.valor }.sumOf { it.monto },
            "pagados" to pagosList.filter { it.estado == EstadoPago.PAGADO.valor }.sumOf { it.monto }
        )
    }
    
    fun clearMessages() {
        _error.value = null
        _success.value = null
    }
}