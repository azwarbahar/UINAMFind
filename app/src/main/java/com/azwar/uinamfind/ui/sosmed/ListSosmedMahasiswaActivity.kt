package com.azwar.uinamfind.ui.sosmed

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
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityListSosmedMahasiswaBinding
import com.azwar.uinamfind.ui.saya.adapter.ListSosmedMahasiswaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListSosmedMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var kategori: String = ""

    private lateinit var listSosmedMahasiswaAdapter: ListSosmedMahasiswaAdapter

    private lateinit var binding: ActivityListSosmedMahasiswaBinding
    private lateinit var swipe_sosmed: SwipeRefreshLayout
    private lateinit var sosmedList: List<Sosmed>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListSosmedMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        kategori = intent.getStringExtra("kategori").toString()
        if (kategori.equals("Lembaga")) {
            id = intent.getStringExtra("id").toString()
        } else if (kategori.equals("Organisasi")) {
            id = intent.getStringExtra("id").toString()
        } else if (kategori.equals("UKM")) {
            id = intent.getStringExtra("id").toString()
        } else if (kategori.equals("Mahasiswa")) {
            id = sharedPref.getString(Constanta.ID_USER).toString()
        }

        binding.continerEmpty.root.visibility = View.GONE
        swipe_sosmed = binding.swipeSosmed
        swipe_sosmed.setOnRefreshListener(this)
        swipe_sosmed.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_sosmed.post(Runnable {
            laodDataSosmed(id, kategori)
        })
        binding.imgAddSosmed.setOnClickListener {
            val intent = Intent(this, AddSosmedMahasiswaActivity::class.java)
            intent.putExtra("kategori", kategori)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener {
            finish()
        }


    }

    private fun laodDataSosmed(id: String, kategori: String) {

        ApiClient.instances.getSosmedKategori(id, kategori)?.enqueue(object :
            Callback<Responses.ResponseSosmed> {
            override fun onResponse(
                call: Call<Responses.ResponseSosmed>,
                response: Response<Responses.ResponseSosmed>
            ) {
                swipe_sosmed.isRefreshing = false
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    val data = response.body()?.sosmed_data
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            binding.continerEmpty.root.visibility = View.GONE
                            val rv_org = binding.rvSosmed
                            rv_org.visibility = View.VISIBLE
                            rv_org.layoutManager =
                                LinearLayoutManager(this@ListSosmedMahasiswaActivity)
                            listSosmedMahasiswaAdapter =
                                ListSosmedMahasiswaAdapter(data)

                            val dividerItemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@ListSosmedMahasiswaActivity, R.drawable.divider
                                    )
                                )
                            rv_org.addItemDecoration(dividerItemDecoration)
                            rv_org.adapter = listSosmedMahasiswaAdapter
                        } else {
                            binding.continerEmpty.root.visibility = View.VISIBLE
                            binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                            val rv_org = binding.rvSosmed
                            rv_org.visibility = View.GONE
                        }
                    } else {
                        binding.continerEmpty.root.visibility = View.VISIBLE
                        binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                        val rv_org = binding.rvSosmed
                        rv_org.visibility = View.GONE
                        Toast.makeText(
                            this@ListSosmedMahasiswaActivity,
                            message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    binding.continerEmpty.root.visibility = View.VISIBLE
                    binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                    val rv_org = binding.rvSosmed
                    rv_org.visibility = View.GONE
                    Toast.makeText(
                        this@ListSosmedMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseSosmed>,
                t: Throwable
            ) {
                binding.continerEmpty.root.visibility = View.VISIBLE
                binding.continerEmpty.tvMessageEmptyData.text = "Belum ada data"
                val rv_org = binding.rvSosmed
                rv_org.visibility = View.GONE
                swipe_sosmed.isRefreshing = false
                Toast.makeText(
                    this@ListSosmedMahasiswaActivity,
                    "Server Tidak Merespon",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        laodDataSosmed(id, kategori)
    }

    override fun onRefresh() {
        laodDataSosmed(id, kategori)
    }
}