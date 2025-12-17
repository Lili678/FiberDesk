package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.network.ApiClient
import kotlinx.coroutines.launch

class InventarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_inventario)

        // Configurar toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Inventario"
        }

        // Inicializar vistas del dashboardxx
        val txtInstEnProgreso = findViewById<TextView>(R.id.txtInstEnProgreso)
        val txtLowStock = findViewById<TextView>(R.id.txtLowStock)
        val txtInstPendientes = findViewById<TextView>(R.id.txtInstPendientes)
        val txtInstCompletadas = findViewById<TextView>(R.id.txtInstCompletadas)
        val txtWelcome = findViewById<TextView>(R.id.txtWelcome)
        
        val cardGestionarInventario = findViewById<View>(R.id.cardGestionarInventario)
        val cardMisInstalaciones = findViewById<View>(R.id.cardMisInstalaciones)

        // Obtener nombre del usuario de SharedPreferences
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val userName = prefs.getString("user_name", "Operario") ?: "Operario"
        txtWelcome.text = "Bienvenido/a, $userName"

        // Cargar estadísticas
        loadStatistics(txtInstEnProgreso, txtLowStock, txtInstPendientes, txtInstCompletadas)

        // Click listeners para accesos rápidos
        cardGestionarInventario.setOnClickListener {
            // Abrir pantalla de gestión de materiales
            showMaterialesScreen()
        }

        cardMisInstalaciones.setOnClickListener {
            // Abrir pantalla de instalaciones
            showInstalacionesScreen()
        }
    }
    
    private fun loadStatistics(
        txtInstEnProgreso: TextView,
        txtLowStock: TextView,
        txtInstPendientes: TextView,
        txtInstCompletadas: TextView
    ) {
        lifecycleScope.launch {
            try {
                // Cargar instalaciones
                val instalacionesResponse = ApiClient.apiService.getInstalaciones()
                val enProgreso = instalacionesResponse.count { it.estado == "en_progreso" }
                val pendientes = instalacionesResponse.count { it.estado == "pendiente" }
                val completadas = instalacionesResponse.count { it.estado == "completada" }
                
                txtInstEnProgreso.text = enProgreso.toString()
                txtInstPendientes.text = pendientes.toString()
                txtInstCompletadas.text = completadas.toString()
                
                // Cargar materiales
                val materialesResponse = ApiClient.apiService.getMateriales()
                val bajoStock = materialesResponse.count { it.cantidad < 10 }
                txtLowStock.text = bajoStock.toString()
                
            } catch (e: Exception) {
                android.util.Log.e("InventarioActivity", "Error cargando estadísticas", e)
                Toast.makeText(
                    this@InventarioActivity,
                    "Error al cargar estadísticas: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showMaterialesScreen() {
        // TODO: Implementar navegación a lista de materiales
        Toast.makeText(this, "Función de materiales en desarrollo", Toast.LENGTH_SHORT).show()
    }

    private fun showInstalacionesScreen() {
        // Navegar a InstalacionesListFragment
        val fragment = InstalacionesListFragment()
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
