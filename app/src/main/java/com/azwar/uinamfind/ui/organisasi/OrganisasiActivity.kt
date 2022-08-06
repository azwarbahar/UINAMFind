package com.azwar.uinamfind.ui.organisasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityOrganisasiBinding
import com.azwar.uinamfind.ui.organisasi.adapter.OrganisasiAdapter
import kotlinx.android.synthetic.main.activity_organisasi.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrganisasiActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityOrganisasiBinding
    private lateinit var listOrganisasi: List<Organisasi>
    private lateinit var organisasiAdapter: OrganisasiAdapter

    private lateinit var swipe_organisasi: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrganisasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        swipe_organisasi = binding.swipeOrganisasi
        swipe_organisasi.setOnRefreshListener(this)
        swipe_organisasi.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_organisasi.post(Runnable {
            loadDataOrganisasi()
        })

        binding.imgSearchOrganisasi.setOnClickListener {
            val intent_search_organisasi = Intent(this, SearchOrganisasiActivity::class.java)
            startActivity(intent_search_organisasi)
        }

        binding.imgBackOrganisasi.setOnClickListener {
            finish()
        }

    }

    private fun loadDataOrganisasi() {

        ApiClient.instances.getOrganisasi()?.enqueue(object :
            Callback<Responses.ResponseOrganisasi> {
            override fun onResponse(
                call: Call<Responses.ResponseOrganisasi>,
                response: Response<Responses.ResponseOrganisasi>
            ) {
                if (response.isSuccessful) {
                    listOrganisasi = response.body()?.organisasi_data!!
                    binding.rvOrganisasi.layoutManager =
                        LinearLayoutManager(this@OrganisasiActivity)
                    organisasiAdapter = OrganisasiAdapter(listOrganisasi)
                    binding.rvOrganisasi.adapter = organisasiAdapter
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseOrganisasi>, t: Throwable) {

            }

        })

    }

    override fun onRefresh() {

        loadDataOrganisasi()
    }
}