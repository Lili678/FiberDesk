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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketListActivity : AppCompatActivity() {

    private lateinit var adapter: TicketAdapter
    private lateinit var rvTickets: RecyclerView
    private lateinit var edtBuscar: EditText

    private lateinit var btnCrearTicket: Button


    private var listaOriginal = listOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_list)  // Asegúrate que este sea tu XML

        rvTickets = findViewById(R.id.rvTickets)
        edtBuscar = findViewById(R.id.edtBuscar)


        adapter = TicketAdapter(listOf())
        rvTickets.adapter = adapter
        rvTickets.layoutManager = LinearLayoutManager(this)

        cargarTickets()
        btnCrearTicket = findViewById(R.id.btnCrearTicket)

        btnCrearTicket.setOnClickListener {
            val intent = Intent(this, TicketNewActivity::class.java)
            startActivity(intent)
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
        cargarTickets()   // <--- recarga cada que vuelves a la pantalla
    }


    private fun cargarTickets() {
        ApiClient.service.getTickets().enqueue(object : Callback<List<Ticket>> {
            override fun onResponse(call: Call<List<Ticket>>, response: Response<List<Ticket>>) {
                if (response.isSuccessful && response.body() != null) {
                    listaOriginal = response.body()!!
                    adapter.actualizarLista(listaOriginal)
                } else {
                    Toast.makeText(this@TicketListActivity, "Error al obtener tickets", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                Toast.makeText(this@TicketListActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filtrar(texto: String) {
        val filtrados = listaOriginal.filter {
            it.folio.contains(texto, ignoreCase = true) ||
                    it.cliente.contains(texto, ignoreCase = true) ||
                    it.asunto.contains(texto, ignoreCase = true)
        }
        adapter.actualizarLista(filtrados)
    }
}
