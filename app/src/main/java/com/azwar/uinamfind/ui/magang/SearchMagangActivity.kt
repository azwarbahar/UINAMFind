package com.azwar.uinamfind.ui.magang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivitySearchMagangBinding

class SearchMagangActivity : AppCompatActivity() {

    private lateinit var searchMagangBinding: ActivitySearchMagangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchMagangBinding = ActivitySearchMagangBinding.inflate(layoutInflater)
        setContentView(searchMagangBinding.root)
    }
}