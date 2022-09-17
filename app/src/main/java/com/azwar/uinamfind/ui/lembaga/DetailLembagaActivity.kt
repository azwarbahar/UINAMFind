package com.azwar.uinamfind.ui.lembaga

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
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
        "Foto",
        "Pengaturan"
    )
    val tabArrayAdmin = arrayOf(
        "Tentang",
        "Anggota",
        "Foto",
        "Pengaturan"
    )

    private lateinit var binding: ActivityDetailLembagaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLembagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        var id = sharedPref.getString(Constanta.ID_USER).toString()
        lembaga = intent.getParcelableExtra("lembaga")!!


        val toolbar = binding.toolbarLembagaDetail
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(lembaga.nama)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        setupShared(lembaga)
        setupTabFragment(id, lembaga)


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
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "Lembaga")
    }

    private fun setupTabFragment(id: String, lembaga: LembagaKampus) {

        val viewPager = binding.viewPagerLembaga
        val tabLayout = binding.tabLayoutLembaga

        val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 3)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

//        var admin_id = lembaga.admin
//        if (admin_id != null) {
//            if (admin_id.equals(id)) {
//                val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 4)
//                viewPager.adapter = adapter
//                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                    tab.text = tabArrayAdmin[position]
//                }.attach()
//            } else {
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}