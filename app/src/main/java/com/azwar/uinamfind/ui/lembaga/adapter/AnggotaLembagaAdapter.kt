package com.azwar.uinamfind.ui.lembaga.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Anggota
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemAnggotaBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnggotaLembagaAdapter(private val list: List<Anggota>) :
    RecyclerView.Adapter<AnggotaLembagaAdapter.MyHolderView>() {


    class MyHolderView(private val binding: ItemAnggotaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Anggota) {
            with(itemView) {

                var sharedPref: PreferencesHelper
                sharedPref = PreferencesHelper(context)
                var id = sharedPref.getString(Constanta.ID_USER).toString()

                binding.tvJabatan.setText(get.tahun_jabatan + " - " + get.jabatan)
                var user: User

                ApiClient.instances.getMahasiswaID(get.user_id)
                    ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                        override fun onResponse(
                            call: Call<Responses.ResponseMahasiswa>,
                            response: Response<Responses.ResponseMahasiswa>
                        ) {
                            val pesanRespon = response.message()
                            val message = response.body()?.pesan
                            val kode = response.body()?.kode
                            user = response.body()?.result_mahasiswa!!
                            if (response.isSuccessful) {
                                var nim = user.nim

                                // nama
                                var nama_depan = user.nama_depan
                                var nama_belakang = user.nama_belakang
                                var text_nama_lengkap = binding.tvNamaUser
                                if (nama_depan == null) {
                                    text_nama_lengkap.text = nim
                                } else if (nama_belakang == null) {
                                    var nama_lengkap = user.nama_depan
                                    text_nama_lengkap.text = nama_lengkap
                                } else {
                                    var nama_lengkap = user.nama_depan + " " + user.nama_belakang
                                    text_nama_lengkap.text = nama_lengkap
                                }
                                val foto = user.foto
                                if (foto.equals("-") || foto.isNullOrEmpty()) {
                                } else {
                                    Glide.with(itemView)
                                        .load(foto)
                                        .into(binding.imgPhotoUser)
                                }

                                if (!id.equals(user.id)) {
                                    itemView.setOnClickListener {
                                        val intent_detail_mahasiswa =
                                            Intent(context, DetailMahasiswaActivity::class.java)
                                        intent_detail_mahasiswa.putExtra("mahasiswa", user)
                                        context.startActivity(intent_detail_mahasiswa)
                                    }
                                }

                            } else {

                            }
                        }

                        override fun onFailure(
                            call: Call<Responses.ResponseMahasiswa>,
                            t: Throwable
                        ) {

                        }

                    })


            }
        }

        private fun loadUser(userId: String?, itemView: View) {
            var user: User
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        var binding =
            ItemAnggotaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )

        return AnggotaLembagaAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}