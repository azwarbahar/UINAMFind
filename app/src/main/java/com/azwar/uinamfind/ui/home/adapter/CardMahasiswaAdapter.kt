package com.azwar.uinamfind.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemCardMahasiswaBinding
import com.azwar.uinamfind.models.Mahasiswa
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import kotlinx.android.synthetic.main.item_card_mahasiswa.view.*

class CardMahasiswaAdapter() : RecyclerView.Adapter<CardMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(binding: ItemCardMahasiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {

//        fun bind(mahasiswa: Mahasiswa) {
        fun bind(position: Int) {
            with(itemView) {
                img_bacl_logo_card.alpha = 0.1f

                itemView.setOnClickListener {
                    val intent_mahasiswa = Intent(context, DetailMahasiswaActivity::class.java)
                    context.startActivity(intent_mahasiswa)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemCardMahasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)

    }

//    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(mahasiswas[position])
    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}