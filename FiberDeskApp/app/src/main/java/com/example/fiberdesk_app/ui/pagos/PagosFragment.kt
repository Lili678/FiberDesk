package com.example.fiberdesk_app.ui.pagos

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.*
import com.example.fiberdesk_app.databinding.DialogPagoBinding
import com.example.fiberdesk_app.databinding.FragmentPagosBinding
import java.text.SimpleDateFormat
import java.util.*

class PagosFragment : Fragment() {
    
    private var _binding: FragmentPagosBinding? = null
    private val binding get() = _binding!!
    
    private val viewModel: PagosViewModel by viewModels()
    private lateinit var pagosAdapter: PagosAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPagosBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupObservers()
        setupListeners()
        
        // Cargar pagos al iniciar
        viewModel.cargarPagos()
    }
    
    private fun setupRecyclerView() {
        pagosAdapter = PagosAdapter(
            onEditClick = { pago ->
                mostrarDialogPago(pago)
            },
            onDeleteClick = { pago ->
                mostrarDialogEliminar(pago)
            }
        )
        
        binding.recyclerViewPagos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagosAdapter
        }
    }
    
    private fun setupObservers() {
        // Observar lista de pagos
        viewModel.pagos.observe(viewLifecycleOwner) { pagos ->
            pagosAdapter.submitList(pagos)
            
            // Mostrar/ocultar empty state
            if (pagos.isEmpty()) {
                binding.emptyState.visibility = View.VISIBLE
                binding.recyclerViewPagos.visibility = View.GONE
            } else {
                binding.emptyState.visibility = View.GONE
                binding.recyclerViewPagos.visibility = View.VISIBLE
            }
            
            // Actualizar estadísticas
            actualizarEstadisticas()
        }
        
        // Observar loading
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }
        
        // Observar mensajes de éxito
        viewModel.success.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
    }
    
    private fun setupListeners() {
        binding.fabAgregarPago.setOnClickListener {
            mostrarDialogPago(null)
        }
    }
    
    private fun actualizarEstadisticas() {
        val stats = viewModel.calcularEstadisticas()
        binding.tvTotalPagos.text = String.format("%.2f", stats["total"] ?: 0.0)
        binding.tvPendientes.text = String.format("%.2f", stats["pendientes"] ?: 0.0)
        binding.tvParciales.text = String.format("%.2f", stats["parciales"] ?: 0.0)
        binding.tvPagados.text = String.format("%.2f", stats["pagados"] ?: 0.0)
    }
    
    private fun mostrarDialogPago(pago: Pago?) {
        val dialogBinding = DialogPagoBinding.inflate(layoutInflater)
        
        // Configurar spinner de método de pago
        val metodosPago = MetodoPago.values().map { it.displayName }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, metodosPago)
        dialogBinding.spinnerMetodoPago.setAdapter(adapter)
        
        // Si es edición, llenar campos
        pago?.let {
            dialogBinding.tvTitulo.text = "Editar Pago"
            dialogBinding.etUsuarioId.setText(it.usuarioId)
            dialogBinding.etMonto.setText(it.monto.toString())
            dialogBinding.etAbono.setText(it.abono.toString())
            dialogBinding.spinnerMetodoPago.setText(
                MetodoPago.fromValor(it.metodoPago)?.displayName ?: "Efectivo",
                false
            )
            dialogBinding.etFechaPago.setText(formatearFecha(it.fechaPago))
            dialogBinding.etDescripcion.setText(it.descripcion)
            dialogBinding.etUsuarioId.isEnabled = false
        } ?: run {
            dialogBinding.tvTitulo.text = "Nuevo Pago"
            dialogBinding.etFechaPago.setText(obtenerFechaActual())
            dialogBinding.spinnerMetodoPago.setText("Efectivo", false)
        }
        
        // Configurar selector de fecha
        dialogBinding.etFechaPago.setOnClickListener {
            mostrarDatePicker { fecha ->
                dialogBinding.etFechaPago.setText(fecha)
            }
        }
        
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()
        
        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        
        dialogBinding.btnGuardar.setOnClickListener {
            if (validarCampos(dialogBinding)) {
                val usuarioId = dialogBinding.etUsuarioId.text.toString()
                val monto = dialogBinding.etMonto.text.toString().toDouble()
                val abono = dialogBinding.etAbono.text.toString().toDoubleOrNull() ?: 0.0
                val metodoPago = obtenerMetodoPagoValor(dialogBinding.spinnerMetodoPago.text.toString())
                val fechaPago = convertirFechaISO(dialogBinding.etFechaPago.text.toString())
                val descripcion = dialogBinding.etDescripcion.text.toString()
                
                if (pago == null) {
                    // Crear nuevo pago
                    val nuevoPago = CrearPagoRequest(
                        usuarioId = usuarioId,
                        monto = monto,
                        abono = abono,
                        metodoPago = metodoPago,
                        fechaPago = fechaPago,
                        descripcion = descripcion
                    )
                    viewModel.crearPago(nuevoPago)
                } else {
                    // Actualizar pago existente
                    val actualizarPago = ActualizarPagoRequest(
                        monto = monto,
                        abono = abono,
                        metodoPago = metodoPago,
                        descripcion = descripcion
                    )
                    viewModel.actualizarPago(pago._id ?: "", actualizarPago)
                }
                
                dialog.dismiss()
            }
        }
        
        dialog.show()
    }
    
    private fun mostrarDialogEliminar(pago: Pago) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Pago")
            .setMessage("¿Estás seguro de que deseas eliminar este pago?\n\nMonto: $${pago.monto}")
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarPago(pago._id ?: "")
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun validarCampos(binding: DialogPagoBinding): Boolean {
        if (binding.etUsuarioId.text.isNullOrBlank()) {
            binding.etUsuarioId.error = "Campo requerido"
            return false
        }
        
        if (binding.etMonto.text.isNullOrBlank()) {
            binding.etMonto.error = "Campo requerido"
            return false
        }
        
        val monto = binding.etMonto.text.toString().toDoubleOrNull()
        if (monto == null || monto <= 0) {
            binding.etMonto.error = "Monto inválido"
            return false
        }
        
        return true
    }
    
    private fun mostrarDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val fecha = String.format("%02d/%02d/%04d", day, month + 1, year)
                onDateSelected(fecha)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
    
    private fun formatearFecha(isoFecha: String): String {
        return try {
            val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdfISO.parse(isoFecha)
            sdfDisplay.format(date ?: Date())
        } catch (e: Exception) {
            isoFecha.substring(0, 10) // Fallback: tomar solo la fecha
        }
    }
    
    private fun convertirFechaISO(fecha: String): String {
        return try {
            val sdfDisplay = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdfISO.timeZone = TimeZone.getTimeZone("UTC")
            val date = sdfDisplay.parse(fecha)
            sdfISO.format(date ?: Date())
        } catch (e: Exception) {
            val sdfISO = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            sdfISO.timeZone = TimeZone.getTimeZone("UTC")
            sdfISO.format(Date())
        }
    }
    
    private fun obtenerMetodoPagoValor(displayName: String): String {
        return MetodoPago.values().find { it.displayName == displayName }?.valor ?: "efectivo"
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
