package com.example.fiberdesk_app.ui.inventario

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.databinding.ItemMaterialBinding
import com.example.fiberdesk_app.data.model.Material

class MaterialAdapter(
    private var items: List<Material>,
    private val listener: OnItemActionListener? = null
) : RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder>() {

    inner class MaterialViewHolder(val binding: ItemMaterialBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaterialViewHolder {
        val binding = ItemMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaterialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaterialViewHolder, position: Int) {
        val material = items[position]
        holder.binding.txtMaterialName.text = material.nombre
        holder.binding.txtMaterialQuantity.text = "Stock: ${material.cantidad}"
        holder.itemView.setOnClickListener {
            listener?.onItemClick(material)
        }
        holder.itemView.setOnLongClickListener {
            listener?.onItemLongClick(material) ?: false
        }
        holder.binding.txtMaterialDescription.text = material.descripcion ?: ""
        holder.binding.txtMaterialDate.text = "Registrado: ${material.formatFecha()}"
    }

    override fun getItemCount() = items.size

    fun updateData(newItems: List<Material>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnItemActionListener {
        fun onItemClick(material: Material)
        fun onItemLongClick(material: Material): Boolean
    }
}
