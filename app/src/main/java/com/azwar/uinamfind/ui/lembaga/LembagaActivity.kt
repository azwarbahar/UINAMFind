package com.azwar.uinamfind.ui.lembaga

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ActivityLembagaBinding
import com.azwar.uinamfind.ui.lembaga.adapter.FakultasLembagaAdapter

class LembagaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fakultasLembagaAdapter: FakultasLembagaAdapter

    private lateinit var binding: ActivityLembagaBinding

    private lateinit var swipe_lembaga: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLembagaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipe_lembaga = binding.swipeLembaga
        swipe_lembaga.setOnRefreshListener(this)
        swipe_lembaga.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_lembaga.post(Runnable {
            initData()
        })

        binding.imgBackLembaga.setOnClickListener {
            finish()
        }
//
//        binding.imgSearchLembaga.setOnClickListener {
//            var intent_search = Intent(this, SearchLembagaActivity::class.java)
//            startActivity(intent_search)
//        }

    }

    private fun initData() {

        val listLembaga = arrayOf(
            "Universitas", "Syariah dan Hukum", "Tarbiah dan Keguruan",
            "Ushuluddin dan Filsafat", "Adab dan Humaniora", "Dakwah dan Komunikasi",
            "Sains dan Teknologi", "Kedokteran dan Ilmu Kesehatan", "Ekonomi dan Bisnis"
        )

        // List Fakultas Lembaga
        val rv_fakultas = binding.rvFakultasLembaga
        rv_fakultas.layoutManager = LinearLayoutManager(this)
        fakultasLembagaAdapter = FakultasLembagaAdapter(listLembaga)
        rv_fakultas.adapter = fakultasLembagaAdapter

        swipe_lembaga.isRefreshing = false
    }

    override fun onRefresh() {
        initData()
    }
}