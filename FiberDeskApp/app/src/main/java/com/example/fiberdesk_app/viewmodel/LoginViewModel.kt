package com.example.fiberdesk_app.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fiberdesk_app.models.UsuarioData
import com.example.fiberdesk_app.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = AuthRepository()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val usuario = MutableLiveData<UsuarioData?>()
    
    fun login(correo: String, contrasena: String) {
        Log.d("LoginViewModel", "Iniciando login con correo: $correo")
        loading.value = true
        viewModelScope.launch {
            try {
                Log.d("LoginViewModel", "Llamando a repository.login")
                val response = repository.login(correo, contrasena)
                Log.d("LoginViewModel", "Respuesta recibida: ${response?.success}")

                if (response != null) {
                    if (response.success) {
                        Log.d("LoginViewModel", "Login exitoso")
                        usuario.value = response.data
                    } else {
                        Log.e("LoginViewModel", "Login fallido: ${response.message}")
                        error.value = response.message
                    }
                } else {
                    Log.e("LoginViewModel", "Respuesta nula del servidor")
                    error.value = "Error en la respuesta del servidor"
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Excepción en login", e)
                error.value = "Error de conexión: ${e.message}"
            } finally {
                loading.value = false
            }
        }
    }
}
