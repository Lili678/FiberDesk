package com.example.fiberdesk_app

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.ui.clientes.ClientesViewModel

class AgregarClienteActivity : AppCompatActivity() {

    private val CODIGO_PERMISO_GPS = 100
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var locationManager: LocationManager
    private lateinit var viewModel: ClientesViewModel
    private lateinit var progressBar: ProgressBar
    
    // Referencias a todos los campos del formulario
    private lateinit var etNombre: EditText
    private lateinit var etSegundoNombre: EditText
    private lateinit var etApellidoPaterno: EditText
    private lateinit var etApellidoMaterno: EditText
    private lateinit var etTelefono: EditText
    private lateinit var etEmail: EditText
    private lateinit var etCalle: EditText
    private lateinit var etNumExterior: EditText
    private lateinit var etNumInterior: EditText
    private lateinit var etColonia: EditText
    private lateinit var etMunicipio: EditText
    private lateinit var etEstado: EditText
    private lateinit var etCP: EditText

    // Launcher para recibir datos del mapa manual
    private val mapaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val lat = data?.getDoubleExtra("latitud_seleccionada", 0.0)
            val lon = data?.getDoubleExtra("longitud_seleccionada", 0.0)
            etLatitud.setText(lat.toString())
            etLongitud.setText(lon.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)
        supportActionBar?.title = "Nuevo Cliente"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        viewModel = ViewModelProvider(this)[ClientesViewModel::class.java]

