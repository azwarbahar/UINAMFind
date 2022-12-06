package com.azwar.uinamfind.ui.market

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.databinding.ActivityTambahMarketBinding

class TambahMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}