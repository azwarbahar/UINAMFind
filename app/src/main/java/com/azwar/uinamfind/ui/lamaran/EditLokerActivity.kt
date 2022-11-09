package com.azwar.uinamfind.ui.lamaran

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityEditLokerBinding

class EditLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditLokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}