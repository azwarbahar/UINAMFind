package com.azwar.uinamfind.ui.saya.tentang

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditTentangMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTentangMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var tentang_saya: String = ""

    private lateinit var binding: ActivityEditTentangMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTentangMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        tentang_saya = intent.getStringExtra("tentang").toString()

        initData(tentang_saya)

        binding.imgBackEditTentang.setOnClickListener { finish() }

        binding.imgSimpanPengalaman.setOnClickListener {
            sendData()
        }
    }

    private fun sendData() {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val text_tentang = binding.tieTentangSaya.text

        ApiClient.instances.updateTentangSaya(
            text_tentang.toString(),
            id
        )?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseMahasiswa>,
                response: Response<Responses.ResponseMahasiswa>
            ) {

                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode

                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@EditTentangMahasiswaActivity,
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
                            this@EditTentangMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditTentangMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon1")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditTentangMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText(t.localizedMessage)
                    .show()
            }

        })


    }

    private fun initData(tentangSaya: String) {
        if (tentangSaya.isEmpty() || tentangSaya.equals("null")){
            binding.tieTentangSaya.setText("")
        } else {
            binding.tieTentangSaya.setText(tentangSaya)
        }

    }
}