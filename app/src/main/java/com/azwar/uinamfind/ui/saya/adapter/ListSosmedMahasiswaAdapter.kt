package com.azwar.uinamfind.ui.saya.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.databinding.ItemPengalamanMahasiswaEditBinding
import com.azwar.uinamfind.databinding.ItemSosmedBinding
import com.azwar.uinamfind.ui.saya.pengalaman.EditPengalamanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.sosmed.EditSosmedMahasiswaActivity

class ListSosmedMahasiswaAdapter(private var list: List<Sosmed>) :
    RecyclerView.Adapter<ListSosmedMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemSosmedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Sosmed) {
            with(itemView) {

                binding.tvNamaSosmed.setText(get.nama_sosmed.toString())
                binding.tvUrlSosmed.setText(get.url_sosmed.toString())

                binding.imgEdit.setOnClickListener {
                    val intent = Intent(context, EditSosmedMahasiswaActivity::class.java)
                    intent.putExtra("sosmed", get)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemSosmedBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ListSosmedMahasiswaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}