package com.example.fiberdesk_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClientesAdapter(
    private var listaClientes: ArrayList<Cliente>,
    private val onItemClick: (Cliente) -> Unit // <- NUEVO: Evento de clic
) : RecyclerView.Adapter<ClientesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombreCliente)
        val tvDireccion: TextView = view.findViewById(R.id.tvDireccionCliente)
        val tvTelefono: TextView = view.findViewById(R.id.tvTelefonoCliente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cliente = listaClientes[position]

        holder.tvNombre.text = "${cliente.nombre} ${cliente.apellidos}"
        holder.tvDireccion.text = "${cliente.calle} ${cliente.numExterior}, ${cliente.colonia}"
        holder.tvTelefono.text = cliente.telefono

        // DETECTAR EL CLIC
        holder.itemView.setOnClickListener {
            onItemClick(cliente)
        }
    }

    override fun getItemCount(): Int = listaClientes.size

    fun actualizarLista(nuevaLista: ArrayList<Cliente>) {
        listaClientes = nuevaLista
        notifyDataSetChanged()
    }
}