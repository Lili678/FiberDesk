package com.example.fiberdesk_app.ui.pagos

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fiberdesk_app.data.model.EstadoPago
import com.example.fiberdesk_app.data.model.MetodoPago
import com.example.fiberdesk_app.data.model.Pago
import com.example.fiberdesk_app.databinding.ItemPagoBinding
import java.text.SimpleDateFormat
import java.util.*

class PagosAdapter(
    private val onEditClick: (Pago) -> Unit,
    private val onDeleteClick: (Pago) -> Unit
) : ListAdapter<Pago, PagosAdapter.PagoViewHolder>(PagoDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagoViewHolder {
        val binding = ItemPagoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PagoViewHolder(binding, onEditClick, onDeleteClick)
    }
    
    override fun onBindViewHolder(holder: PagoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class PagoViewHolder(
        private val binding: ItemPagoBinding,
        private val onEditClick: (Pago) -> Unit,
        private val onDeleteClick: (Pago) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pago: Pago) {
            // Estado
            val estadoPago = EstadoPago.fromValor(pago.estado)
            binding.tvEstado.text = estadoPago?.displayName?.uppercase() ?: pago.estado.uppercase()
            
            // Colores según estado
            when (pago.estado) {
                "pendiente" -> {
                    binding.tvEstado.setBackgroundColor(Color.parseColor("#D32F2F"))
                }
                "parcial" -> {
                    binding.tvEstado.setBackgroundColor(Color.parseColor("#FB8C00"))
                }
                "pagado" -> {
                    binding.tvEstado.setBackgroundColor(Color.parseColor("#43A047"))
                }
            }
            
            // Método de pago
            val metodoPago = MetodoPago.fromValor(pago.metodoPago)
            binding.tvMetodoPago.text = metodoPago?.displayName ?: pago.metodoPago
            
            // Fecha
            binding.tvFecha.text = formatearFecha(pago.fechaPago)
            
            // Montos
            binding.tvMonto.text = String.format("$%.2f", pago.monto)
            binding.tvAbono.text = String.format("$%.2f", pago.abono)
            
            // Descripción
            binding.tvDescripcion.text = if (pago.descripcion.isNotBlank()) {
                pago.descripcion
            } else {
                "Sin descripción"
            }
            
            // Botones
            binding.btnEditar.setOnClickListener {
                onEditClick(pago)
            }
            
            binding.btnEliminar.setOnClickListener {
                onDeleteClick(pago)
            }
        }
        
        private fun formatearFecha(isoFecha: String): String {
            return try {
                val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdfISO.parse(isoFecha)
                sdfDisplay.format(date ?: Date())
            } catch (e: Exception) {
                try {
                    // Intentar con formato de solo fecha
                    isoFecha.substring(0, 10).let {
                        val parts = it.split("-")
                        "${parts[2]}/${parts[1]}/${parts[0]}"
                    }
                } catch (e: Exception) {
                    isoFecha
                }
            }
        }
    }
    
    class PagoDiffCallback : DiffUtil.ItemCallback<Pago>() {
        override fun areItemsTheSame(oldItem: Pago, newItem: Pago): Boolean {
            return oldItem._id == newItem._id
        }
        
        override fun areContentsTheSame(oldItem: Pago, newItem: Pago): Boolean {
            return oldItem == newItem
        }
    }
}
