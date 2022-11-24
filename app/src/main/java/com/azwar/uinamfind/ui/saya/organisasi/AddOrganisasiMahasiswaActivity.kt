package com.azwar.uinamfind.ui.saya.organisasi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityAddOrganisasiMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddOrganisasiMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityAddOrganisasiMahasiswaBinding

    private lateinit var et_tanggal_berakhir: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrganisasiMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        binding.etTanggalBerakhirAddOrganisasi.setOnClickListener {
            showDatPickerDialig(binding.etTanggalBerakhirAddOrganisasi)
        }

        binding.etTanggalMulaiAddOrganisasi.setOnClickListener {
            showDatPickerDialig(binding.etTanggalMulaiAddOrganisasi)
        }

        et_tanggal_berakhir = binding.etTanggalBerakhirAddOrganisasi
        binding.cbMasihMenjadiAnggota.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                berjalan()
            } else {
                tidakBerjalan()
            }
        }

        binding.rlSimpanPerubahan.setOnClickListener {
            clickSImpan()
        }

        binding.imgBackAddOrganisasi.setOnClickListener { finish() }

    }

    private fun clickSImpan() {
        val nama_organisasi = binding.etNamaOrganisasi.text
        val jabatan = binding.etPosisiOrganisasi.text
        val tgl_mulai = binding.etTanggalMulaiAddOrganisasi.text
        var tgl_berakhir = binding.etTanggalBerakhirAddOrganisasi.text.toString()
        val deskripsi = binding.etDeskripsiOrganisasi.text
        var berjalan = "Berjalan"
        if (binding.cbMasihMenjadiAnggota.isChecked) {
            berjalan = "Berjalan"
            tgl_berakhir = ""
        } else {
            berjalan = "Berakhir"
        }

        var internal = "Internal"
        internal = if (binding.cbInternalOrganisasi.isChecked) {
            "Internal"
        } else {
            "External"
        }

        val organisasi_id = null

        when {
            nama_organisasi.isEmpty() -> {
                binding.etNamaOrganisasi.error = "Lengkapi"
            }
            jabatan.isEmpty() -> {
                binding.etPosisiOrganisasi.error = "Lengkapi"
            }
            tgl_mulai.isEmpty() -> {
                binding.etTanggalMulaiAddOrganisasi.error = "Lengkapi"
            }
            tgl_berakhir.isEmpty() -> {
                if (berjalan.equals("Berakhir")) {
                    binding.etTanggalBerakhirAddOrganisasi.error = "Lengkapi"
                } else {
                    startAddData(
                        nama_organisasi,
                        jabatan,
                        tgl_mulai,
                        tgl_berakhir,
                        deskripsi,
                        berjalan,
                        internal,
                        organisasi_id,
                        id
                    )
                }
            }
            else -> {
                startAddData(
                    nama_organisasi,
                    jabatan,
                    tgl_mulai,
                    tgl_berakhir,
                    deskripsi,
                    berjalan,
                    internal,
                    organisasi_id,
                    id
                )
            }
        }


    }

    private fun startAddData(
        namaOrganisasi: Editable?,
        jabatan: Editable?,
        tglMulai: Editable?,
        tglBerakhir: String,
        deskripsi: Editable?,
        berjalan: String?,
        internal: String?,
        organisasiId: String?,
        id: String?
    ) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.addOrganisasiUser(
            namaOrganisasi.toString(),
            jabatan.toString(),
            tglMulai.toString(),
            tglBerakhir.toString(),
            berjalan,
            internal,
            organisasiId,
            deskripsi.toString(),
            id
        )?.enqueue(object : Callback<Responses.ResponseOrganisasiMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseOrganisasiMahasiswa>,
                response: Response<Responses.ResponseOrganisasiMahasiswa>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode

                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@AddOrganisasiMahasiswaActivity,
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
                            this@AddOrganisasiMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@AddOrganisasiMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseOrganisasiMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@AddOrganisasiMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText("Server tidak merespon")
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

    @SuppressLint("ResourceAsColor")
    private fun tidakBerjalan() {
        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(R.color.colorPrimary)
        binding.etTanggalBerakhirAddOrganisasi.backgroundTintList = colorStateList
        binding.etTanggalBerakhirAddOrganisasi.setText("")
        binding.etTanggalBerakhirAddOrganisasi.isEnabled = true

    }

    @SuppressLint("ResourceAsColor")
    private fun berjalan() {

        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, R.color.grey_hint),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(R.color.grey_hint)
        binding.etTanggalBerakhirAddOrganisasi.backgroundTintList = colorStateList
        binding.etTanggalBerakhirAddOrganisasi.setText("Berjalan")
        binding.etTanggalBerakhirAddOrganisasi.isEnabled = false


    }
}