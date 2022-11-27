package com.azwar.uinamfind.ui.lamaran

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityLamaranSayaBinding
import com.azwar.uinamfind.ui.lamaran.adapter.LamaranSayaAdapter
import com.azwar.uinamfind.ui.loker.adapter.LokerAdapter
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LamaranSayaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityLamaranSayaBinding

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private lateinit var lamaranSayaAdapter: LamaranSayaAdapter
    private lateinit var lamarans: List<Lamaran>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLamaranSayaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

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
            loadLamaran()
        })

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
                    swipe_refresh.isRefreshing = false
                    dialogProgress.dismiss()
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            lamarans = response.body()?.lamaran_data!!
                            if (lamarans.size > 0) {

                                binding.llDataKosong.visibility = View.GONE
                                binding.rvLamaran.visibility = View.VISIBLE

                                val rv_lamaran = binding.rvLamaran
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(this@LamaranSayaActivity)
                                rv_lamaran.layoutManager = layoutManagerLoker
                                lamaranSayaAdapter = LamaranSayaAdapter(lamarans)
                                rv_lamaran.adapter = lamaranSayaAdapter

                            } else {
                                binding.llDataKosong.visibility = View.VISIBLE
                                binding.rvLamaran.visibility = View.GONE
                            }
                        } else {

                            binding.llDataKosong.visibility = View.VISIBLE
                            binding.rvLamaran.visibility = View.GONE
                        }
                    } else {

                        binding.llDataKosong.visibility = View.VISIBLE
                        binding.rvLamaran.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                    dialogProgress.dismiss()
                    swipe_refresh.isRefreshing = false
                    binding.llDataKosong.visibility = View.VISIBLE
                    binding.rvLamaran.visibility = View.GONE
                }
            })
    }

    override fun onRefresh() {
        loadLamaran()
    }

}