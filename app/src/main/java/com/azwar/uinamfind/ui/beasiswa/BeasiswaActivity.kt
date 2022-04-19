package com.azwar.uinamfind.ui.beasiswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ActivityBeasiswaBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter

class BeasiswaActivity : AppCompatActivity() {

    private lateinit var beasiswaBinding: ActivityBeasiswaBinding

    private lateinit var beasiswaAdapter: BeasiswaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beasiswaBinding = ActivityBeasiswaBinding.inflate(layoutInflater)
        setContentView(beasiswaBinding.root)

        val layoutManagerBeasiswa: RecyclerView.LayoutManager = LinearLayoutManager(this)
        beasiswaBinding.rvBeasiswa.layoutManager = layoutManagerBeasiswa
        beasiswaAdapter = BeasiswaAdapter()
        beasiswaBinding.rvBeasiswa.adapter = beasiswaAdapter

        beasiswaBinding.imgBackBeasiswa.setOnClickListener {
            finish()
        }

    }
}