package com.azwar.uinamfind.ui.lembaga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azwar.uinamfind.databinding.FragmentFotoLembagaBinding

class FotoLembagaFragment : Fragment() {
    private var _binding: FragmentFotoLembagaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFotoLembagaBinding.inflate(inflater, container, false)
        val view = binding.root



        return view
    }
}