package com.example.fiberdesk_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TicketDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val txtFolio: TextView = findViewById(R.id.txtFolio)
        val txtCliente: TextView = findViewById(R.id.txtCliente)
        val txtAsunto: TextView = findViewById(R.id.txtAsunto)
        val txtDescripcion: TextView = findViewById(R.id.txtDescripcion)
        val txtPrioridad: TextView = findViewById(R.id.txtPrioridad)
        val txtTecnico: TextView = findViewById(R.id.txtTecnico)
        val txtEstado: TextView = findViewById(R.id.txtEstado)
        val btnRegresar: Button = findViewById(R.id.btnRegresar)

        // Recibir ticket
        val ticket = intent.getParcelableExtra<Ticket>("ticket")

        ticket?.let {
            txtFolio.text = "Folio: ${it.folio}"
            txtCliente.text = "Cliente: ${it.cliente}"
            txtAsunto.text = "Asunto: ${it.asunto}"
            txtDescripcion.text = "Descripción: ${it.descripcion}"
            txtPrioridad.text = "Prioridad: ${it.prioridad}"
            txtTecnico.text = "Técnico: ${it.tecnico}"
            txtEstado.text = "Estado: ${it.estado}"
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }
}
