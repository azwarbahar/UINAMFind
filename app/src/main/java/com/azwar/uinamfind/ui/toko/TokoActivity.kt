package com.azwar.uinamfind.ui.toko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.home.adapter.BeasiswaTerbaruAdapter
import com.azwar.uinamfind.ui.toko.adapter.MarketAdapter
import kotlinx.android.synthetic.main.activity_toko.*

class TokoActivity : AppCompatActivity() {

    private lateinit var marketAdapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toko)

        // List Data Market
        rv_market.layoutManager = GridLayoutManager(this, 2)
        marketAdapter = MarketAdapter()
        rv_market.adapter = marketAdapter
        
        img_back_market.setOnClickListener {
            finish()
        }

    }
}