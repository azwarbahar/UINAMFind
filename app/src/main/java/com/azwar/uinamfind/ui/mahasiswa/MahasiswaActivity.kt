package com.azwar.uinamfind.ui.mahasiswa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.mahasiswa.adapter.MahasiswaGridAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.MahasiswaHorizontalAdapter
import kotlinx.android.synthetic.main.activity_mahasiswa.*

class MahasiswaActivity : AppCompatActivity() {

    private lateinit var mahasiswaHorizontalAdapter: MahasiswaHorizontalAdapter

    private lateinit var mahasiswaGridAdapter: MahasiswaGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mahasiswa)

        // List Data Mahasiswa Sefakultas
        rv_mahasiswa_sefakultas.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mahasiswaHorizontalAdapter = MahasiswaHorizontalAdapter()
        rv_mahasiswa_sefakultas.adapter = mahasiswaHorizontalAdapter

        // List data mahasiswa random
        rv_mahasiswa_random.layoutManager = GridLayoutManager(this, 2)
        mahasiswaGridAdapter = MahasiswaGridAdapter()
        rv_mahasiswa_random.adapter = mahasiswaGridAdapter


        img_back_mahasiswa.setOnClickListener {
            finish()
        }
    }
}