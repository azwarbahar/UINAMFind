package com.azwar.uinamfind.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import kotlinx.android.synthetic.main.activity_daftar_tamu.*
import kotlinx.android.synthetic.main.activity_login_tamu.*

class DaftarTamuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_tamu)

        ll_btn_kembali_daftar_tamu.setOnClickListener {
            finish()
        }

        rl_btn_submit_daftar_tamu.setOnClickListener {
            val inten_main = Intent(this, MainActivity::class.java)
            startActivity(inten_main)
        }

        tv_login_daftar_tamu.setOnClickListener {
            val inten_main = Intent(this, LoginTamuActivity::class.java)
            startActivity(inten_main)
        }
    }
}