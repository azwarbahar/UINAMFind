package com.azwar.uinamfind.ui.magang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemMagangTerbaruBinding

class MagangTerbaruAdapter() : RecyclerView.Adapter<MagangTerbaruAdapter.MyHolderView>() {
    class MyHolderView(itemMagangTerbaruBinding: ItemMagangTerbaruBinding) :
        RecyclerView.ViewHolder(itemMagangTerbaruBinding.root) {

        fun bind(position: Int) {
            with(itemView) {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {

        val itemMagangTerbaruBinding =
            ItemMagangTerbaruBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemMagangTerbaruBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}