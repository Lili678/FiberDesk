package com.example.fiberdesk_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.launch

class TicketListActivity : AppCompatActivity() {

    private lateinit var adapter: TicketAdapter
    private lateinit var rvTickets: RecyclerView
    private lateinit var edtBuscar: EditText
    private lateinit var btnCrearTicket: com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var btnVerArchivados: Button
    private lateinit var btnBack: android.widget.ImageView

    private var listaOriginal = listOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            android.util.Log.d("TicketList", "=== INICIANDO TICKETLISTACTIVITY ===")
            android.util.Log.d("TicketList", "Base URL: ${com.example.fiberdesk_app.network.NetworkConfig.getBaseUrl()}")
            
            setContentView(R.layout.activity_ticket_list)
            android.util.Log.d("TicketList", "Layout cargado exitosamente")

            rvTickets = findViewById(R.id.rvTickets)
            edtBuscar = findViewById(R.id.edtBuscar)
            btnCrearTicket = findViewById(R.id.btnCrearTicket)
            btnBack = findViewById(R.id.btnBack)
            btnVerArchivados = findViewById(R.id.btnVerArchivados)
            android.util.Log.d("TicketList", "Vistas encontradas exitosamente")

            // **Adapter CORRECTO con callback**
            adapter = TicketAdapter(
                listOf(),
                onArchivarClick = { ticket ->
                    archivarTicket(ticket)
                },
                onItemClick = { ticket ->
                    val intent = Intent(this, TicketDetailActivity::class.java)
                    intent.putExtra("ticket", ticket)
                    startActivity(intent)
                }
            )
            android.util.Log.d("TicketList", "Adapter creado exitosamente")

            rvTickets.layoutManager = LinearLayoutManager(this)
            rvTickets.adapter = adapter
            android.util.Log.d("TicketList", "RecyclerView configurado exitosamente")

            // Botón regresar
            btnBack.setOnClickListener {
                finish()
            }

            // FAB crear ticket
            btnCrearTicket.setOnClickListener {
                startActivity(Intent(this, TicketNewActivity::class.java))
            }

            btnVerArchivados.setOnClickListener {
                startActivity(Intent(this, TicketArchivedActivity::class.java))
            }
            android.util.Log.d("TicketList", "Listeners configurados exitosamente")

            cargarTickets()
            
        } catch (e: Exception) {
            android.util.Log.e("TicketList", "ERROR EN ONCREATE: ${e.message}", e)
            e.printStackTrace()
            Toast.makeText(this, "Error al inicializar la pantalla: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }

        edtBuscar.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrar(s.toString())
            }
        })
    }

    override fun onResume() {
        super.onResume()
        cargarTickets()
    }

    private fun cargarTickets() {
        lifecycleScope.launch {
            try {
                android.util.Log.d("TicketList", "Iniciando carga de tickets...")
                val response = ApiClient.apiService.getTickets()
                android.util.Log.d("TicketList", "Respuesta recibida: ${response.code()}")
                
                if (response.isSuccessful && response.body() != null) {
                    listaOriginal = response.body()!!
                    android.util.Log.d("TicketList", "Tickets cargados: ${listaOriginal.size}")
                    adapter.actualizarLista(listaOriginal)
                    
                    if (listaOriginal.isEmpty()) {
                        Toast.makeText(this@TicketListActivity, "No hay tickets registrados", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorMsg = "Error ${response.code()}: ${response.message()}"
                    android.util.Log.e("TicketList", errorMsg)
                    Toast.makeText(this@TicketListActivity, errorMsg, Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                val errorMsg = "Error de conexión: ${e.message}"
                android.util.Log.e("TicketList", errorMsg, e)
                Toast.makeText(this@TicketListActivity, errorMsg, Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun filtrar(texto: String) {
        val filtrados = listaOriginal.filter {
            it.folio.contains(texto, ignoreCase = true) ||
                    it.cliente.contains(texto, ignoreCase = true) ||
                    it.asunto.contains(texto, ignoreCase = true)
        }
        adapter.actualizarLista(filtrados)
    }

    private fun archivarTicket(ticket: Ticket) {
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.archivarTicket(ticket.folio)
                if (response.isSuccessful) {
                    Toast.makeText(this@TicketListActivity, "Ticket archivado", Toast.LENGTH_SHORT).show()
                    cargarTickets()
                } else {
                    Toast.makeText(this@TicketListActivity, "Error al archivar", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TicketListActivity, "Fallo conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
