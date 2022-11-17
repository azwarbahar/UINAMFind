package com.azwar.uinamfind.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Beasiswa
import com.azwar.uinamfind.databinding.ItemBeasiswaTerbaruHomeBinding
import com.azwar.uinamfind.databinding.ItemCardMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat

class BeasiswaTerbaruAdapter(private var list: List<Beasiswa>) :
    RecyclerView.Adapter<BeasiswaTerbaruAdapter.MyHolderView>() {

    class MyHolderView(private var binding: ItemBeasiswaTerbaruHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Beasiswa) {
            with(itemView) {

                binding.tvJudulBeasiswaTerbaru.setText(get.judul.toString())
                binding.tvNamaKantorBeasiswaTerbaruItem.setText(get.instansi.toString())
                binding.tvTanggalAkhirBeasiswaTerbaru.setText("Sampai : " + convertDate(get.batas_tanggal.toString()))

                Glide.with(context)
                    .load(Constanta.URL_PHOTO + "beasiswa/" + get.foto.toString())
                    .into(binding.imgBeasiswaTerbaruHome)

            }
        }

        private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
            val parser = SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemBeasiswaTerbaruHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return BeasiswaTerbaruAdapter.MyHolderView(binding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}