package com.azwar.uinamfind.ui.akun.adpter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.databinding.ItemLamaranMahasiswaBinding

class PelamaranLokerAdapter(private val list: List<Lamaran>) :
    RecyclerView.Adapter<PelamaranLokerAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemLamaranMahasiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Lamaran) {
            with(itemView){

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemLamaranMahasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PelamaranLokerAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(list.get(position))

    override fun getItemCount() = list.size
}