package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemPendidikanBinding

class PendidikanAdapter() : RecyclerView.Adapter<PendidikanAdapter.MyHolderView>() {
    class MyHolderView(itemPendidikanBinding: ItemPendidikanBinding) :
        RecyclerView.ViewHolder(itemPendidikanBinding.root) {

        fun bind() {
            with(itemView) {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemPendidikanBinding =
            ItemPendidikanBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemPendidikanBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 2
}