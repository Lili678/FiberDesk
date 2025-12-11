package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fiberdesk_app.data.model.Material
import com.example.fiberdesk_app.databinding.FragmentAddItemBinding

class AddItemFragment : Fragment() {

    private var _binding: FragmentAddItemBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: InventoryViewModel by viewModels()

        // If we received args, populate fields for editing
        val args = arguments
        val existingId = args?.getString("_id")
        val existingName = args?.getString("nombre")
        val existingCantidad = args?.getInt("cantidad")
        val existingDescripcion = args?.getString("descripcion")

        if (!existingName.isNullOrEmpty()) {
            binding.etMaterialName.setText(existingName)
        }
        if (existingCantidad != null) {
            binding.etMaterialQuantity.setText(existingCantidad.toString())
        }
        if (!existingDescripcion.isNullOrEmpty()) {
            binding.etMaterialDescription.setText(existingDescripcion)
        }

        binding.btnAdd.setOnClickListener {
            val name = binding.etMaterialName.text.toString().trim()
            if (name.isNotEmpty()) {
                val cantidad = binding.etMaterialQuantity.text.toString().toIntOrNull() ?: 0
                val descripcion = binding.etMaterialDescription.text.toString().trim().ifEmpty { null }

                val material = Material(
                    _id = existingId,
                    nombre = name,
                    cantidad = cantidad,
                    descripcion = descripcion,
                    fechaRegistro = null
                )

                if (existingId != null) {
                    // update
                    viewModel.actualizarMaterial(existingId, material)
                    Toast.makeText(requireContext(), "Material actualizado", Toast.LENGTH_SHORT).show()
                } else {
                    // add
                    viewModel.agregarMaterial(material)
                    Toast.makeText(requireContext(), "Material agregado: $name", Toast.LENGTH_SHORT).show()
                }
                // navigate back to list
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Ingresa un nombre", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
