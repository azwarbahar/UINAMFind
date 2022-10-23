package com.azwar.uinamfind.ui.organisasi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.databinding.ItemOrganisasiBinding
import com.azwar.uinamfind.ui.organisasi.DetailOrganisasiActivity
import com.bumptech.glide.Glide

class OrganisasiAdapter(private var list: List<Organisasi>) :
    RecyclerView.Adapter<OrganisasiAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemOrganisasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Organisasi) {
            with(itemView) {

                binding.tvNamaItemOrganisasi.setText(get.nama_organisasi)
                binding.tvJenisItemOrganisasi.setText(get.kategori)

                val foto = get.foto.toString()
                if (foto.equals("-") || foto.equals("null")) {
                } else {
                    Glide.with(this)
                        .load(foto)
                        .into(binding.imgPhotoItemOrganisasi)
                }

                itemView.setOnClickListener {
                    val intent_detail_organisasi =
                        Intent(context, DetailOrganisasiActivity::class.java)
                    intent_detail_organisasi.putExtra("organisasi", get)
                    context.startActivity(intent_detail_organisasi)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemOrganisasiBinding =
            ItemOrganisasiBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemOrganisasiBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}