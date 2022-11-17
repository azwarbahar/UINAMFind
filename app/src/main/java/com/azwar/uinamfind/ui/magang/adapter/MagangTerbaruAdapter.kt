package com.azwar.uinamfind.ui.magang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Magang
import com.azwar.uinamfind.databinding.ItemMagangTerbaruBinding
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class MagangTerbaruAdapter(private var list: List<Magang>) : RecyclerView.Adapter<MagangTerbaruAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemMagangTerbaruBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Magang) {
            with(itemView) {

                binding.tvJudulItemMagangTerbaru.setText(get.judul.toString())
                binding.tvTanggalTayangItemMagangTerbaru.setText("Tanggal Tayang : " + convertDate(get.created_at.toString()))
                binding.tvDescItemMagangTerbaru.setText(get.deskripsi.toString())
                Glide.with(context)
                    .load(Constanta.URL_PHOTO + "magang/" + get.foto.toString())
                    .into(binding.imgLogoPerusahaanItemMagangTerbaru)


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

        val itemMagangTerbaruBinding =
            ItemMagangTerbaruBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemMagangTerbaruBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(list.get(position))

    override fun getItemCount() = list.size
}