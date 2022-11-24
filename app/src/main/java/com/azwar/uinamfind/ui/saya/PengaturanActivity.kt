package com.azwar.uinamfind.ui.saya

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityPengaturanBinding
import com.azwar.uinamfind.ui.MasukActivity
import com.azwar.uinamfind.ui.akun.UbahPasswordRecruiterActivity

class PengaturanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengaturanBinding

    private lateinit var sharedPref: PreferencesHelper

    private var imei: String? = null
    private var device: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

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


    }
}