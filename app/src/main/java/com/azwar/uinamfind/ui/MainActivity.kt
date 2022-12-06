package com.azwar.uinamfind.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.ui.chat.ChatActivity
import com.azwar.uinamfind.ui.notifikasi.NotifikasiActivity
import com.azwar.uinamfind.ui.saya.UbahPasswordActivity
import com.azwar.uinamfind.utils.Constanta
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var role: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()

        val bottomNavigationView = bottomNavigationView
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView, navController)

        if (role.equals("user")) {
            checkPassword(id)
        }

    }

    private fun checkPassword(id: String) {

        ApiClient.instances.checkPassword(id)
            ?.enqueue(object : Callback<Responses.LoginUserResponse> {
                override fun onResponse(
                    call: Call<Responses.LoginUserResponse?>,
                    response: Response<Responses.LoginUserResponse?>
                ) {
                    val pesanRespon = response.message()
                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {

                            SweetAlertDialog(this@MainActivity, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                                .setTitleText("Ganti Password")
                                .setCustomImage(R.drawable.illustration_pass)
                                .setContentText(message)
                                .setCancelText("Tidak")
                                .setConfirmButton(
                                    "Ganti"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    val intent =
                                        Intent(this@MainActivity, UbahPasswordActivity::class.java)
                                    startActivity(intent)
                                }
                                .showCancelButton(true)
                                .setCancelClickListener { sDialog -> sDialog.cancel() }
                                .show()

                        } else {
//                            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
//                        Toast.makeText(
//                            this@MainActivity,
//                            "Server Tidak Merespon",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                }

                override fun onFailure(call: Call<Responses.LoginUserResponse?>, t: Throwable) {
//                    Toast.makeText(this@MainActivity, "Server Tidak Merespon", Toast.LENGTH_SHORT)
//                        .show()
                }

            })

    }


}