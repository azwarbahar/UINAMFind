package com.azwar.uinamfind.ui.ukm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.ukm.adapter.PendaftaranUKMAdapter
import com.azwar.uinamfind.ui.ukm.adapter.UKMAdapter
import kotlinx.android.synthetic.main.activity_ukm.*

class UKMActivity : AppCompatActivity() {

    private lateinit var pendaftaranUKMAdapter: PendaftaranUKMAdapter
    private lateinit var ukmAdapter: UKMAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ukm)

        // List Data Pendaftaran Terbuka UKM
        rv_pendaftaran_terbuka_ukm.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        pendaftaranUKMAdapter = PendaftaranUKMAdapter()
        rv_pendaftaran_terbuka_ukm.adapter = pendaftaranUKMAdapter


        // List Data UKM
        rv_ukm.layoutManager = GridLayoutManager(this, 2)
        ukmAdapter = UKMAdapter()
        rv_ukm.adapter = ukmAdapter

        rl_toolbar_ukm.setOnClickListener {
            finish()
        }

    }
}