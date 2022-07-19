package com.azwar.uinamfind.ui.home.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemCardMahasiswaBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardMahasiswaAdapter(private val list: List<User>) :
    RecyclerView.Adapter<CardMahasiswaAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemCardMahasiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //        fun bind(mahasiswa: Mahasiswa) {
        fun bind(get: User) {
            with(itemView) {

                binding.imgBaclLogoCard.alpha = 0.1f
                itemView.setOnClickListener {
                    val intent_mahasiswa = Intent(context, DetailMahasiswaActivity::class.java)
                    context.startActivity(intent_mahasiswa)
                }

                val lable = get.status_kemahasiswaan
                if (lable.equals("Lulus")) {
                    binding.imgLableMahasiswa.visibility = View.VISIBLE
                } else {
                    binding.imgLableMahasiswa.visibility = View.GONE
                }

                var nim = get.nim

                // nama
                var nama_depan = get.nama_depan
                var nama_belakang = get.nama_belakang
                var text_nama_lengkap = binding.tvNamaCardItemMahasiswa
                if (nama_depan == null) {
                    text_nama_lengkap.text = nim
                } else if (nama_belakang == null) {
                    var nama_lengkap = get.nama_depan
                    text_nama_lengkap.text = nama_lengkap
                } else {
                    var nama_lengkap = get.nama_depan + " " + get.nama_belakang
                    text_nama_lengkap.text = nama_lengkap
                }

                // jurusan
                var text_jurusan = binding.tvJurusanCardItemMahasiswa
                var jurusan = get.jurusan
                var fakultas = get.fakultas
                text_jurusan.text = jurusan + ", " + fakultas

                // lokasi
                var text_lokasi = binding.tvLokasiCardItemMahasiswa
                var lokasi = get.lokasi
                text_lokasi.text = lokasi

                // motto
                loadMotto(get.id, binding.tvMottoCardItemMahasiswa)

            }
        }

        private fun loadMotto(id: String, tvMottoCardItemMahasiswa: TextView) {

            ApiClient.instances.getMottoId(id)
                ?.enqueue(object : Callback<Responses.ResponseMotto> {
                    override fun onResponse(
                        call: Call<Responses.ResponseMotto>,
                        response: Response<Responses.ResponseMotto>
                    ) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        val motto = response.body()?.result_motto
                        if (response.isSuccessful) {
                            if (kode.equals("1")) {
                                val motto_ = motto?.motto_profesional
                                if (motto_ == null) {
                                    tvMottoCardItemMahasiswa.text = "  -"
                                } else {
                                    tvMottoCardItemMahasiswa.text = motto_
                                }
                            } else {
                                tvMottoCardItemMahasiswa.text = "  -"
                            }
                        } else {
                            tvMottoCardItemMahasiswa.text = "  -"
                        }
                    }

                    override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
                        tvMottoCardItemMahasiswa.text = "  -"
                    }

                })

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemCardMahasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)

    }

    //    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(mahasiswas[position])
    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}