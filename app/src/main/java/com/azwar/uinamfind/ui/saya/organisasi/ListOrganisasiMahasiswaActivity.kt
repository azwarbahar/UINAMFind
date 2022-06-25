package com.azwar.uinamfind.ui.saya.organisasi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityListOrganisasiMahasiswaBinding
import com.azwar.uinamfind.databinding.ItemOrganisasiMahasiswaBinding
import com.azwar.uinamfind.ui.saya.adapter.ListOrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListOrganisasiMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityListOrganisasiMahasiswaBinding

    private lateinit var listOrganisasiMahasiswaAdapter: ListOrganisasiMahasiswaAdapter

    private lateinit var swipe_organisasi: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOrganisasiMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        swipe_organisasi = binding.swipeOrganisasi
        swipe_organisasi.setOnRefreshListener(this)
        swipe_organisasi.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_organisasi.post(Runnable {
            laodDataOrganisasi(id)
        })


        val img_add_organisasi = binding.imgAddOrganisasi
        img_add_organisasi.setOnClickListener {

        }

    }

    private fun laodDataOrganisasi(id: String) {

        ApiClient.instances.getOrganisasiUser(id)
            ?.enqueue(object : Callback<Responses.ResponseOrganisasiMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    response: Response<Responses.ResponseOrganisasiMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val data = response.body()?.organisasi_data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            if (data!!.size > 0) {
                                val rv_org = binding.rvOrganisasiSaya
                                rv_org.layoutManager = LinearLayoutManager(this@ListOrganisasiMahasiswaActivity)
                                listOrganisasiMahasiswaAdapter = ListOrganisasiMahasiswaAdapter(data)

                                val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@ListOrganisasiMahasiswaActivity, R.drawable.divider
                                    )
                                )
                                rv_org.addItemDecoration(dividerItemDecoration)
                                rv_org.adapter = listOrganisasiMahasiswaAdapter
                            } else {

                            }
                        } else {
                            Toast.makeText(this@ListOrganisasiMahasiswaActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ListOrganisasiMahasiswaActivity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    t: Throwable
                ) {
                    Toast.makeText(this@ListOrganisasiMahasiswaActivity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })

    }


    override fun onRefresh() {
        laodDataOrganisasi(id)
    }
}