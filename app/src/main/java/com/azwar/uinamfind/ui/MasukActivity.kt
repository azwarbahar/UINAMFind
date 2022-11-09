package com.azwar.uinamfind.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.azwar.uinamfind.R
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.databinding.ActivityMasukBinding
import com.azwar.uinamfind.ui.login.LoginMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta

class MasukActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMasukBinding

    private lateinit var sharedPref: PreferencesHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMasukBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)

        if (sharedPref.getBoolean(Constanta.IS_LOGIN)) {

            val role = sharedPref.getString(Constanta.ROLE)
            if (role.equals("user")) {
                val menu_mahasiswa = Intent(this, MainActivity::class.java)
                startActivity(menu_mahasiswa)
                finish()
            } else if (role.equals("recruiter")) {
                val menu_recruiter = Intent(this, MainRecruiterActivity::class.java)
                startActivity(menu_recruiter)
                finish()
//                Toast.makeText(this, "Masuk Tamu", Toast.LENGTH_SHORT).show()
            }

        }

        binding.rlBtnMahasiswa.setOnClickListener {
            val intent_login_mahasiswa = Intent(this, LoginMahasiswaActivity::class.java)
            startActivity(intent_login_mahasiswa)
        }

        binding.rlBtnTamu.setOnClickListener {
            val intent_login_tamu = Intent(this, LoginTamuActivity::class.java)
            startActivity(intent_login_tamu)
        }

    }
}