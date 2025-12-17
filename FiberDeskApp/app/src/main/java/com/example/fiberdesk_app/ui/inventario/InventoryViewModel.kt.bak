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
                android.util.Log.d("InventoryVM", "Obteniendo materiales...")
                // Llamada al backend o fallback local
                val lista = repository.getMateriales()
                android.util.Log.d("InventoryVM", "Materiales obtenidos: ${lista.size}")
                _materiales.postValue(lista)
            } catch (e: IOException) {
                android.util.Log.e("InventoryVM", "IOException: ${e.message}", e)
                _error.postValue("Error de conexión: ${e.message}")
            } catch (e: HttpException) {
                android.util.Log.e("InventoryVM", "HttpException: ${e.message()}", e)
                _error.postValue("Error en la respuesta del servidor: ${e.message()}")
            } catch (e: Exception) {
                android.util.Log.e("InventoryVM", "Exception: ${e.message}", e)
                _error.postValue("Error desconocido: ${e.message}")
            }
        }
    }

    fun agregarMaterial(material: Material) {
        viewModelScope.launch {
            try {
                android.util.Log.d("InventoryVM", "Agregando material: ${material.nombre}, cant: ${material.cantidad}")
                val result = repository.addMaterial(material)
                android.util.Log.d("InventoryVM", "Material agregado con ID: ${result._id}")
                obtenerMateriales()
            } catch (e: retrofit2.HttpException) {
                android.util.Log.e("InventoryVM", "HttpException: ${e.code()} - ${e.message()}")
                _error.postValue("Error servidor: ${e.code()} ${e.message()}")
            } catch (e: Exception) {
                android.util.Log.e("InventoryVM", "Exception: ${e.message}", e)
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
