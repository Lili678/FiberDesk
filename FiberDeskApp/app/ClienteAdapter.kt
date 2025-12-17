package com.example.fiberdesk_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.models.Cliente

class ClienteAdapter(private val clientes: List<Cliente>) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    class ClienteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreCompleto)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefono)
        val tvDireccion: TextView = view.findViewById(R.id.tvDireccion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.tvNombre.text = "${cliente.nombre} ${cliente.apellidos}"
        holder.tvTelefono.text = cliente.telefono
        
        // Manejo seguro de la dirección opcional
        holder.tvDireccion.text = cliente.direccion?.calle ?: "Sin dirección registrada"
    }

    override fun getItemCount(): Int = clientes.size
}