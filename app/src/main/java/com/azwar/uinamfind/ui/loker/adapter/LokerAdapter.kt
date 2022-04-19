package com.azwar.uinamfind.ui.loker.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemLokerBinding
import com.azwar.uinamfind.ui.loker.DetailLokerActivity

class LokerAdapter() : RecyclerView.Adapter<LokerAdapter.MyHolderView>() {
    class MyHolderView(itemLokerBinding: ItemLokerBinding) :
        RecyclerView.ViewHolder(itemLokerBinding.root) {

        fun bind(position: Int) {
            with(itemView) {

                itemView.setOnClickListener {
                    val intent_detail_loker = Intent(context, DetailLokerActivity::class.java)
                    context.startActivity(intent_detail_loker)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemLokerBinding =
            ItemLokerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemLokerBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(position)

    override fun getItemCount() = 10
}