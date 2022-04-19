package com.azwar.uinamfind.ui.magang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemMagangBersertifikatBinding

class MagangBersertifikatAdapter() :
    RecyclerView.Adapter<MagangBersertifikatAdapter.MyHolderView>() {
    class MyHolderView(itemMagangBersertifikatBinding: ItemMagangBersertifikatBinding) :
        RecyclerView.ViewHolder(itemMagangBersertifikatBinding.root) {

            fun bind(position: Int) {
                with(itemView){

                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {

        val itemMagangBersertifikatBinding = ItemMagangBersertifikatBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return MyHolderView(itemMagangBersertifikatBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}