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
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.databinding.FragmentMaterialsListBinding

class MaterialsListFragment : Fragment(), MaterialAdapter.OnItemActionListener {

    private var _binding: FragmentMaterialsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var adapter: MaterialAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MaterialAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.materiales.observe(viewLifecycleOwner) { lista ->
            adapter.updateData(lista ?: emptyList())
            // stop refreshing when data arrives
            binding.swipeRefresh.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
            binding.swipeRefresh.isRefreshing = false
        }

        // Pull-to-refresh
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.obtenerMateriales()
        }

        // FAB to add new material
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addItemFragment)
        }

        // initial load
        binding.recyclerView.post { viewModel.obtenerMateriales() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Edit: open AddItemFragment with args
    override fun onItemClick(material: Material) {
        val bundle = Bundle().apply {
            putString("_id", material._id)
            putString("nombre", material.nombre)
            putInt("cantidad", material.cantidad)
            putString("descripcion", material.descripcion)
        }
        findNavController().navigate(R.id.addItemFragment, bundle)
    }

    // Delete on long click (confirmation handled in ViewModel or here via Toast)
    override fun onItemLongClick(material: Material): Boolean {
        // Simple confirm via toast and perform delete
        viewModel.eliminarMaterial(material._id ?: return true)
        Toast.makeText(requireContext(), "Eliminando ${material.nombre}", Toast.LENGTH_SHORT).show()
        return true
    }
}
