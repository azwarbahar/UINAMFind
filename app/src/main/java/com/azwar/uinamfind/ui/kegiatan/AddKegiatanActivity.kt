package com.azwar.uinamfind.ui.kegiatan

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityAddKegiatanBinding
import com.azwar.uinamfind.utils.Constanta
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.*

class AddKegiatanActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var kategori: String = ""

    private lateinit var binding: ActivityAddKegiatanBinding

    private val IMAGE_REQ_CODE = 101

    var uri_photo: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKegiatanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)

        kategori = intent.getStringExtra("kategori").toString()
        if (kategori.equals("Lembaga")) {
            id = intent.getStringExtra("id").toString()
        } else if (kategori.equals("Organisasi")) {
            id = intent.getStringExtra("id").toString()
        } else if (kategori.equals("UKM")) {
            id = intent.getStringExtra("id").toString()
        }

        binding.imgBack.setOnClickListener { finish() }

        binding.etTanggal.setOnClickListener {
            showDatPickerDialig(binding.etTanggal)
        }

        binding.cvFoto.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(IMAGE_REQ_CODE)
        }

        binding.tvKirim.setOnClickListener {
            clickSimpan()
        }

    }

    private fun clickSimpan() {

        var nama = binding.etNama.text.toString()
        var deskripsi = binding.etDeskripsi.text.toString()
        var tempat = binding.etTempat.text.toString()
        var tanggal = binding.etTanggal.text.toString()

        if (nama.isEmpty() || nama.equals("")) {
            binding.etNama.error = "Lengkapi"
            binding.etNama.focusable
        } else if (uri_photo == null) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Gambar")
                .setContentText("Silahkan lengkapi gambar!")
                .show()
        } else if (tempat.isEmpty() || tempat.equals("")) {
            binding.etTempat.error = "Lengkapi"
            binding.etTempat.focusable
        } else if (tanggal.isEmpty() || tanggal.equals("")) {
            binding.etTanggal.error = "Lengkapi"
            binding.etTanggal.focusable
        } else if (deskripsi.isEmpty() || deskripsi.equals("")) {
            binding.etDeskripsi.error = "Lengkapi"
            binding.etDeskripsi.focusable
        } else {
            startSend(nama, deskripsi, tempat, tanggal)
        }
    }

    private fun startSend(nama: String, deskripsi: String, tempat: String, tanggal: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val kategori_send = RequestBody.create("text/plain".toMediaTypeOrNull(), kategori)
        val nama_send = RequestBody.create("text/plain".toMediaTypeOrNull(), nama)
        val deskripsi_send = RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsi)
        val tempat_send = RequestBody.create("text/plain".toMediaTypeOrNull(), tempat)
        val tanggal_send = RequestBody.create("text/plain".toMediaTypeOrNull(), tanggal)
        val from_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), id)

        val file = File(uri_photo!!.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send =
            MultipartBody.Part.createFormData("foto", file.name, foto)

        ApiClient.instances.addKegiatan(
            kategori_send,
            nama_send,
            deskripsi_send,
            tempat_send,
            foto_send,
            tanggal_send,
            from_id_send
        )?.enqueue(object : Callback<Responses.ResponseKegiatan> {
            override fun onResponse(
                call: Call<Responses.ResponseKegiatan>,
                response: Response<Responses.ResponseKegiatan>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        SweetAlertDialog(
                            this@AddKegiatanActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Postingan Berhasil terkirim..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@AddKegiatanActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@AddKegiatanActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseKegiatan>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@AddKegiatanActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }
        })

    }

    private fun showDatPickerDialig(et: EditText) {
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                var dayfix = "" + dayOfMonth;
                var monthfix = "" + monthOfYear;

                if (monthOfYear < 10) {
                    monthfix = "0" + monthfix;
                }
                if (dayOfMonth < 10) {
                    dayfix = "0" + dayfix;
                }
                et.setText("" + dayfix + "-" + monthfix + "-" + year)

            }, year, month, day
        )

        dpd.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_REQ_CODE) {
                val uri: Uri = data?.data!!
                uri_photo = uri
                binding.imgFoto.setImageURI(null)
                binding.imgFoto.setImageURI(uri)
            }
        }
    }

}