package com.example.fiberdesk_app

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val CODIGO_PERMISO_GPS = 100
    private lateinit var etLatitud: EditText
    private lateinit var etLongitud: EditText
    private lateinit var locationManager: LocationManager

    // Para recibir datos del mapa manual
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
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Registro (Administrador)"

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Referencias
        val etCalle = findViewById<EditText>(R.id.etCalle)
        etLatitud = findViewById(R.id.etLatitud)
        etLongitud = findViewById(R.id.etLongitud)

        val btnIrALista = findViewById<Button>(R.id.btnIrALista)
        val btnMapaManual = findViewById<Button>(R.id.btnMapaManual)
        val btnObtenerGPS = findViewById<Button>(R.id.btnObtenerGPS)
        val btnVerMapa = findViewById<Button>(R.id.btnVerMapa)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        // Botón para ir a la ventana de TÉCNICOS
        btnIrALista.setOnClickListener {
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        btnMapaManual.setOnClickListener {
            val intent = Intent(this, MapaPickerActivity::class.java)
            mapaLauncher.launch(intent)
        }

        btnObtenerGPS.setOnClickListener {
            verificarPermisosYObtenerUbicacion()
        }

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

        btnGuardar.setOnClickListener {
            Toast.makeText(this, "Cliente Guardado", Toast.LENGTH_SHORT).show()
        }
    }

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
}