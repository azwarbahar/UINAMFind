package com.azwar.uinamfind.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.login.LoginMahasiswaActivity
import kotlinx.android.synthetic.main.activity_masuk.*

class MasukActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        rl_btn_mahasiswa.setOnClickListener {
            val intent_login_mahasiswa = Intent(this, LoginMahasiswaActivity::class.java)
            startActivity(intent_login_mahasiswa)
        }

        rl_btn_tamu.setOnClickListener {
            val intent_login_tamu = Intent(this, LoginTamuActivity::class.java)
            startActivity(intent_login_tamu)
        }

    }
}