package com.azwar.uinamfind.ui.akun

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityUbahPasswordRecruiterBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UbahPasswordRecruiterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUbahPasswordRecruiterBinding

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUbahPasswordRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()

        binding.imgBack.setOnClickListener { finish() }

        binding.rlSimpan.setOnClickListener { clickSimpan() }

    }

    private fun clickSimpan() {

        var old_password = binding.tiePasswordLama.text.toString()
        var new_password = binding.tiePasswordBaru.text.toString()

        if (old_password.isEmpty() || old_password.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password lama tidak boleh kosong!")
                .show()
        } else if (new_password.isEmpty() || new_password.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password baru tidak boleh kosong!")
                .show()
        } else if (old_password.equals(new_password)) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gagal...")
                .setContentText("Password baru dan lama tidak boleh sama!")
                .show()
        } else {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ubah Password")
                .setContentText("Yakin ingin mengubah password ?")
                .setCancelButton(
                    "Tidak"
                ) { sweetAlertDialog -> sweetAlertDialog.dismiss() }
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startUpdatePassword(old_password, new_password)
                }
                .show()
        }


    }

    private fun startUpdatePassword(oldPassword: String, newPassword: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updatePasswordRecruiter(id, oldPassword, newPassword)
            ?.enqueue(object : Callback<Responses.ResponseRecruiter> {
                override fun onResponse(
                    call: Call<Responses.ResponseRecruiter>,
                    response: Response<Responses.ResponseRecruiter>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@UbahPasswordRecruiterActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Data berhasil diubah..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@UbahPasswordRecruiterActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@UbahPasswordRecruiterActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseRecruiter>, t: Throwable) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(
                        this@UbahPasswordRecruiterActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Maaf..")
                        .setContentText("Terjadi Kesalahan Sistem")
                        .show()
                }

            })


    }
}