package com.example.fiberdesk_app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.databinding.ActivityProfileBinding
import com.example.fiberdesk_app.ui.login.LoginActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Mi Perfil"
        }

        cargarDatosUsuario()
        setupListeners()
    }

    private fun cargarDatosUsuario() {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        val userEmail = sharedPref.getString("userEmail", "correo@example.com") ?: "correo@example.com"
        val token = sharedPref.getString("token", null)

        // Mostrar datos del usuario
        binding.textViewUserName.text = userName
        binding.textViewUserEmail.text = userEmail
        
        // Mostrar iniciales en el avatar
        val initials = userName.split(" ")
            .take(2)
            .map { it.firstOrNull()?.uppercaseChar() ?: "" }
            .joinToString("")
        binding.textViewInitials.text = initials.ifEmpty { "U" }

        // Mostrar información adicional
        binding.textViewTokenStatus.text = if (token != null) "Sesión activa" else "Sin sesión"
    }

    private fun setupListeners() {
        // Botón de cerrar sesión
        binding.buttonLogout.setOnClickListener {
            mostrarDialogoCerrarSesion()
        }

        // Botón de editar perfil
        binding.buttonEditProfile.setOnClickListener {
            mostrarDialogoEditarPerfil()
        }

        // Botón de cambiar contraseña
        binding.buttonChangePassword.setOnClickListener {
            mostrarDialogoCambiarContrasena()
        }
    }

    private fun mostrarDialogoEditarPerfil() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(com.example.fiberdesk_app.R.layout.dialog_edit_profile, null)
        
        val editTextName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.fiberdesk_app.R.id.editTextName)
        val editTextEmail = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.fiberdesk_app.R.id.editTextEmail)
        
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val currentName = sharedPref.getString("userName", "") ?: ""
        val currentEmail = sharedPref.getString("userEmail", "") ?: ""
        
        editTextName.setText(currentName)
        editTextEmail.setText(currentEmail)
        
        builder.setView(dialogView)
            .setTitle("Editar Perfil")
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoNombre = editTextName.text.toString().trim()
                val nuevoEmail = editTextEmail.text.toString().trim()
                
                if (nuevoNombre.isNotEmpty() && nuevoEmail.isNotEmpty()) {
                    actualizarPerfil(nuevoNombre, nuevoEmail)
                } else {
                    Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogoCambiarContrasena() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(com.example.fiberdesk_app.R.layout.dialog_change_password, null)
        
        val editTextCurrentPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.fiberdesk_app.R.id.editTextCurrentPassword)
        val editTextNewPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.fiberdesk_app.R.id.editTextNewPassword)
        val editTextConfirmPassword = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(com.example.fiberdesk_app.R.id.editTextConfirmPassword)
        
        builder.setView(dialogView)
            .setTitle("Cambiar Contraseña")
            .setPositiveButton("Cambiar") { _, _ ->
                val passwordActual = editTextCurrentPassword.text.toString()
                val passwordNueva = editTextNewPassword.text.toString()
                val passwordConfirm = editTextConfirmPassword.text.toString()
                
                when {
                    passwordActual.isEmpty() || passwordNueva.isEmpty() || passwordConfirm.isEmpty() -> {
                        Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    passwordNueva != passwordConfirm -> {
                        Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                    passwordNueva.length < 6 -> {
                        Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        cambiarContrasena(passwordActual, passwordNueva)
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarPerfil(nombre: String, email: String) {
        lifecycleScope.launch {
            try {
                val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
                val userId = sharedPref.getString("userId", "") ?: ""
                
                val response = com.example.fiberdesk_app.network.ApiClient.apiService.actualizarUsuario(
                    userId,
                    mapOf("nombre" to nombre, "email" to email)
                )
                
                if (response.isSuccessful) {
                    sharedPref.edit().apply {
                        putString("userName", nombre)
                        putString("userEmail", email)
                        apply()
                    }
                    cargarDatosUsuario()
                    Toast.makeText(this@ProfileActivity, "✅ Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                    
                    // Notificar que el nombre cambió para que se actualice en HomeActivity
                    setResult(RESULT_OK)
                } else {
                    Toast.makeText(this@ProfileActivity, "Error al actualizar perfil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cambiarContrasena(passwordActual: String, passwordNueva: String) {
        lifecycleScope.launch {
            try {
                val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
                val userId = sharedPref.getString("userId", "") ?: ""
                
                val response = com.example.fiberdesk_app.network.ApiClient.apiService.cambiarContrasena(
                    userId,
                    mapOf(
                        "passwordActual" to passwordActual,
                        "passwordNueva" to passwordNueva
                    )
                )
                
                if (response.isSuccessful) {
                    Toast.makeText(this@ProfileActivity, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProfileActivity, "Error: Contraseña actual incorrecta", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun mostrarDialogoCerrarSesion() {
        AlertDialog.Builder(this)
            .setTitle("Cerrar Sesión")
            .setMessage("¿Estás seguro de que deseas cerrar sesión?")
            .setPositiveButton("Sí") { _, _ ->
                cerrarSesion()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun cerrarSesion() {
        // Limpiar SharedPreferences
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // Limpiar configuración de red si es necesario
        val networkPref = getSharedPreferences("network_config", MODE_PRIVATE)
        // No borramos la IP guardada para que la recuerde
        
        Toast.makeText(this, "Sesión cerrada correctamente", Toast.LENGTH_SHORT).show()

        // Navegar al LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
