package com.azwar.uinamfind.ui.akun.adpter

import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemLokerRecruiterBinding
import com.azwar.uinamfind.ui.loker.DetailPostinganLokerActivity
import com.azwar.uinamfind.ui.loker.EditLokerActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat

class LokerRecruiterAdapter(private val list: List<Loker>) :
    RecyclerView.Adapter<LokerRecruiterAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemLokerRecruiterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Loker) {
            with(itemView) {
                binding.tvJudulLokerItem.setText(get.posisi)
                binding.tvKotaLokerItem.setText(get.lokasi)
                binding.tvRangeGajiLokerItem.setText("Diperbarui pada " + convertDate(get.updated_at))
                loadPerusahaan(get.perusahaan_id, binding.imgLogoKantorItemLoker)
                loadPelamar(get.id)

                binding.imgEditLoker.setOnClickListener {
                    val intent = Intent(context, EditLokerActivity::class.java)
                    intent.putExtra("loker", get)
                    context.startActivity(intent)
                }

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailPostinganLokerActivity::class.java)
                    intent.putExtra("loker", get)
                    context.startActivity(intent)
                }

            }

        }

        private fun loadPelamar(id: String?) {

            ApiClient.instances.getLamaranLokerRecruiter(id)
                ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                    override fun onResponse(
                        call: Call<Responses.ResponseLamaran>,
                        response: Response<Responses.ResponseLamaran>
                    ) {
                        if (response.isSuccessful) {
                            val pesanRespon = response.message()
                            val message = response.body()?.pesan
                            val kode = response.body()?.kode

                            if (kode.equals("1")) {
                                var lamaran: List<Lamaran> = response.body()?.lamaran_data!!
                                if (lamaran.size > 0) {
                                    binding.tvJumlahPelamar.text =
                                        lamaran.size.toString() + " pelamar langsung"

                                } else {
                                }
                            } else {
                            }
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {

//                    }
                    }
                })

        }

        private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }

        private fun moneyFormatConvert(value: String): String? {
            val nilai = value.toDouble()
            val formatter: NumberFormat = DecimalFormat("#,###")
            return formatter.format(nilai)
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
                        binding.tvNamaKantorLokerItem.setText(perusahaan.nama.toString())
                        var foto_perusahaan = perusahaan.foto
                        if (foto_perusahaan != null) {
                            Glide.with(itemView)
                                .load(BuildConfig.BASE_URL + "/upload/perusahaan/" +foto_perusahaan)
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
        val binding =
            ItemLokerRecruiterBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}