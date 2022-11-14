package com.azwar.uinamfind.ui.loker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailPostinganLokerBinding
import com.azwar.uinamfind.ui.akun.adpter.LamaranMahasiswaBaruAdapter
import com.azwar.uinamfind.ui.akun.adpter.LokerRecruiterAdapter
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DetailPostinganLokerActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var perusahaan: Perusahaan
    private lateinit var recruiter: Recruiter
    private lateinit var loker: Loker

    private lateinit var lamaranMahasiswaBaruAdapter: LamaranMahasiswaBaruAdapter
    private lateinit var lamarans: List<Lamaran>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private lateinit var binding: ActivityDetailPostinganLokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPostinganLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()
        loker = intent.getParcelableExtra("loker")!!

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            initData(loker)
        })

        binding.imgBack.setOnClickListener { finish() }

    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
    }

    private fun initData(loker: Loker) {

        binding.tvNamaLoker.setText(loker.posisi.toString())
        binding.tvLokasiLoker.setText(loker.lokasi.toString() + ", Indonesia")
        binding.tvTglLoker.setText("Dibagikan pada " + convertDate(loker.created_at.toString()))
        loadPelamar(loker.id.toString())
    }

    private fun loadPelamar(id: String?) {

        ApiClient.instances.getLamaranLokerRecruiter(id)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    swipe_refresh.isRefreshing = false
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            lamarans = response.body()?.lamaran_data!!
                            if (lamarans.size > 0) {
                                binding.tvPelamarTidakAda.visibility = View.GONE
                                binding.rvPelamar.visibility = View.VISIBLE
                                binding.tv1Pelamar.text =
                                    "Pelamar (" + lamarans.size.toString() + ")"
                                val rv_lamaran = binding.rvPelamar
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(this@DetailPostinganLokerActivity)
                                rv_lamaran.layoutManager = layoutManagerLoker
                                lamaranMahasiswaBaruAdapter = LamaranMahasiswaBaruAdapter(lamarans)
                                rv_lamaran.adapter = lamaranMahasiswaBaruAdapter
                            } else {
                                binding.tvPelamarTidakAda.visibility = View.VISIBLE
                                binding.rvPelamar.visibility = View.GONE
                            }
                        } else {
                            binding.tvPelamarTidakAda.visibility = View.VISIBLE
                            binding.rvPelamar.visibility = View.GONE
                        }
                    } else {
                        binding.tvPelamarTidakAda.visibility = View.VISIBLE
                        binding.rvPelamar.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    binding.tvPelamarTidakAda.visibility = View.VISIBLE
                    binding.rvPelamar.visibility = View.GONE
                }
            })

    }

    override fun onRefresh() {
        initData(loker)
    }

}