package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.databinding.FragmentUseMaterialBinding

class UseMaterialFragment : Fragment() {

    private var _binding: FragmentUseMaterialBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InstalacionViewModel by viewModels()
    private var instalacionId: String? = null
    private var materiales: List<Material> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUseMaterialBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener args
        arguments?.let {
            instalacionId = it.getString("instalacionId")
            val cliente = it.getString("cliente", "")
            val direccion = it.getString("direccion", "")
            binding.txtInstalacionInfo.text = "$cliente - $direccion"
        }

        // Cargar materiales
        viewModel.obtenerMateriales()

        viewModel.materiales.observe(viewLifecycleOwner) { lista ->
            materiales = lista
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                lista.map { "${it.nombre} (Stock: ${it.cantidad})" }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerMaterial.adapter = adapter

            binding.spinnerMaterial.setOnItemSelectedListener(
                object : android.widget.AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: android.widget.AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val stock = materiales.getOrNull(position)?.cantidad ?: 0
                        binding.txtStockDisponible.text = "Stock disponible: $stock"
                    }

                    override fun onNothingSelected(parent: android.widget.AdapterView<*>?) {
                        binding.txtStockDisponible.text = "Stock disponible: -"
                    }
                }
            )
        }

        binding.btnUsar.setOnClickListener {
            val selectedIndex = binding.spinnerMaterial.selectedItemPosition
            if (selectedIndex < 0) {
                Toast.makeText(requireContext(), "Selecciona un material", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = binding.etCantidad.text.toString().toIntOrNull()
            if (cantidad == null || cantidad <= 0) {
                Toast.makeText(requireContext(), "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val material = materiales.getOrNull(selectedIndex)
            if (material == null) {
                Toast.makeText(requireContext(), "Material inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (material.cantidad < cantidad) {
                Toast.makeText(requireContext(), "Stock insuficiente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.usarMaterial(instalacionId ?: return@setOnClickListener, material._id ?: return@setOnClickListener, cantidad)
            Toast.makeText(requireContext(), "Material usado correctamente", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        binding.btnCancelar.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
