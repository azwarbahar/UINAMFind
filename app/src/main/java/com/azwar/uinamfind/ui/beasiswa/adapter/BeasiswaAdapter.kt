package com.azwar.uinamfind.ui.beasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Beasiswa
import com.azwar.uinamfind.databinding.ItemBeasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class BeasiswaAdapter(private var list: List<Beasiswa>) :
    RecyclerView.Adapter<BeasiswaAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemBeasiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Beasiswa) {
            with(itemView) {

                binding.tvJudulItemBeasiswa.setText(get.judul.toString())
                binding.tvNamaPerusahaanItemBeasiswa.setText(get.instansi.toString())
                binding.tvTanggalTayangItemBeasiswa.setText("Tanggal Tayang : " + convertDate(get.created_at.toString()))
                binding.tvDescItemBeasiswa.setText(get.deskripsi.toString())
                Glide.with(context)
                    .load(Constanta.URL_PHOTO + "beasiswa/" + get.foto.toString())
                    .into(binding.imgLogoPerusahaanItemBeasiswa)

            }
        }

        private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemBeasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}