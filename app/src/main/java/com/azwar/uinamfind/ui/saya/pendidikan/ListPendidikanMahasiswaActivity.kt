package com.azwar.uinamfind.ui.saya.pendidikan

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
import com.azwar.uinamfind.databinding.ActivityListKeahlianMahasiswaBinding
import com.azwar.uinamfind.databinding.ActivityListPendidikanMahasiswaBinding
import com.azwar.uinamfind.ui.saya.adapter.ListOrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.ListPendidikanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PendidikanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.organisasi.AddOrganisasiMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListPendidikanMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityListPendidikanMahasiswaBinding

    private lateinit var listPendidikanMahasiswaAdapter: ListPendidikanMahasiswaAdapter

    private lateinit var swipe: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPendidikanMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        binding.continerEmpty.root.visibility = View.GONE
        swipe = binding.swipeOrganisasi
        swipe.setOnRefreshListener(this)
        swipe.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe.post(Runnable {
            laodDataPendidikan(id)
        })


        val img_add_organisasi = binding.imgAddPendidikan
        img_add_organisasi.setOnClickListener {
            val intent = Intent(this, AddPendidikanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.imgBackPendidikan.setOnClickListener {
            finish()
        }
    }

    private fun laodDataPendidikan(id: String) {

        ApiClient.instances.getPendidikanUser(id)
            ?.enqueue(object : Callback<Responses.ResponsePendidikanMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponsePendidikanMahasiswa>,
                    response: Response<Responses.ResponsePendidikanMahasiswa>
                ) {
                    swipe.isRefreshing = false
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val data = response.body()?.pendidikan_data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            if (data!!.size > 0) {
                                binding.continerEmpty.root.visibility = View.GONE
                                val rv_pendidikan = binding.rvPendidikan
                                rv_pendidikan.visibility = View.VISIBLE
                                rv_pendidikan.layoutManager =
                                    LinearLayoutManager(this@ListPendidikanMahasiswaActivity)
                                listPendidikanMahasiswaAdapter =
                                    ListPendidikanMahasiswaAdapter(data)

                                val dividerItemDecoration: RecyclerView.ItemDecoration =
                                    DividerItemDecorator(
                                        ContextCompat.getDrawable(
                                            this@ListPendidikanMahasiswaActivity, R.drawable.divider
                                        )
                                    )
                                rv_pendidikan.addItemDecoration(dividerItemDecoration)
                                rv_pendidikan.adapter = listPendidikanMahasiswaAdapter
                            } else {
                                binding.continerEmpty.root.visibility = View.VISIBLE
                                binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                                val rv_pendidikan = binding.rvPendidikan
                                rv_pendidikan.visibility = View.VISIBLE
                            }
                        } else {
                            binding.continerEmpty.root.visibility = View.VISIBLE
                            binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                            val rv_pendidikan = binding.rvPendidikan
                            rv_pendidikan.visibility = View.VISIBLE
                            Toast.makeText(
                                this@ListPendidikanMahasiswaActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        binding.continerEmpty.root.visibility = View.VISIBLE
                        binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                        val rv_pendidikan = binding.rvPendidikan
                        rv_pendidikan.visibility = View.VISIBLE
                        Toast.makeText(
                            this@ListPendidikanMahasiswaActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponsePendidikanMahasiswa>,
                    t: Throwable
                ) {
                    binding.continerEmpty.root.visibility = View.VISIBLE
                    binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                    val rv_pendidikan = binding.rvPendidikan
                    rv_pendidikan.visibility = View.VISIBLE
                    swipe.isRefreshing = false
                    Toast.makeText(
                        this@ListPendidikanMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    override fun onResume() {
        super.onResume()
        laodDataPendidikan(id)
    }

    override fun onRefresh() {
        laodDataPendidikan(id)
    }
}