package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
<<<<<<< HEAD
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.ui.login.LoginActivity
=======
import android.view.View
import androidx.appcompat.app.AppCompatActivity
>>>>>>> 840621e91d9e3e1855dd5f4de26903e23515a06b

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        
        // Verificar si el usuario está logueado
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("token", null)
        
        if (token != null) {
            // Usuario logueado, ir a HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Usuario no logueado, ir a LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
=======
        setContentView(R.layout.activity_main)
        supportActionBar?.hide() // Opcional: Ocultar la barra superior en el menú

        // Botón Ver Clientes
        val btnVer = findViewById<View>(R.id.cardVerClientes)
        btnVer.setOnClickListener {
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        // Botón Agregar Cliente
        val btnAgregar = findViewById<View>(R.id.cardAgregarCliente)
        btnAgregar.setOnClickListener {
            val intent = Intent(this, AgregarClienteActivity::class.java)
            startActivity(intent)
>>>>>>> 840621e91d9e3e1855dd5f4de26903e23515a06b
        }
    }
}
