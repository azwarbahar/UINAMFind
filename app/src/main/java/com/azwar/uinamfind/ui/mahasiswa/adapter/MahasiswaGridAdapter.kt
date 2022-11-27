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
import com.azwar.uinamfind.databinding.ItemMahasiswa2Binding
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaGridAdapter(private val list: List<User>) :
    RecyclerView.Adapter<MahasiswaGridAdapter.MyHolderView>() {

    class MyHolderView(private val binding: ItemMahasiswa2Binding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(get: User) {
            with(itemView) {

                val lable = get.status_kemahasiswaan
                if (lable.equals("Lulus")) {
                    binding.imgLableItemMahasiswa2.visibility = View.VISIBLE
                } else {
                    binding.imgLableItemMahasiswa2.visibility = View.GONE
                }

                var nim = get.nim
                //nama
                var nama_depan = get.nama_depan
                var nama_belakang = get.nama_belakang
                var text_nama_lengkap = binding.tvNamaItemMahasiswa2
                if (nama_depan == null) {
                    text_nama_lengkap.text = nim
                } else if (nama_belakang == null) {
                    var nama_lengkap = get.nama_depan
                    text_nama_lengkap.text = nama_lengkap
                } else {
                    var nama_lengkap = get.nama_depan + " " + get.nama_belakang
                    text_nama_lengkap.text = nama_lengkap
                }

                val foto = get.foto
                if (foto !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "/upload/photo/" +foto)
                        .into(binding.imgPhotoItemMahasiswa2)
                } else {

                }

                val sampul = get.foto_sampul
                if (sampul !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "/upload/photo/" +sampul)
                        .into(binding.imgHeaderItemMahasiswa2)
                } else {

                }


                // jurusan
                var text_jurusan = binding.tvJurusanItemMahasiswa2
                var jurusan = get.jurusan
                text_jurusan.text = jurusan

                // motto
                loadMotto(get.id!!, binding.tvMottoItemMahasiswa2)


                binding.tvPesanItemMahasiswa2.setOnClickListener {
                    val intent_room_chat = Intent(context, RoomChatActivity::class.java)
                    intent_room_chat.putExtra("mahasiswa", get)
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

        private fun loadMotto(id: String, tvMottoItemMahasiswa2: TextView) {

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
                                    tvMottoItemMahasiswa2.text = "  -"
                                } else {
                                    tvMottoItemMahasiswa2.text = motto_
                                }
                            } else {
                                tvMottoItemMahasiswa2.text = "  -"
                            }
                        } else {
                            tvMottoItemMahasiswa2.text = "  -"
                        }
                    }

                    override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
                        tvMottoItemMahasiswa2.text = "  -"
                    }

                })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.item_mahasiswa2, parent, false)

        var view = ItemMahasiswa2Binding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(view)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}