package com.azwar.uinamfind.ui.kegiatan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Kegiatan
import com.azwar.uinamfind.databinding.ActivityDetailKegiatanBinding
import com.azwar.uinamfind.ui.ShowPhotoActivity
import com.bumptech.glide.Glide

class DetailKegiatanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailKegiatanBinding

    private lateinit var kegiatan: Kegiatan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailKegiatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kegiatan = intent.getParcelableExtra("kegiatan")!!
        initDataIntent(kegiatan)

        binding.imgBack.setOnClickListener { finish() }

        binding.imgPhotoKegiatan.setOnClickListener {
            var foto = kegiatan.foto
            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }
    }

    private fun initDataIntent(kegiatan: Kegiatan) {
        var foto = kegiatan.foto.toString()
        if (foto.equals("") || foto.equals("null")) {

        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(binding.imgPhotoKegiatan)
        }

        binding.tvNamaKegiatan.setText(kegiatan.nama)
        binding.tvTempat.setText(kegiatan.tempat)
        binding.tvYanggal.setText(kegiatan.tanggal)
        binding.tvDeskripsiKegiatan.setText(kegiatan.deskripsi)

    }
}