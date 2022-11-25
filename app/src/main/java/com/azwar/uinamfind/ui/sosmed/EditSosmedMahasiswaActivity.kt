package com.azwar.uinamfind.ui.sosmed

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditSosmedMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditSosmedMahasiswaActivity : AppCompatActivity() {
    private var sosmed_id: String = ""
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var kategori: String = ""

    private lateinit var binding: ActivityEditSosmedMahasiswaBinding

    private lateinit var sosmed: Sosmed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditSosmedMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        sosmed = intent.getParcelableExtra("sosmed")!!
        initDataIntent(sosmed)

        binding.imgBack.setOnClickListener { finish() }

        binding.llHapus.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startDelet(sosmed_id)
                }
                .show()
        }

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }


    }

    private fun clickSimpan() {
        val url_sosmed = binding.etUrlSosmed.text
        if (url_sosmed.isEmpty()) {
            binding.etUrlSosmed.error = "Lengkapi"
        } else {
            startEditData(sosmed_id, url_sosmed.toString())
        }
    }

    private fun startEditData(sosmedId: String, urlSosmed: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateSosmed(
            sosmedId, urlSosmed
        )?.enqueue(object : Callback<Responses.ResponseSosmed> {
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
                            this@EditSosmedMahasiswaActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil Diperbarui..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditSosmedMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditSosmedMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon1")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseSosmed>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditSosmedMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText(t.localizedMessage)
                    .show()
            }

        })


    }

    private fun startDelet(sosmedId: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.deleteSosmed(sosmedId)
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
                                this@EditSosmedMahasiswaActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Data Berhasil dihapus..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@EditSosmedMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@EditSosmedMahasiswaActivity,
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
                    SweetAlertDialog(
                        this@EditSosmedMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })
    }


    private fun initDataIntent(sosmed: Sosmed) {
        sosmed_id = sosmed.id.toString()
        binding.tvNamaSosmed.setText(sosmed.nama_sosmed.toString())
        binding.etUrlSosmed.setText(sosmed.url_sosmed.toString())
    }
}