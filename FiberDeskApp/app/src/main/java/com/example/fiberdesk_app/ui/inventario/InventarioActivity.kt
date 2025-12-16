package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.network.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class InventarioActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var progressBar: ProgressBar
    
    private val materials = mutableListOf<Material>()
    private lateinit var adapter: MaterialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventario)

        // Configurar toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Inventario"
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.materialsRecyclerView)
        emptyMessage = findViewById(R.id.emptyMessage)
        progressBar = findViewById(R.id.progressBar)
        
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add_material)
        fabAdd.setOnClickListener {
            showAddMaterialDialog()
        }

        // Configurar RecyclerView
        adapter = MaterialAdapter(materials) { material ->
            showMaterialOptions(material)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Cargar materiales
        loadMaterials()
    }

    private fun loadMaterials() {
        progressBar.visibility = View.VISIBLE
        emptyMessage.visibility = View.GONE
        
        lifecycleScope.launch {
            try {
                val response = ApiClient.apiService.getMateriales()
                
                materials.clear()
                materials.addAll(response)
                adapter.notifyDataSetChanged()
                
                if (materials.isEmpty()) {
                    emptyMessage.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@InventarioActivity,
                    "Error al cargar materiales: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                emptyMessage.visibility = View.VISIBLE
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showAddMaterialDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_material, null)
        val nombreInput = dialogView.findViewById<TextInputEditText>(R.id.nombreInput)
        val cantidadInput = dialogView.findViewById<TextInputEditText>(R.id.cantidadInput)
        val descripcionInput = dialogView.findViewById<TextInputEditText>(R.id.descripcionInput)

        AlertDialog.Builder(this)
            .setTitle("Agregar Material")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = nombreInput.text.toString()
                val cantidad = cantidadInput.text.toString().toIntOrNull() ?: 0
                val descripcion = descripcionInput.text.toString()

                if (nombre.isNotEmpty()) {
                    addMaterial(nombre, cantidad, descripcion)
                } else {
                    Toast.makeText(this, "El nombre es requerido", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun addMaterial(nombre: String, cantidad: Int, descripcion: String) {
        progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val material = Material(
                    nombre = nombre,
                    cantidad = cantidad,
                    descripcion = descripcion
                )
                
                ApiClient.apiService.addMaterial(material)
                Toast.makeText(this@InventarioActivity, "Material agregado", Toast.LENGTH_SHORT).show()
                loadMaterials()
            } catch (e: Exception) {
                Toast.makeText(
                    this@InventarioActivity,
                    "Error al agregar material: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun showMaterialOptions(material: Material) {
        val options = arrayOf("Editar", "Eliminar")
        
        AlertDialog.Builder(this)
            .setTitle(material.nombre)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showEditMaterialDialog(material)
                    1 -> confirmDeleteMaterial(material)
                }
            }
            .show()
    }

    private fun showEditMaterialDialog(material: Material) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_material, null)
        val nombreInput = dialogView.findViewById<TextInputEditText>(R.id.nombreInput)
        val cantidadInput = dialogView.findViewById<TextInputEditText>(R.id.cantidadInput)
        val descripcionInput = dialogView.findViewById<TextInputEditText>(R.id.descripcionInput)

        nombreInput.setText(material.nombre)
        cantidadInput.setText(material.cantidad.toString())
        descripcionInput.setText(material.descripcion)

        AlertDialog.Builder(this)
            .setTitle("Editar Material")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = nombreInput.text.toString()
                val cantidad = cantidadInput.text.toString().toIntOrNull() ?: 0
                val descripcion = descripcionInput.text.toString()

                if (nombre.isNotEmpty() && material._id != null) {
                    updateMaterial(material._id, nombre, cantidad, descripcion)
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun updateMaterial(id: String, nombre: String, cantidad: Int, descripcion: String) {
        progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                val material = Material(
                    _id = id,
                    nombre = nombre,
                    cantidad = cantidad,
                    descripcion = descripcion
                )
                
                ApiClient.apiService.updateMaterial(id, material)
                Toast.makeText(this@InventarioActivity, "Material actualizado", Toast.LENGTH_SHORT).show()
                loadMaterials()
            } catch (e: Exception) {
                Toast.makeText(
                    this@InventarioActivity,
                    "Error al actualizar material: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
            }
        }
    }

    private fun confirmDeleteMaterial(material: Material) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Material")
            .setMessage("¿Estás seguro de eliminar ${material.nombre}?")
            .setPositiveButton("Eliminar") { _, _ ->
                material._id?.let { deleteMaterial(it) }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun deleteMaterial(id: String) {
        progressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            try {
                ApiClient.apiService.deleteMaterial(id)
                Toast.makeText(this@InventarioActivity, "Material eliminado", Toast.LENGTH_SHORT).show()
                loadMaterials()
            } catch (e: Exception) {
                Toast.makeText(
                    this@InventarioActivity,
                    "Error al eliminar material: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
                progressBar.visibility = View.GONE
            }
        }
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
