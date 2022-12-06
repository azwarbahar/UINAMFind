package com.azwar.uinamfind.ui.loker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityLokerBinding
import com.azwar.uinamfind.ui.home.adapter.LokerHomeAdapter
import com.azwar.uinamfind.ui.loker.adapter.LokerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LokerActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var lokerBinding: ActivityLokerBinding

    private lateinit var lokerAdapter: LokerAdapter
    lateinit var loker: List<Loker>

    private lateinit var swipe_loker: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lokerBinding = ActivityLokerBinding.inflate(layoutInflater)
        setContentView(lokerBinding.root)
        swipe_loker = lokerBinding.swipeLoker
        swipe_loker.setOnRefreshListener(this)
        swipe_loker.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_loker.post(Runnable {
            loadData()
        })

        lokerBinding.imgBackLoker.setOnClickListener {
            finish()
        }
    }

    private fun loadData() {

        ApiClient.instances.getLoker("dsa")?.enqueue(object : Callback<Responses.ResponseLoker> {
            override fun onResponse(
                call: Call<Responses.ResponseLoker>,
                response: Response<Responses.ResponseLoker>
            ) {
                swipe_loker.isRefreshing = false
                if (response.isSuccessful) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    loker = response.body()?.loker_data!!

                    val rv_loker = lokerBinding.rvLoker
                    val layoutManagerLoker: RecyclerView.LayoutManager =
                        LinearLayoutManager(this@LokerActivity)
                    rv_loker.layoutManager = layoutManagerLoker
                    lokerAdapter = LokerAdapter(loker)
                    rv_loker.adapter = lokerAdapter

                }

            }

            override fun onFailure(call: Call<Responses.ResponseLoker>, t: Throwable) {
                // do something
                swipe_loker.isRefreshing = false
            }

        })

    }

    override fun onRefresh() {
        loadData()
    }
}