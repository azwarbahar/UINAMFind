package com.azwar.uinamfind.ui.saya.sosmed

import android.R
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityAddSosmedMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddSosmedMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var nama_sosmed: String = ""

    private lateinit var binding: ActivityAddSosmedMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSosmedMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        setupSpinner()

        binding.imgBack.setOnClickListener { finish() }

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }
    }

    private fun clickSimpan() {
        val url_sosmed = binding.etUrlSosmed.text

        if (url_sosmed.isEmpty()) {
            binding.etUrlSosmed.error = "Lengkapi"
        } else {
            var kategori = "Mahasiswa"
            startAddData(nama_sosmed, kategori, id, url_sosmed.toString())
        }
    }

    private fun startAddData(namaSosmed: String, kategori: String, id: String, urlSosmed: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.addSosmed(namaSosmed, kategori, id, urlSosmed)
            ?.enqueue(object : Callback<Responses.ResponseSosmed> {
                override fun onResponse(
                    call: Call<Responses.ResponseSosmed>,
                    response: Response<Responses.ResponseSosmed>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {

                            SweetAlertDialog(
                                this@AddSosmedMahasiswaActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Data Berhasil tersimpan..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@AddSosmedMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@AddSosmedMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseSosmed>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@AddSosmedMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })
    }

    private fun setupSpinner() {
        val arrayLevelSkill = arrayOf(
            "Facebook",
            "Instagram",
            "Whatsapp",
            "Twitter",
            "TikTok",
            "Youtube",
            "Linkedin",
            "Github",
            "Telegram",
            "Pinterest",
            "Website"
        )
        val spinnerLevelSkill = binding.spNamaSosmed
        val arrayAdapterLevelSkill =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayLevelSkill)
        spinnerLevelSkill.adapter = arrayAdapterLevelSkill

        spinnerLevelSkill?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    nama_sosmed = arrayLevelSkill[position]
//                Toast.makeText(applicationContext, arrayJenisPengalaman[position], Toast.LENGTH_SHORT).show()
                }
            }
    }
}