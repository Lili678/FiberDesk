package com.example.fiberdesk_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.launch

class TicketArchivedActivity : AppCompatActivity() {

    private lateinit var rvTickets: RecyclerView
    private lateinit var adapter: TicketArchivedAdapter
    private lateinit var btnRegresar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_archived)

        rvTickets = findViewById(R.id.rvTickets)
        btnRegresar = findViewById(R.id.btnRegresar)

        btnRegresar.setOnClickListener {
            finish() // regresar
        }

        adapter = TicketArchivedAdapter(listOf()) { ticket ->
            desarchivarTicket(ticket)
        }

        rvTickets.layoutManager = LinearLayoutManager(this)
        rvTickets.adapter = adapter

        cargarArchivados()
    }

    private fun cargarArchivados() {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getArchivedTickets()
                if (response.isSuccessful && response.body() != null) {
                    adapter.actualizarLista(response.body()!!)
                } else {
                    Toast.makeText(
                        this@TicketArchivedActivity,
                        "Error al cargar tickets archivados",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@TicketArchivedActivity,
                    "Fallo conexión: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun desarchivarTicket(ticket: Ticket) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.desarchivarTicket(ticket.folio)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@TicketArchivedActivity,
                        "Ticket desarchivado",
                        Toast.LENGTH_SHORT
                    ).show()
                    cargarArchivados()
                } else {
                    Toast.makeText(
                        this@TicketArchivedActivity,
                        "Error al desarchivar",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@TicketArchivedActivity,
                    "Fallo de conexión: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
