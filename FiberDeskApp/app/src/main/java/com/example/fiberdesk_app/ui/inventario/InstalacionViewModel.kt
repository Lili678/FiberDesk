package com.example.fiberdesk_app.ui.inventario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.data.repository.InventarioRepository
import kotlinx.coroutines.launch
import java.io.IOException

class InstalacionViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = InventarioRepository(application.applicationContext)

    private val _instalaciones = MutableLiveData<List<Instalacion>>()
    val instalaciones: LiveData<List<Instalacion>> get() = _instalaciones

    private val _materiales = MutableLiveData<List<Material>>()
    val materiales: LiveData<List<Material>> get() = _materiales

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _success = MutableLiveData<String>()
    val success: LiveData<String> get() = _success

    fun obtenerInstalaciones() {
        viewModelScope.launch {
            try {
                val lista = repository.getInstalaciones()
                _instalaciones.postValue(lista)
            } catch (e: IOException) {
                _error.postValue("Error de conexión: ${e.message}")
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    fun obtenerMateriales() {
        viewModelScope.launch {
            try {
                val lista = repository.getMateriales()
                _materiales.postValue(lista)
            } catch (e: Exception) {
                _error.postValue("Error al cargar materiales: ${e.message}")
            }
        }
    }

    fun createInstalacion(cliente: String, direccion: String) {
        viewModelScope.launch {
            try {
                val instalacion = Instalacion(
                    cliente = cliente,
                    direccion = direccion,
                    estado = "pendiente"
                )
                repository.createInstalacion(instalacion)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.postValue("Error al crear: ${e.message}")
            }
        }
    }

    fun usarMaterial(instalacionId: String, materialId: String, cantidad: Int) {
        viewModelScope.launch {
            try {
                repository.usarMaterial(instalacionId, materialId, cantidad)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.postValue("Error al usar material: ${e.message}")
            }
        }
    }

    // Batch usage: send all materials in one request
    fun usarMaterials(instalacionId: String, selections: Map<String, Int>) {
        viewModelScope.launch {
            try {
                android.util.Log.d("InstalacionVM", "Iniciando usarMaterials: $instalacionId, ${selections.size} items")
                val result = repository.usarMateriales(instalacionId, selections)
                android.util.Log.d("InstalacionVM", "Respuesta recibida: ${result.materialesUsados.size} materiales usados")
                obtenerInstalaciones()
                _success.postValue("Materiales guardados correctamente")
            } catch (e: retrofit2.HttpException) {
                android.util.Log.e("InstalacionVM", "HttpException: ${e.code()} - ${e.message()}")
                _error.postValue("Error servidor: ${e.code()} ${e.message()}")
            } catch (e: Exception) {
                android.util.Log.e("InstalacionVM", "Exception: ${e.message}", e)
                _error.postValue("Error al usar materiales: ${e.message}")
            }
        }
    }

    fun removerMaterialDeInstalacion(instalacionId: String, materialId: String) {
        viewModelScope.launch {
            try {
                repository.removerMaterial(instalacionId, materialId)
                obtenerInstalaciones()
                _success.postValue("Material removido correctamente")
            } catch (e: retrofit2.HttpException) {
                _error.postValue("Error servidor: ${e.code()} ${e.message}")
            } catch (e: Exception) {
                _error.postValue("Error al remover material: ${e.message}")
            }
        }
    }

    fun updateEstado(instalacionId: String, estado: String) {
        viewModelScope.launch {
            try {
                repository.updateEstado(instalacionId, estado)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.postValue("Error al actualizar estado: ${e.message}")
            }
        }
    }

    fun deleteInstalacion(instalacionId: String) {
        viewModelScope.launch {
            try {
                repository.deleteInstalacion(instalacionId)
                obtenerInstalaciones()
            } catch (e: Exception) {
                _error.postValue("Error al eliminar: ${e.message}")
            }
        }
    }
}
