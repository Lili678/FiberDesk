package com.example.fiberdesk_app.adapters

// MÓDULO DE INVENTARIO DESHABILITADO TEMPORALMENTE
// Enfocado en módulo de pagos únicamente

/*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material

class MaterialAdapter(
    private val items: MutableList<Material>
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtMaterialName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_material, parent, false)
        return MaterialViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = items[position]
        holder.txtName.text = "${material.nombre} - Cantidad: ${material.cantidad}"
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Material>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
*/