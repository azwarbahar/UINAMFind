package com.azwar.uinamfind.ui.mahasiswa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

class MahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var fakultas: String = ""

    private lateinit var binding: ActivityMahasiswaBinding

    private lateinit var mahasiswaFakultas: List<User>
    private lateinit var mahasiswaHorizontalAdapter: MahasiswaHorizontalAdapter
    private lateinit var mahasiswaGridAdapter: MahasiswaGridAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        fakultas = sharedPref.getString(Constanta.FAKULTAS_USER).toString()


        // List data mahasiswa random
        binding.rvMahasiswaRandom.layoutManager = GridLayoutManager(this, 2)
        mahasiswaGridAdapter = MahasiswaGridAdapter()
        binding.rvMahasiswaRandom.adapter = mahasiswaGridAdapter


        binding.imgBackMahasiswa.setOnClickListener {
            finish()
        }

        loadData()

    }

    private fun loadData() {
        mahasiswaSefakultas()
    }

    private fun mahasiswaSefakultas() {

        ApiClient.instances.getMahasiswaFakultas(fakultas, id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    if (response.isSuccessful) {
                        mahasiswaFakultas = response.body()?.mahasiswa_data!!
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

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }
}