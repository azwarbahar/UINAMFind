package com.azwar.uinamfind.ui.loker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityLamarLokerBinding

class LamarLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLamarLokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLamarLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}