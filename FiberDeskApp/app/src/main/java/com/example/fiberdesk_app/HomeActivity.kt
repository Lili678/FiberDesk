package com.example.fiberdesk_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.adapters.RecentActivityAdapter
import com.example.fiberdesk_app.adapters.RecentActivityItem
import com.example.fiberdesk_app.network.ApiClient
import com.example.fiberdesk_app.ui.login.LoginActivity
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    
    private lateinit var recyclerRecentActivity: RecyclerView
    private lateinit var txtEmptyActivity: TextView
    private lateinit var adapter: RecentActivityAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtener nombre del usuario desde SharedPreferences
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        
        // Actualizar nombre en la pantalla
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        welcomeText.text = "Bienvenido/a, $userName"
        
        // Inicializar RecyclerView de actividad reciente
        recyclerRecentActivity = findViewById(R.id.recyclerRecentActivity)
        txtEmptyActivity = findViewById(R.id.txtEmptyActivity)
        
        adapter = RecentActivityAdapter(emptyList()) { item ->
            // Click en item de actividad
            when {
                item.type == "TICKET" -> {
                    val intent = Intent(this, TicketListActivity::class.java)
                    startActivity(intent)
                }
                item.type == "INSTALACIÓN" -> {
                    val intent = Intent(this, com.example.fiberdesk_app.ui.inventario.InventarioActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        
        recyclerRecentActivity.layoutManager = LinearLayoutManager(this)
        recyclerRecentActivity.adapter = adapter
        
        // Cargar actividad reciente
        loadRecentActivity()

        // Botón Perfil
        val profileButton: ImageView = findViewById(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.profile.ProfileActivity::class.java)
            startActivity(intent)
        }

        // Botón Atrás
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Módulos
        val clientsButton: ConstraintLayout = findViewById(R.id.clientsButton)
        val ticketsButton: ConstraintLayout = findViewById(R.id.ticketsButton)
        val paymentsButton: ConstraintLayout = findViewById(R.id.paymentsButton)
        val inventoryButton: ConstraintLayout = findViewById(R.id.inventoryButton)

        clientsButton.setOnClickListener {
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        ticketsButton.setOnClickListener {
            val intent = Intent(this, TicketListActivity::class.java)
            startActivity(intent)
        }

        paymentsButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.pagos.PagosActivity::class.java)
            startActivity(intent)
        }

        inventoryButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.inventario.InventarioActivity::class.java)
            startActivity(intent)
        }

        // Manejar el botón de atrás - no permitir volver atrás desde home
        onBackPressedDispatcher.addCallback(this) {
            finish()
        }
    }
    
    private fun loadRecentActivity() {
        lifecycleScope.launch {
            try {
                val activities = mutableListOf<RecentActivityItem>()
                
                // Cargar tickets
                try {
                    val ticketsResponse = ApiClient.apiService.getTickets()
                    if (ticketsResponse.isSuccessful) {
                        val tickets = ticketsResponse.body() ?: emptyList()
                        val recentTickets = tickets.take(3)
                        recentTickets.forEach { ticket ->
                            activities.add(
                                RecentActivityItem(
                                    title = "Ticket: ${ticket.asunto}",
                                    subtitle = "Cliente: ${ticket.cliente}",
                                    type = "TICKET",
                                    typeColor = android.R.color.holo_blue_dark
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    android.util.Log.e("HomeActivity", "Error cargando tickets", e)
                }
                
                // Cargar instalaciones pendientes
                try {
                    val instalaciones = ApiClient.apiService.getInstalaciones()
                    val pendientes = instalaciones.filter { it.estado == "pendiente" }
                    val recentPendientes = pendientes.take(3)
                    recentPendientes.forEach { instalacion ->
                        activities.add(
                            RecentActivityItem(
                                title = "Instalación: ${instalacion.cliente}",
                                subtitle = "Dirección: ${instalacion.direccion}",
                                type = "INSTALACIÓN",
                                typeColor = android.R.color.holo_orange_dark
                            )
                        )
                    }
                } catch (e: Exception) {
                    android.util.Log.e("HomeActivity", "Error cargando instalaciones", e)
                }
                
                // Actualizar UI
                runOnUiThread {
                    if (activities.isEmpty()) {
                        recyclerRecentActivity.visibility = View.GONE
                        txtEmptyActivity.visibility = View.VISIBLE
                    } else {
                        recyclerRecentActivity.visibility = View.VISIBLE
                        txtEmptyActivity.visibility = View.GONE
                        adapter.updateData(activities)
                    }
                }
                
            } catch (e: Exception) {
                android.util.Log.e("HomeActivity", "Error general en loadRecentActivity", e)
            }
        }
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
