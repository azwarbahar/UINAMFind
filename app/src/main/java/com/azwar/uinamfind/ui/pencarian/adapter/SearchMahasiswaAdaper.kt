package com.azwar.uinamfind.ui.pencarian.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.SearchMahasiswa
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ItemCardMahasiswaBinding
import com.azwar.uinamfind.databinding.ItemCardMahasiswaSearchBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchMahasiswaAdaper(private val list: List<SearchMahasiswa>) :
    RecyclerView.Adapter<SearchMahasiswaAdaper.MyHolderView>() {

    class MyHolderView(private val binding: ItemCardMahasiswaSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var user: User

        //        fun bind(mahasiswa: Mahasiswa) {
        fun bind(get: SearchMahasiswa) {
            with(itemView) {

                getUser(get.id.toString())

                binding.imgBaclLogoCard.alpha = 0.1f
                itemView.setOnClickListener {
                    val intent_mahasiswa = Intent(context, DetailMahasiswaActivity::class.java)
                    intent_mahasiswa.putExtra("mahasiswa", user)
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

                val foto = get.foto
                if (foto !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                        .into(binding.imgPhotoCardItemMahasiswa)
                } else {

                }

                val sampul = get.foto_sampul
                if (sampul !== null) {
                    Glide.with(this)
                        .load(BuildConfig.BASE_URL + "/upload/photo/" + sampul)
                        .into(binding.imgHeaderCardItemMahasiswa)
                } else {

                }

                // lokasi
                var text_lokasi = binding.tvLokasiCardItemMahasiswa
                var lokasi = get.lokasi
                text_lokasi.text = lokasi

                val motto_ = get.motto_profesional
                if (motto_ == null) {
                    binding.tvMottoCardItemMahasiswa.text = "  -"
                } else {
                    binding.tvMottoCardItemMahasiswa.text = motto_
                }
                // motto
//                loadMotto(get.id!!, binding.tvMottoCardItemMahasiswa)

            }
        }

        private fun getUser(id: String) {
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

                    }

                    override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {

                    }

                })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolderView {
        val binding =
            ItemCardMahasiswaSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyHolderView(binding)

    }

    //    override fun onBindViewHolder(holder: MyHolderView, position: Int) = holder.bind(mahasiswas[position])
    override fun onBindViewHolder(holder: MyHolderView, position: Int) =
        holder.bind(list.get(position))

    override fun getItemCount() = list.size
}