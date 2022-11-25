package com.azwar.uinamfind.ui.lembaga

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.LembagaKampus
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailLembagaBinding
import com.azwar.uinamfind.ui.lembaga.adapter.ViewPagerLembagaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class DetailLembagaActivity : AppCompatActivity() {

    private val PROFILE_IMAGE_REQ_CODE = 101

    private lateinit var sharedPref: PreferencesHelper
    private lateinit var lembaga: LembagaKampus
    private var id: String = ""
    private var role: String = ""

    val tabArray = arrayOf(
        "Tentang",
        "Anggota",
        "Foto"
    )
    val tabArrayAdmin = arrayOf(
        "Tentang",
        "Anggota",
        "Foto"
    )

    private lateinit var binding: ActivityDetailLembagaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLembagaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()
        lembaga = intent.getParcelableExtra("lembaga")!!


        val toolbar = binding.toolbarLembagaDetail
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(lembaga.nama)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        setupShared(lembaga)
        setupTabFragment(id, lembaga)

        val foto = lembaga.foto.toString()
        if (foto.equals("-") || foto.equals("null")) {
        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(binding.imgHeaderLembagaDetail)
        }

        binding.imgHeaderLembagaDetail.setOnClickListener {

            var admin_id = lembaga.admin.toString()
            if (role.equals("user")) {
                if (admin_id.equals(id)) {
                    val pilihan = arrayOf(
                        "Ganti Gambar",
                        "Lihat Gambar"
                    )
                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setTitle("Opsi")
                    builder.setItems(pilihan) { dialog, which ->
                        when (which) {
                            0 -> openImagePicker(PROFILE_IMAGE_REQ_CODE)
                            1 -> openPreviewImage("Profil")
                        }
                    }
                    val dialog = builder.create()
                    dialog.show()
                }
            }

        }

    }

    private fun openPreviewImage(s: String) {

    }

    private fun openImagePicker(s: Int) {
        ImagePicker.with(this)
            .galleryOnly()
            .cropSquare()
            .compress(1024)
            .maxResultSize(620, 620)
            .start(PROFILE_IMAGE_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                binding.imgHeaderLembagaDetail.setImageURI(null)
                binding.imgHeaderLembagaDetail.setImageURI(uri)
                startUploadPhoto(uri)
            }
        }
    }

    private fun startUploadPhoto(uri: Uri) {

        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val file = File(uri.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send = MultipartBody.Part.createFormData("foto", file.name, foto)
        val id = RequestBody.create("text/plain".toMediaTypeOrNull(), lembaga.id.toString())

        ApiClient.instances.updatePhotoLembaga(id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseLembaga> {
                override fun onResponse(
                    call: Call<Responses.ResponseLembaga>,
                    response: Response<Responses.ResponseLembaga>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            lembaga = response.body()?.result_lembaga!!
                            initNewData(lembaga)
                            Toast.makeText(
                                this@DetailLembagaActivity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(this@DetailLembagaActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@DetailLembagaActivity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseLembaga>, t: Throwable) {
                    Toast.makeText(this@DetailLembagaActivity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

    }

    private fun initNewData(lembaga: LembagaKampus) {
        setupShared(lembaga)
        val foto = lembaga.foto.toString()
        if (foto.equals("-") || foto.equals("null")) {
        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(binding.imgHeaderLembagaDetail)
        }
    }


    private fun setupShared(lembaga: LembagaKampus) {
        val gson = Gson()
        val lembagaJson = gson.toJson(lembaga)
        sharedPref.put(Constanta.OBJECT_SELECTED, lembagaJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "Lembaga")
    }

    private fun setupTabFragment(id: String, lembaga: LembagaKampus) {

        val viewPager = binding.viewPagerLembaga
        val tabLayout = binding.tabLayoutLembaga

        val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 3)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

//        var admin_id = lembaga.admin
//        if (admin_id != null) {
//            if (admin_id.equals(id)) {
//                val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 4)
//                viewPager.adapter = adapter
//                TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//                    tab.text = tabArrayAdmin[position]
//                }.attach()
//            } else {
//            }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}