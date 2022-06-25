package com.azwar.uinamfind.ui.loker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ActivityLokerBinding
import com.azwar.uinamfind.ui.loker.adapter.LokerAdapter

class LokerActivity : AppCompatActivity() {

    private lateinit var lokerBinding: ActivityLokerBinding

    private lateinit var lokerAdapter: LokerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lokerBinding = ActivityLokerBinding.inflate(layoutInflater)
        setContentView(lokerBinding.root)

        val rv_loker = lokerBinding.rvLoker

        val layoutManagerLoker: RecyclerView.LayoutManager = LinearLayoutManager(this)
        rv_loker.layoutManager = layoutManagerLoker
        lokerAdapter = LokerAdapter()
        rv_loker.adapter = lokerAdapter

        lokerBinding.imgBackLoker.setOnClickListener {
            finish()
        }
    }
}