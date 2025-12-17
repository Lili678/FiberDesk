package com.example.fiberdesk_app.ui.inventario

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material

class MaterialAdapter(
    private val materials: List<Material>,
    private val onItemClick: (Material) -> Unit
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    class MaterialViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombre: TextView = view.findViewById(R.id.txtMaterialName)
        val cantidad: TextView = view.findViewById(R.id.txtMaterialQuantity)
        val descripcion: TextView = view.findViewById(R.id.txtMaterialDescription)
        val fecha: TextView = view.findViewById(R.id.txtMaterialDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = materials[position]
        holder.nombre.text = material.nombre
        holder.cantidad.text = "Stock: ${material.cantidad}"
        holder.descripcion.text = material.descripcion.ifEmpty { "Sin descripción" }
        holder.fecha.text = if (material.fechaRegistro != null) {
            "Registrado: ${material.fechaRegistro}"
        } else {
            "Registrado: Hoy"
        }
        
        holder.itemView.setOnClickListener {
            onItemClick(material)
        }
    }

    override fun getItemCount() = materials.size
}
