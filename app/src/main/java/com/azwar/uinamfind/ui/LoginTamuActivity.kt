package com.azwar.uinamfind.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import kotlinx.android.synthetic.main.activity_login_mahasiswa.*
import kotlinx.android.synthetic.main.activity_login_tamu.*

class LoginTamuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_tamu)


        ll_btn_kembali_login_tamu.setOnClickListener {
            finish()
        }

        rl_btn_masuk_tamu.setOnClickListener {
            val inten_main = Intent(this, MainActivity::class.java)
            startActivity(inten_main)
        }

        rl_btn_daftar_tamu.setOnClickListener {
            val inten_main = Intent(this, DaftarTamuActivity::class.java)
            startActivity(inten_main)
        }
    }
}