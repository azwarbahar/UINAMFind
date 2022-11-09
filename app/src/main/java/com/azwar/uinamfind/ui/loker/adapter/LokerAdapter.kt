package com.azwar.uinamfind.ui.loker.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
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
import java.text.DecimalFormat
import java.text.NumberFormat

class LokerAdapter(private val list: List<Loker>) :
    RecyclerView.Adapter<LokerAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemLokerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: Loker) {
            with(itemView) {

                binding.tvJudulLokerItem.setText(get.posisi)
                binding.tvKotaLokerItem.setText(get.lokasi)
                var gaji_tercantum = get.gaji_tersedia
                if (gaji_tercantum.equals("Ya")) {
                    binding.tvRangeGajiLokerItem.setText(
                        "Rp." + moneyFormatConvert(get.gaji_min.toString()) + " - Rp." +moneyFormatConvert(
                            get.gaji_max.toString()
                        )
                    )
                } else {
                    binding.tvRangeGajiLokerItem.setText("Salary tidak dicantumkan")
                }

                ApiClient.instances.getPerusahaanId(get.perusahaan_id)?.enqueue(object :
                    Callback<Responses.ResponsePerusahaan> {
                    override fun onResponse(
                        call: Call<Responses.ResponsePerusahaan>,
                        response: Response<Responses.ResponsePerusahaan>
                    ) {
                        val pesanRespon = response.message()
                        if (response.isSuccessful) {
                            val message = response.body()?.pesan
                            val kode = response.body()?.kode
                            val perusahaan: Perusahaan = response.body()?.result_perusahaan!!
                            binding.tvNamaKantorLokerItem.text = perusahaan.nama.toString()
                            Log.e("Perusahaan", perusahaan.nama.toString())
                            var foto_perusahaan = perusahaan.foto
                            if (foto_perusahaan != null) {
                                Glide.with(itemView)
                                    .load(BuildConfig.BASE_URL + "/upload/perusahaan/" +foto_perusahaan)
                                    .into(binding.imgLogoKantorItemLoker)
                            }
                        } else {

                            Log.e("Perusahaan", pesanRespon)
                        }
                    }

                    override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
                        Log.e("Perusahaan", t.message.toString())
                    }

                })
                Log.e("Perusahaan" ,"di sini" )
//                loadPerusahaan(get.perusahaan_id, binding.tvNamaKantorLokerItem, binding.imgLogoKantorItemLoker)
                itemView.setOnClickListener {
                    val intent_detail_loker = Intent(context, DetailLokerActivity::class.java)
                    intent_detail_loker.putExtra("loker", get)
                    context.startActivity(intent_detail_loker)
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
        val itemLokerBinding =
            ItemLokerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemLokerBinding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}