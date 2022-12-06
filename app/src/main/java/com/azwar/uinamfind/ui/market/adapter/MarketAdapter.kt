package com.azwar.uinamfind.ui.market.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Market
import com.azwar.uinamfind.databinding.ItemMarketBinding
import com.azwar.uinamfind.ui.loker.DetailLokerActivity
import com.azwar.uinamfind.ui.market.DetailMarketActivity
import com.bumptech.glide.Glide
import java.text.DecimalFormat
import java.text.NumberFormat


class MarketAdapter(private var list: List<Market>) :
    RecyclerView.Adapter<MarketAdapter.MyHolderView>() {

    class MyHolderView(private var binding: ItemMarketBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Market) {
            with(itemView) {

                var judul = get.judul.toString()
                var no_whatsapp = get.nomor_wa.toString()
                var harga = get.harga.toString()
                binding.tvJudulItemMarket.setText(judul)
                binding.tvHargaItemMarket.setText("Rp. " + moneyFormatConvert(harga))

                var foto1 = get.foto1.toString()
                if (foto1 !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "upload/market/" + foto1)
                        .into(binding.imgFotoItemMarket)
                } else {

                }

                binding.tvHubungiItemMarket.setOnClickListener {
                    val url = "https://api.whatsapp.com/send?phone=$no_whatsapp"
                    val i = Intent(Intent.ACTION_VIEW)
                    i.data = Uri.parse(url)
                    context.startActivity(i)
                }

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailMarketActivity::class.java)
                    intent.putExtra("market", get)
                    context.startActivity(intent)
                }

            }
        }

        private fun moneyFormatConvert(value: String): String? {
            val nilai = value.toDouble()
            val formatter: NumberFormat = DecimalFormat("#,###")
            return formatter.format(nilai)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding = ItemMarketBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MarketAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}