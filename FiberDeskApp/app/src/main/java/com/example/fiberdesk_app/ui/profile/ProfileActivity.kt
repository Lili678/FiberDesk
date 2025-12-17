package com.example.fiberdesk_app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.databinding.ActivityProfileBinding
import com.example.fiberdesk_app.ui.login.LoginActivity

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

        // Botón de editar perfil (opcional, para futuro)
        binding.buttonEditProfile.setOnClickListener {
            Toast.makeText(this, "Función de editar perfil próximamente", Toast.LENGTH_SHORT).show()
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
