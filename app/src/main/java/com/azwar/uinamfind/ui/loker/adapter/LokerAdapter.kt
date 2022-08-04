package com.azwar.uinamfind.ui.loker.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemLokerBinding
import com.azwar.uinamfind.ui.loker.DetailLokerActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LokerAdapter(private val list: List<Loker>) : RecyclerView.Adapter<LokerAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemLokerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Loker) {
            with(itemView) {

                binding.tvJudulLokerItem.setText(get.posisi)
                binding.tvNamaKantorLokerItem.setText(get.perusahaan)
                binding.tvKotaLokerItem.setText(get.lokasi)
                var gaji_tercantum = get.gaji_tersedia
                if (gaji_tercantum.equals("Ya")) {
                    binding.tvRangeGajiLokerItem.setText(get.gaji_min + " - " + get.gaji_max)
                } else {
                    binding.tvRangeGajiLokerItem.setText("Salary tidak dicantumkan")
                }

                loadPerusahaan(get.perusahaan_id, binding.imgLogoKantorItemLoker)

                itemView.setOnClickListener {
                    val intent_detail_loker = Intent(context, DetailLokerActivity::class.java)
                    intent_detail_loker.putExtra("loker", get)
                    context.startActivity(intent_detail_loker)
                }

            }
        }

        private fun loadPerusahaan(perusahaanId: String?, imgLogoKantorItemLoker: ImageView) {

            ApiClient.instances.getPerusahaanId(perusahaanId)?.enqueue(object :
                Callback<Responses.ResponsePerusahaan> {
                override fun onResponse(
                    call: Call<Responses.ResponsePerusahaan>,
                    response: Response<Responses.ResponsePerusahaan>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        val perusahaan: Perusahaan = response.body()?.result_perusahaan!!
                        var foto_perusahaan = perusahaan.foto
                        if (foto_perusahaan != null) {
                            Glide.with(itemView)
                                .load(foto_perusahaan)
                                .into(imgLogoKantorItemLoker)
                        }
                    }
                }

                override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
                }

            })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemLokerBinding =
            ItemLokerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemLokerBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(list.get(position))

    override fun getItemCount() = list.size
}