package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.databinding.FragmentInventarioBinding
import com.example.fiberdesk_app.viewmodel.InstalacionViewModel

class FragmentInventario : Fragment() {

    private var _binding: FragmentInventarioBinding? = null
    private val binding get() = _binding!!

    private val inventoryViewModel: InventoryViewModel by viewModels()
    private val instalacionViewModel: InstalacionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cargar datos del backend
        loadDashboardData()

        // Observar cambios en materiales
        inventoryViewModel.materiales.observe(viewLifecycleOwner) { materiales ->
            val totalMateriales = materiales?.size ?: 0
            val bajoStock = materiales?.count { it.cantidad < 5 } ?: 0
            
            binding.txtTotalMateriales.text = totalMateriales.toString()
            binding.txtLowStock.text = bajoStock.toString()
        }

        // Observar cambios en instalaciones
        instalacionViewModel.instalaciones.observe(viewLifecycleOwner) { instalaciones ->
            val pendientes = instalaciones?.count { it.estado == "pendiente" } ?: 0
            val completadas = instalaciones?.count { it.estado == "completada" } ?: 0
            
            binding.txtInstPendientes.text = pendientes.toString()
            binding.txtInstCompletadas.text = completadas.toString()
        }

        // Errores
        inventoryViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), "Inventario: $mensaje", Toast.LENGTH_SHORT).show()
        }

        instalacionViewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), "Instalaciones: $mensaje", Toast.LENGTH_SHORT).show()
        }

        // Navegación: Gestionar Inventario
        binding.cardGestionarInventario.setOnClickListener {
            findNavController().navigate(R.id.action_inventario_to_menu)
        }

        // Navegación: Mis Instalaciones (vaya directamente a la lista)
        binding.cardMisInstalaciones.setOnClickListener {
            findNavController().navigate(R.id.instalacionesListFragment)
        }

        // Click en instalaciones completadas para filtrar
        binding.txtInstCompletadas.setOnClickListener {
            val bundle = Bundle().apply {
                putString("filter_state", "completada")
            }
            findNavController().navigate(R.id.instalacionesListFragment, bundle)
        }
    }

    private fun loadDashboardData() {
        inventoryViewModel.obtenerMateriales()
        instalacionViewModel.obtenerInstalaciones()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
