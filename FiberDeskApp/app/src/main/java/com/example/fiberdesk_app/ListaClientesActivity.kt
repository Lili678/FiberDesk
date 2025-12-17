package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.data.model.ClienteModel
import com.example.fiberdesk_app.data.model.getNombreCompleto
import com.example.fiberdesk_app.ui.clientes.ClientesViewModel

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var adapter: ClientesAdapter
    private lateinit var viewModel: ClientesViewModel
    private lateinit var progressBar: ProgressBar
    private var listaCompleta: ArrayList<Cliente> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)
        supportActionBar?.title = "Lista de Clientes"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Inicializar ViewModel
        viewModel = ViewModelProvider(this)[ClientesViewModel::class.java]

        // Inicializar vistas
        val rvClientes = findViewById<RecyclerView>(R.id.rvClientes)
        val etBuscar = findViewById<EditText>(R.id.etBuscarCliente)
        progressBar = findViewById(R.id.progressBar)

        // Configurar RecyclerView
        rvClientes.layoutManager = LinearLayoutManager(this)
        adapter = ClientesAdapter(listaCompleta) { clienteSeleccionado ->
            val intent = Intent(this, DetalleClienteActivity::class.java)
            intent.putExtra("objeto_cliente", clienteSeleccionado)
            startActivity(intent)
        }
        rvClientes.adapter = adapter

        // Configurar Buscador
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val texto = s.toString().trim()
                if (texto.isNotEmpty()) {
                    viewModel.buscarClientes(texto)
                } else {
                    viewModel.cargarClientes()
                }
            }
        })

        // Observar cambios
        observarViewModel()

        // Cargar datos iniciales
        viewModel.cargarClientes()
    }

    private fun observarViewModel() {
        // Observar lista de clientes
        viewModel.clientes.observe(this) { clientes ->
            listaCompleta.clear()
            clientes.forEach { clienteModel ->
                listaCompleta.add(convertirClienteModelACliente(clienteModel))
            }
            adapter.actualizarLista(listaCompleta)
        }

        // Observar estado de carga
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observar errores
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajes()
            }
        }
    }

    // Convertir ClienteModel (del backend) a Cliente (modelo viejo para UI)
    private fun convertirClienteModelACliente(modelo: ClienteModel): Cliente {
        return Cliente(
            nombre = modelo.Name.FirstName + if (modelo.Name.MiddleName.isNotBlank()) " ${modelo.Name.MiddleName}" else "",
            apellidos = "${modelo.LastName.PaternalLastName} ${modelo.LastName.MaternalLastName}".trim(),
            telefono = modelo.PhoneNumber.firstOrNull() ?: "",
            correo = modelo.Email,
            calle = modelo.Location.Address.Street,
            numExterior = modelo.Location.Address.ExteriorNumber,
            numInterior = modelo.Location.Address.InteriorNumber,
            colonia = modelo.Location.Address.Neighborhood,
            municipio = modelo.Location.Address.City,
            estado = modelo.Location.Address.State,
            cp = modelo.Location.Address.ZipCode,
            latitud = modelo.Location.Coordinates.Latitude,
            longitud = modelo.Location.Coordinates.Longitude
        )
    }

    fun filtrar(texto: String) {
        val listaFiltrada = ArrayList<Cliente>()
        for (cliente in listaCompleta) {
            if (cliente.nombre.lowercase().contains(texto.lowercase()) ||
                cliente.apellidos.lowercase().contains(texto.lowercase())) {
                listaFiltrada.add(cliente)
            }
        }
        adapter.actualizarLista(listaFiltrada)
    }

    override fun onResume() {
        super.onResume()
        // Recargar datos al volver a la pantalla
        viewModel.cargarClientes()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}