package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.ItemOrganisasiMahasiswaBinding

class OrganisasiMahasiswaAdapter() :
    RecyclerView.Adapter<OrganisasiMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(itemOrganisasiMahasiswaBinding: ItemOrganisasiMahasiswaBinding) :
        RecyclerView.ViewHolder(itemOrganisasiMahasiswaBinding.root) {

        fun bind() {
            with(itemView) {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {

        val itemOrganisasiMahasiswaBinding = ItemOrganisasiMahasiswaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return MyHolderView(itemOrganisasiMahasiswaBinding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 2
}