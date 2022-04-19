package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ItemMahasiswa2Binding
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import kotlinx.android.synthetic.main.item_mahasiswa2.view.*

class MahasiswaGridAdapter() : RecyclerView.Adapter<MahasiswaGridAdapter.MyHolderView>() {

    class MyHolderView(itemMahasiswa2Binding: ItemMahasiswa2Binding) :
        RecyclerView.ViewHolder(itemMahasiswa2Binding.root) {

        fun bind() {
            with(itemView) {
                tv_pesan_item_mahasiswa2.setOnClickListener {
                    val intent_room_chat = Intent(context, RoomChatActivity::class.java)
                    context.startActivity(intent_room_chat)
                }

                itemView.setOnClickListener {
                    val intent_detail_mahasiswa = Intent(context, DetailMahasiswaActivity::class.java)
                    context.startActivity(intent_detail_mahasiswa)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa2, parent, false)

        var view = ItemMahasiswa2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(view)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 20
}