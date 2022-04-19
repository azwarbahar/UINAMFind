package com.azwar.uinamfind.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemLokerHomeBinding
import com.azwar.uinamfind.ui.loker.DetailLokerActivity

class LokerHomeAdapter() :
    RecyclerView.Adapter<LokerHomeAdapter.ViewHolder>() {

    class ViewHolder(itemLokerHomeBinding: ItemLokerHomeBinding) :
        RecyclerView.ViewHolder(itemLokerHomeBinding.root) {

        fun bind() {
            with(itemView) {

                itemView.setOnClickListener {
                    val intent_detail_loker = Intent(context, DetailLokerActivity::class.java)
                    context.startActivity(intent_detail_loker)
                }

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemLokerHomeBinding =
            ItemLokerHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)


        return ViewHolder(itemLokerHomeBinding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    override fun getItemCount() = 10

}