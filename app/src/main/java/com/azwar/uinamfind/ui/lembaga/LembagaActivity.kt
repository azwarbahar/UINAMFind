package com.azwar.uinamfind.ui.lembaga

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.lembaga.adapter.FakultasLembagaAdapter
import kotlinx.android.synthetic.main.activity_lembaga.*

class LembagaActivity : AppCompatActivity() {

    private lateinit var fakultasLembagaAdapter: FakultasLembagaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lembaga)

        // List Fakultas Lembaga
        val rv_fakultas_lembaga: RecyclerView = findViewById(R.id.rv_fakultas_lembaga)
        rv_fakultas_lembaga.layoutManager = LinearLayoutManager(this)
        fakultasLembagaAdapter = FakultasLembagaAdapter()
        rv_fakultas_lembaga.adapter = fakultasLembagaAdapter

        img_back_lembaga.setOnClickListener {
            finish()
        }

        img_search_lembaga.setOnClickListener {
            var intent_search = Intent(this, SearchLembagaActivity::class.java)
            startActivity(intent_search)
        }

    }
}