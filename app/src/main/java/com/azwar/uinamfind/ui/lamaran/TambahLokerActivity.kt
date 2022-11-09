package com.azwar.uinamfind.ui.lamaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityTambahLokerBinding

class TambahLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahLokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}