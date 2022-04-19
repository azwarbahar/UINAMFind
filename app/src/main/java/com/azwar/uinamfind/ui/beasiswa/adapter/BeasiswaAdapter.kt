package com.azwar.uinamfind.ui.beasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemBeasiswaBinding

class BeasiswaAdapter() : RecyclerView.Adapter<BeasiswaAdapter.MyHolderView>() {
    class MyHolderView(itemBeasiswaBinding: ItemBeasiswaBinding) :
        RecyclerView.ViewHolder(itemBeasiswaBinding.root) {

        fun bind(position: Int) {
            with(itemView) {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemBeasiswaBinding =
            ItemBeasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemBeasiswaBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}