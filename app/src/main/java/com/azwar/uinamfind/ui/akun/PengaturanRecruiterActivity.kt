package com.azwar.uinamfind.ui.akun

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build;
import android.os.Bundle
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityPengaturanRecruiterBinding

class PengaturanRecruiterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPengaturanRecruiterBinding

    private lateinit var sharedPref: PreferencesHelper

    private var imei: String? = null
    private var device: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengaturanRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        imei = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        device = Build.DEVICE + " - " + Build.BRAND;

        binding.imgBack.setOnClickListener { finish() }

        binding.tvPerangkat.setText(device)

        binding.rlKeluar.setOnClickListener {

            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Keluar Akun")
                .setContentText("Yakin ingin keluar dari akun ini ?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()

                    sharedPref.logout()
                    val intent = Intent(this, PengaturanRecruiterActivity::class.java)
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