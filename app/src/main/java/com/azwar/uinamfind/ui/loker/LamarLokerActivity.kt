package com.azwar.uinamfind.ui.loker

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityLamarLokerBinding
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat

class LamarLokerActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var loker_id: String = ""
    private var recruiter_id: String = ""

    private lateinit var perusahaan: Perusahaan
    private lateinit var recruiter: Recruiter
    private lateinit var loker: Loker
    private lateinit var user: User

    private lateinit var binding: ActivityLamarLokerBinding

    private lateinit var swipe_refresh: SwipeRefreshLayout

    private val IMAGE_REQ_CODE = 101

    var uri_photo: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLamarLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        loker = intent.getParcelableExtra("loker")!!


        swipe_refresh = binding.swipeRefresh
        swipe_refresh.setOnRefreshListener(this)
        swipe_refresh.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_refresh.post(Runnable {
            initData(loker)
            loadMahasiswa(id)
        })


        binding.imgBack.setOnClickListener { finish() }

        binding.cvResume.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .galleryMimeTypes(
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .compress(1024)
                .maxResultSize(620, 620)
                .start(IMAGE_REQ_CODE)
        }

        binding.tvKirim.setOnClickListener {
            clickSimpan()
        }

    }

    private fun clickSimpan() {

        var email = binding.etEmail.text.toString()
        var telpon = binding.etTelpon.text.toString()

        if (email.isEmpty() || email.equals("")) {
            binding.etEmail.error = "Lengkapi"
            binding.etEmail.focusable
        } else if (telpon.isEmpty() || telpon.equals("")) {
            binding.etTelpon.error = "Lengkapi"
            binding.etTelpon.focusable
        } else if (uri_photo == null) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Resume")
                .setContentText("Silahkan lengkapi resume!")
                .show()
        } else {
            startSend(email, telpon, uri_photo!!)
        }


    }

    private fun startSend(email: String, telpon: String, uriPhoto: Uri) {

        var pesan = binding.etPesan.text.toString()

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val loker_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), loker_id)
        val recruiter_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), recruiter_id)
        val mahasiswa_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), id)
        val pesan_send = RequestBody.create("text/plain".toMediaTypeOrNull(), pesan)
        val telpon_send = RequestBody.create("text/plain".toMediaTypeOrNull(), telpon)
        val email_send = RequestBody.create("text/plain".toMediaTypeOrNull(), email)

        val file = File(uriPhoto.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val dokumen_lamaran_send =
            MultipartBody.Part.createFormData("dokumen_lamaran", file.name, foto)

        ApiClient.instances.addLamaran(
            loker_id_send,
            recruiter_id_send,
            mahasiswa_id_send,
            pesan_send,
            dokumen_lamaran_send,
            telpon_send,
            email_send
        )?.enqueue(object : Callback<Responses.ResponseLamaran> {
            override fun onResponse(
                call: Call<Responses.ResponseLamaran>,
                response: Response<Responses.ResponseLamaran>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        SweetAlertDialog(
                            this@LamarLokerActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Lamaran Berhasil terkirim..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@LamarLokerActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@LamarLokerActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@LamarLokerActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_REQ_CODE) {
                val uri: Uri = data?.data!!
                uri_photo = uri
                binding.imgResume.setImageURI(null)
                binding.imgResume.setImageURI(uri)
            }
        }
    }

    private fun loadMahasiswa(id: String) {

        ApiClient.instances.getMahasiswaID(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    swipe_refresh.isRefreshing = false
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataUser(user)
                        } else {
                            Toast.makeText(this@LamarLokerActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@LamarLokerActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    swipe_refresh.isRefreshing = false
                    Toast.makeText(
                        this@LamarLokerActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    private fun initDataUser(user: User) {
        var nim = user.nim

        // nama
        var nama_depan = user.nama_depan
        var nama_belakang = user.nama_belakang
        var text_nama_lengkap = binding.tvNamaPelamar
        if (nama_depan == null) {
            text_nama_lengkap.text = nim
        } else if (nama_belakang == null) {
            var nama_lengkap = user.nama_depan
            text_nama_lengkap.text = nama_lengkap
        } else {
            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
            text_nama_lengkap.text = nama_lengkap
        }

        binding.etEmail.setText(user.email_uinam.toString())
        if (user.telpon != null) {
            binding.etTelpon.setText(user.telpon.toString())
        }

        var foto = user.foto
        if (foto !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                .into(binding.imgPhoto)
        } else {

        }
        // motto
        loadMotto(id)
    }

    private fun loadMotto(id: String) {
        ApiClient.instances.getMottoId(id)
            ?.enqueue(object : Callback<Responses.ResponseMotto> {
                override fun onResponse(
                    call: Call<Responses.ResponseMotto>,
                    response: Response<Responses.ResponseMotto>
                ) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val motto = response.body()?.result_motto
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            val text_motto = binding.tvMottoPelamar
                            val motto_ = motto?.motto_profesional
                            if (motto_ == null) {
                                text_motto.text = "  -"
                            } else {
                                text_motto.text = motto_
                            }
                        } else {
//                            Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                            val text_motto = binding.tvMottoPelamar
                            text_motto.text = "  -"
                        }
                    } else {
//                        Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                        val text_motto = binding.tvMottoPelamar
                        text_motto.text = "  -"
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
//                    Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                    val text_motto = binding.tvMottoPelamar
                    text_motto.text = "  -"
                }

            })
    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
    }


    private fun initData(loker: Loker) {

        loker_id = loker.id.toString()
        recruiter_id = loker.recruiter_id.toString()

        binding.tvNamaLoker.setText(loker.posisi.toString())
        binding.tvLokasiLoker.setText(loker.lokasi.toString() + ", Indonesia")
        binding.tvTglLoker.setText("Dibagikan pada " + convertDate(loker.created_at.toString()))
//        loadPelamar(loker.id.toString())
    }


    override fun onRefresh() {

        initData(loker)
        loadMahasiswa(id)
    }
}