package com.azwar.uinamfind.ui.lembaga

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityDetailLembagaBinding
import com.azwar.uinamfind.ui.lembaga.adapter.ViewPagerLembagaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson


class DetailLembagaActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private lateinit var lembaga: LembagaKampus

    val tabArray = arrayOf(
        "Tentang",
        "Anggota",
        "Foto"
    )

    private lateinit var binding: ActivityDetailLembagaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLembagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)

        lembaga = intent.getParcelableExtra("lembaga")!!
        setupShared(lembaga)
        setupTabFragment()

        val toolbar = binding.toolbarLembagaDetail
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(lembaga.nama)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)



        val foto = lembaga.foto
        if (foto.equals("-")) {
        } else {
            Glide.with(this)
                .load(foto)
                .into(binding.imgHeaderLembagaDetail)
        }


    }

    private fun setupShared(lembaga: LembagaKampus) {
        val gson = Gson()
        val lembagaJson = gson.toJson(lembaga)
        sharedPref.put(Constanta.OBJECT_SELECTED, lembagaJson)
    }

    private fun setupTabFragment() {

        val viewPager = binding.viewPagerLembaga
        val tabLayout = binding.tabLayoutLembaga

        val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle)
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