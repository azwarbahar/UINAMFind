package com.azwar.uinamfind.ui.lamaran.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemBeasiswaTerbaruHomeBinding
import com.azwar.uinamfind.databinding.ItemLamaranSayaBinding
import com.azwar.uinamfind.ui.home.adapter.BeasiswaTerbaruAdapter
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class LamaranSayaAdapter(private var list: List<Lamaran>) :
    RecyclerView.Adapter<LamaranSayaAdapter.MyHolderView>() {
    class MyHolderView(private var binding: ItemLamaranSayaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Lamaran) {
            with(itemView) {

                var loker_id = get.loker_id
                var mahasiswa_id = get.mahasiswa_id
                loadLoker(loker_id)
                var tgl = convertDate(get.created_at)
                binding.tvTglLamaran.text = "Lamaran pada " + tgl
                binding.status.setText(get.status_lamaran)
            }
        }

        private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
            val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val formatter = SimpleDateFormat("dd MMM yyyy")
            val output = formatter.format(parser.parse(date))
            return output
        }

        private fun loadLoker(lokerId: String?) {

            ApiClient.instances.getLokerId(lokerId)
                ?.enqueue(object : Callback<Responses.ResponseLoker> {
                    override fun onResponse(
                        call: Call<Responses.ResponseLoker>,
                        response: Response<Responses.ResponseLoker>
                    ) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (response.isSuccessful) {
                            val loker: Loker = response.body()?.result_loker!!
                            binding.tvNamaLoker.text = loker.posisi.toString()
                            binding.tvLokasiLoker.setText(loker.lokasi + ", Indonesia")
                        } else {
                            binding.tvNamaLoker.text = "Nama Loker"
                        }
                    }

                    override fun onFailure(
                        call: Call<Responses.ResponseLoker>,
                        t: Throwable
                    ) {
                        binding.tvNamaLoker.text = "Nama Loker"
                    }

                })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemLamaranSayaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return LamaranSayaAdapter.MyHolderView(binding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}