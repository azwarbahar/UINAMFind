package com.azwar.uinamfind.ui.loker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.BuildConfig
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
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat


class DetailLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLokerBinding
    private var lamar_mudah = ""
    private var link_lamar = ""
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

        binding.rlBrnLamar.setOnClickListener {

            if (lamar_mudah.equals("Ya")) {
                val intent = Intent(this, LamarLokerActivity::class.java)
                intent.putExtra("loker", loker)
                startActivity(intent)
            } else {
                if (link_lamar.equals("-") || link_lamar.equals("")) {
                    Toast.makeText(this, "Link tidak terdeteksi", Toast.LENGTH_SHORT)
                } else {
                    val defaultBrowser = Intent.makeMainSelectorActivity(
                        Intent.ACTION_MAIN,
                        Intent.CATEGORY_APP_BROWSER
                    )
                    defaultBrowser.data = Uri.parse(link_lamar)
                    startActivity(defaultBrowser)
                }
            }
        }

        binding.fabShareDetailLoker.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hi, ada loker nih " + loker.posisi.toString() + ",  ayo cek di :\n" +
                        "https://uinamfind.com/loker/" + loker.slug.toString()
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
        }


    }

    private fun initData(loker: Loker) {

        binding.tvJudulDetailLoker.setText(loker.posisi)
        binding.tvLokasiDetailLoker.setText(loker.lokasi)
        binding.tvJenisDetailLoker.setText(loker.jenis_pekerjaan)
        var gaji_tercantum = loker.gaji_tersedia
        if (gaji_tercantum.equals("Ya")) {
            binding.tvRangeGajiDetailLoker.setText(
                moneyFormatConvert(loker.gaji_min.toString()) + " - " + moneyFormatConvert(
                    loker.gaji_max.toString()
                )
            )
        } else {
            binding.tvRangeGajiDetailLoker.setText("Salary tidak dicantumkan")
        }
        binding.tvTanggalTayangDetailLoker.setText("Tayang pada " + convertDate(loker.created_at))
        binding.tvDeskripsiDetailLoker.setText(loker.deskripsi)
        binding.tvJobdesDetailLoker.setText(loker.jobdesk)

        lamar_mudah = loker.lamar_mudah!!
        if (lamar_mudah.equals("Ya")) {
            binding.tvBtnLamarLoker.setText("Lamar Sekarang")
        } else {
            binding.tvBtnLamarLoker.setText("Kunjungi Untuk Melamar")
        }
        link_lamar = loker.link_lamar!!
        loadPerusahaan(loker.perusahaan_id)
    }

    private fun moneyFormatConvert(value: String): String? {
        val nilai = value.toDouble()
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(nilai)
    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
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
                            .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto_perusahaan)
                            .into(binding.imgLogoKantorDetailLoker)
                        Glide.with(this@DetailLokerActivity)
                            .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto_perusahaan)
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