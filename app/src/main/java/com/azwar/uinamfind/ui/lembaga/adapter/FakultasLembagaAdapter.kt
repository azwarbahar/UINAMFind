package com.azwar.uinamfind.ui.lembaga.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.R
import com.azwar.uinamfind.R.*
import com.azwar.uinamfind.databinding.ItemFakultasLembagaBinding
import kotlinx.android.synthetic.main.activity_lembaga.view.*
import kotlinx.android.synthetic.main.item_fakultas_lembaga.view.*

class FakultasLembagaAdapter() : RecyclerView.Adapter<FakultasLembagaAdapter.MyHolderView>() {


    class MyHolderView(fakultasLembagaBinding: ItemFakultasLembagaBinding) :
        RecyclerView.ViewHolder(fakultasLembagaBinding.root) {

        lateinit var lembagaAdapter: LembagaAdapter
        fun bind() {
            with(itemView) {

                var rv_lembaga_item: RecyclerView = findViewById(R.id.rv_lembaga_item)
                rv_lembaga_item.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                lembagaAdapter = LembagaAdapter()
                rv_lembaga_item.adapter = lembagaAdapter


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
//        var view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_fakultas_lembaga, parent, false)

        var fakultasLembagaBinding =
            ItemFakultasLembagaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )

        return MyHolderView(fakultasLembagaBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind()

    override fun getItemCount() = 9
}