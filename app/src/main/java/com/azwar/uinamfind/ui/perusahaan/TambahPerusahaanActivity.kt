package com.azwar.uinamfind.ui.perusahaan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.databinding.ActivityTambahPerusahaanBinding

class TambahPerusahaanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahPerusahaanBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}