package com.example.fiberdesk_app.ui.inventario

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fiberdesk_app.R
import com.example.fiberdesk_app.databinding.FragmentInventoryMenuBinding

class InventoryMenuFragment : Fragment() {

    private var _binding: FragmentInventoryMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInventoryMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón para ver lista de materiales
        binding.cardViewList.setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_list)
        }

        // Botón para agregar nuevo material
        binding.cardViewAdd.setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_add)
        }

        // Botón para ver instalaciones
        binding.cardViewInstalaciones.setOnClickListener {
            findNavController().navigate(R.id.action_menu_to_instalaciones)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
