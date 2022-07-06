package com.azwar.uinamfind.ui.saya.keahlian

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityListKeahlianMahasiswaBinding
import com.azwar.uinamfind.ui.saya.adapter.ListKeahlianMahasiswaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListKeahlianMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityListKeahlianMahasiswaBinding
    private lateinit var listKeahlianMahasiswaAdapter: ListKeahlianMahasiswaAdapter
    private lateinit var swipe_keahlian: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListKeahlianMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        binding.continerEmpty.root.visibility = View.GONE

        swipe_keahlian = binding.swipeKeahlian
        swipe_keahlian.setOnRefreshListener(this)
        swipe_keahlian.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_keahlian.post(Runnable {
            laodDataKeahlian(id)
        })
        val img_add_organisasi = binding.imgAddKeahlian
        img_add_organisasi.setOnClickListener {
            val intent = Intent(this, AddKeahlianMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.imgBackKeahlian.setOnClickListener {
            finish()
        }
    }

    private fun laodDataKeahlian(id: String) {
        ApiClient.instances.getKeahlianUser(id)?.enqueue(object :
            Callback<Responses.ResponseKeahlianMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                response: Response<Responses.ResponseKeahlianMahasiswa>
            ) {
                swipe_keahlian.isRefreshing = false
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                val data = response.body()?.keahlian_data

                if (response.isSuccessful) {
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            binding.continerEmpty.root.visibility = View.GONE
                            val rv_org = binding.rvKeahlian
                            rv_org.visibility = View.VISIBLE
                            rv_org.layoutManager =
                                LinearLayoutManager(this@ListKeahlianMahasiswaActivity)
                            listKeahlianMahasiswaAdapter =
                                ListKeahlianMahasiswaAdapter(data)

                            val dividerItemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@ListKeahlianMahasiswaActivity, R.drawable.divider
                                    )
                                )
                            rv_org.addItemDecoration(dividerItemDecoration)
                            rv_org.adapter = listKeahlianMahasiswaAdapter
                        } else {
                            binding.continerEmpty.root.visibility = View.VISIBLE
                            binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                            val rv_org = binding.rvKeahlian
                            rv_org.visibility = View.VISIBLE
                        }
                    } else {
                        binding.continerEmpty.root.visibility = View.VISIBLE
                        binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                        val rv_org = binding.rvKeahlian
                        rv_org.visibility = View.VISIBLE
                        Toast.makeText(
                            this@ListKeahlianMahasiswaActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    binding.continerEmpty.root.visibility = View.VISIBLE
                    binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                    val rv_org = binding.rvKeahlian
                    rv_org.visibility = View.VISIBLE
                    Toast.makeText(
                        this@ListKeahlianMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                t: Throwable
            ) {
                binding.continerEmpty.root.visibility = View.VISIBLE
                binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                val rv_org = binding.rvKeahlian
                rv_org.visibility = View.VISIBLE
                swipe_keahlian.isRefreshing = false
                Toast.makeText(
                    this@ListKeahlianMahasiswaActivity,
                    "Server Tidak Merespon",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        laodDataKeahlian(id)
    }

    override fun onRefresh() {
        laodDataKeahlian(id)
    }
}