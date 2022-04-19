package com.azwar.uinamfind.ui.toko.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R

class MarketAdapter() : RecyclerView.Adapter<MarketAdapter.MyHolderView>() {

    class MyHolderView(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_market, parent, false)

        return MyHolderView(view)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {
    }

    override fun getItemCount() = 10
}