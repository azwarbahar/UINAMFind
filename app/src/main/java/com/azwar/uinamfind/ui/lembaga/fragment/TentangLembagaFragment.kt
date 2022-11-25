package com.azwar.uinamfind.ui.lembaga.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.*
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentTentangLembagaBinding
import com.azwar.uinamfind.ui.kegiatan.AddKegiatanActivity
import com.azwar.uinamfind.ui.kegiatan.adapter.KegiatanGridAdapter
import com.azwar.uinamfind.ui.lembaga.EditLembagaActivity
import com.azwar.uinamfind.ui.lembaga.adapter.SosmedLembagaAdapter
import com.azwar.uinamfind.ui.sosmed.AddSosmedMahasiswaActivity
import com.azwar.uinamfind.ui.sosmed.ListSosmedMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.MyTextViewDesc
import com.bumptech.glide.Glide
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TentangLembagaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var _binding: FragmentTentangLembagaBinding? = null
    private val binding get() = _binding!!
    private var id:String = ""
    private var role:String = ""

    private lateinit var user: User

    private lateinit var sosmedList: List<Sosmed>
    private lateinit var kegiatanList: List<Kegiatan>

    private lateinit var sosmedLembagaAdapter: SosmedLembagaAdapter
    private lateinit var kegiatanGridAdapter: KegiatanGridAdapter

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private lateinit var lembaga: LembagaKampus
    private lateinit var organisasi: Organisasi
    private lateinit var ukm: Ukm

    private var kategori_objek = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTentangLembagaBinding.inflate(inflater, container, false)
        val view = binding.root
        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        kategori_objek = sharedPref.getString(Constanta.KEY_OBJECT_SELECTED).toString()
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
                setDataLembaga(lembaga, kategori_objek)
            } else if (kategori_objek.equals("Organisasi")) {
                setDataOrganisasi(organisasi, kategori_objek)
            } else if (kategori_objek.equals("UKM")) {
                setDataUkm(ukm, kategori_objek)
            }
        })

        binding.imgAddSosmed.setOnClickListener {
            var intent_id = "-"
            if (kategori_objek.equals("Lembaga")) {
                intent_id = lembaga.id.toString()
            } else if (kategori_objek.equals("Organisasi")) {
                intent_id = organisasi.id.toString()
            } else if (kategori_objek.equals("UKM")) {
                intent_id = ukm.id.toString()
            }
            val intent = Intent(context, AddSosmedMahasiswaActivity::class.java)
            intent.putExtra("kategori", kategori_objek)
            intent.putExtra("id", intent_id)
            startActivity(intent)
        }

        binding.imgAddKegiatan.setOnClickListener {
            var intent_id = "-"
            if (kategori_objek.equals("Lembaga")) {
                intent_id = lembaga.id.toString()
            } else if (kategori_objek.equals("Organisasi")) {
                intent_id = organisasi.id.toString()
            } else if (kategori_objek.equals("UKM")) {
                intent_id = ukm.id.toString()
            }
            val intent = Intent(context, AddKegiatanActivity::class.java)
            intent.putExtra("kategori", kategori_objek)
            intent.putExtra("id", intent_id)
            startActivity(intent)
        }

        binding.imgEditSosmed.setOnClickListener {
            var intent_id = "-"
            if (kategori_objek.equals("Lembaga")) {
                intent_id = lembaga.id.toString()
            } else if (kategori_objek.equals("Organisasi")) {
                intent_id = organisasi.id.toString()
            } else if (kategori_objek.equals("UKM")) {
                intent_id = ukm.id.toString()
            }
            val intent = Intent(context, ListSosmedMahasiswaActivity::class.java)
            intent.putExtra("kategori", kategori_objek)
            intent.putExtra("id", intent_id)
            startActivity(intent)
        }

        binding.imgEditInfo.setOnClickListener {
            val intent = Intent(context, EditLembagaActivity::class.java)
            intent.putExtra("lembaga", lembaga)
            startActivity(intent)
        }


        return view
    }

    private fun setDataLembaga(lembaga: LembagaKampus, kategori_objek: String?) {
        var admin_id = lembaga.admin.toString()
        if (role.equals("user")) {
            if (admin_id.equals(id)) {
                binding.imgAddSosmed.visibility = View.VISIBLE
                binding.imgEditSosmed.visibility = View.VISIBLE

                binding.imgEditInfo.visibility = View.VISIBLE

                binding.imgAddKegiatan.visibility = View.VISIBLE

            } else {
                binding.imgAddSosmed.visibility = View.GONE
                binding.imgEditSosmed.visibility = View.GONE

                binding.imgEditInfo.visibility = View.GONE

                binding.imgAddKegiatan.visibility = View.GONE
            }
        } else {
            binding.imgAddSosmed.visibility = View.GONE
            binding.imgEditSosmed.visibility = View.GONE

            binding.imgEditInfo.visibility = View.GONE

            binding.imgAddKegiatan.visibility = View.GONE
        }

        var deskripsi = lembaga.deskripsi.toString()
        var tv_desc = binding.tvDeskripsiLembaga
        if (deskripsi.equals("-") || deskripsi.equals("null")) {
            tv_desc.text = deskripsi
        } else {
            tv_desc.text = deskripsi
//                    if (binding.tvDescItemPengalamanMahasiswa.lineCount > 2) {
            val myTextViewDesc = MyTextViewDesc()
            myTextViewDesc.makeTextViewResizable(tv_desc, 5, ".. Lihat lengkap", true)
//                    }
        }

        binding.llKategori.visibility = View.GONE
        var thn_berdiri = lembaga.tahun_berdiri.toString()
        if (thn_berdiri.equals("") || thn_berdiri.equals("null")) {
            binding.tvTahunBerdiri.setText("-")
        } else {
            binding.tvTahunBerdiri.setText(thn_berdiri)
        }

        var kontak = lembaga.kontak.toString()
        if (kontak.equals("") || kontak.equals("null")) {
            binding.tvKontak.setText("-")
        } else {
            binding.tvKontak.setText(kontak)
        }

        var email = lembaga.email.toString()
        if (email.equals("") || email.equals("null")) {
            binding.tvEmail.setText("-")
        } else {
            binding.tvEmail.setText(email)
        }

        var alamat = lembaga.alamat_sekretariat.toString()
        if (alamat.equals("") || alamat.equals("null")) {
            binding.tvAlamat.setText("-")
        } else {
            binding.tvAlamat.setText(alamat)
        }

        val id = lembaga.id
        if (kategori_objek != null) {
            setSosmed(id, kategori_objek)
            setKegiatan(id, kategori_objek)
        }

    }

    private fun setDataOrganisasi(organisasi: Organisasi, kategori_objek: String?) {
        var admin_id = organisasi.admin.toString()
        if (role.equals("user")) {
            if (admin_id.equals(id)) {
                binding.imgAddSosmed.visibility = View.VISIBLE
                binding.imgEditSosmed.visibility = View.VISIBLE

                binding.imgEditInfo.visibility = View.VISIBLE

                binding.imgAddKegiatan.visibility = View.VISIBLE

            } else {
                binding.imgAddSosmed.visibility = View.GONE
                binding.imgEditSosmed.visibility = View.GONE

                binding.imgEditInfo.visibility = View.GONE

                binding.imgAddKegiatan.visibility = View.GONE
            }
        } else {
            binding.imgAddSosmed.visibility = View.GONE
            binding.imgEditSosmed.visibility = View.GONE

            binding.imgEditInfo.visibility = View.GONE

            binding.imgAddKegiatan.visibility = View.GONE
        }


        var deskripsi = organisasi.deskripsi.toString()
        var tv_desc = binding.tvDeskripsiLembaga
        if (deskripsi.equals("-") || deskripsi.equals("null")) {
            tv_desc.text = deskripsi
        } else {
            tv_desc.text = deskripsi
//                    if (binding.tvDescItemPengalamanMahasiswa.lineCount > 2) {
            val myTextViewDesc = MyTextViewDesc()
            myTextViewDesc.makeTextViewResizable(tv_desc, 5, ".. Lihat lengkap", true)
//                    }
        }

        val id = organisasi.id
        if (kategori_objek != null) {
            setSosmed(id, kategori_objek)
            setKegiatan(id, kategori_objek)
        }
    }

    private fun setDataUkm(ukm: Ukm, kategori_objek: String?) {
        var admin_id = ukm.admin.toString()
        if (role.equals("user")) {
            if (admin_id.equals(id)) {
                binding.imgAddSosmed.visibility = View.VISIBLE
                binding.imgEditSosmed.visibility = View.VISIBLE

                binding.imgEditInfo.visibility = View.VISIBLE

                binding.imgAddKegiatan.visibility = View.VISIBLE

            } else {
                binding.imgAddSosmed.visibility = View.GONE
                binding.imgEditSosmed.visibility = View.GONE

                binding.imgEditInfo.visibility = View.GONE

                binding.imgAddKegiatan.visibility = View.GONE
            }
        } else {
            binding.imgAddSosmed.visibility = View.GONE
            binding.imgEditSosmed.visibility = View.GONE

            binding.imgEditInfo.visibility = View.GONE

            binding.imgAddKegiatan.visibility = View.GONE
        }


//        var deskripsi = ukm.deskripsi
//        var tv_desc = binding.tvDeskripsiLembaga
//        if (deskripsi.equals("-") || deskripsi == null) {
//            tv_desc.text = deskripsi
//        } else {
//            tv_desc.text = deskripsi
////                    if (binding.tvDescItemPengalamanMahasiswa.lineCount > 2) {
//            val myTextViewDesc = MyTextViewDesc()
//            myTextViewDesc.makeTextViewResizable(tv_desc, 5, ".. Lihat lengkap", true)
////                    }
//        }
//
//        val id = organisasi.id
//        setSosmed(id, "Organisasi")
//        setKegiatan(id, "Organisasi")
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

                    } else {

                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                }
            })
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
        val json = sharedPref.getString(Constanta.OBJECT_SELECTED)
        val kategori_objek = sharedPref.getString(Constanta.KEY_OBJECT_SELECTED)
        val gson = Gson()
        if (kategori_objek.equals("Lembaga")) {
            lembaga = gson.fromJson(json, LembagaKampus::class.java)
            setDataLembaga(lembaga, kategori_objek)
        } else if (kategori_objek.equals("Organisasi")) {
            organisasi = gson.fromJson(json, Organisasi::class.java)
            setDataOrganisasi(organisasi, kategori_objek)
        } else if (kategori_objek.equals("UKM")) {
            ukm = gson.fromJson(json, Ukm::class.java)
            setDataUkm(ukm, kategori_objek)
        }
    }

}