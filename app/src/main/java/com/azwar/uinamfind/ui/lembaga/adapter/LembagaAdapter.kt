package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ItemLembagaBinding
import com.azwar.uinamfind.ui.lembaga.DetailLembagaActivity

class LembagaAdapter() : RecyclerView.Adapter<LembagaAdapter.MyHolderView>() {

    class MyHolderView(itemLembagaBinding: ItemLembagaBinding) :
        RecyclerView.ViewHolder(itemLembagaBinding.root) {

        fun bind(position: Int) {
            with(itemView) {

                itemView.setOnClickListener {
                    var intent_lembaga_detail = Intent(context, DetailLembagaActivity::class.java)
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

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}