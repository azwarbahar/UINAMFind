package com.azwar.uinamfind.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.azwar.uinamfind.R
import com.azwar.uinamfind.databinding.ActivityShowPhotoBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class ShowPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShowPhotoBinding

    private var foto: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        foto = intent.getStringExtra("foto")!!
//        val options: RequestOptions = RequestOptions()
//            .placeholder(R.drawable.loading_animation)
//            .error(R.drawable.ic_baseline_broken_image_white)
//            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(this)
            .load(foto)
            .into(binding.imgZoom)

        binding.imgClose.setOnClickListener { finish() }

    }
}