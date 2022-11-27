package com.azwar.uinamfind.ui.ukm.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.databinding.ItemUkmBinding
import com.azwar.uinamfind.ui.organisasi.DetailOrganisasiActivity
import com.azwar.uinamfind.ui.ukm.DetailUkmActivity
import com.bumptech.glide.Glide

class UKMAdapter(private var list: List<Ukm>) : RecyclerView.Adapter<UKMAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemUkmBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Ukm) {
            with(itemView) {

                binding.tvNamaItemUkm.setText(get.nama_ukm)

                val foto = get.foto.toString()
                if (foto.equals("") || foto.equals("null")) {
                } else {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                        .into(binding.imgCoverItemUkm)
                }

                itemView.setOnClickListener {
                    val intent =
                        Intent(context, DetailUkmActivity::class.java)
                    intent.putExtra("ukm", get)
                    context.startActivity(intent)
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemUkmBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}