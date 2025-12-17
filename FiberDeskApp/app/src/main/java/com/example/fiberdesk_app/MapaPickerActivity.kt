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
        setContentView(R.layout.activity_mapa_picker)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
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
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        
        try {
            // Ubicación inicial (CDMX por ejemplo)
            val inicio = LatLng(19.4326, -99.1332)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(inicio, 13f))
            Toast.makeText(this, "Mueve el mapa para seleccionar la ubicación y confirma", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            android.util.Log.e("MapaPicker", "Error cargando mapa", e)
            Toast.makeText(this, "Error: Verifica la API Key de Google Maps", Toast.LENGTH_LONG).show()
        }
    }
}