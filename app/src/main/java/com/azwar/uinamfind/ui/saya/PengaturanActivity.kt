package com.azwar.uinamfind.ui.saya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityPengaturanBinding
import com.azwar.uinamfind.ui.MasukActivity

class PengaturanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengaturanBinding

    private lateinit var sharedPref: PreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        binding.btnKeluar.setOnClickListener {
            sharedPref.logout()
            val intent = Intent(this, MasukActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}