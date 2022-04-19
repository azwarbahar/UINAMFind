package com.azwar.uinamfind.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.azwar.uinamfind.R
import com.azwar.uinamfind.ui.chat.ChatActivity
import com.azwar.uinamfind.ui.notifikasi.NotifikasiActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = bottomNavigationView
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment)
        setupWithNavController(bottomNavigationView, navController)

    }
}