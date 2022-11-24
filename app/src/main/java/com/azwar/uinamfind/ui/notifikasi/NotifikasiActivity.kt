package com.azwar.uinamfind.ui.notifikasi

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Informasi
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityNotifikasiBinding
import com.azwar.uinamfind.ui.MainRecruiterActivity
import com.azwar.uinamfind.ui.lamaran.LamaranSayaActivity
import com.azwar.uinamfind.ui.loker.adapter.LokerAdapter
import com.azwar.uinamfind.ui.notifikasi.adapter.InformasiAdapter
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifikasiActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityNotifikasiBinding

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var recruiter_id: String = ""
    private var role: String = ""

    private lateinit var informasi: List<Informasi>
    private lateinit var informasiAdapter: InformasiAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout
    private lateinit var lamarans: List<Lamaran>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        recruiter_id = sharedPref.getString(Constanta.ID_RECRUITER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()

        binding.imgBack.setOnClickListener { finish() }

        swipe_refresh = binding.swipeRefresh
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

        if (role.equals("recruiter")) {
            binding.rlLamaranAnda.visibility = View.GONE
        }


        binding.rlLamaranAnda.setOnClickListener {
            val intent = Intent(this, LamaranSayaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun loadData() {
        loadInformasi()
        if (role.equals("user")) {
            loadLamaran()
        }

    }

    private fun loadLamaran() {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.getLamaranMahasiswa(id)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    dialogProgress.dismiss()
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            lamarans = response.body()?.lamaran_data!!
                            binding.tvJumlahLamaran.setText(lamarans.size.toString())
                        } else {

                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                    dialogProgress.dismiss()

                }
            })
    }

    private fun loadInformasi() {
        ApiClient.instances.getInformasi(role)
            ?.enqueue(object : Callback<Responses.ResponseInformasi> {
                override fun onResponse(
                    call: Call<Responses.ResponseInformasi>,
                    response: Response<Responses.ResponseInformasi>
                ) {
                    swipe_refresh.isRefreshing = false
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            informasi = response.body()?.informasi_data!!
                            if (informasi.size > 0) {
                                binding.llDataKosong.visibility = View.GONE
                                binding.rvInformasi.visibility = View.VISIBLE
                                val rv_informasi = binding.rvInformasi
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(this@NotifikasiActivity)
                                rv_informasi.layoutManager = layoutManagerLoker
                                informasiAdapter = InformasiAdapter(informasi)
                                rv_informasi.adapter = informasiAdapter
                            } else {
                                binding.llDataKosong.visibility = View.VISIBLE
                                binding.rvInformasi.visibility = View.GONE
                            }
                        } else {
                            binding.llDataKosong.visibility = View.VISIBLE
                            binding.rvInformasi.visibility = View.GONE
                        }
                    } else {
                        binding.llDataKosong.visibility = View.VISIBLE
                        binding.rvInformasi.visibility = View.GONE
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseInformasi>, t: Throwable) {
                    // do something
                    swipe_refresh.isRefreshing = false
                    binding.llDataKosong.visibility = View.VISIBLE
                    binding.rvInformasi.visibility = View.GONE
                }

            })
    }


    override fun onRefresh() {
        loadData()
    }
}