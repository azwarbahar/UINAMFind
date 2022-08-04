package com.azwar.uinamfind.ui.kegiatan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.data.models.Kegiatan
import com.azwar.uinamfind.databinding.ActivityDetailKegiatanBinding

class DetailKegiatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKegiatanBinding

    private lateinit var kegiatan: Kegiatan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKegiatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kegiatan = intent.getParcelableExtra("event")!!
        initDataIntent(kegiatan)

        binding.imgBack.setOnClickListener { finish() }

    }

    private fun initDataIntent(kegiatan: Kegiatan) {

        var foto = kegiatan.foto

        binding.tvNamaKegiatan.setText(kegiatan.nama)
        binding.tvTempat.setText(kegiatan.tempat)
        binding.tvYanggal.setText(kegiatan.tanggal)
        binding.tvDeskripsiKegiatan.setText(kegiatan.deskripsi)


    }
}