package com.azwar.uinamfind.ui.mahasiswa

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityMahasiswaBinding
import com.azwar.uinamfind.ui.mahasiswa.adapter.MahasiswaGridAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.MahasiswaHorizontalAdapter
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var role: String = ""
    private var fakultas: String = ""

    private lateinit var binding: ActivityMahasiswaBinding

    private lateinit var swipe: SwipeRefreshLayout

    private lateinit var mahasiswaFakultas: List<User>
    private lateinit var mahasiswaHorizontalAdapter: MahasiswaHorizontalAdapter
    private lateinit var mahasiswaGridAdapter: MahasiswaGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()
        fakultas = sharedPref.getString(Constanta.FAKULTAS_USER).toString()

        binding.imgBackMahasiswa.setOnClickListener {
            finish()
        }
        swipe = binding.swipe
        swipe.setOnRefreshListener(this)
        swipe.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe.post(Runnable {
            loadData()
        })

        if (role.equals("recruiter")) {
            binding.tv1Mahasiswa.visibility = View.GONE
            binding.tvLihatLainnyaSefakultas.visibility = View.GONE
            binding.cvSefakultas.visibility = View.GONE
        }

    }

    private fun loadData() {
        if (role.equals("user")) {
            mahasiswaSefakultas()
        }
        mahasiswaData()
    }

    private fun mahasiswaData() {
        ApiClient.instances.getMahasiswaRandom(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    if (response.isSuccessful) {
                        mahasiswaFakultas = response.body()?.mahasiswa_data!!

                        // List data mahasiswa random
                        binding.rvMahasiswaRandom.layoutManager =
                            GridLayoutManager(this@MahasiswaActivity, 2)
                        mahasiswaGridAdapter = MahasiswaGridAdapter(mahasiswaFakultas)
                        binding.rvMahasiswaRandom.adapter = mahasiswaGridAdapter
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {

                }

            })

    }

    private fun mahasiswaSefakultas() {

        ApiClient.instances.getMahasiswaFakultas(fakultas, id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    swipe.isRefreshing = false
                    if (response.isSuccessful) {
                        mahasiswaFakultas = response.body()?.mahasiswa_data!!
//                        Toast.makeText(
//                            this@MahasiswaActivity,
//                            "Create list sefakultas",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        // List Data Mahasiswa Sefakultas
                        binding.rvMahasiswaSefakultas.layoutManager =
                            LinearLayoutManager(
                                this@MahasiswaActivity,
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        mahasiswaHorizontalAdapter =
                            MahasiswaHorizontalAdapter(mahasiswaFakultas)
                        binding.rvMahasiswaSefakultas.adapter = mahasiswaHorizontalAdapter

                    } else {
                        Toast.makeText(
                            this@MahasiswaActivity,
                            response.message(),
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    swipe.isRefreshing = false
                    Toast.makeText(
                        this@MahasiswaActivity,
                        t.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    override fun onRefresh() {
        loadData()
    }
}