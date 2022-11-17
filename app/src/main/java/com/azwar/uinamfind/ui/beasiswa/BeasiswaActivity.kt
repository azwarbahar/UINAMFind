package com.azwar.uinamfind.ui.beasiswa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Beasiswa
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityBeasiswaBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter
import com.azwar.uinamfind.ui.loker.adapter.LokerAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var beasiswaBinding: ActivityBeasiswaBinding

    private lateinit var beasiswaAdapter: BeasiswaAdapter
    private lateinit var beasiswa: List<Beasiswa>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beasiswaBinding = ActivityBeasiswaBinding.inflate(layoutInflater)
        setContentView(beasiswaBinding.root)

        swipe_refresh = beasiswaBinding.swipeBeasiswa
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

        beasiswaBinding.imgBackBeasiswa.setOnClickListener {
            finish()
        }

    }

    private fun loadData() {

        ApiClient.instances.getBeasiswa()?.enqueue(object : Callback<Responses.ResponseBeasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseBeasiswa>,
                response: Response<Responses.ResponseBeasiswa>
            ) {
                swipe_refresh.isRefreshing = false
                if (response.isSuccessful) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    beasiswa = response.body()?.beasiswa_data!!



                    val layoutManagerBeasiswa: RecyclerView.LayoutManager = LinearLayoutManager(this@BeasiswaActivity)
                    beasiswaBinding.rvBeasiswa.layoutManager = layoutManagerBeasiswa
                    beasiswaAdapter = BeasiswaAdapter(beasiswa)
                    beasiswaBinding.rvBeasiswa.adapter = beasiswaAdapter


                }

            }

            override fun onFailure(call: Call<Responses.ResponseBeasiswa>, t: Throwable) {
                // do something
                swipe_refresh.isRefreshing = false
            }

        })

    }

    override fun onRefresh() {
        loadData()
    }
}