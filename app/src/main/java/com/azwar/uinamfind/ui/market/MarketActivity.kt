package com.azwar.uinamfind.ui.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.market.adapter.MarketAdapter
import kotlinx.android.synthetic.main.activity_market.*

class MarketActivity : AppCompatActivity() {

    private lateinit var marketAdapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)

        // List Data Market
        rv_market.layoutManager = GridLayoutManager(this, 2)
        marketAdapter = MarketAdapter()
        rv_market.adapter = marketAdapter

        img_back_market.setOnClickListener {
            finish()
        }

    }
}