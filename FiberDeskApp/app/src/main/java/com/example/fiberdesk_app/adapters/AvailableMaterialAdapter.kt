package com.example.fiberdesk_app.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Material

class AvailableMaterialAdapter(
    private val items: List<Material>,
    private val selections: MutableMap<String, Int>,
    private val editable: Boolean,
    private val onSelectionChanged: (materialId: String, selected: Boolean, quantity: Int) -> Unit
) : RecyclerView.Adapter<AvailableMaterialAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val check: CheckBox = view.findViewById(R.id.checkSelect)
        val txtName: TextView = view.findViewById(R.id.txtAvailableName)
        val etQty: EditText = view.findViewById(R.id.etQty)
        var isUpdating = false
        var watcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_available_material, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val material = items[position]
        val id = material._id ?: return

        holder.txtName.text = "${material.nombre} (Disp: ${material.cantidad})"
        holder.check.isEnabled = editable
        holder.etQty.isEnabled = editable

        // LIMPIAR listeners previos
        holder.watcher?.let { holder.etQty.removeTextChangedListener(it) }
        holder.check.setOnCheckedChangeListener(null)

        val isSelected = selections.containsKey(id)
        val qty = selections[id] ?: 0

        // Set initial values
        holder.isUpdating = true
        holder.check.isChecked = isSelected && qty > 0
        holder.etQty.setText(if (qty > 0) qty.toString() else "")
        holder.isUpdating = false

        // LISTENER PARA CHECKBOX
        holder.check.setOnCheckedChangeListener { _, checked ->
            if (holder.isUpdating) return@setOnCheckedChangeListener

            if (checked) {
                // Si intenta marcar, enfocar en cantidad
                holder.etQty.requestFocus()
                val currentQty = holder.etQty.text.toString().toIntOrNull() ?: 0
                if (currentQty <= 0) {
                    // Desmarcar temporalmente hasta que ingrese cantidad
                    holder.isUpdating = true
                    holder.check.isChecked = false
                    holder.isUpdating = false
                }
            } else {
                // Desmarcar: limpiar cantidad
                selections.remove(id)
                holder.etQty.setText("")
                onSelectionChanged(id, false, 0)
            }
        }

        // LISTENER PARA CANTIDAD
        holder.watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (holder.isUpdating) return

                val q = s.toString().toIntOrNull() ?: 0

                if (q > 0) {
                    // Cantidad válida: guardar y marcar checkbox
                    selections[id] = q
                    if (!holder.check.isChecked) {
                        holder.isUpdating = true
                        holder.check.isChecked = true
                        holder.isUpdating = false
                    }
                    onSelectionChanged(id, true, q)
                } else {
                    // Cantidad inválida: desmarcar y remover
                    selections.remove(id)
                    if (holder.check.isChecked) {
                        holder.isUpdating = true
                        holder.check.isChecked = false
                        holder.isUpdating = false
                    }
                    onSelectionChanged(id, false, 0)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        holder.etQty.addTextChangedListener(holder.watcher)
    }

    override fun getItemCount(): Int = items.size
}

