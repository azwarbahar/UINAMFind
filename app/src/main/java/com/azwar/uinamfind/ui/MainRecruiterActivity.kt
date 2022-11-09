package com.azwar.uinamfind.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.azwar.uinamfind.R
import kotlinx.android.synthetic.main.activity_main_recruiter.*

class MainRecruiterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_recruiter)

        val bottomNavigationView = bottomNavigationViewRecruiter
        val navController: NavController =
            Navigation.findNavController(this, R.id.nav_host_fragment_recruiter)
        NavigationUI.setupWithNavController(bottomNavigationViewRecruiter, navController)

    }
}