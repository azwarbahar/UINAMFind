package com.azwar.uinamfind.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityRiwayatMarketBinding

class RiwayatMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRiwayatMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}