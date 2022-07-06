package com.azwar.uinamfind.ui.saya.pengalaman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityListPengalamanMahasiswaBinding
import com.azwar.uinamfind.ui.saya.adapter.ListOrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.ListPengalamanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.organisasi.AddOrganisasiMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPengalamanMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityListPengalamanMahasiswaBinding
    private lateinit var listPengalamanMahasiswaAdapter: ListPengalamanMahasiswaAdapter
    private lateinit var swipe_pengalaman: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPengalamanMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        binding.continerEmpty.root.visibility = View.GONE
        swipe_pengalaman = binding.swipePengalaman
        swipe_pengalaman.setOnRefreshListener(this)
        swipe_pengalaman.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_pengalaman.post(Runnable {
            loadDataPengalaman(id)
        })

        binding.imgAddPengalaman.setOnClickListener {
            val intent = Intent(this, AddPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.imgBackPengalaman.setOnClickListener {
            finish()
        }

    }

    private fun loadDataPengalaman(id: String) {

        ApiClient.instances.getPengalamanUser(id)
            ?.enqueue(object : Callback<Responses.ResponsePengalamanMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponsePengalamanMahasiswa>,
                    response: Response<Responses.ResponsePengalamanMahasiswa>
                ) {
                    swipe_pengalaman.isRefreshing = false
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val data = response.body()?.pengalaman_data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            if (data!!.size > 0) {
                                binding.continerEmpty.root.visibility = View.GONE
                                val rv_pengalaman = binding.rvPengalamanSaya
                                rv_pengalaman.visibility = View.VISIBLE
                                rv_pengalaman.layoutManager =
                                    LinearLayoutManager(this@ListPengalamanMahasiswaActivity)
                                listPengalamanMahasiswaAdapter =
                                    ListPengalamanMahasiswaAdapter(data)

                                val dividerItemDecoration: RecyclerView.ItemDecoration =
                                    DividerItemDecorator(
                                        ContextCompat.getDrawable(
                                            this@ListPengalamanMahasiswaActivity, R.drawable.divider
                                        )
                                    )
                                rv_pengalaman.addItemDecoration(dividerItemDecoration)
                                rv_pengalaman.adapter = listPengalamanMahasiswaAdapter
                            } else {
                                binding.continerEmpty.root.visibility = View.VISIBLE
                                binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                                val rv_pengalaman = binding.rvPengalamanSaya
                                rv_pengalaman.visibility = View.VISIBLE
                            }
                        } else {
                            binding.continerEmpty.root.visibility = View.VISIBLE
                            binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                            val rv_pengalaman = binding.rvPengalamanSaya
                            rv_pengalaman.visibility = View.VISIBLE
                            Toast.makeText(
                                this@ListPengalamanMahasiswaActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.continerEmpty.root.visibility = View.VISIBLE
                        binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                        val rv_pengalaman = binding.rvPengalamanSaya
                        rv_pengalaman.visibility = View.VISIBLE
                        Toast.makeText(
                            this@ListPengalamanMahasiswaActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponsePengalamanMahasiswa>,
                    t: Throwable
                ) {
                    binding.continerEmpty.root.visibility = View.VISIBLE
                    binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                    val rv_pengalaman = binding.rvPengalamanSaya
                    rv_pengalaman.visibility = View.VISIBLE
                    swipe_pengalaman.isRefreshing = false
                    Toast.makeText(
                        this@ListPengalamanMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    override fun onRestart() {
        super.onRestart()
        loadDataPengalaman(id)
    }

    override fun onRefresh() {
        loadDataPengalaman(id)
    }
}