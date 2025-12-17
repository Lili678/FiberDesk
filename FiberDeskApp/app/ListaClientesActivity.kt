package com.example.fiberdesk_app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.models.ApiResponse
import com.example.fiberdesk_app.models.Cliente
import com.example.fiberdesk_app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaClientesActivity : AppCompatActivity() {

    private lateinit var rvClientes: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_clientes)

        rvClientes = findViewById(R.id.rvClientes)
        rvClientes.layoutManager = LinearLayoutManager(this)

        cargarClientes()
    }

    private fun cargarClientes() {
        RetrofitClient.instance.obtenerClientes().enqueue(object : Callback<ApiResponse<List<Cliente>>> {
            override fun onResponse(call: Call<ApiResponse<List<Cliente>>>, response: Response<ApiResponse<List<Cliente>>>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.success) {
                        val lista = apiResponse.data ?: emptyList()
                        // Asignar el adaptador con los datos recibidos
                        rvClientes.adapter = ClienteAdapter(lista)
                    } else {
                        Toast.makeText(applicationContext, "Error del servidor: ${apiResponse?.error}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error al cargar: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Cliente>>>, t: Throwable) {
                Toast.makeText(applicationContext, "Fallo de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
