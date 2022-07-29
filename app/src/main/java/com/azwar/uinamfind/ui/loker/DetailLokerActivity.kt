package com.azwar.uinamfind.ui.loker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailLokerBinding
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class DetailLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLokerBinding

    private lateinit var loker: Loker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loker = intent.getParcelableExtra("loker")!!
        initData(loker)

        val toolbar_loker_detail = binding.toolbarLokerDetail
        setSupportActionBar(toolbar_loker_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
    }

    private fun initData(loker: Loker) {

        binding.tvJudulDetailLoker.setText(loker.posisi)
        binding.tvLokasiDetailLoker.setText(loker.lokasi)
        binding.tvJenisDetailLoker.setText(loker.jenis_pekerjaan)
        var gaji_tercantum = loker.gaji_tersedia
        if (gaji_tercantum.equals("Ya")) {
            binding.tvRangeGajiDetailLoker.setText(loker.gaji_min + " - " + loker.gaji_max)
        } else {
            binding.tvRangeGajiDetailLoker.setText("Salary tidak dicantumkan")
        }
        binding.tvTanggalTayangDetailLoker.setText("Tayang pada " + convertDate(loker.created_at))
        binding.tvDeskripsiDetailLoker.setText(loker.deskripsi)
        binding.tvJobdesDetailLoker.setText(loker.jobdesk)

        var lamar_mudah = loker.lamar_mudah
        if (lamar_mudah.equals("Ya")) {
            binding.tvBtnLamarLoker.setText("Lamar Sekarang")
        } else {
            binding.tvBtnLamarLoker.setText("Kunjungi Untuk Melamar")
        }

        loadPerusahaan(loker.perusahaan_id)
    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
    }

    private fun loadPerusahaan(perusahaanId: String?) {
        ApiClient.instances.getPerusahaanId(perusahaanId)?.enqueue(object :
            Callback<Responses.ResponsePerusahaan> {
            override fun onResponse(
                call: Call<Responses.ResponsePerusahaan>,
                response: Response<Responses.ResponsePerusahaan>
            ) {
                if (response.isSuccessful) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val perusahaan: Perusahaan = response.body()?.result_perusahaan!!
                    var foto_perusahaan = perusahaan.foto
                    if (foto_perusahaan != null) {
                        Glide.with(this@DetailLokerActivity)
                            .load(foto_perusahaan)
                            .into(binding.imgLogoKantorDetailLoker)
                        Glide.with(this@DetailLokerActivity)
                            .load(foto_perusahaan)
                            .into(binding.imgLogoPerusahaanDetailLoker)
                    }
                    binding.tvNamaKantorDetailLoker.setText(perusahaan.nama)
                    binding.tvNamaPerusahaanDetailLoker.setText(perusahaan.nama)
                    binding.tvJumlahKariawanDetailLoker.setText(perusahaan.ukuran_kariawan + " Kariawan")
                    binding.tvDeskripsiPerusahaanDetailLoker.setText(perusahaan.deskripsi)
                }
            }

            override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
            }

        })

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}