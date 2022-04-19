package com.azwar.uinamfind.ui.magang

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.databinding.ActivityDetailMagangBinding

class DetailMagangActivity : AppCompatActivity() {

    private lateinit var detailMagangBinding: ActivityDetailMagangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailMagangBinding = ActivityDetailMagangBinding.inflate(layoutInflater)
        setContentView(detailMagangBinding.root)

    }
}