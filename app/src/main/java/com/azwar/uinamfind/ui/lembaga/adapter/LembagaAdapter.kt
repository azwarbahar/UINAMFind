package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.databinding.ItemLembagaBinding
import com.azwar.uinamfind.ui.lembaga.DetailLembagaActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide

class LembagaAdapter(private val list: List<LembagaKampus>) :
    RecyclerView.Adapter<LembagaAdapter.MyHolderView>() {

    class MyHolderView(private val binding: ItemLembagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: LembagaKampus) {
            with(itemView) {

                binding.tvNamaLembaga.setText(get.nama)

                val foto = get.foto.toString()
                if (foto.equals("-") || foto.equals("null")) {
                } else {
                    Glide.with(this)
                        .load(Constanta.URL_PHOTO + "" + foto)
                        .into(binding.imgPhotoItemLembaga)
                    Glide.with(this)
                        .load(foto)
                        .into(binding.imgHeaderItemLembaga)
                }
                itemView.setOnClickListener {
                    var intent_lembaga_detail = Intent(context, DetailLembagaActivity::class.java)
                    intent_lembaga_detail.putExtra("lembaga", get)
                    context.startActivity(intent_lembaga_detail)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
//        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_lembaga, parent, false)

        var lembagaBinding =
            ItemLembagaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(lembagaBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}