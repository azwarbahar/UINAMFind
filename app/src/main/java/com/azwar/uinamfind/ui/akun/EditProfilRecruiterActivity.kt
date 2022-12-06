package com.azwar.uinamfind.ui.akun

import android.R
import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditProfilRecruiterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class EditProfilRecruiterActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var jenis_kelamin_pilih: String = ""
    private var lokasi_pilih: String = ""

    private lateinit var binding: ActivityEditProfilRecruiterBinding

    private lateinit var recruiter: Recruiter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recruiter = intent.getParcelableExtra("recruiter")!!
        initData(recruiter)

        binding.imgBack.setOnClickListener { finish() }

        binding.etTanggalLahir.setOnClickListener {
            showDatPickerDialig(binding.etTanggalLahir)
        }

        binding.tvSimpan.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Edit data")
                .setContentText("Yakin ingin mengirim perubahan data ?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    clickSimpan()
                }
                .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                    it.dismiss()
                })
                .show()
        }

    }

    private fun clickSimpan() {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        var nama = binding.etNama.text.toString()
        var jenis_kelamin = jenis_kelamin_pilih
        var tempat_lahir = binding.etTempatLahir.text.toString()
        var tanggal_lahir = binding.etTanggalLahir.text.toString()
        var telpon = binding.etTelpon.text.toString()
        var email = binding.etEmail.text.toString()
        var motto = binding.etMotto.text.toString()
        var lokasi = binding.etLokasiEditProfil.text.toString()

        ApiClient.instances.updateRecruiter(
            id,
            nama,
            jenis_kelamin,
            tempat_lahir,
            tanggal_lahir,
            telpon,
            email,
            motto,
            lokasi
        )?.enqueue(object : Callback<Responses.ResponseRecruiter> {
            override fun onResponse(
                call: Call<Responses.ResponseRecruiter>,
                response: Response<Responses.ResponseRecruiter>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        SweetAlertDialog(
                            this@EditProfilRecruiterActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Data berhasil diubah..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditProfilRecruiterActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditProfilRecruiterActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseRecruiter>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditProfilRecruiterActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }

    private fun initData(recruiter: Recruiter) {
        id = recruiter.id.toString()

        var nama = recruiter.nama
        if (nama != null) {
            binding.etNama.setText(nama)
        }

        var tempat_lahir = recruiter.tempat_lahir
        if (tempat_lahir != null) {
            binding.etTempatLahir.setText(tempat_lahir)
        }

        var tanggal_lahir = recruiter.tanggal_lahir
        if (tanggal_lahir != null) {
            binding.etTanggalLahir.setText(tanggal_lahir)
        }

        var telpon = recruiter.telpon
        if (telpon != null) {
            binding.etTelpon.setText(telpon)
        }

        var email = recruiter.email
        if (email != null) {
            binding.etEmail.setText(email)
        }

        var motto = recruiter.motto
        if (motto != null) {
            binding.etMotto.setText(motto)
        }

        setupAutoCompletLokasi(recruiter.lokasi)

        setupSpinnerInit(recruiter.jenis_kelamin.toString())

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

    private fun setupSpinnerInit(jekel: String) {
        val arrayJenisKelamin = arrayOf("Laki-laki", "Perempuan")
        if (jekel.equals("Laki-laki")) {
            val arrayJenisKelamin = arrayOf("Laki-laki", "Perempuan")
        } else {
            val arrayJenisKelamin = arrayOf("Perempuan", "Laki-laki")
        }
        val spinnerJenisKelamin = binding.spJenisKelaminEditProfil
        val arrayAdapterJenisKelamin =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayJenisKelamin)
        spinnerJenisKelamin.adapter = arrayAdapterJenisKelamin


        spinnerJenisKelamin?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                jenis_kelamin_pilih = arrayJenisKelamin[position]
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }

    private fun setupAutoCompletLokasi(lokasi: String?) {
        val ac_lokasi = binding.etLokasiEditProfil
        var nama_lokasi = resources.getStringArray(com.azwar.uinamfind.R.array.nama_lokasi)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.select_dialog_item, nama_lokasi)
        ac_lokasi.threshold = 1
        ac_lokasi.setAdapter(adapter)
        if (lokasi != null) {
            ac_lokasi.setText(lokasi)
        }
        ac_lokasi.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            lokasi_pilih = arg0.getItemAtPosition(arg2).toString()
        })
    }

}