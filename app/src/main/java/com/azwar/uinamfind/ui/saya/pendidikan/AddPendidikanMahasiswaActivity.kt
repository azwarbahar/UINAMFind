package com.azwar.uinamfind.ui.saya.pendidikan

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
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityAddPendidikanMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class AddPendidikanMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var pendidikan_pilih: String = ""

    private lateinit var binding: ActivityAddPendidikanMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPendidikanMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()

        setupSpinner()

        binding.etTanggalBerakhir.setOnClickListener {
            showDatPickerDialig(binding.etTanggalBerakhir)
        }

        binding.etTanggalMulai.setOnClickListener {
            showDatPickerDialig(binding.etTanggalMulai)
//            show()
        }

        binding.cbMasihMenjadiAnggota.setOnCheckedChangeListener { button, isChecked ->
            if (isChecked) {
                berjalan()
            } else {
                tidakBerjalan()
            }
        }

        binding.imgBackPendidikan.setOnClickListener { finish() }

        binding.rlSimpanPerubahan.setOnClickListener {
            clickSimpan()
        }
    }

    private fun clickSimpan() {
        val tempat = binding.etNamaTempatPendidikan.text
        val jurusan = binding.etJurusanPendidikan.text
        val tgl_mulai = binding.etTanggalMulai.text
        var tgl_berakhir = binding.etTanggalBerakhir.text
        var berjalan = "Masih"
        if (binding.cbMasihMenjadiAnggota.isChecked) {
            berjalan = "Masih"
//            tgl_berakhir = null
        } else {
            berjalan = "Selesai"
        }

        when {
            tempat.isEmpty() -> {
                binding.etNamaTempatPendidikan.error = "Lengkapi"
            }
            jurusan.isEmpty() -> {
                binding.etJurusanPendidikan.error = "Lengkapi"
            }
            tgl_mulai.isEmpty() -> {
                binding.etTanggalMulai.error = "Lengkapi"
            }
            tgl_berakhir.isEmpty() -> {
                binding.etTanggalBerakhir.error = "Lengkapi"
            }
            else -> {
                startAddData(
                    pendidikan_pilih,
                    tempat,
                    jurusan,
                    tgl_mulai,
                    tgl_berakhir,
                    berjalan,
                    id
                )
            }

        }
    }

    private fun startAddData(
        pendidikanPilih: String,
        tempat: Editable?,
        jurusan: Editable?,
        tglMulai: Editable?,
        tglBerakhir: Editable?,
        berjalan: String,
        id: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.addPendidikanUser(
            pendidikanPilih,
            tempat.toString(),
            jurusan.toString(),
            tglMulai.toString(),
            tglBerakhir.toString(),
            berjalan,
            id
        )?.enqueue(object :
            Callback<Responses.ResponsePendidikanMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponsePendidikanMahasiswa>,
                response: Response<Responses.ResponsePendidikanMahasiswa>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@AddPendidikanMahasiswaActivity,
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
                            this@AddPendidikanMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@AddPendidikanMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePendidikanMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@AddPendidikanMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
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
        binding.etTanggalBerakhir.setText("Masih")
        binding.etTanggalBerakhir.isEnabled = false


    }

    private fun setupSpinner() {
        val arrayPendidikan = arrayOf(
            "Sekolah Dasar",
            "Menengah Pertama",
            "Menengah Atas/Kejuruan",
            "Sarjana",
            "Magister",
            "Doktor"
        )
        val spinnerJenisPengalaman = binding.spPendidikan
        val arrayAdapterJenisPengalaman =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayPendidikan)
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
                    pendidikan_pilih = arrayPendidikan[position]
//                Toast.makeText(applicationContext, arrayJenisPengalaman[position], Toast.LENGTH_SHORT).show()
                }
            }
    }

}
