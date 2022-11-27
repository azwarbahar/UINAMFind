package com.azwar.uinamfind.ui.ukm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityUkmBinding
import com.azwar.uinamfind.ui.organisasi.adapter.OrganisasiAdapter
import com.azwar.uinamfind.ui.ukm.adapter.PendaftaranUKMAdapter
import com.azwar.uinamfind.ui.ukm.adapter.UKMAdapter
import kotlinx.android.synthetic.main.activity_ukm.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UKMActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityUkmBinding

    private lateinit var ukmAdapter: UKMAdapter
    private lateinit var ukms: List<Ukm>

    private lateinit var swipe_ukm: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUkmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // List Data Pendaftaran Terbuka UKM
//        rv_pendaftaran_terbuka_ukm.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        pendaftaranUKMAdapter = PendaftaranUKMAdapter()
//        rv_pendaftaran_terbuka_ukm.adapter = pendaftaranUKMAdapter


        swipe_ukm = binding.swipeUkm
        swipe_ukm.setOnRefreshListener(this)
        swipe_ukm.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_ukm.post(Runnable {
            loadDataUkm()
        })

        binding.imgBackUkm.setOnClickListener {
            finish()
        }

    }

    private fun loadDataUkm() {

        ApiClient.instances.getUkm()?.enqueue(object :
            Callback<Responses.ResponseUkm> {
            override fun onResponse(
                call: Call<Responses.ResponseUkm>,
                response: Response<Responses.ResponseUkm>
            ) {
                swipe_ukm.isRefreshing = false
                if (response.isSuccessful) {
                    var kode = response.body()?.kode
                    if (kode.equals("1")){
                        ukms = response.body()?.ukm_data!!
                        if (ukms.size > 0){

                            val decoratorHorizontal = DividerItemDecoration(this@UKMActivity, LinearLayoutManager.HORIZONTAL)
                            decoratorHorizontal.setDrawable(ContextCompat.getDrawable(this@UKMActivity, R.drawable.divider)!!)
                            val decoratorVertikal = DividerItemDecoration(this@UKMActivity, LinearLayoutManager.VERTICAL)
                            decoratorVertikal.setDrawable(ContextCompat.getDrawable(this@UKMActivity, R.drawable.divider)!!)
                            // List Data UKM

                            binding.rvUkm.setHasFixedSize(true)
                            binding.rvUkm.layoutManager = GridLayoutManager(this@UKMActivity, 2)
                            ukmAdapter = UKMAdapter(ukms)
                            binding.rvUkm.addItemDecoration(decoratorVertikal)
                            binding.rvUkm.addItemDecoration(decoratorHorizontal)
                            binding.rvUkm.adapter = ukmAdapter

                        } else {

                        }
                    } else {

                    }
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseUkm>, t: Throwable) {
                swipe_ukm.isRefreshing = false
            }

        })

    }


    override fun onRefresh() {
        loadDataUkm()
    }
}