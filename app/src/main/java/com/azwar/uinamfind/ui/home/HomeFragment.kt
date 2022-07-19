package com.azwar.uinamfind.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azwar.uinamfind.databinding.FragmentHomeBinding
import com.azwar.uinamfind.data.models.BeasiswaModel
import com.azwar.uinamfind.data.models.LokerModel
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.ui.beasiswa.BeasiswaActivity
import com.azwar.uinamfind.ui.home.adapter.BeasiswaTerbaruAdapter
import com.azwar.uinamfind.ui.home.adapter.CardMahasiswaAdapter
import com.azwar.uinamfind.ui.home.adapter.LokerHomeAdapter
import com.azwar.uinamfind.ui.lembaga.LembagaActivity
import com.azwar.uinamfind.ui.loker.LokerActivity
import com.azwar.uinamfind.ui.magang.MagangActivity
import com.azwar.uinamfind.ui.mahasiswa.MahasiswaActivity
import com.azwar.uinamfind.ui.organisasi.OrganisasiActivity
import com.azwar.uinamfind.ui.pencarian.SearchHomeActivity
import com.azwar.uinamfind.ui.ukm.UKMActivity
import com.azwar.uinamfind.utils.Constanta
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!

    lateinit var lokerHomeAdapter: LokerHomeAdapter
    lateinit var lokerModel: List<LokerModel>

    lateinit var beasiswaTerbaruAdapter: BeasiswaTerbaruAdapter
    lateinit var beasiswaModel: List<BeasiswaModel>

    private lateinit var cardMahasiswaAdapter: CardMahasiswaAdapter
    private lateinit var mahasiswa: List<User>

    private lateinit var bottomSheetBehaviorMenuLainnya: BottomSheetBehavior<FrameLayout>

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        // Bottom Sheet Menu
        bottomSheetBehaviorMenuLainnya =
            BottomSheetBehavior.from(homeBinding.continerBottomSheetMenuHome)

        homeBinding.rlAlertLengkapiDataHome.visibility = View.GONE

        homeBinding.rlMahasiswaBottomSheetMenuHome.setOnClickListener {
            bottomSheetBehaviorMenuLainnya.setState(BottomSheetBehavior.STATE_HIDDEN)
            val intent_mahasiswa = Intent(context, MahasiswaActivity::class.java)
            startActivity(intent_mahasiswa)

        }
