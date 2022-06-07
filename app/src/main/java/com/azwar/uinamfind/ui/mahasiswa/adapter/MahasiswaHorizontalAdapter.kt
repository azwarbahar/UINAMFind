package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ItemMahasiswa1Binding
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import kotlinx.android.synthetic.main.item_mahasiswa1.view.*

class MahasiswaHorizontalAdapter() :
    RecyclerView.Adapter<MahasiswaHorizontalAdapter.MyHolderView>() {

    class MyHolderView(itemMahasiswa1Binding: ItemMahasiswa1Binding) :
        RecyclerView.ViewHolder(itemMahasiswa1Binding.root) {
        fun bind() {
            with(itemView) {
                rl_pesan_item_mahasiswa.setOnClickListener {
                    val intent_room_chat = Intent(context, RoomChatActivity::class.java)
                    context.startActivity(intent_room_chat)
                }

                itemView.setOnClickListener {
                    val intent_detail_mahasiswa =
                        Intent(context, DetailMahasiswaActivity::class.java)
                    context.startActivity(intent_detail_mahasiswa)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemMahasiswa1Binding =
            ItemMahasiswa1Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemMahasiswa1Binding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 10
}