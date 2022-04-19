package com.azwar.uinamfind.ui.loker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.azwar.uinamfind.R


class DetailLokerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_loker)

        val toolbar_loker_detail: Toolbar = findViewById(R.id.toolbar_loker_detail)
        setSupportActionBar(toolbar_loker_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}