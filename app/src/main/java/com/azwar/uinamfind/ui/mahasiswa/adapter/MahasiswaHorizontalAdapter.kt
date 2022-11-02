package com.azwar.uinamfind.ui.mahasiswa.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemMahasiswa1Binding
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaHorizontalAdapter(private val list: List<User>) :
    RecyclerView.Adapter<MahasiswaHorizontalAdapter.MyHolderView>() {

    class MyHolderView(private val itemMahasiswa1Binding: ItemMahasiswa1Binding) :
        RecyclerView.ViewHolder(itemMahasiswa1Binding.root) {
        fun bind(get: User) {
            with(itemView) {

                val lable = get.status_kemahasiswaan
                if (lable.equals("Lulus")) {
                    itemMahasiswa1Binding.imgLableItemMahasiswa1.visibility = View.VISIBLE
                } else {
                    itemMahasiswa1Binding.imgLableItemMahasiswa1.visibility = View.GONE
                }

                var nim = get.nim
                //nama
                var nama_depan = get.nama_depan
                var nama_belakang = get.nama_belakang
                var text_nama_lengkap = itemMahasiswa1Binding.tvNamaItemMahasiswa1
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
                var text_jurusan = itemMahasiswa1Binding.tvJurusanItemMahasiswa1
                var jurusan = get.jurusan
                text_jurusan.text = jurusan

                val foto = get.foto
                if (foto !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "/upload/photo/" +foto)
                        .into(itemMahasiswa1Binding.imgPhotoItemMahasiswa1)
                } else {

                }


                // motto
                loadMotto(get.id!!, itemMahasiswa1Binding.tvMottoItemMahasiswa1)


                itemMahasiswa1Binding.rlPesanItemMahasiswa.setOnClickListener {
                    val intent_room_chat = Intent(context, RoomChatActivity::class.java)
                    context.startActivity(intent_room_chat)
                }

                itemView.setOnClickListener {
                    val intent_detail_mahasiswa =
                        Intent(context, DetailMahasiswaActivity::class.java)
                    intent_detail_mahasiswa.putExtra("mahasiswa", get)
                    context.startActivity(intent_detail_mahasiswa)
                }
            }
        }

        private fun loadMotto(id: String, tvMottoItemMahasiswa1: TextView) {
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
                                    tvMottoItemMahasiswa1.text = "  -"
                                } else {
                                    tvMottoItemMahasiswa1.text = motto_
                                }
                            } else {
                                tvMottoItemMahasiswa1.text = "  -"
                            }
                        } else {
                            tvMottoItemMahasiswa1.text = "  -"
                        }
                    }

                    override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
                        tvMottoItemMahasiswa1.text = "  -"
                    }

                })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val itemMahasiswa1Binding =
            ItemMahasiswa1Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(itemMahasiswa1Binding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}