        // Referencias a todas las vistas
        etNombre = findViewById(R.id.etNombre)
        etSegundoNombre = findViewById(R.id.etSegundoNombre)
        etApellidoPaterno = findViewById(R.id.etApellidoPaterno)
        etApellidoMaterno = findViewById(R.id.etApellidoMaterno)
        etTelefono = findViewById(R.id.etTelefono)
        etEmail = findViewById(R.id.etEmail)
        etCalle = findViewById(R.id.etCalle)
        etNumExterior = findViewById(R.id.etNumExterior)
        etNumInterior = findViewById(R.id.etNumInterior)
        etColonia = findViewById(R.id.etColonia)
        etMunicipio = findViewById(R.id.etMunicipio)
        etEstado = findViewById(R.id.etEstado)
        etCP = findViewById(R.id.etCP)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)
        progressBar = findViewById(R.id.progressBar)

        val btnMapaManual = findViewById<Button>(R.id.btnMapaManual)
        val btnObtenerGPS = findViewById<Button>(R.id.btnObtenerGPS)
        val btnVerMapa = findViewById<Button>(R.id.btnVerMapa)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Botón Mapa Manual
        btnMapaManual.setOnClickListener {
            val intent = Intent(this, MapaPickerActivity::class.java)
            mapaLauncher.launch(intent)
        }

        // Botón GPS
        btnObtenerGPS.setOnClickListener {
            verificarPermisosYObtenerUbicacion()
        }

        // Botón Verificar en Maps
        btnVerMapa.setOnClickListener {
            val lat = etLatitud.text.toString()
            val lon = etLongitud.text.toString()
            if (lat.isNotEmpty() && lon.isNotEmpty()) {
                val uri = "geo:$lat,$lon?q=$lat,$lon(Cliente)"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(uri)))
            } else {
                Toast.makeText(this, "Faltan coordenadas", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Guardar
        btnGuardar.setOnClickListener {
            guardarCliente()
        }

        // Observar respuestas del ViewModel
        observarViewModel()
        
        // Auto-geocodificar cuando se complete la dirección
        configurarAutoGeocodificacion()
    }
    
    private fun configurarAutoGeocodificacion() {
        // Listener para cuando cambien los campos de dirección
        val direccionWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                // Cuando se completan los campos principales, geocodificar
                if (etCalle.text.toString().isNotBlank() && 
                    etCP.text.toString().length >= 5) {
                    geocodificarDireccion()
                }
            }
        }
        
        etCalle.addTextChangedListener(direccionWatcher)
        etColonia.addTextChangedListener(direccionWatcher)
        etMunicipio.addTextChangedListener(direccionWatcher)
        etEstado.addTextChangedListener(direccionWatcher)
        etCP.addTextChangedListener(direccionWatcher)
    }
    
    private fun geocodificarDireccion() {
        try {
            val geocoder = Geocoder(this)
            
            // Construir dirección completa
            val direccion = buildString {
                append(etCalle.text.toString())
                if (etNumExterior.text.toString().isNotBlank()) {
                    append(" ${etNumExterior.text}")
                }
                if (etColonia.text.toString().isNotBlank()) {
                    append(", ${etColonia.text}")
                }
                if (etMunicipio.text.toString().isNotBlank()) {
                    append(", ${etMunicipio.text}")
                }
                if (etEstado.text.toString().isNotBlank()) {
                    append(", ${etEstado.text}")
                }
                if (etCP.text.toString().isNotBlank()) {
                    append(", CP ${etCP.text}")
                }
                append(", México")
            }
            
            android.util.Log.d("AgregarCliente", "Geocodificando dirección: $direccion")
            
            // Geocodificar en un thread separado
            Thread {
                try {
                    val addresses = geocoder.getFromLocationName(direccion, 1)
                    if (addresses != null && addresses.isNotEmpty()) {
                        val location = addresses[0]
                        runOnUiThread {
                            etLatitud.setText(location.latitude.toString())
                            etLongitud.setText(location.longitude.toString())
                            android.util.Log.d("AgregarCliente", "Coordenadas encontradas: ${location.latitude}, ${location.longitude}")
                            Toast.makeText(this, "✅ Ubicación encontrada: ${location.latitude}, ${location.longitude}", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this, "⚠️ No se pudo encontrar la ubicación. Usa el mapa o GPS", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("AgregarCliente", "Error geocodificando", e)
                    runOnUiThread {
                        Toast.makeText(this, "⚠️ Error al buscar ubicación. Usa el mapa o GPS", Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
            
        } catch (e: Exception) {
            android.util.Log.e("AgregarCliente", "Error en geocodificación", e)
        }
    }
    
    private fun observarViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.mensaje.observe(this) { mensaje ->
            mensaje?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajes()
                finish() // Volver a la lista
            }
        }
        
        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Error: $it", Toast.LENGTH_LONG).show()
                viewModel.limpiarMensajes()
            }
        }
    }
    
    private fun guardarCliente() {
        // Validar campos obligatorios
        val nombre = etNombre.text.toString().trim()
        val apellidoPaterno = etApellidoPaterno.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val latitud = etLatitud.text.toString().trim()
        val longitud = etLongitud.text.toString().trim()
        
        if (nombre.isEmpty()) {
            Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (apellidoPaterno.isEmpty()) {
            Toast.makeText(this, "El apellido paterno es obligatorio", Toast.LENGTH_SHORT).show()
            return
        }
        if (telefono.isEmpty() || telefono.length != 10) {
            Toast.makeText(this, "Ingrese un teléfono válido de 10 dígitos", Toast.LENGTH_SHORT).show()
            return
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Ingrese un email válido", Toast.LENGTH_SHORT).show()
            return
        }
        if (latitud.isEmpty() || longitud.isEmpty()) {
            Toast.makeText(this, "Las coordenadas son obligatorias", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Crear objeto cliente
        val clienteRequest = CrearClienteRequest(
            Name = Name(
                FirstName = nombre,
                MiddleName = etSegundoNombre.text.toString().trim()
            ),
            LastName = LastName(
                PaternalLastName = apellidoPaterno,
                MaternalLastName = etApellidoMaterno.text.toString().trim()
            ),
            PhoneNumber = listOf(telefono),
            Email = email,
            Location = Location(
                Address = Address(
                    Street = etCalle.text.toString().trim(),
                    ExteriorNumber = etNumExterior.text.toString().trim(),
                    InteriorNumber = etNumInterior.text.toString().trim(),
                    Neighborhood = etColonia.text.toString().trim(),
                    City = etMunicipio.text.toString().trim(),
                    State = etEstado.text.toString().trim(),
                    ZipCode = etCP.text.toString().trim()
                ),
                Coordinates = Coordinates(
                    Latitude = latitud.toDouble(),
                    Longitude = longitud.toDouble()
                )
            )
        )
        
        // Enviar al servidor
        viewModel.crearCliente(clienteRequest)
    }

    // --- Lógica del GPS ---
    private fun verificarPermisosYObtenerUbicacion() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), CODIGO_PERMISO_GPS)
        } else {
            obtenerUbicacion()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_PERMISO_GPS && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacion()
        }
    }

    @SuppressLint("MissingPermission")
    private fun obtenerUbicacion() {
        try {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    etLatitud.setText(location.latitude.toString())
                    etLongitud.setText(location.longitude.toString())
                }
                override fun onStatusChanged(p: String?, s: Int, e: Bundle?) {}
                override fun onProviderEnabled(p: String) {}
                override fun onProviderDisabled(p: String) {}
            }, null)
        } catch (e: Exception) { }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}