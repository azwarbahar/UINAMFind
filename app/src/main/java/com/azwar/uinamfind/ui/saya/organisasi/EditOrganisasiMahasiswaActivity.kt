package com.azwar.uinamfind.ui.saya.organisasi

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.OrganisasiMahasiswa
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditOrganisasiMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditOrganisasiMahasiswaActivity : AppCompatActivity() {
    private var organisasi_id: String = ""
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var organisasiMahasiswa: OrganisasiMahasiswa

    private lateinit var binding: ActivityEditOrganisasiMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditOrganisasiMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        organisasiMahasiswa = intent.getParcelableExtra("organisasi")!!
        initDataIntent(organisasiMahasiswa)

        binding.etTanggalBerakhirOrganisasi.setOnClickListener {
            showDatPickerDialig(binding.etTanggalBerakhirOrganisasi)
        }

        binding.etTanggalMulaiOrganisasi.setOnClickListener {
            showDatPickerDialig(binding.etTanggalMulaiOrganisasi)
        }

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

        binding.imgBackEditOrganisasi.setOnClickListener { finish() }

        binding.imgDeleteOrganisasi.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startDelet(organisasi_id)
                }
                .show()
        }

    }

    private fun startDelet(organisasiId: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.deleteOrganisasiUser(organisasiId)
            ?.enqueue(object : Callback<Responses.ResponseOrganisasiMahasiswa> {
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
                                this@EditOrganisasiMahasiswaActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Data Berhasil dihapus..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@EditOrganisasiMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@EditOrganisasiMahasiswaActivity,
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
                    SweetAlertDialog(
                        this@EditOrganisasiMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })

    }

    private fun clickSImpan() {

        var nama_organisasi = binding.etNamaOrganisasi.text
        var jabatan = binding.etPosisiOrganisasi.text
        var tgl_mulai = binding.etTanggalMulaiOrganisasi.text
        var tgl_berakhir = binding.etTanggalBerakhirOrganisasi.text
        var deskripsi = binding.etDeskripsiOrganisasi.text
        var berjalan = "Berjalan"
        if (binding.cbMasihMenjadiAnggota.isChecked) {
            berjalan = "Berjalan"
            tgl_berakhir = null
        } else {
            berjalan = "Berakhir"
        }

        var internal = "Internal"
        if (binding.cbInternalOrganisasi.isChecked) {
            internal = "Internal"
        } else {
            internal = "External"
        }

        if (nama_organisasi.isEmpty()) {
            binding.etNamaOrganisasi.error = "Lengkapi"
        } else if (jabatan.isEmpty()) {
            binding.etPosisiOrganisasi.error = "Lengkapi"
        } else if (tgl_mulai.isEmpty()) {
            binding.etTanggalMulaiOrganisasi.error = "Lengkapi"
        } else {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Ingin Memperbarui Data?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()

//                    Toast.makeText(this, tgl_mulai, Toast.LENGTH_SHORT).show()

                    startUpdateData(
                        nama_organisasi,
                        jabatan,
                        tgl_mulai,
                        tgl_berakhir,
                        deskripsi,
                        berjalan,
                        internal,
                        organisasi_id,
                    )
                }
                .show()
        }
    }

    private fun startUpdateData(
        namaOrganisasi: Editable?,
        jabatan: Editable?,
        tglMulai: Editable?,
        tglBerakhir: Editable?,
        deskripsi: Editable?,
        berjalan: String,
        internal: String,
        organisasiId: String?,
    ) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateOrgnaisasiUser(
            namaOrganisasi.toString(),
            jabatan.toString(),
            tglMulai.toString(),
            tglBerakhir.toString(),
            berjalan,
            internal,
            deskripsi.toString(),
            id,
            organisasiId
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
                            this@EditOrganisasiMahasiswaActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data Berhasil Diperbarui..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditOrganisasiMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditOrganisasiMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon1")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseOrganisasiMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditOrganisasiMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText(t.localizedMessage)
                    .show()
            }

        })
    }

    @SuppressLint("ResourceAsColor")
    private fun tidakBerjalan() {
        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(R.color.colorPrimary)
        binding.etTanggalBerakhirOrganisasi.backgroundTintList = colorStateList
        binding.etTanggalBerakhirOrganisasi.setText("")
        binding.etTanggalBerakhirOrganisasi.isEnabled = true

    }

    @SuppressLint("ResourceAsColor")
    private fun berjalan() {

        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, R.color.grey_hint),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(R.color.grey_hint)
        binding.etTanggalBerakhirOrganisasi.backgroundTintList = colorStateList
        binding.etTanggalBerakhirOrganisasi.setText("Berjalan")
        binding.etTanggalBerakhirOrganisasi.isEnabled = false


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

    private fun initDataIntent(organisasiMahasiswa: OrganisasiMahasiswa) {
        organisasi_id = organisasiMahasiswa.id.toString()
        var nama = organisasiMahasiswa.nama_organisasi
        var posisi = organisasiMahasiswa.jabatan
        var status = organisasiMahasiswa.status_organisasi_user
        var kedudukan = organisasiMahasiswa.kedudukan_organisasi
        var tanggal_mulai = organisasiMahasiswa.tanggal_mulai
        var tanggal_berakhir = organisasiMahasiswa.tanggal_berakhir
        var deskripsi = organisasiMahasiswa.deskripsi
        binding.etNamaOrganisasi.setText(nama)
        binding.etPosisiOrganisasi.setText(posisi)
        binding.etTanggalMulaiOrganisasi.setText(tanggal_mulai)
        binding.etDeskripsiOrganisasi.setText(deskripsi)
        if (status.equals("Berakhir")) {
            binding.cbMasihMenjadiAnggota.isChecked = false
            binding.etTanggalBerakhirOrganisasi.setText(tanggal_berakhir)
        } else {
            binding.cbMasihMenjadiAnggota.isChecked = true
            binding.etTanggalBerakhirOrganisasi.setText("Berjalan")
        }

        if (kedudukan.equals("Internal")) {
            binding.cbInternalOrganisasi.isChecked = true
        } else {
            binding.cbInternalOrganisasi.isChecked = false
        }


    }
}