package com.azwar.uinamfind.ui.magang

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Magang
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityMagangBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter
import com.azwar.uinamfind.ui.magang.adapter.MagangBersertifikatAdapter
import com.azwar.uinamfind.ui.magang.adapter.MagangTerbaruAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagangActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var magangBersertifikatAdapter: MagangBersertifikatAdapter
    private lateinit var magangTerbaruAdapter: MagangTerbaruAdapter

    private lateinit var magangBinding: ActivityMagangBinding
    private lateinit var magang : List<Magang>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        magangBinding = ActivityMagangBinding.inflate(layoutInflater)
        setContentView(magangBinding.root)

        swipe_refresh = magangBinding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            loadData()
        })


        magangBinding.imgBackMagang.setOnClickListener { finish() }

    }

    private fun loadData() {

        ApiClient.instances.getMagang()?.enqueue(object : Callback<Responses.ResponseMagang> {
            override fun onResponse(
                call: Call<Responses.ResponseMagang>,
                response: Response<Responses.ResponseMagang>
            ) {
                swipe_refresh.isRefreshing = false
                if (response.isSuccessful) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    magang = response.body()?.magang_data!!

                    val rv_magang_terbaru = magangBinding.rvMagangTerbaru
                    rv_magang_terbaru.layoutManager = LinearLayoutManager(this@MagangActivity)
                    magangTerbaruAdapter = MagangTerbaruAdapter(magang)
                    rv_magang_terbaru.adapter = magangTerbaruAdapter


                }

            }

            override fun onFailure(call: Call<Responses.ResponseMagang>, t: Throwable) {
                // do something
                swipe_refresh.isRefreshing = false
            }

        })

    }


    override fun onRefresh() {

        loadData()
    }
}