//        val dialog = BottomSheetDialog(this)

        homeBinding.rlLembagaBottomSheetMenuHome.setOnClickListener {
            bottomSheetBehaviorMenuLainnya.setState(BottomSheetBehavior.STATE_HIDDEN)
            val intent_lembaga = Intent(context, LembagaActivity::class.java)
            startActivity(intent_lembaga)
        }

        homeBinding.llMenuLainnyaHome.setOnClickListener {

//            MenuHomeBottomSheetDialogFragment().apply {
//                show(parentFragmentManager, "TAG")
//            }
            val state =
                if (bottomSheetBehaviorMenuLainnya.state == BottomSheetBehavior.STATE_EXPANDED)
                    BottomSheetBehavior.STATE_COLLAPSED
                else
                    BottomSheetBehavior.STATE_EXPANDED
            bottomSheetBehaviorMenuLainnya.state = state
        }

        homeBinding.llMenuMahasiswaHome.setOnClickListener {
            val intent_mahasiswa = Intent(context, MahasiswaActivity::class.java)
            startActivity(intent_mahasiswa)
        }

        homeBinding.llMenuLembagaHome.setOnClickListener {
            val intent_lembaga = Intent(context, LembagaActivity::class.java)
            startActivity(intent_lembaga)
        }

        homeBinding.llMenuUkmHome.setOnClickListener {
            val inten_ukm = Intent(context, UKMActivity::class.java)
            startActivity(inten_ukm)
        }

        homeBinding.llMenuOrganisasiHome.setOnClickListener {
            val inten_organisasi = Intent(context, OrganisasiActivity::class.java)
            startActivity(inten_organisasi)
        }

        homeBinding.llMenuInfoMagangHome.setOnClickListener {
            val inten_magang = Intent(context, MagangActivity::class.java)
            startActivity(inten_magang)
        }

        homeBinding.llMenuInfoBeasiswaHome.setOnClickListener {
            val inten_beasiswa = Intent(context, BeasiswaActivity::class.java)
            startActivity(inten_beasiswa)
        }

        homeBinding.llMenuInfoLokerHome.setOnClickListener {
            val inten_loker = Intent(context, LokerActivity::class.java)
            startActivity(inten_loker)
        }

        homeBinding.tvLihatSemuaBeasiswaTerbaru.setOnClickListener {
            val inten_beasiswa = Intent(context, BeasiswaActivity::class.java)
            startActivity(inten_beasiswa)
        }

        homeBinding.tvLihatSemuaRekomendasiLoker.setOnClickListener {
            val intent_loker = Intent(context, LokerActivity::class.java)
            startActivity(intent_loker)
        }

        homeBinding.rlSearchHome.setOnClickListener {
            val intent_search_home = Intent(context, SearchHomeActivity::class.java)
            startActivity(intent_search_home)
        }


        homeBinding.tvLihatSemuaProfilTerbaruHome.setOnClickListener {
            val intent_mahasiswa = Intent(context, MahasiswaActivity::class.java)
            startActivity(intent_mahasiswa)
        }


        // List rekomendasi loker
        val layoutManagerRekomendasiLoker: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvRekomendasiLokerHome.layoutManager = layoutManagerRekomendasiLoker
        lokerHomeAdapter = LokerHomeAdapter()
        homeBinding.rvRekomendasiLokerHome.adapter = lokerHomeAdapter

        // List Beasiswa Terbaru
        val linearManagerBeasiswaTerbaru: RecyclerView.LayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        homeBinding.rvBeasiswaTerbaruHome.layoutManager = linearManagerBeasiswaTerbaru
        beasiswaTerbaruAdapter = BeasiswaTerbaruAdapter()
        homeBinding.rvBeasiswaTerbaruHome.adapter = beasiswaTerbaruAdapter

        loadData()

        return homeBinding.root
    }

    private fun loadData() {
//        checkProfil(id)
        loadMahasiswaNewUPdate()

    }

    private fun loadMahasiswaNewUPdate() {

        ApiClient.instances.getMahasiswaNewUpdate()
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        mahasiswa = response.body()?.mahasiswa_data!!

                        // list profile terbaru
                        val layoutManagerProfileTerbaru: RecyclerView.LayoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                        homeBinding.rvProfilTerbaruHome.layoutManager = layoutManagerProfileTerbaru
                        cardMahasiswaAdapter = CardMahasiswaAdapter(mahasiswa)
                        homeBinding.rvProfilTerbaruHome.adapter = cardMahasiswaAdapter

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                }
            })
    }

    private fun checkProfil(id: String) {


        ApiClient.instances.checkProfilMahasiswa(id)
            ?.enqueue(object : Callback<Responses.ResponseCheckProfilMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseCheckProfilMahasiswa>,
                    response: Response<Responses.ResponseCheckProfilMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val foto = response.body()?.foto
                    val tentang = response.body()?.tentang_user
                    val keahlian = response.body()?.keahlian
                    val pendidikan = response.body()?.pendidikan
                    val pengalaman = response.body()?.pengalaman
                    val organisasi = response.body()?.organisasi
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataCheck(
                                foto,
                                tentang,
                                keahlian,
                                pendidikan,
                                pengalaman,
                                organisasi
                            )
                        } else {
                            Toast.makeText(
                                activity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseCheckProfilMahasiswa>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        activity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })


    }

    private fun initDataCheck(
        foto: String?,
        tentang: String?,
        keahlian: Int?,
        pendidikan: Int?,
        pengalaman: Int?,
        organisasi: Int?
    ) {
        if (foto.equals("")) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else if (tentang.equals("")) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else if (keahlian!! < 1) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else if (pendidikan!! < 1) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else if (pengalaman!! < 1) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else if (organisasi!! < 1) {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.VISIBLE
        } else {
            homeBinding.rlAlertLengkapiDataHome.visibility = View.GONE
        }
    }

}