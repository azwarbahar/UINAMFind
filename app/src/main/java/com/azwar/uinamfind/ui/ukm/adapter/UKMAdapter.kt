package com.azwar.uinamfind.ui.ukm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemUkmBinding

class UKMAdapter() : RecyclerView.Adapter<UKMAdapter.MyHolderView>() {
    class MyHolderView(itemUkmBinding: ItemUkmBinding) :
        RecyclerView.ViewHolder(itemUkmBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemUkmBinding =
            ItemUkmBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return MyHolderView(itemUkmBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {

    }

    override fun getItemCount() = 10
}