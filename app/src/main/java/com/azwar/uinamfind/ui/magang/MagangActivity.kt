package com.azwar.uinamfind.ui.magang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azwar.uinamfind.databinding.ActivityMagangBinding
import com.azwar.uinamfind.ui.magang.adapter.MagangBersertifikatAdapter
import com.azwar.uinamfind.ui.magang.adapter.MagangTerbaruAdapter

class MagangActivity : AppCompatActivity() {

    private lateinit var magangBersertifikatAdapter: MagangBersertifikatAdapter
    private lateinit var magangTerbaruAdapter: MagangTerbaruAdapter

    private lateinit var magangBinding: ActivityMagangBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        magangBinding = ActivityMagangBinding.inflate(layoutInflater)
        setContentView(magangBinding.root)

        val rv_magang_bersertifikat = magangBinding.rvMagangBersertifikat
        rv_magang_bersertifikat.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        magangBersertifikatAdapter = MagangBersertifikatAdapter()
        rv_magang_bersertifikat.adapter = magangBersertifikatAdapter

        val rv_magang_terbaru = magangBinding.rvMagangTerbaru
        rv_magang_terbaru.layoutManager = LinearLayoutManager(this)
        magangTerbaruAdapter = MagangTerbaruAdapter()
        rv_magang_terbaru.adapter = magangTerbaruAdapter

    }
}