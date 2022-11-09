package com.azwar.uinamfind.ui.perusahaan

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditPerusahaanBinding
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

class EditPerusahaanActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private var lokasi_pilih: String = ""
    private var jumlah_pegawai: String = ""

    private lateinit var binding: ActivityEditPerusahaanBinding

    private lateinit var perusahaan: Perusahaan

    private val IMAGE_REQ_CODE = 101

    var uri_photo: Uri? = null

    private var imgExists: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPerusahaanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()

        perusahaan = intent.getParcelableExtra("perusahaan")!!
        initData(perusahaan)

        binding.imgBack.setOnClickListener { finish() }

        binding.cvLogoKantorItemLoker.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(620, 620)
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
        } else {
            if (uri_photo == null) {
                startUpdate(
                    perusahaan.id.toString(),
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
                    id
                )
            } else {
                startUpdateWithPhoto(
                    perusahaan.id.toString(),
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


    }

    private fun startUpdate(
        perusahaan_id: String,
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
        recruiter_id: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updatePerusahaan(
            perusahaan_id,
            nama.toString(),
            website.toString(),
            industri.toString(),
            jumlah_pegawai,
            telpon.toString(),
            email.toString(),
            tahunBerdiri.toString(),
            deskripsi.toString(),
            alamat.toString(),
            lokasiPilih.toString(),
            recruiter_id
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
                            this@EditPerusahaanActivity,
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
                            this@EditPerusahaanActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditPerusahaanActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            }

            override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditPerusahaanActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()

            }

        })

    }

    private fun startUpdateWithPhoto(
        perusahaan_id: String,
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

        val perusahaan_id_send = RequestBody.create("text/plain".toMediaTypeOrNull(), perusahaan_id)
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

        ApiClient.instances.updatePerusahaanPhoto(
            perusahaan_id_send,
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
                            this@EditPerusahaanActivity,
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
                            this@EditPerusahaanActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditPerusahaanActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            }

            override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditPerusahaanActivity, SweetAlertDialog.ERROR_TYPE)
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

    private fun initData(perusahaan: Perusahaan) {
        binding.etNamaPerusahaan.setText(perusahaan.nama)
        binding.etIndustri.setText(perusahaan.industri)
        var foto = perusahaan.foto
        var url_website = perusahaan.url_profil
        var telpon = perusahaan.telpon
        var email = perusahaan.email
        var alamat = perusahaan.alamat
        var tahun_berdiri = perusahaan.tahun_berdiri
        var deskripsi = perusahaan.deskripsi

        setupSpinnerInit(perusahaan.ukuran_kariawan.toString())
        setupAutoCompletLokasi(perusahaan.lokasi.toString())
        if (foto !== null) {
            imgExists = true
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto)
                .into(binding.imgLogoKantorItemLoker)
        } else {
            imgExists = false
        }
        if (url_website != null) {
            binding.etWebsite.setText(url_website)
        }
        if (telpon != null) {
            binding.etTelpon.setText(telpon)
        }
        if (email != null) {
            binding.etEmail.setText(email)
        }
        if (alamat != null) {
            binding.etAlamatEditProfil.setText(alamat)
        }
        if (tahun_berdiri != null) {
            binding.etTahunBerdiri.setText(tahun_berdiri)
        }
        if (deskripsi != null) {
            binding.etDeskripsi.setText(deskripsi)
        }

    }

    private fun setupAutoCompletLokasi(lokasi: String) {
        val ac_lokasi = binding.etLokasi
        var nama_lokasi = resources.getStringArray(com.azwar.uinamfind.R.array.nama_lokasi)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.select_dialog_item, nama_lokasi)
        ac_lokasi.threshold = 1
        ac_lokasi.setAdapter(adapter)
        if (lokasi != null) {
            ac_lokasi.setText(lokasi)
        }
        ac_lokasi.setOnItemClickListener(OnItemClickListener { arg0, arg1, arg2, arg3 ->
            lokasi_pilih = arg0.getItemAtPosition(arg2).toString()
        })
    }

    private fun setupSpinnerInit(ukuran_pegawai: String) {
        val arrayJumlahPegawai =
            arrayOf(
                ukuran_pegawai,
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
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

}