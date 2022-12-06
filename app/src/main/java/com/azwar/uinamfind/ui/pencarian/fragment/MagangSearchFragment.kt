package com.azwar.uinamfind.ui.pencarian.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.Beasiswa
import com.azwar.uinamfind.data.models.Magang
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentSearchBeasiswaBinding
import com.azwar.uinamfind.databinding.FragmentSearchMagangBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter
import com.azwar.uinamfind.ui.magang.adapter.MagangTerbaruAdapter
import com.azwar.uinamfind.ui.pencarian.PencarianInterfacr
import com.azwar.uinamfind.ui.pencarian.SearchHomeActivity
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MagangSearchFragment : Fragment(), PencarianInterfacr {
    private var _binding: FragmentSearchMagangBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var searchHomeActivity: SearchHomeActivity

    private lateinit var magang: List<Magang>

    private lateinit var magangTerbaruAdapter: MagangTerbaruAdapter

    var onFirstTime = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        searchHomeActivity = context as SearchHomeActivity
        searchHomeActivity.setPencarianUpdateListener(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchMagangBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedPref = PreferencesHelper(context)
        onLoadingSearch()
        var querySP = sharedPref.getString(Constanta.QUERY_SEARCH).toString()
        if (querySP.equals("") || querySP.equals("null")) {
        } else {
            loadData(querySP)
        }

        return view
    }

    override fun updateQuery(query: String) {
        loadData(query)
    }


    private fun loadData(query: String) {
        onLoadingSearch()
        ApiClient.instances.getMagangQuery(query)
            ?.enqueue(object : Callback<Responses.ResponseSearch> {
                override fun onResponse(
                    call: Call<Responses.ResponseSearch>,
                    response: Response<Responses.ResponseSearch>
                ) {
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode.toString()
                        var pesan = response.body()?.pesan.toString()
                        if (kode.equals("1")) {
                            magang = response.body()?.search_magang_data!!
                            if (magang.size > 0) {
                                onReadySearch()
                                val rv_loker = binding.rvMagang
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(activity)
                                rv_loker.layoutManager = layoutManagerLoker
                                magangTerbaruAdapter = MagangTerbaruAdapter(magang)
                                rv_loker.adapter = magangTerbaruAdapter
                            } else {
//                                Toast.makeText(context, "Kosong", Toast.LENGTH_SHORT).show()
                                onEmptySearch()
                            }
                        } else {
//                            Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show()
                            onEmptySearch()
                        }

                    } else {
//                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        onEmptySearch()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseSearch>, t: Throwable) {
                    onEmptySearch()
//                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })


    }


    private fun onLoadingSearch() {
        binding.continerSearchAnimation.visibility = View.VISIBLE
        binding.continerData.visibility = View.GONE
        binding.llDataKosong.visibility = View.GONE
    }

    private fun onEmptySearch() {
        binding.continerSearchAnimation.visibility = View.GONE
        binding.continerData.visibility = View.GONE
        binding.llDataKosong.visibility = View.VISIBLE
    }

    private fun onReadySearch() {
        binding.continerSearchAnimation.visibility = View.GONE
        binding.continerData.visibility = View.VISIBLE
        binding.llDataKosong.visibility = View.GONE
    }

}