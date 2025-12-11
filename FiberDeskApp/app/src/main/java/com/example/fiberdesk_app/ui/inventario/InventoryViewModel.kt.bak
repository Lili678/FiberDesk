package com.example.fiberdesk_app.ui.inventario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.repository.InventarioRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InventoryViewModel : ViewModel() {

    private val repository = InventarioRepository()

    private val _materiales = MutableLiveData<List<Material>>()
    val materiales: LiveData<List<Material>> get() = _materiales

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun obtenerMateriales() {
        viewModelScope.launch {
            try {
                // Llamada al backend
                val lista = repository.getMateriales()

                // Retrofit 2.9+ puede devolver directamente List<Material>
                _materiales.postValue(lista)
            } catch (e: IOException) {
                // Error de red
                _error.postValue("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                // Error HTTP
                _error.postValue("Error en la respuesta del servidor: ${e.message()}")
            } catch (e: Exception) {
                _error.postValue("Error desconocido: ${e.message}")
            }
        }
    }

    fun agregarMaterial(material: Material) {
        viewModelScope.launch {
            try {
                val creado = repository.addMaterial(material)
                // refrescar lista
                obtenerMateriales()
            } catch (e: Exception) {
                _error.postValue("Error al agregar: ${e.message}")
            }
        }
    }

    fun actualizarMaterial(id: String, material: Material) {
        viewModelScope.launch {
            try {
                repository.updateMaterial(id, material)
                obtenerMateriales()
            } catch (e: Exception) {
                _error.postValue("Error al actualizar: ${e.message}")
            }
        }
    }

    fun eliminarMaterial(id: String) {
        viewModelScope.launch {
            try {
                repository.deleteMaterial(id)
                obtenerMateriales()
            } catch (e: Exception) {
                _error.postValue("Error al eliminar: ${e.message}")
            }
        }
    }
}
