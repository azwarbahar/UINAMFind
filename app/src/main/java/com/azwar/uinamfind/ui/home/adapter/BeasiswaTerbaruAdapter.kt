package com.azwar.uinamfind.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R

class BeasiswaTerbaruAdapter() : RecyclerView.Adapter<BeasiswaTerbaruAdapter.MyHolderView>() {

    class MyHolderView(view: View) : RecyclerView.ViewHolder(view) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_beasiswa_terbaru_home, parent, false)

        return MyHolderView(view)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {

    }

    override fun getItemCount() = 10
}