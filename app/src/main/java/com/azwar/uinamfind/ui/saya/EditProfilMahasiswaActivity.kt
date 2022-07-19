package com.azwar.uinamfind.ui.saya

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditProfilMahasiswaBinding
import com.azwar.uinamfind.ui.saya.keahlian.ListKeahlianMahasiswaActivity
import com.azwar.uinamfind.ui.saya.organisasi.ListOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pendidikan.ListPendidikanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.ListPengalamanMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class EditProfilMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var jenis_kelamin_pilih: String = ""
    private var lokasi_pilih: String = ""

    private lateinit var binding: ActivityEditProfilMahasiswaBinding

    // models
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfilMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        loadDataSaya(id)
        setupSpinner()

        binding.rlBottomEditProfil.setOnClickListener {
            clickSimpan()
        }

        binding.etTanggalLahirEditProfil.setOnClickListener {
            showDatPickerDialig(binding.etTanggalLahirEditProfil)
        }

        binding.imgBackEditProfil.setOnClickListener {
            finish()
        }

        binding.rlPengalanKerja.setOnClickListener {
            val intent = Intent(this, ListPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.rlKeahlian.setOnClickListener {
            val intent = Intent(this, ListKeahlianMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.rlPendidikan.setOnClickListener {
            val intent = Intent(this, ListPendidikanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        binding.rlOrganisasi.setOnClickListener {
            val intent = Intent(this, ListOrganisasiMahasiswaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun clickSimpan() {

        val nama_depan = binding.etNamaDepanEditProfil.text
        val nama_belakang = binding.etNamaBelakangEditProfil.text
        val alamat = binding.etAlamatEditProfil.text
        val tgl_lahir = binding.etTanggalLahirEditProfil.text

        if (nama_depan.isEmpty()) {
            binding.etNamaDepanEditProfil.error = "Lengkapi"
        } else if (lokasi_pilih.equals("")) {
            binding.etLokasiEditProfil.error = "Lengkapi"
        } else if (tgl_lahir.isEmpty()) {
            binding.etTanggalLahirEditProfil.error = "Lengkapi"
        } else {

            startUpdateData(
                nama_depan,
                nama_belakang,
                jenis_kelamin_pilih,
                lokasi_pilih,
                alamat,
                tgl_lahir,
                id
            )
        }

    }

    private fun startUpdateData(
        namaDepan: Editable?,
        namaBelakang: Editable?,
        jenisKelaminPilih: String,
        lokasiPilih: String,
        alamat: Editable?,
        tglLahir: Editable?,
        id: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateMahasiswa(
            id,
            namaDepan.toString(),
            namaBelakang.toString(),
            alamat.toString(),
            jenisKelaminPilih,
            lokasiPilih,
            tglLahir.toString()
        )?.enqueue(object :
            Callback<Responses.ResponseMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseMahasiswa>,
                response: Response<Responses.ResponseMahasiswa>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@EditProfilMahasiswaActivity,
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
                            this@EditProfilMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditProfilMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditProfilMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })

    }

    private fun setupAutoCompletLokasi(lokasi: String) {
        val ac_lokasi = binding.etLokasiEditProfil
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

    private fun loadDataSaya(id: String) {

        ApiClient.instances.getMahasiswaID(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataUser(user)
                        } else {
                            Toast.makeText(
                                this@EditProfilMahasiswaActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@EditProfilMahasiswaActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    Toast.makeText(
                        this@EditProfilMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })


    }

    private fun initDataUser(user: User) {
        binding.etNamaDepanEditProfil.setText(user.nama_depan)
        binding.etNamaBelakangEditProfil.setText(user.nama_belakang)
        setupSpinnerInit(user.jenis_kelamin)
        lokasi_pilih = user.lokasi
        setupAutoCompletLokasi(user.lokasi)
        binding.etAlamatEditProfil.setText(user.alamat)
        binding.etTanggalLahirEditProfil.setText(user.tanggal_lahir)
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
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayJenisKelamin)
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

    private fun setupSpinner() {
        val arrayJenisKelamin = arrayOf("Laki-laki", "Perempuan")
        val spinnerJenisKelamin = binding.spJenisKelaminEditProfil
        val arrayAdapterJenisKelamin =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayJenisKelamin)
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
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
            }
        }
    }
}