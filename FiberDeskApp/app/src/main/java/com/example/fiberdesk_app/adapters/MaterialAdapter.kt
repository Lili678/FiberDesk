package com.example.fiberdesk_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material

class MaterialAdapter(
    private val items: MutableList<Material>,
    private val onClick: ((Material) -> Unit)? = null
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtMaterialName)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClick?.invoke(items[position])
                }
            }
        }
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

    fun add(material: Material) {
        items.add(material)
        notifyItemInserted(items.size - 1)
    }

    fun remove(material: Material) {
        val index = items.indexOf(material)
        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateData(newItems: List<Material>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

