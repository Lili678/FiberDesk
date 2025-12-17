package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fiberdesk_app.ui.login.LoginActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtener nombre del usuario desde SharedPreferences
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        
        // Actualizar nombre en la pantalla
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        welcomeText.text = "Bienvenido/a, $userName"

        // Botón Atrás
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Botón Notificaciones
        val notificationButton: ImageView = findViewById(R.id.notificationButton)
        notificationButton.setOnClickListener {
            // TODO: Implementar notificaciones
        }

        // Botón Perfil
        val profileButton: ImageView = findViewById(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.profile.ProfileActivity::class.java)
            startActivity(intent)
        }

        // Módulos
        val clientsButton: ConstraintLayout = findViewById(R.id.clientsButton)
        val ticketsButton: ConstraintLayout = findViewById(R.id.ticketsButton)
        val paymentsButton: ConstraintLayout = findViewById(R.id.paymentsButton)
        val inventoryButton: ConstraintLayout = findViewById(R.id.inventoryButton)

        clientsButton.setOnClickListener {
            // TODO: Ir a pantalla de clientes
        }

        ticketsButton.setOnClickListener {
            // TODO: Ir a pantalla de tickets
        }

        paymentsButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.pagos.PagosActivity::class.java)
            startActivity(intent)
        }

        inventoryButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.inventario.InventarioActivity::class.java)
            startActivity(intent)
        }

        // Manejar el botón de atrás - no permitir volver atrás desde home
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
