package com.azwar.uinamfind.ui.pencarian.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.databinding.ItemOrganisasiBinding
import com.azwar.uinamfind.ui.lembaga.DetailLembagaActivity
import com.azwar.uinamfind.ui.organisasi.DetailOrganisasiActivity
import com.bumptech.glide.Glide

class SearchLembagaAdapter(private var list: List<LembagaKampus>) :
    RecyclerView.Adapter<SearchLembagaAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemOrganisasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: LembagaKampus) {
            with(itemView) {

                binding.tvNamaItemOrganisasi.setText(get.nama)
                var cakupan = get.cakupan_lembaga.toString()
                if (cakupan.equals("Jurusan")) {
                    binding.tvJenisItemOrganisasi.setText(get.jurusan.toString())
                } else if (cakupan.equals("Fakultas")) {
                    binding.tvJenisItemOrganisasi.setText(get.fakultas.toString())
                } else {
                    binding.tvJenisItemOrganisasi.setText(get.cakupan_lembaga.toString())
                }

                val foto = get.foto.toString()
                if (foto.equals("") || foto.equals("null")) {
                } else {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                        .into(binding.imgPhotoItemOrganisasi)
                }

                itemView.setOnClickListener {
                    val intent_detail_organisasi =
                        Intent(context, DetailLembagaActivity::class.java)
                    intent_detail_organisasi.putExtra("lembaga", get)
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