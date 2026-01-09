package com.example.fiberdesk_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.data.remote.ClientesApiClient
import com.example.fiberdesk_app.data.model.ClienteModel
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TicketNewActivity : AppCompatActivity() {

    private lateinit var spnCliente: Spinner
    private lateinit var edtAsunto: EditText
    private lateinit var spnPrioridad: Spinner
    private lateinit var edtDescripcion: EditText
    private lateinit var edtTecnico: EditText
    private lateinit var btnCrear: Button

    private lateinit var btnRegresar: Button
    
    private var listaClientes: List<ClienteModel> = emptyList()
    private var nombresClientes: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_new)

        // --- Enlazar vistas ---
        btnRegresar = findViewById(R.id.btnRegresar)
        spnCliente = findViewById(R.id.spnCliente)
        edtAsunto = findViewById(R.id.edtAsunto)
        spnPrioridad = findViewById(R.id.spnPrioridad)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtTecnico = findViewById(R.id.edtTecnico)
        btnCrear = findViewById(R.id.btnCrear)

        btnRegresar.setOnClickListener {
            finish() // Cierra activity y regresa a la lista
        }

        // --- Cargar nombre del usuario logueado ---
        cargarNombreUsuario()

        // --- Cargar clientes desde el servidor ---
        cargarClientes()

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

    private fun cargarNombreUsuario() {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val nombreUsuario = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        
        // Pre-llenar el campo de técnico con el nombre del usuario logueado
        edtTecnico.setText(nombreUsuario)
        
        // Hacer el campo de solo lectura (opcional - el usuario puede cambiarlo si lo necesita)
        // edtTecnico.isEnabled = false
        
        Log.d("TicketNewActivity", "✅ Técnico asignado: $nombreUsuario")
    }

    private fun cargarClientes() {
        Log.d("TicketNewActivity", "🔄 Iniciando carga de clientes...")
        lifecycleScope.launch {
            try {
                // Ejecutar en el hilo IO porque ClientesApiClient hace operaciones de red bloqueantes
                val clientes = withContext(Dispatchers.IO) {
                    ClientesApiClient.obtenerClientes(incluirArchivados = false)
                }
                
                Log.d("TicketNewActivity", "✅ Clientes obtenidos: ${clientes.size}")
                
                listaClientes = clientes
                nombresClientes.clear()
                nombresClientes.add("Selecciona un cliente") // Opción por defecto
                
                listaClientes.forEach { cliente ->
                    val nombreCompleto = "${cliente.Name.FirstName} ${cliente.LastName.PaternalLastName}"
                    nombresClientes.add(nombreCompleto)
                    Log.d("TicketNewActivity", "Cliente agregado: $nombreCompleto")
                }
                
                Log.d("TicketNewActivity", "📋 Total de opciones en spinner: ${nombresClientes.size}")
                
                // Configurar spinner de clientes en el hilo principal
                val adapterClientes = ArrayAdapter(
                    this@TicketNewActivity,
                    android.R.layout.simple_spinner_item,
                    nombresClientes
                )
                adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnCliente.adapter = adapterClientes
                
            } catch (e: Exception) {
                Log.e("TicketNewActivity", "❌ Error al cargar clientes", e)
                Toast.makeText(this@TicketNewActivity, "Error al cargar clientes: ${e.message}", Toast.LENGTH_LONG).show()
                
                // Configurar spinner vacío para evitar crashes
                nombresClientes.clear()
                nombresClientes.add("Error - No se pudieron cargar clientes")
                val adapterClientes = ArrayAdapter(
                    this@TicketNewActivity,
                    android.R.layout.simple_spinner_item,
                    nombresClientes
                )
                adapterClientes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spnCliente.adapter = adapterClientes
            }
        }
    }

    private fun generarFolio(): String {
        val fecha = android.text.format.DateFormat.format("yyyyMMdd", java.util.Date())
        val random = (10000..99999).random()
        return "TCK-$fecha-$random"
    }

    private fun crearTicket() {
        val clienteSeleccionado = spnCliente.selectedItemPosition
        
        // Validar que se haya seleccionado un cliente (no la opción por defecto)
        if (clienteSeleccionado == 0) {
            Toast.makeText(this, "Por favor selecciona un cliente", Toast.LENGTH_SHORT).show()
            return
        }
        
        val cliente = nombresClientes[clienteSeleccionado]
        val asunto = edtAsunto.text.toString().trim()
        val prioridad = spnPrioridad.selectedItem.toString()
        val descripcion = edtDescripcion.text.toString().trim()
        val tecnico = edtTecnico.text.toString().trim()

        if (asunto.isEmpty() || descripcion.isEmpty()) {
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

        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.crearTicket(nuevoTicket)
                if (response.isSuccessful) {
                    Toast.makeText(this@TicketNewActivity, "Ticket creado", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@TicketNewActivity, "Error al crear ticket", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TicketNewActivity, "Fallo de conexión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
