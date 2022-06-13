package com.azwar.uinamfind.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login_mahasiswa.*

class LoginMahasiswaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_mahasiswa)

        ll_btn_kembali_login_mahasiswa.setOnClickListener {
            finish()
        }

        rl_btn_masuk_mahasiswa.setOnClickListener {
            val inten_main = Intent(this, MainActivity::class.java)
            startActivity(inten_main)
        }


    }
}