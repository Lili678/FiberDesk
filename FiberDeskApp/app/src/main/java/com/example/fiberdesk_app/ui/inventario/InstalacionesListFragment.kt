package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.adapters.InstalacionAdapter
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.databinding.FragmentInstalacionesListBinding

class InstalacionesListFragment : Fragment(), InstalacionAdapter.OnItemActionListener {

    private var _binding: FragmentInstalacionesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InstalacionViewModel by viewModels()
    private lateinit var adapter: InstalacionAdapter
    private var filterState: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstalacionesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get filter state from arguments
        filterState = arguments?.getString("filter_state")

        adapter = InstalacionAdapter(emptyList(), this)
        binding.recyclerViewInstalaciones.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewInstalaciones.adapter = adapter

        viewModel.instalaciones.observe(viewLifecycleOwner) { lista ->
            // Apply filter if specified
            val filtered = if (filterState != null) {
                lista?.filter { it.estado == filterState } ?: emptyList()
            } else {
                lista ?: emptyList()
            }
            adapter.updateData(filtered)
            binding.swipeRefreshInstalaciones.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            binding.swipeRefreshInstalaciones.isRefreshing = false
        }

        binding.swipeRefreshInstalaciones.setOnRefreshListener {
            viewModel.obtenerInstalaciones()
        }

        binding.fabAddInstalacion.setOnClickListener {
            findNavController().navigate(R.id.createInstalacionFragment)
        }

        binding.recyclerViewInstalaciones.post { viewModel.obtenerInstalaciones() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStatusClick(instalacion: Instalacion) {
        // show dialog to pick new estado
        val options = arrayOf("pendiente", "en_progreso", "completada")
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Cambiar estado")
            .setItems(arrayOf("Pendiente", "En Progreso", "Completada")) { _, which ->
                val newEstado = options[which]
                instalacion._id?.let { id ->
                    viewModel.updateEstado(id, newEstado)
                }
            }
            .show()
    }

    override fun onDelete(instalacion: Instalacion) {
        viewModel.deleteInstalacion(instalacion._id ?: return)
        Toast.makeText(requireContext(), "Instalación eliminada", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(instalacion: Instalacion) {
        val bundle = Bundle().apply {
            putString("instalacionId", instalacion._id)
        }
        findNavController().navigate(R.id.instalacionDetailFragment, bundle)
    }}