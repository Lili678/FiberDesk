package com.example.fiberdesk_app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material

class AvailableMaterialAdapter(
    private val items: List<Material>,
    private val selectionChanged: (materialId: String, selected: Boolean, position: Int) -> Unit
) : RecyclerView.Adapter<AvailableMaterialAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val check: CheckBox = view.findViewById(R.id.checkSelect)
        val txtName: TextView = view.findViewById(R.id.txtAvailableName)
        val txtQty: TextView = view.findViewById(R.id.txtAvailableQty)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_available_material, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val m = items[position]
        holder.txtName.text = m.nombre
        holder.txtQty.text = "Cantidad: ${m.cantidad}"
        holder.check.setOnCheckedChangeListener(null)
        holder.check.isChecked = false
        holder.check.setOnCheckedChangeListener { _, isChecked ->
            m._id?.let { selectionChanged(it, isChecked, position) }
        }
        holder.itemView.setOnClickListener {
            val newState = !holder.check.isChecked
            holder.check.isChecked = newState
            m._id?.let { selectionChanged(it, newState, position) }
        }
    }

    override fun getItemCount(): Int = items.size
}
