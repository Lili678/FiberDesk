package com.example.fiberdesk_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fiberdesk_app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AgregarClienteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        // 1. Referencias a los elementos de la interfaz
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        
        // Datos Personales
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etApellidos = findViewById<EditText>(R.id.etApellidos)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etCorreo = findViewById<EditText>(R.id.etCorreo)
        
        // Dirección
        val etCalle = findViewById<EditText>(R.id.etCalle)
        val etNumExterior = findViewById<EditText>(R.id.etNumExterior)
        val etNumInterior = findViewById<EditText>(R.id.etNumInterior)
        val etCP = findViewById<EditText>(R.id.etCP)
        val spinnerEstado = findViewById<Spinner>(R.id.spinnerEstado)
        
        // Coordenadas (Asumiendo que ya se llenaron con los botones de GPS/Mapa)
        val etLatitud = findViewById<EditText>(R.id.etLatitud)
        val etLongitud = findViewById<EditText>(R.id.etLongitud)

        // 2. Configurar el botón Guardar
        btnGuardar.setOnClickListener {
            // Validar campos básicos (ejemplo simple)
            if (etNombre.text.isEmpty() || etTelefono.text.isEmpty()) {
                Toast.makeText(this, "Por favor completa nombre y teléfono", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 3. Crear el objeto Cliente con los datos del formulario
            val nuevoCliente = Cliente(
                nombre = etNombre.text.toString(),
                apellidos = etApellidos.text.toString(),
                telefono = etTelefono.text.toString(),
                correo = etCorreo.text.toString(),
                calle = etCalle.text.toString(),
                numExterior = etNumExterior.text.toString(),
                numInterior = etNumInterior.text.toString(),
                colonia = "N/A", // Falta este campo en tu XML actual, puedes agregarlo o dejarlo fijo
                municipio = "N/A", // Falta este campo en tu XML actual
                estado = spinnerEstado.selectedItem.toString(),
                cp = etCP.text.toString(),
                // Convertir texto a Double, usando 0.0 si está vacío
                latitud = etLatitud.text.toString().toDoubleOrNull() ?: 0.0,
                longitud = etLongitud.text.toString().toDoubleOrNull() ?: 0.0
            )

            enviarDatosAlBackend(nuevoCliente)
        }
    }

    private fun enviarDatosAlBackend(cliente: Cliente) {
        // 4. Llamada a Retrofit
        RetrofitClient.instance.registrarCliente(cliente).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "¡Cliente guardado exitosamente!", Toast.LENGTH_LONG).show()
                    finish() // Cierra la actividad y vuelve atrás
                } else {
                    Toast.makeText(applicationContext, "Error del servidor: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(applicationContext, "Fallo de conexión: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
```

### Resumen del Flujo
1.  El usuario llena el formulario en la App.
2.  Al dar clic en "Guardar Todo", `AgregarClienteActivity` recopila los textos.
3.  Se crea un objeto `Cliente`.
4.  `RetrofitClient` toma ese objeto, lo convierte a JSON y lo envía a `http://10.0.2.2:3000/api/clientes`.
5.  Si el servidor responde "OK" (código 200-299), la App muestra un mensaje de éxito y cierra la pantalla.

<!--
[PROMPT_SUGGESTION]¿Cómo implemento la lógica para los botones de "Obtener GPS" y "Seleccionar en Mapa" en esta misma Activity?[/PROMPT_SUGGESTION]
[PROMPT_SUGGESTION]Genera el código para el backend (Node.js/Express) que reciba este objeto Cliente y lo guarde en una base de datos.[/PROMPT_SUGGESTION]
-->