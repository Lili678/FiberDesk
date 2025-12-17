package com.example.fiberdesk_app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.databinding.ItemInstalacionBinding
import com.google.android.material.chip.Chip

class InstalacionAdapter(
    private var items: List<Instalacion>,
    private val listener: OnItemActionListener? = null
) : RecyclerView.Adapter<InstalacionAdapter.InstalacionViewHolder>() {

    inner class InstalacionViewHolder(val binding: ItemInstalacionBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstalacionViewHolder {
        val binding = ItemInstalacionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return InstalacionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InstalacionViewHolder, position: Int) {
        val instalacion = items[position]

        holder.binding.apply {

            txtCliente.text = instalacion.cliente
            txtDireccion.text = instalacion.direccion
            txtEstado.text = instalacion.estado.uppercase()

            val colorRes = when (instalacion.estado) {
                "pendiente" -> android.R.color.holo_orange_dark
                "en_progreso" -> android.R.color.holo_blue_dark
                "completada" -> android.R.color.holo_green_dark
                else -> android.R.color.darker_gray
            }

            txtEstado.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.context, colorRes)
            )

            // Limpiar chips previos
            flexboxMateriales.removeAllViews()

            // Filtrar solo materiales con cantidad > 0 y material no nulo
            val materialesConStock = instalacion.materialesUsados?.filter { it.cantidad > 0 && it.material != null } ?: emptyList()

            if (materialesConStock.isEmpty()) {
                txtMateriales.visibility = android.view.View.VISIBLE
                flexboxMateriales.visibility = android.view.View.GONE
            } else {
                txtMateriales.visibility = android.view.View.GONE
                flexboxMateriales.visibility = android.view.View.VISIBLE

                // Agregar chip por cada material usado
                materialesConStock.forEach { used ->
                    used.material?.let { mat ->
                        val chipText = "${mat.nombre} (${used.cantidad} u.)"
                        
                        val chip = Chip(holder.itemView.context).apply {
                            text = chipText
                            isClickable = false
                            isFocusable = false
                        }
                        
                        flexboxMateriales.addView(chip)
                    }
                }
            }

            // CLICK DEL ÍTEM
            root.setOnClickListener {
                listener?.onItemClick(instalacion)
            }

            // Click en estado para cambiarlo
            txtEstado.setOnClickListener {
                listener?.onStatusClick(instalacion)
            }

            // Eliminar instalación
            btnDelete.setOnClickListener {
                listener?.onDelete(instalacion)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<Instalacion>) {
        items = newItems
        notifyDataSetChanged()
    }

    interface OnItemActionListener {
        fun onItemClick(instalacion: Instalacion)
        fun onDelete(instalacion: Instalacion)
        fun onStatusClick(instalacion: Instalacion)
    }
}
