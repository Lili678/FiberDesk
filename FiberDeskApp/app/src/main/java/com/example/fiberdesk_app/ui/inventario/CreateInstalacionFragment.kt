package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.fiberdesk_app.data.model.Instalacion
import com.example.fiberdesk_app.databinding.FragmentCreateInstalacionBinding

class CreateInstalacionFragment : Fragment() {

    private var _binding: FragmentCreateInstalacionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InstalacionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateInstalacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreateInstalacion.setOnClickListener {
            val cliente = binding.etCliente.text.toString().trim()
            val direccion = binding.etDireccion.text.toString().trim()

            if (cliente.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa el nombre del cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (direccion.isEmpty()) {
                Toast.makeText(requireContext(), "Ingresa la dirección", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.createInstalacion(cliente, direccion)
            Toast.makeText(requireContext(), "Instalación creada", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
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
