package com.azwar.uinamfind.ui.saya.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.KeahlianMahasiswa
import com.azwar.uinamfind.databinding.ItemKeahlianBinding

class KeahlianMahasiswaAdapter(private val list: List<KeahlianMahasiswa>) :
    RecyclerView.Adapter<KeahlianMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemKeahlianBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: KeahlianMahasiswa) {
            with(itemView) {
                binding.tvNamaItemKeahlian.setText(get.nama_skill)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemKeahlianBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return KeahlianMahasiswaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}