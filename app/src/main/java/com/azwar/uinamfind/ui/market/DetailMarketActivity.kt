package com.azwar.uinamfind.ui.market

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Market
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailMarketBinding
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.azwar.uinamfind.ui.market.adapter.MarketDetailAdapter
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat

class DetailMarketActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailMarketBinding

    private lateinit var market: Market

    private lateinit var markets: List<Market>
    private lateinit var marketAdapter: MarketDetailAdapter

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar_loker_detail = binding.toolbarMarketDetail
        setSupportActionBar(toolbar_loker_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        market = intent.getParcelableExtra("market")!!
        initData(market)

        binding.llHubungi.setOnClickListener {
            var no_whatsapp = market.nomor_wa.toString()
            val url = "https://api.whatsapp.com/send?phone=$no_whatsapp"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.rlPenjual.setOnClickListener {
            val intent_detail_mahasiswa =
                Intent(this, DetailMahasiswaActivity::class.java)
            intent_detail_mahasiswa.putExtra("mahasiswa", user)
            startActivity(intent_detail_mahasiswa)
        }

    }

    private fun loadData(id: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.getMarketDetail(id)
            ?.enqueue(object : Callback<Responses.ResponseMarket> {
                override fun onResponse(
                    call: Call<Responses.ResponseMarket>,
                    response: Response<Responses.ResponseMarket>
                ) {
                    dialogProgress.dismiss()
                    var message = response.message()
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode
                        var pesan = response.body()?.pesan
                        if (kode.equals("1")) {
                            markets = response.body()?.market_data!!
                            if (markets.size > 0) {
                                // List Data Market
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(this@DetailMarketActivity)
                                binding.rvMarket.layoutManager = layoutManagerLoker
                                marketAdapter = MarketDetailAdapter(markets)
                                binding.rvMarket.adapter = marketAdapter

                            } else {

                            }
                        } else {

                        }
                    } else {

                    }

                }

                override fun onFailure(call: Call<Responses.ResponseMarket>, t: Throwable) {
                    dialogProgress.dismiss()

                }

            })


    }

    private fun initData(market: Market) {
        var id = market.id.toString()
        loadData(id)
        binding.tvJudul.setText(market.judul.toString())
        binding.tvHarga.setText("Rp. " + moneyFormatConvert(market.harga.toString()))
        binding.tvSatuan.setText("/" + market.satuan.toString())
        binding.tvLokasi.setText(market.lokasi.toString())
        binding.tvDeskripsi.setText(market.deskripsi.toString())

        val foto = market.foto1.toString()
        if (foto.equals("-") || foto.equals("null")) {
        } else {
            Glide.with(this@DetailMarketActivity)
                .load(BuildConfig.BASE_URL + "upload/market/" + foto)
                .into(binding.imgHeaderMarketDetail)
        }

        var penjual_id = market.penjual_id.toString()
        initPenjual(penjual_id)

    }

    private fun initPenjual(penjualId: String) {


        ApiClient.instances.getMahasiswaID(penjualId)
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

                        binding.tvJurusan.setText(user.jurusan.toString())

                        var nim = user.nim

                        // nama
                        var nama_depan = user.nama_depan
                        var nama_belakang = user.nama_belakang
                        var text_nama_lengkap = binding.tvNamaPenjual
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
                        if (foto.equals("-") || foto.equals("null")) {
                        } else {
                            Glide.with(this@DetailMarketActivity)
                                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                                .into(binding.imgPhoto)
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                }
            })

    }

    private fun moneyFormatConvert(value: String): String? {
        val nilai = value.toDouble()
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(nilai)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}