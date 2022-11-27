package com.azwar.uinamfind.ui.organisasi

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditOrganisasiBinding
import com.azwar.uinamfind.utils.Constanta
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditOrganisasiActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var organisasi: Organisasi


    private lateinit var binding: ActivityEditOrganisasiBinding

    private var organisasi_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditOrganisasiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        organisasi = intent.getParcelableExtra("organisasi")!!
        initData(organisasi)

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }

        binding.imgBack.setOnClickListener { finish() }

    }

    private fun clickSimpan() {

        val nama = binding.etNama.text.toString()
        val kategori = binding.etBidang.text.toString()
        val alamat_sekretariat = binding.etAlamat.text.toString()
        val kontak = binding.etKontak.text.toString()
        val email = binding.etEmail.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()
        val tahun_berdiri = binding.etTahunBerdiri.text.toString()

        if (nama.equals("")) {
            binding.etNama.error = "Lengkapi"
        } else {
            startUpdateData(
                organisasi_id,
                nama,
                kategori,
                alamat_sekretariat,
                kontak,
                email,
                deskripsi,
                tahun_berdiri
            )
        }

    }

    private fun startUpdateData(
        organisasiId: String,
        nama: String,
        kategori: String,
        alamatSekretariat: String,
        kontak: String,
        email: String,
        deskripsi: String,
        tahunBerdiri: String
    ) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateOrganisasi(
            organisasiId, nama, kategori, tahunBerdiri, deskripsi, kontak, email, alamatSekretariat
        )?.enqueue(object :
            Callback<Responses.ResponseOrganisasi> {
            override fun onResponse(
                call: Call<Responses.ResponseOrganisasi>,
                response: Response<Responses.ResponseOrganisasi>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        organisasi = response.body()?.result_organisasi!!

                        SweetAlertDialog(
                            this@EditOrganisasiActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil tersimpan..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                setupShared(organisasi)
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditOrganisasiActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditOrganisasiActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseOrganisasi>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditOrganisasiActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }

    private fun setupShared(organisasi: Organisasi) {
        val gson = Gson()
        val organisasiJson = gson.toJson(organisasi)
        sharedPref.put(Constanta.OBJECT_SELECTED, organisasiJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "Organisasi")
    }

    private fun initData(organisasi: Organisasi) {
        organisasi_id = organisasi.id.toString()

        var nama = organisasi.nama_organisasi.toString()
        if (nama.equals("-") || nama.equals("null")) {

        } else {
            binding.etNama.setText(nama)
        }

        var kategori = organisasi.kategori.toString()
        if (kategori.equals("-") || kategori.equals("null")) {

        } else {
            binding.etBidang.setText(kategori)
        }

        var deskripsi = organisasi.deskripsi.toString()
        if (deskripsi.equals("-") || deskripsi.equals("null")) {

        } else {
            binding.etDeskripsi.setText(deskripsi)
        }

        var thn_berdiri = organisasi.tahun_berdiri.toString()
        if (thn_berdiri.equals("") || thn_berdiri.equals("null")) {

        } else {
            binding.etTahunBerdiri.setText(thn_berdiri)
        }

        var kontak = organisasi.kontak.toString()
        if (kontak.equals("") || kontak.equals("null")) {

        } else {
            binding.etKontak.setText(kontak)
        }

        var email = organisasi.email.toString()
        if (email.equals("") || email.equals("null")) {

        } else {
            binding.etEmail.setText(email)
        }

        var alamat = organisasi.alamat.toString()
        if (alamat.equals("") || alamat.equals("null")) {
        } else {
            binding.etAlamat.setText(alamat)
        }


    }
}