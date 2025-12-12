package com.example.fiberdesk_app

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
        supportActionBar?.title = "Lista de Técnicos (Solo Lectura)"

        // 1. DATOS DE EJEMPLO (Aquí conectarás tu BD después)
        listaCompleta.add(Cliente("Juan Pérez", "Av. Reforma 123", "5512345678"))
        listaCompleta.add(Cliente("Maria López", "Calle Pino 45", "5598765432"))
        listaCompleta.add(Cliente("Carlos Sanchez", "Lote 4 Manzana 2", "5511223344"))
        listaCompleta.add(Cliente("Ana García", "Blvd. Aeropuerto 500", "5544332211"))

        // 2. Configurar Recycler
        val rvClientes = findViewById<RecyclerView>(R.id.rvClientes)
        rvClientes.layoutManager = LinearLayoutManager(this)

        adapter = ClientesAdapter(listaCompleta)
        rvClientes.adapter = adapter

        // 3. Configurar Buscador
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
            if (cliente.nombre.lowercase().contains(texto.lowercase())) {
                listaFiltrada.add(cliente)
            }
        }
        adapter.actualizarLista(listaFiltrada)
    }
}