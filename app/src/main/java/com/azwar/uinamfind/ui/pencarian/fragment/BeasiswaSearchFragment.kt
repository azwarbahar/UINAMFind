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
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentSearchBeasiswaBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter
import com.azwar.uinamfind.ui.pencarian.PencarianInterfacr
import com.azwar.uinamfind.ui.pencarian.SearchHomeActivity
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BeasiswaSearchFragment : Fragment(), PencarianInterfacr {
    private var _binding: FragmentSearchBeasiswaBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var searchHomeActivity: SearchHomeActivity

    private lateinit var beasiswa: List<Beasiswa>

    private lateinit var beasiswaAdapter: BeasiswaAdapter

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
        _binding = FragmentSearchBeasiswaBinding.inflate(inflater, container, false)
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
        ApiClient.instances.getBeasiswaQuery(query)
            ?.enqueue(object : Callback<Responses.ResponseSearch> {
                override fun onResponse(
                    call: Call<Responses.ResponseSearch>,
                    response: Response<Responses.ResponseSearch>
                ) {
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode.toString()
                        var pesan = response.body()?.pesan.toString()
                        if (kode.equals("1")) {
                            beasiswa = response.body()?.search_beasiswa_data!!
                            if (beasiswa.size > 0) {
                                onReadySearch()
                                val rv_loker = binding.rvBeasiswa
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(activity)
                                rv_loker.layoutManager = layoutManagerLoker
                                beasiswaAdapter = BeasiswaAdapter(beasiswa)
                                rv_loker.adapter = beasiswaAdapter
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