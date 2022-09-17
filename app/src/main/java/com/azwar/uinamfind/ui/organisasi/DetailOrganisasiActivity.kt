package com.azwar.uinamfind.ui.organisasi

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityDetailOrganisasiBinding
import com.azwar.uinamfind.ui.lembaga.adapter.ViewPagerLembagaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class DetailOrganisasiActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper

    private lateinit var binding: ActivityDetailOrganisasiBinding

    private lateinit var organisasi: Organisasi

    val tabArray = arrayOf(
        "Tentang",
        "Anggota",
        "Foto"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailOrganisasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)

        organisasi = intent.getParcelableExtra("organisasi")!!
        setupShared(organisasi)
        setupTabFragment()

        val toolbar = binding.toolbarOrganisasiDetail
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(organisasi.nama_organisasi)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)


        val foto = organisasi.foto
        if (foto.equals("-")) {
        } else {
            Glide.with(this)
                .load(foto)
                .into(binding.imgHeaderOrganisasiDetail)
        }

    }

    private fun setupShared(organisasi: Organisasi) {
        val gson = Gson()
        val organisasiJson = gson.toJson(organisasi)
        sharedPref.put(Constanta.OBJECT_SELECTED, organisasiJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "Organisasi")
    }

    private fun setupTabFragment() {

        val viewPager = binding.viewPagerOrganisasi
        val tabLayout = binding.tabLayoutOrganisasi

        val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 3)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}