package com.azwar.uinamfind.ui.ukm.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemPendaftaranTerbukaUkmBinding

class PendaftaranUKMAdapter() : RecyclerView.Adapter<PendaftaranUKMAdapter.MyHolderView>() {
    class MyHolderView(itemPendaftaranTerbukaUkmBinding: ItemPendaftaranTerbukaUkmBinding) :
        RecyclerView.ViewHolder(itemPendaftaranTerbukaUkmBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {

        var itemPendaftaranTerbukaUkmBinding = ItemPendaftaranTerbukaUkmBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        return MyHolderView(itemPendaftaranTerbukaUkmBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) {

    }

    override fun getItemCount() = 10

}