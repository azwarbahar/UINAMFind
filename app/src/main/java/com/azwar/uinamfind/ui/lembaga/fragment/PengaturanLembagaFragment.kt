package com.azwar.uinamfind.ui.lembaga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.FragmentPengaturanLembagaBinding
import com.azwar.uinamfind.utils.Constanta
import com.google.gson.Gson

class PengaturanLembagaFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper
    private var _binding: FragmentPengaturanLembagaBinding? = null
    private val binding get() = _binding!!

    private lateinit var lembaga: LembagaKampus
    private lateinit var organisasi: Organisasi
    private lateinit var ukm: Ukm
    private var kategori_objek = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPengaturanLembagaBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedPref = PreferencesHelper(context)
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        kategori_objek = sharedPref.getString(Constanta.KEY_OBJECT_SELECTED).toString()
        val gson = Gson()
        if (kategori_objek.equals("Lembaga")) {
            lembaga = gson.fromJson(json, LembagaKampus::class.java)
        } else if (kategori_objek.equals("Organisasi")) {
            organisasi = gson.fromJson(json, Organisasi::class.java)
        } else if (kategori_objek.equals("UKM")) {
            ukm = gson.fromJson(json, Ukm::class.java)
        }

        return view
    }

}