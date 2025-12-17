package com.example.fiberdesk_app

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TicketAdapter(
    private var tickets: List<Ticket>,
    private val onArchivarClick: (Ticket) -> Unit,
    private val onItemClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    inner class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtFolio: TextView = itemView.findViewById(R.id.txtFolio)
        val txtCliente: TextView = itemView.findViewById(R.id.txtCliente)
        val txtAsunto: TextView = itemView.findViewById(R.id.txtAsunto)
        val txtArchivar: TextView = itemView.findViewById(R.id.txtArchivar)
        val viewColorIndicator: View = itemView.findViewById(R.id.viewColorIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val t = tickets[position]

        holder.txtFolio.text = t.folio
        holder.txtCliente.text = "Cliente: ${t.cliente}"
        holder.txtAsunto.text = t.asunto

        // Asignar color según prioridad
        val color = when (t.prioridad?.lowercase()) {
            "alta", "high" -> "#EF4444" // Rojo
            "media", "medium" -> "#F59E0B" // Naranja
            "baja", "low" -> "#10B981" // Verde
            else -> "#6366F1" // Azul por defecto
        }
        holder.viewColorIndicator.setBackgroundColor(Color.parseColor(color))

        // 🔵 Ver detalles (clic en el item)
        holder.itemView.setOnClickListener {
            onItemClick(t)
        }
        
        // Long click para archivar
        holder.itemView.setOnLongClickListener {
            if (!t.archivado) {
                onArchivarClick(t)
            }
            true
        }
    }

    override fun getItemCount(): Int = tickets.size

    fun actualizarLista(nuevaLista: List<Ticket>) {
        tickets = nuevaLista
        notifyDataSetChanged()
    }
}
