package com.azwar.uinamfind.ui.organisasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.organisasi.adapter.OrganisasiAdapter
import kotlinx.android.synthetic.main.activity_organisasi.*

class OrganisasiActivity : AppCompatActivity() {

    private lateinit var organisasiAdapter: OrganisasiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_organisasi)

        val rv_organisasi: RecyclerView = findViewById(R.id.rv_organisasi)
        rv_organisasi.layoutManager = LinearLayoutManager(this)
        organisasiAdapter = OrganisasiAdapter()
        rv_organisasi.adapter = organisasiAdapter

        img_search_organisasi.setOnClickListener {
            val intent_search_organisasi = Intent(this, SearchOrganisasiActivity::class.java)
            startActivity(intent_search_organisasi)
        }

        img_back_organisasi.setOnClickListener {
            finish()
        }

    }
}