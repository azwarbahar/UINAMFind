package com.azwar.uinamfind.ui.saya.pengalaman

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.PengalamanMahasiswa
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditPengalamanMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditPengalamanMahasiswaActivity : AppCompatActivity() {
    private var pengalaman_id: String = ""
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var jenis_pekerjaan: String = ""

    private lateinit var pengalamanMahasiswa: PengalamanMahasiswa

    private lateinit var binding: ActivityEditPengalamanMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPengalamanMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        pengalamanMahasiswa = intent.getParcelableExtra("pengalaman")!!
        initDataIntent(pengalamanMahasiswa)

        binding.etTanggalBerakhir.setOnClickListener {
            showDatPickerDialig(binding.etTanggalBerakhir)
        }

        binding.etTanggalMulai.setOnClickListener {
            showDatPickerDialig(binding.etTanggalMulai)
        }

//        et_tanggal_berakhir = binding.etTanggalBerakhir
        binding.cbMasihMenjadiAnggota.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                berjalan()
            } else {
                tidakBerjalan()
            }
        }

        binding.imgBackPengalaman.setOnClickListener { finish() }

        binding.imgDeletePengalaman.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startDelet(pengalaman_id)
                }
                .show()
        }
        binding.rlSimpanPerubahan.setOnClickListener {
            clickSimpan()
        }
    }

    private fun clickSimpan() {

        val posisi = binding.etPosisiPengalaman.text
        var jenis = "-"
        if (jenis_pekerjaan.equals("-- Pilih --")) {
            jenis = "-"
        } else {
            jenis = jenis_pekerjaan
        }
        val nama_perusahaan = binding.etNamaTempat.text
        val lokasi = binding.etLokasiPengalaman.text
        val tgl_mulai = binding.etTanggalMulai.text
        var tgl_berakhir = binding.etTanggalBerakhir.text
        var berjalan = "Berjalan"
        if (binding.cbMasihMenjadiAnggota.isChecked) {
            berjalan = "Berjalan"
//            tgl_berakhir = null
        } else {
            berjalan = "Berakhir"
        }
        val deskripsi = binding.etDeskripsi.text

        when {
            posisi.isEmpty() -> {
                binding.etPosisiPengalaman.error = "Lengkapi"
            }
            nama_perusahaan.isEmpty() -> {
                binding.etNamaTempat.error = "Lengkapi"
            }
            lokasi.isEmpty() -> {
                binding.etLokasiPengalaman.error = "Lengkapi"
            }
            tgl_mulai.isEmpty() -> {
                binding.etTanggalMulai.error = "Lengkapi"
            }
            tgl_berakhir.isEmpty() -> {
                binding.etTanggalBerakhir.error = "Lengkapi"
            }
            else -> {
                startUpdateData(
                    pengalaman_id,
                    id,
                    posisi,
                    jenis,
                    nama_perusahaan,
                    lokasi,
                    tgl_mulai,
                    tgl_berakhir,
                    berjalan,
                    deskripsi
                )
            }

        }
    }

    private fun startUpdateData(
        pengalamanId: String,
        id: String,
        posisi: Editable?,
        jenis: String,
        namaPerusahaan: Editable?,
        lokasi: Editable?,
        tglMulai: Editable?,
        tglBerakhir: Editable?,
        berjalan: String,
        deskripsi: Editable?
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updatePengalamanUser(
            pengalamanId,
            id,
            posisi.toString(),
            jenis.toString(),
            namaPerusahaan.toString(),
            lokasi.toString(),
            tglMulai.toString(),
            tglBerakhir.toString(),
            berjalan,
            deskripsi.toString()
        )?.enqueue(object : Callback<Responses.ResponsePengalamanMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                response: Response<Responses.ResponsePengalamanMahasiswa>
            ) {

                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode

                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@EditPengalamanMahasiswaActivity,
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
                            this@EditPengalamanMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditPengalamanMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon1")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditPengalamanMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText(t.localizedMessage)
                    .show()
            }

        })

    }

    private fun startDelet(pengalamanId: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.deletePengalamanUser(pengalamanId)
            ?.enqueue(object : Callback<Responses.ResponsePengalamanMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponsePengalamanMahasiswa>,
                    response: Response<Responses.ResponsePengalamanMahasiswa>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@EditPengalamanMahasiswaActivity,
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
                                this@EditPengalamanMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@EditPengalamanMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponsePengalamanMahasiswa>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(
                        this@EditPengalamanMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })

    }

    private fun initDataIntent(pengalamanMahasiswa: PengalamanMahasiswa) {
        pengalaman_id = pengalamanMahasiswa.id.toString()
        var posisi = pengalamanMahasiswa.judul
        var jenis = pengalamanMahasiswa.jenis_pengalaman
        var nama_perusahaan = pengalamanMahasiswa.nama_tempat
        var lokasi = pengalamanMahasiswa.lokasi_tempat
        var tgl_mulai = pengalamanMahasiswa.tanggal_mulai
        var tgl_berakhir = pengalamanMahasiswa.tanggal_berakhir
        var status = pengalamanMahasiswa.status_pengalaman
        var deskrispi = pengalamanMahasiswa.deskripsi
        setupSpinner(jenis)
        binding.etPosisiPengalaman.setText(posisi)
        binding.etNamaTempat.setText(nama_perusahaan)
        binding.etLokasiPengalaman.setText(lokasi)
        binding.etTanggalMulai.setText(tgl_mulai)
        binding.etDeskripsi.setText(deskrispi)
        if (status.equals("Berakhir")) {
            binding.cbMasihMenjadiAnggota.isChecked = false
            binding.etTanggalBerakhir.setText(tgl_berakhir)
        } else {
            binding.cbMasihMenjadiAnggota.isChecked = true
            binding.etTanggalBerakhir.setText("Berjalan")
        }
    }

    private fun setupSpinner(jenis: String?) {
        val arrayJenisPengalaman = arrayOf(
            jenis,
            "Purnawaktu", "Paruh Waktu", "Wiraswasta",
            "Pekerjaan Lepas", "Kontrak", "Magang"
        )
        val spinnerJenisPengalaman = binding.spJenisPengalaman
        val arrayAdapterJenisPengalaman =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayJenisPengalaman)
        spinnerJenisPengalaman.adapter = arrayAdapterJenisPengalaman

        spinnerJenisPengalaman?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    jenis_pekerjaan = arrayJenisPengalaman[position].toString()
//                Toast.makeText(applicationContext, arrayJenisPengalaman[position], Toast.LENGTH_SHORT).show()
                }
            }
    }

    @SuppressLint("ResourceAsColor")
    private fun tidakBerjalan() {
        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, com.azwar.uinamfind.R.color.colorPrimary),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.colorPrimary)
        binding.etTanggalBerakhir.backgroundTintList = colorStateList
        binding.etTanggalBerakhir.setText("")
        binding.etTanggalBerakhir.isEnabled = true

    }

    @SuppressLint("ResourceAsColor")
    private fun berjalan() {

        binding.imgDate1.setColorFilter(
            ContextCompat.getColor(this, com.azwar.uinamfind.R.color.grey_hint),
            PorterDuff.Mode.SRC_IN
        )

        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.grey_hint)
        binding.etTanggalBerakhir.backgroundTintList = colorStateList
        binding.etTanggalBerakhir.setText("Berjalan")
        binding.etTanggalBerakhir.isEnabled = false


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
}