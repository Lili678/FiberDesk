package com.example.fiberdesk_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.data.repository.InventarioRepository
import kotlinx.coroutines.launch

class InstalacionViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = InventarioRepository(application.applicationContext)

    private val _instalaciones = MutableLiveData<List<Instalacion>>()
    val instalaciones: LiveData<List<Instalacion>> = _instalaciones

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun obtenerInstalaciones() {
        viewModelScope.launch {
            try {
                _loading.value = true
                val result = repository.getInstalaciones()
                _instalaciones.value = result
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            } finally {
                _loading.value = false
            }
        }
    }

    fun crearInstalacion(instalacion: Instalacion) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.createInstalacion(instalacion)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al crear instalación"
            } finally {
                _loading.value = false
            }
        }
    }

    fun usarMaterial(instalacionId: String, materialId: String, cantidad: Int) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.usarMaterial(instalacionId, materialId, cantidad)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al usar material"
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarEstado(instalacionId: String, estado: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.updateEstado(instalacionId, estado)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al actualizar estado"
            } finally {
                _loading.value = false
            }
        }
    }

    fun eliminarInstalacion(instalacionId: String) {
        viewModelScope.launch {
            try {
                _loading.value = true
                repository.deleteInstalacion(instalacionId)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.value = e.message ?: "Error al eliminar instalación"
            } finally {
                _loading.value = false
            }
        }
    }
}
