package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemFakultasLembagaBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FakultasLembagaAdapter(private val list: Array<String>) :
    RecyclerView.Adapter<FakultasLembagaAdapter.MyHolderView>() {

    class MyHolderView(private val binding: ItemFakultasLembagaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var lembagaAdapter: LembagaAdapter
        fun bind(s: String) {
            with(itemView) {

                val lembaga = s
                if (lembaga.equals("Universitas")) {
                    binding.tvNamaItemFakultas.setText("Lembaga Universitas")
                    loadLembagaCakupan(context, lembaga)
                } else {
                    binding.tvNamaItemFakultas.setText("Fakultas " + lembaga)
                    loadLembagaFakultas(context, lembaga)
                }


            }
        }

        private fun loadLembagaFakultas(context: Context?, lembaga: String) {

            ApiClient.instances.getLembagaFakultas(lembaga)?.enqueue(object :
                Callback<Responses.ResponseLembaga> {
                override fun onResponse(
                    call: Call<Responses.ResponseLembaga>,
                    response: Response<Responses.ResponseLembaga>
                ) {
                    if (response.isSuccessful) {

                        val kode = response.body()?.kode
                        val pesan = response.body()?.pesan

                        val data = response.body()?.lembaga_data
                        var rv_lembaga_item = binding.rvLembagaItem
                        rv_lembaga_item.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        lembagaAdapter = LembagaAdapter(data!!)
                        rv_lembaga_item.adapter = lembagaAdapter

                    }

                }

                override fun onFailure(call: Call<Responses.ResponseLembaga>, t: Throwable) {

                }

            })

        }

        private fun loadLembagaCakupan(context: Context?, lembaga: String) {

            ApiClient.instances.getLembagaCakupan(lembaga)?.enqueue(object :
                Callback<Responses.ResponseLembaga> {
                override fun onResponse(
                    call: Call<Responses.ResponseLembaga>,
                    response: Response<Responses.ResponseLembaga>
                ) {
                    if (response.isSuccessful) {

                        val kode = response.body()?.kode
                        val pesan = response.body()?.pesan

                        val data = response.body()?.lembaga_data
                        var rv_lembaga_item = binding.rvLembagaItem
                        rv_lembaga_item.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        lembagaAdapter = LembagaAdapter(data!!)
                        rv_lembaga_item.adapter = lembagaAdapter

                    }

                }

                override fun onFailure(call: Call<Responses.ResponseLembaga>, t: Throwable) {

                }

            })
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

    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(list[position])

    override fun getItemCount() = list.size
}