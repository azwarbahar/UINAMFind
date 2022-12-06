package com.azwar.uinamfind.ui.pencarian.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.data.models.SearchMahasiswa
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentSearchMahasiswaBinding
import com.azwar.uinamfind.ui.beasiswa.adapter.BeasiswaAdapter
import com.azwar.uinamfind.ui.pencarian.PencarianInterfacr
import com.azwar.uinamfind.ui.pencarian.SearchHomeActivity
import com.azwar.uinamfind.ui.pencarian.adapter.SearchMahasiswaAdaper
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MahasiswaSearchFragment : Fragment(), PencarianInterfacr {

    private lateinit var sharedPref: PreferencesHelper

    private var _binding: FragmentSearchMahasiswaBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchHomeActivity: SearchHomeActivity

    private lateinit var searchMahasiswa: List<SearchMahasiswa>

    private lateinit var searchMahasiswaAdaper: SearchMahasiswaAdaper

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
        _binding = FragmentSearchMahasiswaBinding.inflate(inflater, container, false)
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
//        if (query.length > 2) {
        loadData(query)
//        } else {
//        }

//        binding.tv1.setText("Hasil Pencarian mahasiswa : $query")
//        Toast.makeText(activity, "hasil : $query", Toast.LENGTH_SHORT).show()
    }

    private fun loadData(query: String) {
        onLoadingSearch()
        ApiClient.instances.getMahasiswaQuery(query)
            ?.enqueue(object : Callback<Responses.ResponseSearch> {
                override fun onResponse(
                    call: Call<Responses.ResponseSearch>,
                    response: Response<Responses.ResponseSearch>
                ) {
                    if (response.isSuccessful) {
                        var kode = response.body()?.kode.toString()
                        var pesan = response.body()?.pesan.toString()
                        if (kode.equals("1")) {
                            searchMahasiswa = response.body()?.search_mahasiswa_data!!
                            if (searchMahasiswa.size > 0) {
                                onReadySearch()
                                val layoutManager: RecyclerView.LayoutManager =
                                    LinearLayoutManager(activity)
                                binding.rvMahasiswa.layoutManager = layoutManager
                                searchMahasiswaAdaper = SearchMahasiswaAdaper(searchMahasiswa)
                                binding.rvMahasiswa.adapter = searchMahasiswaAdaper
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