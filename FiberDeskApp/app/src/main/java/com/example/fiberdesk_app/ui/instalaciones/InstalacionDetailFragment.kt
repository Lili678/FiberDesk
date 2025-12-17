package com.example.fiberdesk_app.ui.instalaciones

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.adapters.AvailableMaterialAdapter
import com.example.fiberdesk_app.adapters.SelectedItem
import com.example.fiberdesk_app.adapters.SelectedMaterialAdapter
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.databinding.FragmentInstalacionDetailBinding
import com.example.fiberdesk_app.ui.inventario.InstalacionViewModel

class InstalacionDetailFragment : Fragment() {

    private var _binding: FragmentInstalacionDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: InstalacionViewModel by viewModels()

    private var currentInstalacion: Instalacion? = null
    private var availableMaterials: List<Material> = emptyList()

    // materialId -> cantidad
    private val selections: MutableMap<String, Int> = mutableMapOf()

    private var availableAdapter: AvailableMaterialAdapter? = null
    private var selectedAdapter: SelectedMaterialAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstalacionDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val instalacionId = arguments?.getString("instalacionId") ?: run {
            parentFragmentManager.popBackStack()
            return
        }

        // SETUP ADAPTERS FIRST
        binding.recyclerAvailableMaterials.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSelectedMaterials.layoutManager = LinearLayoutManager(requireContext())

        selectedAdapter = SelectedMaterialAdapter(
            mutableListOf(),
            removable = true // Se actualizará según estado
        ) { materialId ->
            // REMOVER MATERIAL DE LA INSTALACIÓN
            val instalacionIdSafe = currentInstalacion?._id ?: return@SelectedMaterialAdapter
            
            if (currentInstalacion?.estado == "completada") {
                Toast.makeText(requireContext(), "No se puede modificar una instalación completada", Toast.LENGTH_SHORT).show()
                return@SelectedMaterialAdapter
            }
            
            // Usar post para evitar IllegalStateException
            binding.recyclerSelectedMaterials.post {
                viewModel.removerMaterialDeInstalacion(instalacionIdSafe, materialId)
            }
        }
        binding.recyclerSelectedMaterials.adapter = selectedAdapter

        viewModel.obtenerInstalaciones()
        viewModel.obtenerMateriales()

        // Observar errores
        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
        }

        // Observar éxito (incluyendo remover materiales)
        var lastSuccessMsg = ""
        viewModel.success.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty() && msg != lastSuccessMsg) {
                lastSuccessMsg = msg
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                viewModel.obtenerInstalaciones()
            }
        }

        // INSTALACIÓN
        viewModel.instalaciones.observe(viewLifecycleOwner) { list ->
            currentInstalacion = list.find { it._id == instalacionId }
            currentInstalacion?.let { 
                updateUI(it)
                updateAvailableRecycler(it)
            }
        }

        // MATERIALES
        viewModel.materiales.observe(viewLifecycleOwner) { materiales ->
            availableMaterials = materiales
                ?.filter { (it.cantidad ?: 0) > 0 }
                ?: emptyList()

            // SETUP AVAILABLE RECYCLER (primera vez)
            if (binding.recyclerAvailableMaterials.adapter == null) {
                availableAdapter = AvailableMaterialAdapter(
                    availableMaterials,
                    selections,
                    editable = currentInstalacion?.estado != "completada"
                ) { materialId, selected, quantity ->
                    updateSelectedRecycler()
                }
                binding.recyclerAvailableMaterials.adapter = availableAdapter
            } else {
                availableAdapter?.notifyDataSetChanged()
            }
        }

        // GUARDAR
        binding.btnGuardar.setOnClickListener {
            val instalacionIdSafe = currentInstalacion?._id ?: return@setOnClickListener
            
            // Validar que no esté completada
            if (currentInstalacion?.estado == "completada") {
                Toast.makeText(requireContext(), "No se puede modificar una instalación completada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val filtered = selections.filter { it.value > 0 }
            if (filtered.isEmpty()) {
                Toast.makeText(requireContext(), "No hay materiales seleccionados", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(requireContext())
                .setTitle("Confirmar")
                .setMessage("¿Guardar ${filtered.size} material(es)?")
                .setPositiveButton("Guardar") { _, _ ->
                    android.util.Log.d("InstalacionDetail", "Guardando materiales: $filtered")
                    viewModel.usarMaterials(instalacionIdSafe, filtered)
                    selections.clear()
                    updateSelectedRecycler()
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.btnVolver.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun updateAvailableRecycler(instalacion: Instalacion) {
        val editable = instalacion.estado != "completada"
        
        // Actualizar adaptador con estado nuevo
        if (availableAdapter == null && availableMaterials.isNotEmpty()) {
            availableAdapter = AvailableMaterialAdapter(
                availableMaterials,
                selections,
                editable = editable
            ) { _, _, _ ->
                updateSelectedRecycler()
            }
            binding.recyclerAvailableMaterials.adapter = availableAdapter
        }
    }

    private fun updateUI(instalacion: Instalacion) {
        binding.txtCliente.text = instalacion.cliente
        binding.txtDireccion.text = instalacion.direccion

        val editable = instalacion.estado != "completada"
        
        // DESHABILITAR SI ESTÁ COMPLETADA
        binding.btnGuardar.isEnabled = editable
        binding.btnGuardar.alpha = if (editable) 1f else 0.5f
        binding.recyclerAvailableMaterials.isEnabled = editable
        
        // MOSTRAR MATERIALES YA GUARDADOS EN LA INSTALACIÓN
        if (instalacion.materialesUsados.isNullOrEmpty()) {
            binding.txtMaterialesUsados.visibility = View.VISIBLE
            binding.recyclerSelectedMaterials.visibility = View.GONE
        } else {
            binding.txtMaterialesUsados.visibility = View.GONE
            binding.recyclerSelectedMaterials.visibility = View.VISIBLE
            
            val savedItems = instalacion.materialesUsados
                .filter { it.cantidad > 0 && it.material != null }  // Solo mostrar con cantidad > 0 y material válido
                .mapNotNull { used ->
                    used.material?.let { mat ->
                        SelectedItem(
                            mat._id ?: "",
                            mat.nombre,
                            used.cantidad
                        )
                    }
                }
            
            // Actualizar adapter
            selectedAdapter?.removable = editable
            selectedAdapter?.update(savedItems)
        }
    }

    private fun updateSelectedRecycler() {
        val list = selections.mapNotNull { (id, qty) ->
            if (qty <= 0) null
            else {
                val name = availableMaterials.find { it._id == id }?.nombre ?: id
                SelectedItem(id, name, qty)
            }
        }

        binding.txtMaterialesUsados.visibility =
            if (list.isEmpty()) View.VISIBLE else View.GONE
        binding.recyclerSelectedMaterials.visibility =
            if (list.isEmpty()) View.GONE else View.VISIBLE

        // Usar post para evitar IllegalStateException durante layout calculations
        binding.recyclerSelectedMaterials.post {
            selectedAdapter?.update(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
