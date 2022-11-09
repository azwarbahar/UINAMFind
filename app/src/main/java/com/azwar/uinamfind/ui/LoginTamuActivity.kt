package com.azwar.uinamfind.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityLoginTamuBinding
import com.azwar.uinamfind.utils.Constanta
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login_mahasiswa.*
import kotlinx.android.synthetic.main.activity_login_tamu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTamuActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var binding: ActivityLoginTamuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginTamuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        binding.llBtnKembaliLoginTamu.setOnClickListener {
            finish()
        }

        binding.rlBtnMasukTamu.setOnClickListener {
            val email = binding.tieEmailLoginTamu.text.toString()
            val password = binding.tiePasswordLoginTamu.text.toString()

            if (email.isEmpty()) {
                val snack =
                    Snackbar.make(it, "Lengkapi Eamil anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else if (password.isEmpty()) {
                val snack = Snackbar.make(it, "Lengkapi Password anda!", Snackbar.LENGTH_LONG)
                snack.show()
            } else {
                setLogin(email, password)
            }

        }

        binding.rlBtnDaftarTamu.setOnClickListener {
            val inten_main = Intent(this, DaftarTamuActivity::class.java)
            startActivity(inten_main)
        }
    }

    private fun setLogin(email: String, password: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading"
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.loginRecruiter(email, password)
            ?.enqueue(object : Callback<Responses.LoginRecruiterResponse> {
                override fun onResponse(
                    call: Call<Responses.LoginRecruiterResponse?>,
                    response: Response<Responses.LoginRecruiterResponse?>
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
                        } else {

                            SweetAlertDialog(
                                this@LoginTamuActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Uups..")
                                .setContentText(message)
                                .show()

                        }
                    } else {
                        SweetAlertDialog(this@LoginTamuActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText(pesanRespon)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.LoginRecruiterResponse?>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@LoginTamuActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText(t.localizedMessage)
                        .show()
                }

            })
    }

    private fun setLoginSuccess(role: String, data: Recruiter) {
        sharedPref.put(Constanta.IS_LOGIN, true)
        sharedPref.put(Constanta.ROLE, role)
        sharedPref.put(Constanta.ID_RECRUITER, data.id!!)

        startActivity(Intent(this, MainRecruiterActivity::class.java))

        finish()
//        Toast.makeText(this, "Berhasil Login", Toast.LENGTH_SHORT).show()
    }

}