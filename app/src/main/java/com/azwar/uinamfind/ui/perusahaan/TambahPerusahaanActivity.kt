package com.azwar.uinamfind.ui.perusahaan

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityTambahPerusahaanBinding
import com.azwar.uinamfind.utils.Constanta
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class TambahPerusahaanActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private var lokasi_pilih: String = ""
    private var jumlah_pegawai: String = ""

    private lateinit var binding: ActivityTambahPerusahaanBinding

    private val IMAGE_REQ_CODE = 101

    var uri_photo: Uri? = null

    private var imgExists: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()

        setupAutoCompletLokasi()
        setupSpinnerInit()

        binding.imgBack.setOnClickListener { finish() }

        binding.cvLogoKantorItemLoker.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(1000, 1000)
                .start(IMAGE_REQ_CODE)
        }

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }

    }

    private fun clickSimpan() {

        var nama = binding.etNamaPerusahaan.text
        var industri = binding.etIndustri.text
        var website = binding.etWebsite.text
        var telpon = binding.etTelpon.text
        var email = binding.etEmail.text
        var alamat = binding.etAlamatEditProfil.text
        var tahun_berdiri = binding.etTahunBerdiri.text
        var deskripsi = binding.etDeskripsi.text
        var recruiter_id = id
        var lokasi = binding.etLokasi.text

        if (nama.isEmpty()) {
            binding.etNamaPerusahaan.error = "Lengkapi"
            binding.etNamaPerusahaan.focusable
        } else if (!imgExists) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Logo Perusahaan")
                .setContentText("Silahkan lengkapi Logo Perusahaan")
                .show()
        } else if (industri.isEmpty()) {
            binding.etIndustri.error = "Lengkapi"
        } else if (jumlah_pegawai.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Jumlah Pegawai")
                .setContentText("Silahkan lengkapi Jumlah Pegawai")
                .show()
            binding.spJumlahPegawai.focusable
        } else if (lokasi.isEmpty()) {
            binding.etLokasi.error = "Lengkapi"
        } else if (uri_photo == null) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Logo Perusahaan")
                .setContentText("Harus Sertakan Logo Perusahaan!")
                .show()
            binding.cvLogoKantorItemLoker.focusable
        } else {
            startUpdateWithPhoto(
                nama,
                website,
                industri,
                jumlah_pegawai,
                telpon,
                email,
                tahun_berdiri,
                deskripsi,
                alamat,
                lokasi,
                id,
                uri_photo!!
            )
        }
    }

    private fun startUpdateWithPhoto(
        nama: Editable?,
        website: Editable?,
        industri: Editable?,
        jumlahPegawai: String,
        telpon: Editable?,
        email: Editable?,
        tahunBerdiri: Editable?,
        deskripsi: Editable?,
        alamat: Editable?,
        lokasiPilih: Editable,
        recruiter_id: String,
        uri_photo: Uri
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val nama_send = RequestBody.create("text/plain".toMediaTypeOrNull(), nama.toString())
        val website_send = RequestBody.create("text/plain".toMediaTypeOrNull(), website.toString())
        val industri_send =
            RequestBody.create("text/plain".toMediaTypeOrNull(), industri.toString())
        val jumlahPegawai_send = RequestBody.create("text/plain".toMediaTypeOrNull(), jumlahPegawai)
        val telpon_send = RequestBody.create("text/plain".toMediaTypeOrNull(), telpon.toString())
        val email_send = RequestBody.create("text/plain".toMediaTypeOrNull(), email.toString())
        val tahunBerdiri_send =
            RequestBody.create("text/plain".toMediaTypeOrNull(), tahunBerdiri.toString())
        val deskripsi_send =
            RequestBody.create("text/plain".toMediaTypeOrNull(), deskripsi.toString())
        val alamat_send = RequestBody.create("text/plain".toMediaTypeOrNull(), alamat.toString())
        val lokasiPilih_send =
            RequestBody.create("text/plain".toMediaTypeOrNull(), lokasiPilih.toString())
        val recruiter_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), recruiter_id)

        val file = File(uri_photo.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send = MultipartBody.Part.createFormData("foto", file.name, foto)

        ApiClient.instances.addPerusahaan(
            nama_send,
            website_send,
            industri_send,
            jumlahPegawai_send,
            telpon_send,
            email_send,
            tahunBerdiri_send,
            deskripsi_send,
            alamat_send,
            lokasiPilih_send,
            recruiter_id_send,
            foto_send
        )?.enqueue(object : Callback<Responses.ResponsePerusahaan> {
            override fun onResponse(
                call: Call<Responses.ResponsePerusahaan>,
                response: Response<Responses.ResponsePerusahaan>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@TambahPerusahaanActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil tersimpan..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@TambahPerusahaanActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@TambahPerusahaanActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            }

            override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@TambahPerusahaanActivity, SweetAlertDialog.ERROR_TYPE)
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
                binding.imgLogoKantorItemLoker.setImageURI(null)
                binding.imgLogoKantorItemLoker.setImageURI(uri)
                imgExists = true
            }
        }
    }

    private fun setupAutoCompletLokasi() {
        val ac_lokasi = binding.etLokasi
        var nama_lokasi = resources.getStringArray(com.azwar.uinamfind.R.array.nama_lokasi)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.select_dialog_item, nama_lokasi)
        ac_lokasi.threshold = 1
        ac_lokasi.setAdapter(adapter)

        ac_lokasi.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            lokasi_pilih = arg0.getItemAtPosition(arg2).toString()
        })
    }

    private fun setupSpinnerInit() {
        val arrayJumlahPegawai =
            arrayOf(
                "0-10",
                "10-100",
                "100-500",
                "500-1000",
                "1000>"
            )
        val spinnerJumlahPegawai = binding.spJumlahPegawai
        val arrayAdapterJumlahPegawai =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayJumlahPegawai)
        spinnerJumlahPegawai.adapter = arrayAdapterJumlahPegawai
        spinnerJumlahPegawai?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                jumlah_pegawai = arrayJumlahPegawai[position]
            }
        }
    }

}