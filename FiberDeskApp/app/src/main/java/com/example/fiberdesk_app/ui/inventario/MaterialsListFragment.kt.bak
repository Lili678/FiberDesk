package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.fiberdesk_app.data.local.LocalDataSource
import com.example.fiberdesk_app.utils.FileUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.databinding.FragmentMaterialsListBinding
import androidx.core.widget.addTextChangedListener

class MaterialsListFragment : Fragment(), MaterialAdapter.OnItemActionListener {

    private var _binding: FragmentMaterialsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InventoryViewModel by viewModels()
    private lateinit var adapter: MaterialAdapter
    private lateinit var localDataSource: LocalDataSource
    private var allMaterials: List<Material> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMaterialsListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        localDataSource = LocalDataSource(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MaterialAdapter(emptyList(), this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.materiales.observe(viewLifecycleOwner) { lista ->
            allMaterials = lista ?: emptyList()
            adapter.updateData(allMaterials)
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

        // Search functionality
        binding.etSearch.addTextChangedListener { searchText ->
            val query = searchText.toString().lowercase()
            val filtered = if (query.isEmpty()) {
                allMaterials
            } else {
                allMaterials.filter { it.nombre.lowercase().contains(query) }
            }
            adapter.updateData(filtered)
        }

        // FAB to add new material
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.addItemFragment)
        }

        // initial load
        binding.recyclerView.post { viewModel.obtenerMateriales() }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_export -> {
                val materials = viewModel.materiales.value ?: emptyList()
                val file = FileUtil.exportMaterials(requireContext(), materials)
                Toast.makeText(requireContext(), "Exportado: ${file.name}", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_import -> {
                val imported = FileUtil.importMaterials(requireContext())
                // save to local DB and refresh UI
                imported.forEach { localDataSource.saveMaterial(it) }
                adapter.updateData(localDataSource.getAllMaterials())
                Toast.makeText(requireContext(), "Importado ${imported.size} materiales", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_settings -> {
                // open simple dialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Ajustes")
                    .setMessage("Aquí puedes configurar preferencias locales.")
                    .setPositiveButton("Aceptar", null)
                    .show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar material")
            .setMessage("¿Deseas eliminar ${material.nombre}?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Eliminar") { _, _ ->
                viewModel.eliminarMaterial(material._id ?: return@setPositiveButton)
                localDataSource.deleteMaterial(material._id ?: return@setPositiveButton)
                Toast.makeText(requireContext(), "Eliminado ${material.nombre}", Toast.LENGTH_SHORT).show()
            }
            .show()
        return true
    }
}
