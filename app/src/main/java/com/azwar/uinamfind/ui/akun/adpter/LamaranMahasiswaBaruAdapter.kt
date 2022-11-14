package com.azwar.uinamfind.ui.akun.adpter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemLamaranMahasiswaBinding
import com.azwar.uinamfind.ui.lamaran.DetailLamaranRecruiterActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class LamaranMahasiswaBaruAdapter(private val list: List<Lamaran>) :
    RecyclerView.Adapter<LamaranMahasiswaBaruAdapter.MyHolderView>() {
    class MyHolderView(private val binding: ItemLamaranMahasiswaBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(get: Lamaran) {
            with(itemView) {

                var loker_id = get.loker_id
                var mahasiswa_id = get.mahasiswa_id
                loadLoker(loker_id)
                loadMahasiswa(mahasiswa_id)
                var tgl = convertDate(get.created_at)
                binding.tvTanggal.text = tgl

                var status_lamaran = get.status_lamaran.toString()
                binding.tvStatus.setText(status_lamaran)

                itemView.setOnClickListener {
                    val intent = Intent(context, DetailLamaranRecruiterActivity::class.java)
                    intent.putExtra("lamaran", get)
                    context.startActivity(intent)
                }

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

        private fun loadMahasiswa(mahasiswaId: String?) {

            ApiClient.instances.getMahasiswaID(mahasiswaId)
                ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                    override fun onResponse(
                        call: Call<Responses.ResponseMahasiswa>,
                        response: Response<Responses.ResponseMahasiswa>
                    ) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (response.isSuccessful) {

                            val user: User = response.body()?.result_mahasiswa!!
                            var nim = user.nim.toString()

                            // nama
                            var nama_depan = user.nama_depan.toString()
                            var nama_belakang = user.nama_belakang.toString()
                            var text_nama_lengkap = binding.tvNamaMahasiswa
                            if (nama_depan == null) {
                                text_nama_lengkap.text = nim
                            } else if (nama_belakang == null) {
                                var nama_lengkap = user.nama_depan
                                text_nama_lengkap.text = nama_lengkap
                            } else {
                                var nama_lengkap = user.nama_depan + " " + user.nama_belakang
                                text_nama_lengkap.text = nama_lengkap
                            }
                            val foto = user.foto.toString()
                            if (foto.equals("-") || foto.isEmpty()) {
                            } else {
                                Glide.with(itemView)
                                    .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                                    .into(binding.imgPhoto)
                            }

                        } else {
                            binding.tvNamaMahasiswa.text = "Nama Pelamar"
                        }
                    }

                    override fun onFailure(
                        call: Call<Responses.ResponseMahasiswa>,
                        t: Throwable
                    ) {
                        binding.tvNamaMahasiswa.text = "Nama Pelamar"

                    }

                })


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
                            binding.tvNamaLamaran.text = loker.posisi.toString()
                        } else {
                            binding.tvNamaLamaran.text = "Nama Loker"
                        }
                    }

                    override fun onFailure(
                        call: Call<Responses.ResponseLoker>,
                        t: Throwable
                    ) {
                        binding.tvNamaLamaran.text = "Nama Loker"
                    }

                })

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemLamaranMahasiswaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return LamaranMahasiswaBaruAdapter.MyHolderView(binding)
    }

    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}