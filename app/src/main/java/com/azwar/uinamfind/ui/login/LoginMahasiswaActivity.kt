package com.azwar.uinamfind.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityLoginMahasiswaBinding
import com.azwar.uinamfind.ui.MainActivity
import com.azwar.uinamfind.ui.bantuan.BantuanActivity
import com.azwar.uinamfind.utils.Constanta
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginMahasiswaActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var loginMahasiswaBinding: ActivityLoginMahasiswaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginMahasiswaBinding = ActivityLoginMahasiswaBinding.inflate(layoutInflater)
        setContentView(loginMahasiswaBinding.root)

        sharedPref = PreferencesHelper(this)

        loginMahasiswaBinding.llBtnKembaliLoginMahasiswa.setOnClickListener {
            finish()
        }

        loginMahasiswaBinding.rlBtnMasukMahasiswa.setOnClickListener {
            val nim = loginMahasiswaBinding.tieNimLoginMahasiswa.text.toString()
            val password = loginMahasiswaBinding.tiePasswordLoginMahasiswa.text.toString()

            if (nim.isEmpty()) {
                val snack =
                    Snackbar.make(it, "Lengkapi NIM atau Username anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else if (password.isEmpty()) {
                val snack = Snackbar.make(it, "Lengkapi Password anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                setLogin(nim, password)
            }

//            Toast.makeText(this, "NIM " + nim, Toast.LENGTH_SHORT).show()

//            val inten_main = Intent(this, MainActivity::class.java)
//            startActivity(inten_main)
        }

        loginMahasiswaBinding.tvBantuan.setOnClickListener {
            val intent = Intent(this, BantuanActivity::class.java)
            startActivity(intent)
        }

    }

    private fun setLogin(nim: String, password: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading"
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.login(nim, password)
            ?.enqueue(object : Callback<Responses.LoginUserResponse> {
                override fun onResponse(
                    call: Call<Responses.LoginUserResponse?>,
                    response: Response<Responses.LoginUserResponse?>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val role = response.body()?.role
                    val data = response.body()?.data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            setLoginSuccess(role!!, data!!)
                            updateStausUser(data)
                        } else {

                            SweetAlertDialog(
                                this@LoginMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Uups..")
                                .setContentText(message)
                                .show()

                        }
                    } else {
                        SweetAlertDialog(this@LoginMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText(pesanRespon)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Responses.LoginUserResponse?>, t: Throwable) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@LoginMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText(t.localizedMessage)
                        .show()
                }

            })

    }

    private fun updateStausUser(data: User) {


    }

    private fun setLoginSuccess(role: String, data: User) {
        sharedPref.put(Constanta.IS_LOGIN, true)

        sharedPref.put(Constanta.ROLE, role)
        sharedPref.put(Constanta.ID_USER, data.id!!)
        sharedPref.put(Constanta.FAKULTAS_USER, data.fakultas!!)
        sharedPref.put(Constanta.JURUSAN_USER, data.jurusan!!)

        startActivity(Intent(this, MainActivity::class.java))

        finish()
    }
}