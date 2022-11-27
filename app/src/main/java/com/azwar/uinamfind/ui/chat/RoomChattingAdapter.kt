package com.azwar.uinamfind.ui.chat

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.RoomChat
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemChattingBinding
import com.azwar.uinamfind.ui.saya.tentang.EditTentangMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomChattingAdapter(private var list: List<RoomChat>) :
    RecyclerView.Adapter<RoomChattingAdapter.MyHolderView>() {


    class MyHolderView(private var binding: ItemChattingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var user: User

        fun bind(get: RoomChat) {
            with(itemView) {
                loadDataSaya(get.to_user.toString())

                itemView.setOnClickListener {
                    val intent = Intent(context, RoomChatActivity::class.java)
                    intent.putExtra("mahasiswa", user)
                    context.startActivity(intent)
                }

            }
        }

        private fun loadDataSaya(id: String) {
            ApiClient.instances.getMahasiswaID(id)
                ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                    override fun onResponse(
                        call: Call<Responses.ResponseMahasiswa>,
                        response: Response<Responses.ResponseMahasiswa>
                    ) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        user = response.body()?.result_mahasiswa!!
                        binding.tvUserame.setText(user.username.toString())
                        var foto = user.foto
                        if (foto !== null) {
                            Glide.with(itemView)
                                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                                .into(binding.imgPhoto)
                        } else {

                        }

                    }

                    override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {

                    }

                })
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemChattingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoomChattingAdapter.MyHolderView(binding)

    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}