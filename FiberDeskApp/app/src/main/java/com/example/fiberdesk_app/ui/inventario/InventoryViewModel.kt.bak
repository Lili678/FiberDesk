package com.example.fiberdesk_app.ui.inventario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.data.repository.InventarioRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = InventarioRepository(application.applicationContext)

    private val _materiales = MutableLiveData<List<Material>>()
    val materiales: LiveData<List<Material>> get() = _materiales

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun obtenerMateriales() {
        viewModelScope.launch {
            try {
                // Llamada al backend o fallback local
                val lista = repository.getMateriales()
                _materiales.postValue(lista)
            } catch (e: IOException) {
                _error.postValue("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                _error.postValue("Error en la respuesta del servidor: ${e.message()}")
            } catch (e: Exception) {
                _error.postValue("Error desconocido: ${e.message}")
            }
        }
    }

    fun agregarMaterial(material: Material) {
        viewModelScope.launch {
            try {
                repository.addMaterial(material)
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
