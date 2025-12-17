package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.databinding.FragmentInstalacionDetailBinding

class InstalacionDetailFragment : Fragment() {

    private var _binding: FragmentInstalacionDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InstalacionViewModel by viewModels()
    private var instalacion: Instalacion? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstalacionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ocultar elementos no necesarios en este fragmento
        binding.recyclerAvailableMaterials.visibility = View.GONE
        binding.recyclerSelectedMaterials.visibility = View.GONE
        binding.btnGuardar.visibility = View.GONE
        binding.btnVolver.visibility = View.GONE
        binding.btnUsarMateriales.visibility = View.VISIBLE
        binding.btnChangeStatus.visibility = View.VISIBLE
        binding.btnBack.visibility = View.VISIBLE

        // Obtener instalación desde argumentos
        instalacion = arguments?.getParcelable("instalacion")

        instalacion?.let { inst ->
            // Mostrar información
            binding.txtCliente.text = "Cliente: ${inst.cliente}"
            binding.txtDireccion.text = "Dirección: ${inst.direccion}"
            binding.txtEstado.text = "Estado: ${inst.estado}"
            binding.txtFecha.text = "Fecha: ${inst.fechaInicio ?: "N/A"}"

            // Mostrar materiales usados
            if (inst.materialesUsados.isNullOrEmpty()) {
                binding.txtMaterialesUsados.text = "No hay materiales usados"
            } else {
                val materialesText = inst.materialesUsados.joinToString("\n") { material ->
                    "• ${material.material?.nombre ?: "Material"}: ${material.cantidad} unidades"
                }
                binding.txtMaterialesUsados.text = "Materiales usados:\n$materialesText"
            }

            // Botón para usar materiales
            binding.btnUsarMateriales.setOnClickListener {
                val fragment = UseMaterialFragment().apply {
                    arguments = Bundle().apply {
                        putString("instalacion_id", inst._id)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit()
            }

            // Botón para cambiar estado
            binding.btnChangeStatus.setOnClickListener {
                val options = arrayOf("pendiente", "en_progreso", "completada")
                androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Cambiar estado")
                    .setItems(arrayOf("Pendiente", "En Progreso", "Completada")) { _, which ->
                        val newEstado = options[which]
                        inst._id?.let { id ->
                            viewModel.updateEstado(id, newEstado)
                            binding.txtEstado.text = "Estado: $newEstado"
                            Toast.makeText(requireContext(), "Estado actualizado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .show()
            }

            // Botón regresar
            binding.btnBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(instalacion: Instalacion): InstalacionDetailFragment {
            return InstalacionDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("instalacion", instalacion)
                }
            }
        }
    }
}
