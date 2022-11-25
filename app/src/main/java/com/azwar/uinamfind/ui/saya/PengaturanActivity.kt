package com.azwar.uinamfind.ui.saya

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityPengaturanBinding
import com.azwar.uinamfind.ui.MasukActivity
import com.azwar.uinamfind.ui.akun.UbahPasswordRecruiterActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PengaturanActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityPengaturanBinding

    private lateinit var sharedPref: PreferencesHelper

    private var imei: String? = null
    private var device: String? = null
    private var id: String = ""
    private var firstOpen = true

    private lateinit var swipe_saya: SwipeRefreshLayout

    // models
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        device = Build.DEVICE + " - " + Build.BRAND;

        binding.imgBack.setOnClickListener { finish() }

        binding.tvPerangkat.setText(device)

        binding.rlGantiPassword.setOnClickListener {
            val intent = Intent(this, UbahPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.rlDataAkun.setOnClickListener {
            val intent = Intent(this, DataAkunActivity::class.java)
            startActivity(intent)
        }

        binding.rlKeluar.setOnClickListener {

            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar Akun")
                .setContentText("Yakin ingin keluar dari akun ini ?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()

                    sharedPref.logout()
                    val intent = Intent(this, MasukActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                    it.dismiss()
                })
                .show()
        }

        binding.rlPenilaian.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=$packageName")
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }

        binding.switchAlumni

        binding.switchAlumni.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                sendData("Lulus")
            } else {
                sendData("Belum Lulus")
            }
        }

        swipe_saya = binding.swipeRefresh
        swipe_saya.setOnRefreshListener(this)
        swipe_saya.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_saya.post(Runnable {
            loadDataSaya(id)
        })
    }

    private fun sendData(status_kemahasiswaan: String) {
        // show progress loading
//        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
//        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
//        dialogProgress.titleText = "Loading.."
//        dialogProgress.setCancelable(false)
//        dialogProgress.show()

        ApiClient.instances.updateKelulusanMahasiswa(
            status_kemahasiswaan,
            id
        )?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseMahasiswa>,
                response: Response<Responses.ResponseMahasiswa>
            ) {

//                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode

                if (response.isSuccessful) {
                    if (kode.equals("1")) {

//                        SweetAlertDialog(
//                            this@PengaturanActivity,
//                            SweetAlertDialog.SUCCESS_TYPE
//                        )
//                            .setTitleText("Success..")
//                            .setContentText("Data Berhasil Diperbarui..")
//                            .setConfirmButton(
//                                "Ok"
//                            ) { sweetAlertDialog ->
//                                sweetAlertDialog.dismiss()
//
//                            }
//                            .show()
                    } else {
//                        SweetAlertDialog(
//                            this@PengaturanActivity,
//                            SweetAlertDialog.ERROR_TYPE
//                        )
//                            .setTitleText("Gagal..")
//                            .setContentText(message)
//                            .show()
                    }
                } else {
//                    SweetAlertDialog(
//                        this@PengaturanActivity,
//                        SweetAlertDialog.ERROR_TYPE
//                    )
//                        .setTitleText("Gagal..")
//                        .setContentText("Server tidak merespon1")
//                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseMahasiswa>,
                t: Throwable
            ) {
//                dialogProgress.dismiss()
//                SweetAlertDialog(this@PengaturanActivity, SweetAlertDialog.ERROR_TYPE)
//                    .setTitleText("Gagal..")
//                    .setContentText(t.localizedMessage)
//                    .show()
            }

        })


    }

    private fun loadDataSaya(id: String) {
        ApiClient.instances.getMahasiswaID(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    swipe_saya.isRefreshing = false
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataUser(user)
                        } else {
                            Toast.makeText(this@PengaturanActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@PengaturanActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    Toast.makeText(
                        this@PengaturanActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun initDataUser(user: User) {
        val lable = user.status_kemahasiswaan.toString()
        if (lable.equals("Lulus")) {
            binding.switchAlumni.isChecked = true
        } else {
            binding.switchAlumni.isChecked = false
        }
    }

    override fun onRefresh() {
        loadDataSaya(id)
    }

}