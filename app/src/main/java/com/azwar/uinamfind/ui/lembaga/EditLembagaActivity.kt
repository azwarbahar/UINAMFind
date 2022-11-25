package com.azwar.uinamfind.ui.lembaga

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditLembagaBinding
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.MyTextViewDesc
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLembagaActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var lembaga: LembagaKampus

    private lateinit var binding: ActivityEditLembagaBinding

    private var lembaga_id: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLembagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)

        lembaga = intent.getParcelableExtra("lembaga")!!
        initData(lembaga)

        binding.tvSimpanEditProfil.setOnClickListener {
            clickSimpan()
        }

        binding.imgBack.setOnClickListener { finish() }

    }

    private fun clickSimpan() {

        val nama = binding.etNama.text.toString()
        val alamat_sekretariat = binding.etAlamat.text.toString()
        val kontak = binding.etKontak.text.toString()
        val email = binding.etEmail.text.toString()
        val deskripsi = binding.etDeskripsi.text.toString()
        val tahun_berdiri = binding.etTahunBerdiri.text.toString()

        if (nama.equals("")) {
            binding.etNama.error = "Lengkapi"
        } else {
            startUpdateData(
                lembaga_id,
                nama,
                alamat_sekretariat,
                kontak,
                email,
                deskripsi,
                tahun_berdiri
            )
        }

    }

    private fun startUpdateData(
        lembagaId: String,
        nama: String,
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

        ApiClient.instances.updateLembaga(
            lembagaId, nama, alamatSekretariat, kontak, email, deskripsi, tahunBerdiri
        )?.enqueue(object :
            Callback<Responses.ResponseLembaga> {
            override fun onResponse(
                call: Call<Responses.ResponseLembaga>,
                response: Response<Responses.ResponseLembaga>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        lembaga = response.body()?.result_lembaga!!

                        SweetAlertDialog(
                            this@EditLembagaActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil tersimpan..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                setupShared(lembaga)
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditLembagaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditLembagaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseLembaga>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditLembagaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }

    private fun setupShared(lembaga: LembagaKampus) {
        val gson = Gson()
        val lembagaJson = gson.toJson(lembaga)
        sharedPref.put(Constanta.OBJECT_SELECTED, lembagaJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "Lembaga")
    }

    private fun initData(lembaga: LembagaKampus) {
        lembaga_id = lembaga.id.toString()

        var nama = lembaga.nama.toString()
        if (nama.equals("-") || nama.equals("null")) {

        } else {
            binding.etNama.setText(nama)
        }

        var deskripsi = lembaga.deskripsi.toString()
        if (deskripsi.equals("-") || deskripsi.equals("null")) {

        } else {
            binding.etDeskripsi.setText(deskripsi)
        }

        var thn_berdiri = lembaga.tahun_berdiri.toString()
        if (thn_berdiri.equals("") || thn_berdiri.equals("null")) {

        } else {
            binding.etTahunBerdiri.setText(thn_berdiri)
        }

        var kontak = lembaga.kontak.toString()
        if (kontak.equals("") || kontak.equals("null")) {

        } else {
            binding.etKontak.setText(kontak)
        }

        var email = lembaga.email.toString()
        if (email.equals("") || email.equals("null")) {

        } else {
            binding.etEmail.setText(email)
        }

        var alamat = lembaga.alamat_sekretariat.toString()
        if (alamat.equals("") || alamat.equals("null")) {
        } else {
            binding.etAlamat.setText(alamat)
        }


    }
}