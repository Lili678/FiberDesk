package com.example.fiberdesk_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.models.ApiResponse
import com.example.fiberdesk_app.models.Cliente
import com.example.fiberdesk_app.models.Direccion
import com.example.fiberdesk_app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgregarClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        btnGuardar.setOnClickListener {
            guardarCliente()
        }
    }

    private fun guardarCliente() {
        // 1. Obtener datos de los campos
        val nombre = findViewById<EditText>(R.id.etNombre).text.toString()
        val apellidos = findViewById<EditText>(R.id.etApellidos).text.toString()
        val telefono = findViewById<EditText>(R.id.etTelefono).text.toString()
        val correo = findViewById<EditText>(R.id.etCorreo).text.toString()
        val calle = findViewById<EditText>(R.id.etCalle).text.toString()
        
        // Conversión segura de lat/long a Double
        val latStr = findViewById<EditText>(R.id.etLatitud).text.toString()
        val lonStr = findViewById<EditText>(R.id.etLongitud).text.toString()
        val latitud = latStr.toDoubleOrNull() ?: 0.0
        val longitud = lonStr.toDoubleOrNull() ?: 0.0

        // Validaciones básicas
        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Nombre y Teléfono son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Crear objetos
        val direccion = Direccion(calle, latitud, longitud)
        val nuevoCliente = Cliente(
            nombre = nombre,
            apellidos = apellidos,
            telefono = telefono,
            correo = correo,
            direccion = direccion
        )

        // 3. Enviar a la API
        RetrofitClient.instance.crearCliente(nuevoCliente).enqueue(object : Callback<ApiResponse<Cliente>> {
            override fun onResponse(call: Call<ApiResponse<Cliente>>, response: Response<ApiResponse<Cliente>>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    Toast.makeText(applicationContext, "Cliente guardado con éxito", Toast.LENGTH_LONG).show()
                    finish() // Cierra la pantalla y vuelve a la anterior
                } else {
                    val errorMsg = response.body()?.error ?: "Error desconocido"
                    Toast.makeText(applicationContext, "Error al guardar: $errorMsg", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Cliente>>, t: Throwable) {
                Toast.makeText(applicationContext, "Fallo de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}