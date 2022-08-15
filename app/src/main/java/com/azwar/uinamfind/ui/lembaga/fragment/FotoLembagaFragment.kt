package com.azwar.uinamfind.ui.lembaga.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Foto
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentFotoLembagaBinding
import com.azwar.uinamfind.ui.lembaga.adapter.FotoLembagaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FotoLembagaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var _binding: FragmentFotoLembagaBinding? = null
    private val binding get() = _binding!!

    private lateinit var fotoLembagaAdapter: FotoLembagaAdapter
    private lateinit var fotoList: List<Foto>

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFotoLembagaBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedPref = PreferencesHelper(context)
        var id = sharedPref.getString(Constanta.ID_USER).toString()
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val gson = Gson()
        val lembaga: LembagaKampus = gson.fromJson(json, LembagaKampus::class.java)


        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            setData(lembaga)
        })



        return view
    }

    private fun setData(lembaga: LembagaKampus) {
        loadFoto("Lembaga", lembaga.id)
    }

    private fun loadFoto(kategori: String, from_id: String?) {

        ApiClient.instances.getFoto(kategori, from_id)?.enqueue(object :
            Callback<Responses.ResponseFoto> {
            override fun onResponse(
                call: Call<Responses.ResponseFoto>,
                response: Response<Responses.ResponseFoto>
            ) {
                swipe_refresh.isRefreshing = false

                if (response.isSuccessful) {
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        fotoList = response.body()?.foto_data!!
                        if (fotoList.size > 1) {
                            binding.llEmpty.visibility = View.GONE
                            binding.rvFoto.visibility = View.VISIBLE

                            binding.rvFoto.layoutManager =
                                GridLayoutManager(activity, 3)
                            fotoLembagaAdapter = FotoLembagaAdapter(fotoList)
                            binding.rvFoto.adapter = fotoLembagaAdapter
                        } else {
                            binding.rvFoto.visibility = View.GONE
                            binding.llEmpty.visibility = View.VISIBLE
                        }
                    } else {
                        binding.rvFoto.visibility = View.GONE
                        binding.llEmpty.visibility = View.VISIBLE
                    }
                } else {
                    binding.rvFoto.visibility = View.GONE
                    binding.llEmpty.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Responses.ResponseFoto>, t: Throwable) {
                swipe_refresh.isRefreshing = false
                binding.rvFoto.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
            }

        })


    }

    override fun onRefresh() {
        var id = sharedPref.getString(Constanta.ID_USER).toString()
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val gson = Gson()
        val lembaga: LembagaKampus = gson.fromJson(json, LembagaKampus::class.java)
        setData(lembaga)
    }
}