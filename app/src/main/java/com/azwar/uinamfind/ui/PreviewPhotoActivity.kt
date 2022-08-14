package com.azwar.uinamfind.ui

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.data.models.Foto
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityPreviewPhotoBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class PreviewPhotoActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private lateinit var binding: ActivityPreviewPhotoBinding

    private lateinit var foto: Foto
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        var id = sharedPref.getString(Constanta.ID_USER).toString()
        foto = intent.getParcelableExtra("foto")!!
        setupData(foto)

        binding.imgClose.setOnClickListener { finish() }
        binding.tvNamaPengupload.setOnClickListener {
            if (!id.equals(user.id)) {
                val intent_detail_mahasiswa =
                    Intent(this, DetailMahasiswaActivity::class.java)
                intent_detail_mahasiswa.putExtra("mahasiswa", user)
                startActivity(intent_detail_mahasiswa)
            }
        }

    }

    private fun setupData(foto: Foto) {
        binding.tvJudul.setText(foto.judul)
        var ft = foto.nama_foto
        if (ft !== null) {
            Glide.with(this)
                .load("http://192.168.1.19/api_uinamfind/upload/galeri/" + ft)
                .into(binding.imgZoom)
        } else {
        }
        loadUser(foto.uploader_id)
        binding.tvDeskripsi.setText(foto.deskripsi)
        binding.tvTanggal.setText(convertDate(foto.created_at))
    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
    }

    private fun loadUser(user_id: String?) {
        ApiClient.instances.getMahasiswaID(user_id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {

                        var nim = user.nim

                        // nama
                        var nama_depan = user.nama_depan
                        var nama_belakang = user.nama_belakang
                        var text_nama_lengkap = binding.tvNamaPengupload
                        if (nama_depan == null) {
                            text_nama_lengkap.text = nim
                        } else if (nama_belakang == null) {
                            var nama_lengkap = user.nama_depan
                            text_nama_lengkap.text = nama_lengkap
                        } else {
                            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
                            text_nama_lengkap.text = nama_lengkap
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                }

            })
    }
}