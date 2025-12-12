package com.example.fiberdesk_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private var tickets: List<Ticket>,
    private val onArchivarClick: (Ticket) -> Unit     // <-- callback
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFolio: TextView = itemView.findViewById(R.id.txtFolio)
        val txtCliente: TextView = itemView.findViewById(R.id.txtCliente)
        val txtAsunto: TextView = itemView.findViewById(R.id.txtAsunto)
        val txtArchivar: TextView = itemView.findViewById(R.id.txtArchivar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val t = tickets[position]

        holder.txtFolio.text = t.folio
        holder.txtCliente.text = t.cliente
        holder.txtAsunto.text = t.asunto
        holder.txtArchivar.text = if (t.archivado) "Archivado" else "Archivar"

        // --- Evento Archivar ---
        holder.txtArchivar.setOnClickListener {
            if (!t.archivado) {
                onArchivarClick(t)     // <--- Enviar el ticket a la Activity
            }
        }
    }

    override fun getItemCount(): Int = tickets.size

    fun actualizarLista(nuevaLista: List<Ticket>) {
        tickets = nuevaLista
        notifyDataSetChanged()
    }
}
