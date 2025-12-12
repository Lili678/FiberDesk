package com.example.fiberdesk_app.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiberdesk_app.models.UsuarioData
import com.example.fiberdesk_app.repositories.AuthRepository
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val repository = AuthRepository()

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val usuario = MutableLiveData<UsuarioData?>()

    fun registro(correo: String, contraseña: String, nombre: String) {
        loading.value = true

        viewModelScope.launch {
            try {
                val response = repository.registro(correo, contraseña, nombre)

                if (response != null) {
                    if (response.success) {
                        usuario.value = response.data
                    } else {
                        error.value = response.message
                    }
                } else {
                    error.value = "Error en la respuesta del servidor"
                }

            } catch (e: Exception) {
                error.value = "Error de conexión: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }
}