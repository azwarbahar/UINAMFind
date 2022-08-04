package com.azwar.uinamfind.ui.kegiatan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Kegiatan
import com.azwar.uinamfind.databinding.ItemKegiatanBinding
import com.azwar.uinamfind.ui.kegiatan.DetailKegiatanActivity
import com.azwar.uinamfind.ui.saya.keahlian.EditKeahlianMahasiswaActivity
import com.bumptech.glide.Glide

class KegiatanGridAdapter(private val list: List<Kegiatan>) :
    RecyclerView.Adapter<KegiatanGridAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemKegiatanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Kegiatan) {
            with(itemView) {
                binding.tvJudulKegiatan.setText(get.nama)
                binding.tvDeskripsiKegiatan.setText(get.deskripsi)
                var tempat = get.tempat
                var tanggal = get.tanggal
                binding.tvTanggalKegiatan.setText(tempat + " - " + tanggal)

                var foto = get.foto
                if (foto.equals("-")) {

                } else {
                    Glide.with(this)
                        .load(foto)
                        .into(binding.imgPhotoKegiatan)
                }

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailKegiatanActivity::class.java)
                    intent.putExtra("kegiatan", get)
                    context.startActivity(intent)
                }


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val kegiatanBinding =
            ItemKegiatanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyHolderView(kegiatanBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}