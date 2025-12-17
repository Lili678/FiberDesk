package com.example.fiberdesk_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        ApiClient.service.getArchivedTickets()
            .enqueue(object : Callback<List<Ticket>> {
                override fun onResponse(
                    call: Call<List<Ticket>>,
                    response: Response<List<Ticket>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        adapter.actualizarLista(response.body()!!)
                    } else {
                        Toast.makeText(
                            this@TicketArchivedActivity,
                            "Error al cargar tickets archivados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                    Toast.makeText(
                        this@TicketArchivedActivity,
                        "Fallo conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun desarchivarTicket(ticket: Ticket) {
        ApiClient.service.desarchivarTicket(ticket.folio)
            .enqueue(object : Callback<Map<String, Any>> {

                override fun onResponse(
                    call: Call<Map<String, Any>>,
                    response: Response<Map<String, Any>>
                ) {
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
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(
                        this@TicketArchivedActivity,
                        "Fallo de conexión",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
}
