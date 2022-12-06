package com.azwar.uinamfind.ui.ukm

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Organisasi
import com.azwar.uinamfind.data.models.Ukm
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailUkmBinding
import com.azwar.uinamfind.ui.ShowPhotoActivity
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

class DetailUkmActivity : AppCompatActivity() {

    private val PROFILE_IMAGE_REQ_CODE = 101

    private lateinit var sharedPref: PreferencesHelper

    private lateinit var binding: ActivityDetailUkmBinding

    private lateinit var ukm: Ukm
    private var id: String = ""
    private var role: String = ""

    val tabArray = arrayOf(
        "Tentang",
        "Anggota",
        "Foto"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUkmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()
        ukm = intent.getParcelableExtra("ukm")!!

        setupShared(ukm)
        setupTabFragment()

        val toolbar = binding.toolbarOrganisasiDetail
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(ukm.nama_ukm)
//        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        val foto = ukm.foto.toString()
        if (foto.equals("-") || foto.equals("null")) {
        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(binding.imgHeaderOrganisasiDetail)
        }
        binding.imgHeaderOrganisasiDetail.setOnClickListener {

            var admin_id = ukm.admin.toString()
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
        var foto = ukm.foto
        var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto
        val intent = Intent(this, ShowPhotoActivity::class.java)
        intent.putExtra("foto", foto_intent)
        startActivity(intent)
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
                binding.imgHeaderOrganisasiDetail.setImageURI(null)
                binding.imgHeaderOrganisasiDetail.setImageURI(uri)
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
        val id = RequestBody.create("text/plain".toMediaTypeOrNull(), ukm.id.toString())

        ApiClient.instances.updatePhotoUkm(id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseUkm> {
                override fun onResponse(
                    call: Call<Responses.ResponseUkm>,
                    response: Response<Responses.ResponseUkm>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            ukm = response.body()?.result_ukm!!
                            initNewData(ukm)
                            Toast.makeText(
                                this@DetailUkmActivity,
                                "Success upload photo!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        } else {

                            Toast.makeText(this@DetailUkmActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@DetailUkmActivity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseUkm>, t: Throwable) {
                    Toast.makeText(this@DetailUkmActivity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })
    }

    private fun initNewData(ukm: Ukm) {
        setupShared(ukm)
        val foto = ukm.foto.toString()
        if (foto.equals("-") || foto.equals("null")) {
        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(binding.imgHeaderOrganisasiDetail)
        }
    }

    private fun setupShared(ukm: Ukm) {
        val gson = Gson()
        val ukmJson = gson.toJson(ukm)
        sharedPref.put(Constanta.OBJECT_SELECTED, ukmJson)
        sharedPref.put(Constanta.KEY_OBJECT_SELECTED, "UKM")
    }

    private fun setupTabFragment() {

        val viewPager = binding.viewPagerOrganisasi
        val tabLayout = binding.tabLayoutOrganisasi

        val adapter = ViewPagerLembagaAdapter(supportFragmentManager, lifecycle, 3)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabArray[position]
        }.attach()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}