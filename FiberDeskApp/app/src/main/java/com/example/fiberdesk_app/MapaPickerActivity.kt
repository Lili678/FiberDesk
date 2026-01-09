package com.example.fiberdesk_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapaPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("MapaPicker", "🗺️ Iniciando MapaPickerActivity")
        android.util.Log.d("MapaPicker", "Package: ${packageName}")
        setContentView(R.layout.activity_mapa_picker)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        android.util.Log.d("MapaPicker", "SupportMapFragment encontrado, solicitando mapa...")
        mapFragment.getMapAsync(this)

        val btnConfirmar = findViewById<Button>(R.id.btnConfirmarUbicacion)

        btnConfirmar.setOnClickListener {
            if (::mMap.isInitialized) {
                val target: LatLng = mMap.cameraPosition.target
                val resultIntent = Intent()
                resultIntent.putExtra("latitud_seleccionada", target.latitude)
                resultIntent.putExtra("longitud_seleccionada", target.longitude)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        android.util.Log.d("MapaPicker", "✅ onMapReady llamado - El mapa está listo")
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        
        try {
            // Verificar si se recibieron coordenadas iniciales
            val latInicial = intent.getDoubleExtra("latitud_inicial", 0.0)
            val lonInicial = intent.getDoubleExtra("longitud_inicial", 0.0)
            
            val ubicacionInicial: LatLng
            val zoom: Float
            var mensaje = ""
            
            if (latInicial != 0.0 && lonInicial != 0.0) {
                // Usar ubicación proporcionada
                ubicacionInicial = LatLng(latInicial, lonInicial)
                zoom = 16f  // Más cerca porque es una ubicación específica
                mensaje = "📍 Ubicación aproximada basada en la dirección. Ajusta si es necesario."
                android.util.Log.d("MapaPicker", "✅ Mapa centrado en ubicación proporcionada: $latInicial, $lonInicial")
            } else {
                // Ubicación inicial por defecto (CDMX)
                ubicacionInicial = LatLng(19.4326, -99.1332)
                zoom = 13f
                mensaje = "🗺️ Mueve el mapa para seleccionar la ubicación exacta"
                android.util.Log.d("MapaPicker", "✅ Mapa centrado en CDMX (por defecto)")
            }
            
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionInicial, zoom))
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("MapaPicker", "❌ Error cargando mapa", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onResume() {
        super.onResume()
        android.util.Log.d("MapaPicker", "Activity resumed - esperando mapa...")
    }
}