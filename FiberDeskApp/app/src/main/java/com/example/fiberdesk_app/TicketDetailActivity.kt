package com.example.fiberdesk_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.launch

class TicketDetailActivity : AppCompatActivity() {

    private var currentTicket: Ticket? = null
    private lateinit var spinnerEstado: Spinner
    private var estadoInicial: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        val txtFolio: TextView = findViewById(R.id.txtFolio)
        val txtCliente: TextView = findViewById(R.id.txtCliente)
        val txtAsunto: TextView = findViewById(R.id.txtAsunto)
        val txtDescripcion: TextView = findViewById(R.id.txtDescripcion)
        val txtPrioridad: TextView = findViewById(R.id.txtPrioridad)
        val txtTecnico: TextView = findViewById(R.id.txtTecnico)
        spinnerEstado = findViewById(R.id.spinnerEstado)
        val btnRegresar: Button = findViewById(R.id.btnRegresar)

        // Recibir ticket
        currentTicket = intent.getParcelableExtra<Ticket>("ticket")

        currentTicket?.let { ticket ->
            txtFolio.text = "Folio: ${ticket.folio}"
            txtCliente.text = "Cliente: ${ticket.cliente}"
            txtAsunto.text = "Asunto: ${ticket.asunto}"
            txtDescripcion.text = "Descripción: ${ticket.descripcion}"
            txtPrioridad.text = "Prioridad: ${ticket.prioridad}"
            txtTecnico.text = "Técnico: ${ticket.tecnico}"

            // Configurar spinner de estados
            estadoInicial = ticket.estado ?: "Pendiente"
            configurarSpinnerEstados(estadoInicial)
        }

        btnRegresar.setOnClickListener {
            finish()
        }
    }

    private fun configurarSpinnerEstados(estadoActual: String) {
        val estados = listOf("Pendiente", "En Espera", "En Progreso", "Realizado")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, estados)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapter

        // Seleccionar el estado actual sin disparar el listener
        val posicion = estados.indexOf(estadoActual)
        if (posicion >= 0) {
            spinnerEstado.setSelection(posicion, false)
        }

        // Si el estado es "Realizado", deshabilitar el spinner
        if (estadoActual == "Realizado") {
            spinnerEstado.isEnabled = false
            return
        }
        
        // Configurar listener después de establecer el valor inicial
        spinnerEstado.post {
            spinnerEstado.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val nuevoEstado = estados[position]
                    
                    // Solo mostrar confirmación si realmente cambió el estado
                    if (nuevoEstado != estadoInicial) {
                        mostrarConfirmacionCambioEstado(nuevoEstado)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }
    
    private fun mostrarConfirmacionCambioEstado(nuevoEstado: String) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmar cambio de estado")
        builder.setMessage("¿Está seguro que desea cambiar el estado a \"$nuevoEstado\"?")
        
        builder.setPositiveButton("Confirmar") { dialog, _ ->
            actualizarEstado(nuevoEstado)
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            // Revertir la selección del spinner al estado anterior
            val estados = listOf("Pendiente", "En Espera", "En Progreso", "Realizado")
            val posicionAnterior = estados.indexOf(estadoInicial)
            if (posicionAnterior >= 0) {
                spinnerEstado.setSelection(posicionAnterior, false)
            }
            dialog.dismiss()
        }
        
        builder.setCancelable(false)
        builder.show()
    }

    private fun actualizarEstado(nuevoEstado: String) {
        currentTicket?.let { ticket ->
            lifecycleScope.launch {
                try {
                    android.util.Log.d("TicketDetail", "Actualizando estado de ${ticket.folio} a $nuevoEstado")
                    
                    val response = ApiClient.apiService.actualizarEstadoTicket(
                        ticket.folio,
                        mapOf("estado" to nuevoEstado)
                    )
                    
                    android.util.Log.d("TicketDetail", "Response code: ${response.code()}")
                    
                    if (response.isSuccessful) {
                        val body = response.body()
                        android.util.Log.d("TicketDetail", "Response body: $body")
                        
                        // Actualizar el ticket local y el estado inicial
                        currentTicket = ticket.copy(estado = nuevoEstado)
                        estadoInicial = nuevoEstado
                        
                        // Si el nuevo estado es "Realizado", deshabilitar el spinner
                        if (nuevoEstado == "Realizado") {
                            spinnerEstado.isEnabled = false
                        }
                        
                        Toast.makeText(
                            this@TicketDetailActivity, 
                            "✓ Estado actualizado exitosamente a: $nuevoEstado", 
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        android.util.Log.e("TicketDetail", "Error al actualizar: $errorMsg")
                        Toast.makeText(
                            this@TicketDetailActivity, 
                            "Error: No se pudo actualizar el estado", 
                            Toast.LENGTH_LONG
                        ).show()
                        
                        // Revertir la selección del spinner al estado anterior
                        val estados = listOf("Pendiente", "En Espera", "En Progreso", "Realizado")
                        val posicionAnterior = estados.indexOf(estadoInicial)
                        if (posicionAnterior >= 0) {
                            spinnerEstado.setSelection(posicionAnterior, false)
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("TicketDetail", "Exception al actualizar", e)
                    Toast.makeText(
                        this@TicketDetailActivity, 
                        "Error de conexión: ${e.message}", 
                        Toast.LENGTH_LONG
                    ).show()
                    
                    // Revertir la selección del spinner al estado anterior
                    val estados = listOf("Pendiente", "En Espera", "En Progreso", "Realizado")
                    val posicionAnterior = estados.indexOf(estadoInicial)
                    if (posicionAnterior >= 0) {
                        spinnerEstado.setSelection(posicionAnterior, false)
                    }
                }
            }
        }
    }
}
