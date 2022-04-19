package com.azwar.uinamfind.ui.organisasi.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemOrganisasiBinding
import com.azwar.uinamfind.ui.organisasi.DetailOrganisasiActivity

class OrganisasiAdapter() : RecyclerView.Adapter<OrganisasiAdapter.MyHolderView>() {
    class MyHolderView(itemOrganisasiBinding: ItemOrganisasiBinding) :
        RecyclerView.ViewHolder(itemOrganisasiBinding.root) {

        fun bind() {
            with(itemView) {

                itemView.setOnClickListener {
                    val intent_detail_organisasi =
                        Intent(context, DetailOrganisasiActivity::class.java)
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

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 10
}