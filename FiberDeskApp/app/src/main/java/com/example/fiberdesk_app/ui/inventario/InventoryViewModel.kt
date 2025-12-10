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
}
