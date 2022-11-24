package com.azwar.uinamfind.ui.notifikasi.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Informasi
import com.azwar.uinamfind.databinding.ItemInformasiBinding
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class InformasiAdapter(private var list: List<Informasi>) :
    RecyclerView.Adapter<InformasiAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemInformasiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Informasi) {
            with(itemView) {

                binding.tvTanggal.setText(convertDate(get.created_at.toString()))
                binding.tvJudul.setText(get.judul.toString())
                binding.tvPesan.setText(get.pesan.toString())

                var cakupan = get.cakupan.toString()
                if (cakupan.equals("Mahasiswa")) {
                    binding.tvCakupan.setText("Khusus")
                } else {
                    binding.tvCakupan.setText("Publik")
                }

                Glide.with(context)
                    .load(Constanta.URL_PHOTO + "informasi/" + get.gambar.toString())
                    .into(binding.imgInformasi)

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
            ItemInformasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InformasiAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}