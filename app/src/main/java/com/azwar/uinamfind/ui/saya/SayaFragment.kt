package com.azwar.uinamfind.ui.saya

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.FragmentSayaBinding
import com.azwar.uinamfind.ui.home.HomeFragment
import com.azwar.uinamfind.ui.mahasiswa.adapter.KeahlianAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PendidikanAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PengalamanAdapter

class SayaFragment : Fragment() {

    private var _sayaBinding: FragmentSayaBinding? = null
    private val sayaBinding get() = _sayaBinding!!

    private lateinit var keahlianAdapter: KeahlianAdapter

    private lateinit var pengalamanAdapter: PengalamanAdapter

    private lateinit var pendidikanAdapter: PendidikanAdapter

    private lateinit var organisasiMahasiswaAdapter: OrganisasiMahasiswaAdapter

    companion object {
        fun newInstance() = SayaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _sayaBinding = FragmentSayaBinding.inflate(inflater, container, false)



        return sayaBinding.root
    }
}