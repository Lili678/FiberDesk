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
    
    // LiveData para la lista de pagos
    private val _pagos = MutableLiveData<List<Pago>>()
    val pagos: LiveData<List<Pago>> = _pagos
    
    // LiveData para el pago seleccionado
    private val _pagoSeleccionado = MutableLiveData<Pago?>()
    val pagoSeleccionado: LiveData<Pago?> = _pagoSeleccionado
    
    // LiveData para estados de carga y errores
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage
    
    // Cargar todos los pagos
    fun cargarPagos() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.obtenerPagos()) {
                is Result.Success -> {
                    _pagos.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Cargar pagos por usuario
    fun cargarPagosPorUsuario(usuarioId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.obtenerPagosPorUsuario(usuarioId)) {
                is Result.Success -> {
                    _pagos.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Cargar un pago específico
    fun cargarPago(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.obtenerPagoPorId(id)) {
                is Result.Success -> {
                    _pagoSeleccionado.value = result.data
                    _isLoading.value = false
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Crear nuevo pago
    fun crearPago(pago: CrearPagoRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.crearPago(pago)) {
                is Result.Success -> {
                    _successMessage.value = result.data.mensaje
                    _isLoading.value = false
                    cargarPagos() // Recargar lista
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Actualizar pago
    fun actualizarPago(id: String, pago: ActualizarPagoRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.actualizarPago(id, pago)) {
                is Result.Success -> {
                    _successMessage.value = result.data.mensaje
                    _isLoading.value = false
                    cargarPagos() // Recargar lista
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Eliminar pago
    fun eliminarPago(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            when (val result = repository.eliminarPago(id)) {
                is Result.Success -> {
                    _successMessage.value = result.data
                    _isLoading.value = false
                    cargarPagos() // Recargar lista
                }
                is Result.Error -> {
                    _error.value = result.message
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
    
    // Limpiar mensajes
    fun clearMessages() {
        _error.value = null
        _successMessage.value = null
    }
    
    // Seleccionar pago
    fun seleccionarPago(pago: Pago?) {
        _pagoSeleccionado.value = pago
    }
    
    // Calcular estadísticas
    fun calcularEstadisticas(): PagosEstadisticas {
        val listaPagos = _pagos.value ?: emptyList()
        return PagosEstadisticas(
            totalPagos = listaPagos.size,
            totalMonto = listaPagos.sumOf { it.monto },
            totalAbonos = listaPagos.sumOf { it.abono },
            pendientes = listaPagos.count { it.estado == "pendiente" },
            parciales = listaPagos.count { it.estado == "parcial" },
            pagados = listaPagos.count { it.estado == "pagado" }
        )
    }
}

// Data class para estadísticas de pagos
data class PagosEstadisticas(
    val totalPagos: Int,
    val totalMonto: Double,
    val totalAbonos: Double,
    val pendientes: Int,
    val parciales: Int,
    val pagados: Int
)