package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Foto
import com.azwar.uinamfind.databinding.ItemFotoBinding
import com.azwar.uinamfind.ui.PreviewPhotoActivity
import com.bumptech.glide.Glide


class FotoLembagaAdapter(private val list: List<Foto>) :
    RecyclerView.Adapter<FotoLembagaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemFotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Foto) {
            with(itemView) {
                var foto = get.nama_foto
                if (foto !== null) {
                    Glide.with(this)
                        .load("http://192.168.1.19/api_uinamfind/upload/galeri/" + foto)
                        .into(binding.imgPhoto)
                } else {

                }

                itemView.setOnClickListener {
                    val intent = Intent(context, PreviewPhotoActivity::class.java)
                    intent.putExtra("foto", get)
                    context.startActivity(intent)
                }

            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {

        var binding =
            ItemFotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return FotoLembagaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}