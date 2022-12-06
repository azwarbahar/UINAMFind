package com.azwar.uinamfind.ui.pencarian.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.databinding.ItemOrganisasiBinding
import com.azwar.uinamfind.ui.lembaga.DetailLembagaActivity
import com.azwar.uinamfind.ui.organisasi.DetailOrganisasiActivity
import com.azwar.uinamfind.ui.ukm.DetailUkmActivity
import com.bumptech.glide.Glide

class SearchUkmAdapter(private var list: List<Ukm>) :
    RecyclerView.Adapter<SearchUkmAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemOrganisasiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Ukm) {
            with(itemView) {

                binding.tvNamaItemOrganisasi.setText(get.nama_ukm.toString())
                binding.tvJenisItemOrganisasi.setText(get.kategori.toString())


                val foto = get.foto.toString()
                if (foto.equals("") || foto.equals("null")) {
                } else {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                        .into(binding.imgPhotoItemOrganisasi)
                }

                itemView.setOnClickListener {
                    val intent_detail_organisasi =
                        Intent(context, DetailUkmActivity::class.java)
                    intent_detail_organisasi.putExtra("ukm", get)
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