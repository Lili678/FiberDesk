package com.example.fiberdesk_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R

data class SelectedItem(
    val materialId: String,
    val nombre: String,
    val cantidad: Int
)

class SelectedMaterialAdapter(
    private var items: MutableList<SelectedItem>,
    private var removable: Boolean,
    private val onRemove: (String) -> Unit
) : RecyclerView.Adapter<SelectedMaterialAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtSelectedName)
        val txtQty: TextView = view.findViewById(R.id.txtSelectedQty)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemoveSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_material, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.txtName.text = item.nombre
        holder.txtQty.text = "Cantidad: ${item.cantidad}"

        holder.btnRemove.isEnabled = removable
        holder.btnRemove.setOnClickListener {
            onRemove(item.materialId)
        }
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<SelectedItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}
