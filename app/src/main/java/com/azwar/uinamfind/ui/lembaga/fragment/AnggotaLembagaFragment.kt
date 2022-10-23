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
import com.azwar.uinamfind.data.models.*
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
    private lateinit var lembaga: LembagaKampus
    private lateinit var organisasi: Organisasi
    private lateinit var ukm: Ukm

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
        val kategori_objek = sharedPref.getString(Constanta.KEY_OBJECT_SELECTED)
        val gson = Gson()
        if (kategori_objek.equals("Lembaga")) {
            lembaga = gson.fromJson(json, LembagaKampus::class.java)
        } else if (kategori_objek.equals("Organisasi")) {
            organisasi = gson.fromJson(json, Organisasi::class.java)
        } else if (kategori_objek.equals("UKM")) {
            ukm = gson.fromJson(json, Ukm::class.java)
        }

        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            if (kategori_objek.equals("Lembaga")) {
                setDataLembaga(lembaga)
            } else if (kategori_objek.equals("Organisasi")) {
                setDataOrganisasi(organisasi)
            } else if (kategori_objek.equals("UKM")) {
                setDataUkm(ukm)
            }
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

    private fun setDataLembaga(lembaga: LembagaKampus) {
        loadUser(lembaga.admin)
        loadAnggota("Lembaga", lembaga.id, lembaga.admin)
    }

    private fun setDataOrganisasi(organisasi: Organisasi) {
        loadUser(organisasi.admin)
        loadAnggota("Organisasi", organisasi.id, organisasi.admin)
    }

    private fun setDataUkm(ukm: Ukm) {
//        loadUser(organisasi.admin)
//        loadAnggota("Organisasi", organisasi.id, organisasi.admin)
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
                    if (size > 0) {
                        binding.rvAnggotaLembaga.visibility = View.VISIBLE
                        binding.llEmpty.visibility = View.GONE
                        binding.rvAnggotaLembaga.layoutManager =
                            LinearLayoutManager(activity)
                        anggotaLembagaAdapter = AnggotaLembagaAdapter(listAnggota)
                        val dividerItemDecoration: RecyclerView.ItemDecoration =
                            DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    context!!, R.drawable.divider
                                )
                            )
                        binding.rvAnggotaLembaga.addItemDecoration(dividerItemDecoration)
                        binding.rvAnggotaLembaga.adapter = anggotaLembagaAdapter
                    } else {
                        binding.rvAnggotaLembaga.visibility = View.GONE
                        binding.llEmpty.visibility = View.VISIBLE
                    }
                } else {
                    binding.rvAnggotaLembaga.visibility = View.GONE
                    binding.llEmpty.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<Responses.ResponseAnggota>, t: Throwable) {
                binding.rvAnggotaLembaga.visibility = View.GONE
                binding.llEmpty.visibility = View.VISIBLE
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
                        val foto = user.foto.toString()
                        if (foto.equals("-") || foto.equals("null")) {
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
        var id = sharedPref.getString(Constanta.ID_USER).toString()
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val kategori_objek = sharedPref.getString(Constanta.KEY_OBJECT_SELECTED)
        val gson = Gson()
        if (kategori_objek.equals("Lembaga")) {
            lembaga = gson.fromJson(json, LembagaKampus::class.java)
        } else if (kategori_objek.equals("Organisasi")) {
            organisasi = gson.fromJson(json, Organisasi::class.java)
        } else if (kategori_objek.equals("UKM")) {
            ukm = gson.fromJson(json, Ukm::class.java)
        }
        if (kategori_objek.equals("Lembaga")) {
            setDataLembaga(lembaga)
        } else if (kategori_objek.equals("Organisasi")) {
            setDataOrganisasi(organisasi)
        } else if (kategori_objek.equals("UKM")) {
            setDataUkm(ukm)
        }
    }

}