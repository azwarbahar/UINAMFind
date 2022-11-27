package com.azwar.uinamfind.ui.ukm

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditUkmBinding
import com.azwar.uinamfind.utils.Constanta
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditUkmActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var ukm: Ukm

    private lateinit var binding: ActivityEditUkmBinding

    private var ukm_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUkmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        ukm = intent.getParcelableExtra("ukm")!!
        initData(ukm)

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
                ukm_id,
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

        ApiClient.instances.updateUkm(
            organisasiId, nama, kategori, tahunBerdiri, deskripsi, kontak, email, alamatSekretariat
        )?.enqueue(object :
            Callback<Responses.ResponseUkm> {
            override fun onResponse(
                call: Call<Responses.ResponseUkm>,
                response: Response<Responses.ResponseUkm>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        ukm = response.body()?.result_ukm!!

                        SweetAlertDialog(
                            this@EditUkmActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil tersimpan..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                setupShared(ukm)
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditUkmActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditUkmActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseUkm>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditUkmActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }


    private fun setupShared(ukm: Ukm) {
        val gson = Gson()
        val ukmJson = gson.toJson(ukm)
        sharedPref.put(Constanta.OBJECT_SELECTED, ukmJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "UKM")
    }

    private fun initData(ukm: Ukm) {
        ukm_id = ukm.id.toString()

        var nama = ukm.nama_ukm.toString()
        if (nama.equals("-") || nama.equals("null")) {

        } else {
            binding.etNama.setText(nama)
        }

        var kategori = ukm.kategori.toString()
        if (kategori.equals("-") || kategori.equals("null")) {

        } else {
            binding.etBidang.setText(kategori)
        }

        var deskripsi = ukm.deskripsi.toString()
        if (deskripsi.equals("-") || deskripsi.equals("null")) {

        } else {
            binding.etDeskripsi.setText(deskripsi)
        }

        var thn_berdiri = ukm.tahun_berdiri.toString()
        if (thn_berdiri.equals("") || thn_berdiri.equals("null")) {

        } else {
            binding.etTahunBerdiri.setText(thn_berdiri)
        }

        var kontak = ukm.kontak.toString()
        if (kontak.equals("") || kontak.equals("null")) {

        } else {
            binding.etKontak.setText(kontak)
        }

        var email = ukm.email.toString()
        if (email.equals("") || email.equals("null")) {

        } else {
            binding.etEmail.setText(email)
        }

        var alamat = ukm.alamat.toString()
        if (alamat.equals("") || alamat.equals("null")) {
        } else {
            binding.etAlamat.setText(alamat)
        }


    }
}