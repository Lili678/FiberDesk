package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }
    }
}