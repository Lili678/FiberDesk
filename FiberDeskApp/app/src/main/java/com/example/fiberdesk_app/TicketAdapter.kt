package com.example.fiberdesk_app
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.TicketDetailActivity
import com.example.fiberdesk_app.Ticket

class TicketAdapter(private val tickets: List<Ticket>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketViewHolder {
        TODO("Not yet implemented")
    }

    // ... (Métodos onCreateViewHolder y getItemCount omitidos por brevedad) ...

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]

        // Asignación de datos visibles en la lista
        // (Ya tienes la lógica de asignación de Folio, Cliente y color de Prioridad aquí)

        holder.itemView.setOnClickListener {
            // 1. Crear el Intent para ir a la actividad de detalles
            val intent = Intent(holder.itemView.context, TicketDetailActivity::class.java).apply {
                // 2. Adjuntar el objeto Ticket completo al Intent
                // La clave "TICKET_EXTRA" se usa para recuperarlo después
                putExtra("TICKET_EXTRA", ticket)
            }
            // 3. Iniciar la actividad
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // ... (Define aquí tus TextViews del item_ticket.xml: ticketCode, statusCircle, etc.) ...
    }
}