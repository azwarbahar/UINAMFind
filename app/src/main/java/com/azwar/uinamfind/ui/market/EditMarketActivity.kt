package com.azwar.uinamfind.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityEditMarketBinding

class EditMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditMarketBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}