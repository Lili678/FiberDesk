package com.example.fiberdesk_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.fiberdesk_app.Ticket
import com.example.fiberdesk_app.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketNewActivity : AppCompatActivity() {

    private lateinit var edtCliente: EditText
    private lateinit var edtAsunto: EditText
    private lateinit var spnPrioridad: Spinner
    private lateinit var edtDescripcion: EditText
    private lateinit var edtTecnico: EditText
    private lateinit var btnCrear: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_new)

        // --- Enlazar vistas ---
        edtCliente = findViewById(R.id.edtCliente)
        edtAsunto = findViewById(R.id.edtAsunto)
        spnPrioridad = findViewById(R.id.spnPrioridad)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtTecnico = findViewById(R.id.edtTecnico)
        btnCrear = findViewById(R.id.btnCrear)

        // --- Cargar prioridades en el Spinner ---
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.prioridades,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnPrioridad.adapter = adapter

        // --- Evento de botón Crear ---
        btnCrear.setOnClickListener {
            crearTicket()
        }
    }

    private fun generarFolio(): String {
        val fecha = android.text.format.DateFormat.format("yyyyMMdd", java.util.Date())
        val random = (10000..99999).random()
        return "TCK-$fecha-$random"
    }

    private fun crearTicket() {
        val cliente = edtCliente.text.toString().trim()
        val asunto = edtAsunto.text.toString().trim()
        val prioridad = spnPrioridad.selectedItem.toString()
        val descripcion = edtDescripcion.text.toString().trim()
        val tecnico = edtTecnico.text.toString().trim()

        if (cliente.isEmpty() || asunto.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }


        val nuevoTicket = Ticket(
            folio = generarFolio(),
            cliente = cliente,
            prioridad = prioridad,
            asunto = asunto,
            tecnico = tecnico,
            creadoPor = "AndroidApp",
            estado = "Abierto",
            fecha = System.currentTimeMillis().toString(),
            descripcion = descripcion
        )

        ApiClient.service.crearTicket(nuevoTicket)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TicketNewActivity, "Ticket creado", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@TicketNewActivity, "Error al crear ticket", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    Toast.makeText(this@TicketNewActivity, "Fallo de conexión", Toast.LENGTH_SHORT).show()
                }
            })
    }}
