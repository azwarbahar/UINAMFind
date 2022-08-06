package com.azwar.uinamfind.ui.lembaga.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Anggota
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentAnggotaLembagaBinding
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.lembaga.adapter.AnggotaLembagaAdapter
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.azwar.uinamfind.ui.organisasi.adapter.OrganisasiAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_anggota_lembaga.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnggotaLembagaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var _binding: FragmentAnggotaLembagaBinding? = null
    private val binding get() = _binding!!

    private lateinit var listAnggota: List<Anggota>
    private lateinit var anggotaLembagaAdapter: AnggotaLembagaAdapter

    private lateinit var user: User

    private lateinit var swipe_refresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAnggotaLembagaBinding.inflate(inflater, container, false)
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

        binding.rlAdmin.setOnClickListener {
            if (!id.equals(user.id)) {
                val intent_detail_mahasiswa =
                    Intent(context, DetailMahasiswaActivity::class.java)
                intent_detail_mahasiswa.putExtra("mahasiswa", user)
                startActivity(intent_detail_mahasiswa)
            }
        }

        binding.imgChat.setOnClickListener {
            if (!id.equals(user.id)) {
                val intent_room_chat = Intent(context, RoomChatActivity::class.java)
                startActivity(intent_room_chat)
            }
        }



        return view
    }

    private fun setData(lembaga: LembagaKampus) {
        loadUser(lembaga.admin)
        loadAnggota("Lembaga", lembaga.id, lembaga.admin)
    }

    private fun loadAnggota(kategori: String, from_id: String?, user_id: String?) {

        ApiClient.instances.getAnggota(kategori, from_id, user_id)?.enqueue(object :
            Callback<Responses.ResponseAnggota> {
            override fun onResponse(
                call: Call<Responses.ResponseAnggota>,
                response: Response<Responses.ResponseAnggota>
            ) {
                if (response.isSuccessful) {
                    listAnggota = response.body()?.anggota_data!!
                    var size = listAnggota.size
                    binding.tv2.setText(size.toString() + " Orang")
                    binding.rvAnggotaLembaga.layoutManager =
                        LinearLayoutManager(activity)
                    anggotaLembagaAdapter = AnggotaLembagaAdapter(listAnggota)
                    val dividerItemDecoration: RecyclerView.ItemDecoration = DividerItemDecorator(
                        ContextCompat.getDrawable(
                            context!!, R.drawable.divider
                        )
                    )
                    binding.rvAnggotaLembaga.addItemDecoration(dividerItemDecoration)
                    binding.rvAnggotaLembaga.adapter = anggotaLembagaAdapter

                }

            }

            override fun onFailure(call: Call<Responses.ResponseAnggota>, t: Throwable) {

            }

        })

    }

    private fun loadUser(userId: String?) {

        ApiClient.instances.getMahasiswaID(userId)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {

                        var nim = user.nim

                        // nama
                        var nama_depan = user.nama_depan
                        var nama_belakang = user.nama_belakang
                        var text_nama_lengkap = binding.tvNamaUser
                        if (nama_depan == null) {
                            text_nama_lengkap.text = nim
                        } else if (nama_belakang == null) {
                            var nama_lengkap = user.nama_depan
                            text_nama_lengkap.text = nama_lengkap
                        } else {
                            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
                            text_nama_lengkap.text = nama_lengkap
                        }
                        val foto = user.foto
                        if (foto.equals("-")) {
                        } else {
                            Glide.with(this@AnggotaLembagaFragment)
                                .load(foto)
                                .into(binding.imgPhotoUser)
                        }
                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                }

            })

    }


    override fun onRefresh() {
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val gson = Gson()
        val lembaga: LembagaKampus = gson.fromJson(json, LembagaKampus::class.java)
        setData(lembaga)
    }

}