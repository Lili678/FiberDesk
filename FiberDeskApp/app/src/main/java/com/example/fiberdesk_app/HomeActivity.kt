package com.example.fiberdesk_app

// ============================================
// IMPORTS - Librerías necesarias para el Home
// ============================================
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

/**
 * ============================================
 * HomeActivity - Pantalla Principal de FiberDesk
 * ============================================
 * Esta es la pantalla principal después del login
 * Muestra:
 * - Mensaje de bienvenida con el nombre del usuario
 * - 4 módulos principales (Clientes, Tickets, Pagos, Inventario)
 * - Actividad reciente (últimos tickets e instalaciones pendientes)
 * - Botones de perfil y navegación
 */
class HomeActivity : AppCompatActivity() {
    
    // ============================================
    // VARIABLES - Componentes de actividad reciente
    // ============================================
    private lateinit var recyclerRecentActivity: RecyclerView    // Lista de actividad reciente
    private lateinit var txtEmptyActivity: TextView              // Texto cuando no hay actividad
    private lateinit var adapter: RecentActivityAdapter          // Adaptador del RecyclerView
    
    /**
     * ============================================
     * onCreate - Método principal al crear el Home
     * ============================================
     * Configura toda la pantalla principal:
     * - Mensaje de bienvenida
     * - Botones de módulos
     * - Actividad reciente
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // ============================================
        // MENSAJE DE BIENVENIDA - Mostrar nombre del usuario
        // ============================================
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        welcomeText.text = "Bienvenido/a, $userName"
        
        // ============================================
        // ACTIVIDAD RECIENTE - Configurar RecyclerView
        // ============================================
        recyclerRecentActivity = findViewById(R.id.recyclerRecentActivity)
        txtEmptyActivity = findViewById(R.id.txtEmptyActivity)
        
        // Crear adaptador con listener para clicks en items
        adapter = RecentActivityAdapter(emptyList()) { item ->
            // Determinar a qué pantalla ir según el tipo
            when {
                item.type == "TICKET" -> {
                    // Ir a lista de tickets
                    val intent = Intent(this, TicketListActivity::class.java)
                    startActivity(intent)
                }
                item.type == "INSTALACIÓN" -> {
                    // Ir a inventario
                    val intent = Intent(this, com.example.fiberdesk_app.ui.inventario.InventarioActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        
        // Configurar RecyclerView con layout vertical
        recyclerRecentActivity.layoutManager = LinearLayoutManager(this)
        recyclerRecentActivity.adapter = adapter
        
        // Cargar datos de actividad reciente desde el servidor
        loadRecentActivity()

        // ============================================
        // BOTÓN PERFIL - Abrir pantalla de perfil
        // ============================================
        val profileButton: ImageView = findViewById(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.profile.ProfileActivity::class.java)
            startActivity(intent)
        }

        // ============================================
        // BOTÓN ATRÁS - Cerrar la app
        // ============================================
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // ============================================
        // MÓDULOS PRINCIPALES - 4 tarjetas de navegación
        // ============================================
        val clientsButton: ConstraintLayout = findViewById(R.id.clientsButton)
        val ticketsButton: ConstraintLayout = findViewById(R.id.ticketsButton)
        val paymentsButton: ConstraintLayout = findViewById(R.id.paymentsButton)
        val inventoryButton: ConstraintLayout = findViewById(R.id.inventoryButton)

        // MÓDULO CLIENTES - Ver y gestionar clientes
        clientsButton.setOnClickListener {
            val intent = Intent(this, ListaClientesActivity::class.java)
            startActivity(intent)
        }

        // MÓDULO TICKETS - Ver y gestionar tickets de soporte
        ticketsButton.setOnClickListener {
            val intent = Intent(this, TicketListActivity::class.java)
            startActivity(intent)
        }

        // MÓDULO PAGOS - Ver y gestionar pagos
        paymentsButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.pagos.PagosActivity::class.java)
            startActivity(intent)
        }

        // MÓDULO INVENTARIO - Ver materiales e instalaciones
        inventoryButton.setOnClickListener {
            val intent = Intent(this, com.example.fiberdesk_app.ui.inventario.InventarioActivity::class.java)
            startActivity(intent)
        }

        // ============================================
        // BOTÓN ATRÁS DEL SISTEMA - Cerrar app
        // ============================================
        onBackPressedDispatcher.addCallback(this) {
            finish()  // No permitir volver al login
        }
    }
    
    /**
     * ============================================
     * loadRecentActivity - Cargar actividad reciente
     * ============================================
     * Obtiene del servidor:
     * - Los últimos 3 tickets creados
     * - Las últimas 3 instalaciones pendientes
     * Y los muestra en el RecyclerView
     */
    private fun loadRecentActivity() {
        lifecycleScope.launch {
            try {
                val activities = mutableListOf<RecentActivityItem>()
                
                // ============================================
                // CARGAR TICKETS - Obtener últimos tickets
                // ============================================
                try {
                    val ticketsResponse = ApiClient.apiService.getTickets()
                    if (ticketsResponse.isSuccessful) {
                        val tickets = ticketsResponse.body() ?: emptyList()
                        val recentTickets = tickets.take(3)  // Tomar solo los 3 más recientes
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
                
                // ============================================
                // CARGAR INSTALACIONES - Solo pendientes
                // ============================================
                try {
                    val instalaciones = ApiClient.apiService.getInstalaciones()
                    val pendientes = instalaciones.filter { it.estado == "pendiente" }  // Filtrar pendientes
                    val recentPendientes = pendientes.take(3)  // Tomar solo las 3 más recientes
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
                
                // ============================================
                // ACTUALIZAR UI - Mostrar resultados
                // ============================================
                runOnUiThread {
                    if (activities.isEmpty()) {
                        // No hay actividad - mostrar mensaje vacío
                        recyclerRecentActivity.visibility = View.GONE
                        txtEmptyActivity.visibility = View.VISIBLE
                    } else {
                        // Hay actividad - mostrar lista
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

    /**
     * ============================================
     * onResume - Actualizar datos al volver a la actividad
     * ============================================
     * Se ejecuta cada vez que la actividad vuelve a primer plano
     * Actualiza el mensaje de bienvenida por si cambió el nombre
     */
    override fun onResume() {
        super.onResume()
        
        // Actualizar mensaje de bienvenida
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        val userName = sharedPref.getString("userName", "Usuario") ?: "Usuario"
        val welcomeText: TextView = findViewById(R.id.welcomeText)
        welcomeText.text = "Bienvenido/a, $userName"
    }

    /**
     * ============================================
     * logout - Cerrar sesión del usuario
     * ============================================
     * Borra todos los datos guardados y vuelve al login
     * (Esta función no se está usando actualmente)
     */
    private fun logout() {
        // Limpiar SharedPreferences (borrar token y datos del usuario)
        val sharedPref = getSharedPreferences("AuthPrefs", MODE_PRIVATE)
        sharedPref.edit().clear().apply()
        
        // Volver al login
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
