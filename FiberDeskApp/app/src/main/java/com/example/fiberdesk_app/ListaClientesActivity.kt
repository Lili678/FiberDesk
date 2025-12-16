package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var adapter: ClientesAdapter
    private var listaCompleta: ArrayList<Cliente> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)
        supportActionBar?.title = "Lista de Técnicos"

        // 1. DATOS DE EJEMPLO ACTUALIZADOS (Con todos los campos nuevos)
        cargarDatosDePrueba()

        // 2. Configurar Recycler + EVENTO CLICK
        val rvClientes = findViewById<RecyclerView>(R.id.rvClientes)
        rvClientes.layoutManager = LinearLayoutManager(this)

        adapter = ClientesAdapter(listaCompleta) { clienteSeleccionado ->
            // ESTO SE EJECUTA AL DAR CLIC EN UN ELEMENTO
            val intent = Intent(this, DetalleClienteActivity::class.java)
            intent.putExtra("objeto_cliente", clienteSeleccionado)
            startActivity(intent)
        }

        rvClientes.adapter = adapter

        // 3. Configurar Buscador (Igual que antes)
        val etBuscar = findViewById<EditText>(R.id.etBuscarCliente)
        etBuscar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filtrar(s.toString())
            }
        })
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

    private fun cargarDatosDePrueba() {
        listaCompleta.add(Cliente(
            "Juan", "Pérez", "5512345678", "juan@gmail.com",
            "Av. Reforma", "123", "", "Centro", "CDMX", "CDMX", "06000",
            19.4326, -99.1332
        ))
        listaCompleta.add(Cliente(
            "Maria", "López", "5598765432", "maria@hotmail.com",
            "Calle Pino", "45", "Int 2", "Bosques", "Naucalpan", "Mex", "53000",
            19.5000, -99.2000
        ))
    }
}