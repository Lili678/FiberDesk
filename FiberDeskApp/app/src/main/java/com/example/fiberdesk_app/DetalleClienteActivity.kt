package com.example.fiberdesk_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_cliente)
        supportActionBar?.title = "Detalle del Cliente"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // 1. Recibir el objeto Cliente
        val cliente = intent.getSerializableExtra("objeto_cliente") as? Cliente

        if (cliente != null) {
            // 2. Llenar los campos
            findViewById<TextView>(R.id.tvDetalleNombre).text = "${cliente.nombre} ${cliente.apellidos}"
            findViewById<TextView>(R.id.tvDetalleTelefono).text = "📞 ${cliente.telefono}"
            findViewById<TextView>(R.id.tvDetalleCorreo).text = "📧 ${cliente.correo}"

            val direccionCompleta = "${cliente.calle} #${cliente.numExterior} Int.${cliente.numInterior}"
            findViewById<TextView>(R.id.tvDetalleDireccion).text = direccionCompleta

            val ubicacion = "${cliente.colonia}, ${cliente.municipio}, ${cliente.estado}. CP: ${cliente.cp}"
            findViewById<TextView>(R.id.tvDetalleUbicacion).text = ubicacion

            // 3. Configurar botón del mapa
            val btnMapa = findViewById<Button>(R.id.btnAbrirMapa)
            btnMapa.setOnClickListener {
                val uri = "geo:${cliente.latitud},${cliente.longitud}?q=${cliente.latitud},${cliente.longitud}(${cliente.nombre})"
                val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                startActivity(mapIntent)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}