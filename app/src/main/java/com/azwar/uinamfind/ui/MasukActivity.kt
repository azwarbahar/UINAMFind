package com.azwar.uinamfind.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.azwar.uinamfind.R
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.ui.login.LoginMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import kotlinx.android.synthetic.main.activity_masuk.*

class MasukActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)
        sharedPref = PreferencesHelper(this)

        if (sharedPref.getBoolean(Constanta.IS_LOGIN)) {

            val role = sharedPref.getString(Constanta.ROLE)
            if (role.equals("user")) {
                val menu_mahasiswa = Intent(this, MainActivity::class.java)
                startActivity(menu_mahasiswa)
                finish()
            } else {
//                val menu_tamu = Intent(this, LoginTamuActivity::class.java)
//                startActivity(menu_tamu)

                Toast.makeText(this, "Masuk Tamu", Toast.LENGTH_SHORT).show()

            }

        }

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