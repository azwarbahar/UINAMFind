package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemPengalamanBinding

class PengalamanAdapter() : RecyclerView.Adapter<PengalamanAdapter.MyHolderView>() {
    class MyHolderView(itemPengalamanBinding: ItemPengalamanBinding) :
        RecyclerView.ViewHolder(itemPengalamanBinding.root) {

        fun bind() {
            with(itemView) {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemPengalamanBinding = ItemPengalamanBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )


        return MyHolderView(itemPengalamanBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 4
}