package com.example.fiberdesk_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketArchivedAdapter(
    private var lista: List<Ticket>,
    private val onDesarchivarClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketArchivedAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFolio: TextView = view.findViewById(R.id.txtFolio)
        val txtCliente: TextView = view.findViewById(R.id.txtCliente)
        val txtAsunto: TextView = view.findViewById(R.id.txtAsunto)
        val txtDesarchivar: TextView = view.findViewById(R.id.txtDesarchivar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket_archivado, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = lista[position]

        holder.txtFolio.text = ticket.folio
        holder.txtCliente.text = ticket.cliente
        holder.txtAsunto.text = ticket.asunto

        holder.txtDesarchivar.setOnClickListener {
            onDesarchivarClick(ticket)
        }
    }

    override fun getItemCount() = lista.size

    fun actualizarLista(nuevaLista: List<Ticket>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
