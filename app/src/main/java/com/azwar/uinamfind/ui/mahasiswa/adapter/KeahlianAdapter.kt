package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemKeahlianBinding

class KeahlianAdapter() : RecyclerView.Adapter<KeahlianAdapter.MyHolderView>() {
    class MyHolderView(itemKeahlianBinding: ItemKeahlianBinding) :
        RecyclerView.ViewHolder(itemKeahlianBinding.root) {

        fun bind() {
            with(itemView) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemKeahlianBinding =
            ItemKeahlianBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return MyHolderView(itemKeahlianBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 10
}