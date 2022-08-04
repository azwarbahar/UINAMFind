package com.azwar.uinamfind.ui.organisasi.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Kegiatan
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentTentangLembagaBinding
import com.azwar.uinamfind.ui.kegiatan.adapter.KegiatanGridAdapter
import com.azwar.uinamfind.ui.lembaga.adapter.SosmedLembagaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.MyTextViewDesc
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TentangOrganisasiFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var _binding: FragmentTentangLembagaBinding? = null
    private val binding get() = _binding!!

    private lateinit var organisasi: Organisasi
    private lateinit var sosmedList: List<Sosmed>
    private lateinit var kegiatanList: List<Kegiatan>

    private lateinit var sosmedLembagaAdapter: SosmedLembagaAdapter
    private lateinit var kegiatanGridAdapter: KegiatanGridAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTentangLembagaBinding.inflate(inflater, container, false)
        val view = binding.root

        sharedPref = PreferencesHelper(context)
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val gson = Gson()
        organisasi = gson.fromJson(json, Organisasi::class.java)

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            setData(organisasi)
        })

        return view
    }

    private fun setData(organisasi: Organisasi?) {
        var deskripsi = organisasi?.deskripsi
        var tv_desc = binding.tvDeskripsiLembaga
        if (deskripsi.equals("-") || deskripsi == null) {
            tv_desc.text = deskripsi
        } else {
            tv_desc.text = deskripsi
//                    if (binding.tvDescItemPengalamanMahasiswa.lineCount > 2) {
            val myTextViewDesc = MyTextViewDesc()
            myTextViewDesc.makeTextViewResizable(tv_desc, 5, ".. Lihat lengkap", true)
//                    }
        }

        val id = organisasi?.id
        setSosmed(id, "Organisasi")
        setKegiatan(id, "Organisasi")


    }

    private fun setKegiatan(id: String?, kategori: String) {


        ApiClient.instances.getKegiatanKategori(id, kategori)?.enqueue(object :
            Callback<Responses.ResponseKegiatan> {
            override fun onResponse(
                call: Call<Responses.ResponseKegiatan>,
                response: Response<Responses.ResponseKegiatan>
            ) {
                swipe_refresh.isRefreshing = false
                if (response.isSuccessful) {
                    kegiatanList = response.body()?.kegiatan_data!!
                    val rv_kegiatan = binding.rvKegiatanLembaga
                    rv_kegiatan.layoutManager =
                        GridLayoutManager(activity, 2)
                    kegiatanGridAdapter = KegiatanGridAdapter(kegiatanList)
                    rv_kegiatan.adapter = kegiatanGridAdapter
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseKegiatan>, t: Throwable) {
                swipe_refresh.isRefreshing = false
            }

        })

    }

    private fun setSosmed(id: String?, kategori: String) {

        ApiClient.instances.getSosmedKategori(id, kategori)?.enqueue(object :
            Callback<Responses.ResponseSosmed> {
            override fun onResponse(
                call: Call<Responses.ResponseSosmed>,
                response: Response<Responses.ResponseSosmed>
            ) {
                swipe_refresh.isRefreshing = false
                if (response.isSuccessful) {
                    sosmedList = response.body()?.sosmed_data!!
                    val rv_sosmed = binding.rvSosmedLembaga
                    rv_sosmed.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    sosmedLembagaAdapter = SosmedLembagaAdapter(sosmedList)
                    rv_sosmed.adapter = sosmedLembagaAdapter
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseSosmed>, t: Throwable) {
                swipe_refresh.isRefreshing = false
            }

        })

    }


    override fun onRefresh() {
        setData(organisasi)
    }

}