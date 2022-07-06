package com.azwar.uinamfind.ui.saya

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentSayaBinding
import com.azwar.uinamfind.ui.mahasiswa.adapter.KeahlianAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PendidikanAdapter
import com.azwar.uinamfind.ui.mahasiswa.adapter.PengalamanAdapter
import com.azwar.uinamfind.ui.saya.adapter.ListPengalamanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PengalamanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.organisasi.AddOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.organisasi.ListOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.AddPengalamanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.EditPengalamanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.ListPengalamanMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SayaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var sharedPref: PreferencesHelper
    private var _sayaBinding: FragmentSayaBinding? = null
    private val sayaBinding get() = _sayaBinding!!

    private lateinit var swipe_saya: SwipeRefreshLayout

    // models
    private lateinit var user: User

    // adapter
    private lateinit var keahlianAdapter: KeahlianAdapter
    private lateinit var pengalamanAdapter: PengalamanAdapter
    private lateinit var pendidikanAdapter: PendidikanAdapter
    private lateinit var organisasiMahasiswaAdapter: OrganisasiMahasiswaAdapter
    private lateinit var pengalamanMahasiswaAdapter: PengalamanMahasiswaAdapter

    private var id: String = ""

    private lateinit var dialogProgress: SweetAlertDialog

    companion object {
        fun newInstance() = SayaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _sayaBinding = FragmentSayaBinding.inflate(inflater, container, false)
        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        sayaBinding.rlEditProfilDetailMahasiswa.setOnClickListener {
            val intent_edit_profil = Intent(context, EditProfilMahasiswaActivity::class.java)
            startActivity(intent_edit_profil)
        }

        //organisasi
        sayaBinding.imgAddOrganisasiSaya.setOnClickListener {
            val intent = Intent(context, AddOrganisasiMahasiswaActivity::class.java)
            startActivity(intent)
        }
        sayaBinding.imgEditOrganisasiSaya.setOnClickListener {
            val intent = Intent(context, ListOrganisasiMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Pengalaman
        sayaBinding.imgAddPengalamanSaya.setOnClickListener {
            val intent = Intent(context, AddPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        sayaBinding.imgEditPengalamanSaya.setOnClickListener {
            val intent = Intent(context, ListPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }


        // logo back trans in card
        val img_back_logo_card = sayaBinding.imgBaclLogoCard
        img_back_logo_card.alpha = 0.1f

        // show progress loading

        dialogProgress = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        swipe_saya = sayaBinding.swipeSaya
        swipe_saya.setOnRefreshListener(this)
        swipe_saya.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_saya.post(Runnable {
            loadData()
        })

        return sayaBinding.root
    }

    private fun loadData() {
        loadDataSaya(id)
        laodDataOrganisasi(id)
        loadDataPengalaman(id)
    }

    private fun loadDataPengalaman(id: String) {
        ApiClient.instances.getPengalamanUser(id)?.enqueue(object :
            Callback<Responses.ResponsePengalamanMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                response: Response<Responses.ResponsePengalamanMahasiswa>
            ) {
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                val data = response.body()?.pengalaman_data
                if (response.isSuccessful) {
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            val rv_pengalaman = sayaBinding.rvPengalamanDetailMahasiswa
                            rv_pengalaman.layoutManager = LinearLayoutManager(activity)
                            pengalamanMahasiswaAdapter = PengalamanMahasiswaAdapter(data)

                            val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    context!!, R.drawable.divider
                                )
                            )
                            rv_pengalaman.addItemDecoration(dividerItemDecoration)
                            rv_pengalaman.adapter = pengalamanMahasiswaAdapter
                        } else {

                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun laodDataOrganisasi(id: String) {

        ApiClient.instances.getOrganisasiUser(id)
            ?.enqueue(object : Callback<Responses.ResponseOrganisasiMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    response: Response<Responses.ResponseOrganisasiMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val data = response.body()?.organisasi_data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            if (data!!.size > 0) {
                                val rv_org = sayaBinding.rvOrganisasiMahasiswaDetailMahasiswa
                                rv_org.layoutManager = LinearLayoutManager(activity)
                                organisasiMahasiswaAdapter = OrganisasiMahasiswaAdapter(data)

                                val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        context!!, R.drawable.divider
                                    )
                                )
                                rv_org.addItemDecoration(dividerItemDecoration)
                                rv_org.adapter = organisasiMahasiswaAdapter
                            } else {

                            }
                        } else {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun loadDataSaya(id: String) {
        swipe_saya.isRefreshing = false

        ApiClient.instances.getMahasiswaID(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataUser(user)
                        } else {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    dialogProgress.dismiss()
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })


    }

    private fun initDataUser(user: User) {
        var nim = user.nim

        // username
        var text_username = sayaBinding.tvToolbarSaya
        var username = user.username
        if (username == null) {
            text_username.text = nim
        } else {
            text_username.text = username
        }

        // nama
        var nama_depan = user.nama_depan
        var nama_belakang = user.nama_belakang
        var text_nama_lengkap = sayaBinding.tvNamaCardDetailMahasiswa
        if (nama_depan == null) {
            text_nama_lengkap.text = nim
        } else if (nama_belakang == null) {
            var nama_lengkap = user.nama_depan
            text_nama_lengkap.text = nama_lengkap
        } else {
            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
            text_nama_lengkap.text = nama_lengkap
        }

        // tentang saya
        var tentang_user = user.tentang_user
        var text_tentang_saya = sayaBinding.tvTentangSayaDetailMahasiswa
        if (tentang_user == null) {
            text_tentang_saya.setTypeface(text_tentang_saya.getTypeface(), Typeface.ITALIC)
            text_tentang_saya.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.dimens_fint_12)
            )
            text_tentang_saya.setTextColor(Color.parseColor("#ABABAB"))
            text_tentang_saya.text = "Belum diatur"
        } else {
            text_tentang_saya.text = tentang_user
        }

        // jurusan
        var text_jurusan = sayaBinding.tvJurusanCardDetailMahasiswa
        var jurusan = user.jurusan
        var fakultas = user.fakultas
        text_jurusan.text = jurusan + ", " + fakultas

        // lokasi
        var text_lokasi = sayaBinding.tvLokasiCardDetailMahasiswa
        var lokasi = user.lokasi
        text_lokasi.text = lokasi

        // motto
        loadMotto(id)

    }

    private fun loadMotto(id: String) {
        ApiClient.instances.getMottoId(id)
            ?.enqueue(object : Callback<Responses.ResponseMotto> {
                override fun onResponse(
                    call: Call<Responses.ResponseMotto>,
                    response: Response<Responses.ResponseMotto>
                ) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val motto = response.body()?.result_motto
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                            val motto_ = motto?.motto_profesional
                            if (motto_ == null) {
                                text_motto.text = "  -"
                            } else {
                                text_motto.text = motto_
                            }
                        } else {
                            Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                            val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                            text_motto.text = "  -"
                        }
                    } else {
                        Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                        val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                        text_motto.text = "  -"
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
                    Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                    val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                    text_motto.text = "  -"
                }

            })
    }

    override fun onRefresh() {
        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}