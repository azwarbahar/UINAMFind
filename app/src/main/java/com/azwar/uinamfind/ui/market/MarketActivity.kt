package com.azwar.uinamfind.ui.market

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Market
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityMarketBinding
import com.azwar.uinamfind.ui.market.adapter.MarketAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MarketActivity : AppCompatActivity() {

    private lateinit var marketAdapter: MarketAdapter

    private lateinit var binding: ActivityMarketBinding

    private lateinit var market: List<Market>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()

        binding.imgBackMarket.setOnClickListener {
            finish()
        }

        binding.llTambahJualanMarket.setOnClickListener {
            val intent =
                Intent(this, TambahMarketActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadData() {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.getMarket()?.enqueue(object : Callback<Responses.ResponseMarket> {
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
                        market = response.body()?.market_data!!
                        if (market.size > 0) {
                            // List Data Market
                            binding.rvMarket.layoutManager =
                                GridLayoutManager(this@MarketActivity, 2)
                            marketAdapter = MarketAdapter(market)
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
}