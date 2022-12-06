package com.azwar.uinamfind.ui.bantuan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.databinding.ActivityBantuanBinding


class BantuanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBantuanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBantuanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener { finish() }

        binding.cvTelegram.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://t.me/azwarbahar")
            val appName = "org.telegram.messenger"
            try {
                if (isAppAvailable(this.getApplicationContext(), appName)) i.setPackage(
                    appName
                )
            } catch (e: PackageManager.NameNotFoundException) {
            }
            startActivity(i)
        }

    }

    fun isAppAvailable(context: Context, appName: String?): Boolean {
        val pm: PackageManager = context.getPackageManager()
        return try {
            pm.getPackageInfo(appName!!, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}