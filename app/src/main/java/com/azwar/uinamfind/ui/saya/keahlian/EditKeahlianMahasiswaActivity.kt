package com.azwar.uinamfind.ui.saya.keahlian

import android.R
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.KeahlianMahasiswa
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditKeahlianMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditKeahlianMahasiswaActivity : AppCompatActivity() {
    private var keahlian_id: String = ""
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var level_keahlian: String = ""

    private lateinit var binding: ActivityEditKeahlianMahasiswaBinding
    private lateinit var keahlianMahasiswa: KeahlianMahasiswa
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditKeahlianMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        keahlianMahasiswa = intent.getParcelableExtra("keahlian")!!
        initDataIntent(keahlianMahasiswa)

        binding.rlSimpanPerubahan.setOnClickListener {
            clickSimpan()
        }

        binding.imgBackEditKeahlian.setOnClickListener { finish() }

        binding.imgDeleteKeahlian.setOnClickListener {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Won't be able to recover this file!")
                .setConfirmText("Yes,delete it!")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    startDelet(keahlian_id)
                }
                .show()
        }
    }

    private fun startDelet(keahlianId: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.deleteKeahlianUser(keahlianId)
            ?.enqueue(object : Callback<Responses.ResponseKeahlianMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseKeahlianMahasiswa>,
                    response: Response<Responses.ResponseKeahlianMahasiswa>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@EditKeahlianMahasiswaActivity,
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
                                this@EditKeahlianMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@EditKeahlianMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseKeahlianMahasiswa>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(
                        this@EditKeahlianMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })
    }

    private fun clickSimpan() {
        val nama_keahlian = binding.etNamaKeahlian.text

        if (nama_keahlian.isEmpty()) {
            binding.etNamaKeahlian.error = "Lengkapi"
        } else {
            startEditData(keahlian_id, id, nama_keahlian, level_keahlian)
        }
    }

    private fun startEditData(
        keahlianId: String,
        id: String,
        namaKeahlian: Editable?,
        levelKeahlian: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateKeahlianUser(
            keahlianId, id, namaKeahlian.toString(), levelKeahlian
        )?.enqueue(object : Callback<Responses.ResponseKeahlianMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                response: Response<Responses.ResponseKeahlianMahasiswa>
            ) {

                dialogProgress.dismiss()
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode

                if (response.isSuccessful) {
                    if (kode.equals("1")) {

                        SweetAlertDialog(
                            this@EditKeahlianMahasiswaActivity,
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
                            this@EditKeahlianMahasiswaActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditKeahlianMahasiswaActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon1")
                        .show()
                }

            }

            override fun onFailure(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditKeahlianMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Gagal..")
                    .setContentText(t.localizedMessage)
                    .show()
            }

        })

    }

    private fun initDataIntent(keahlianMahasiswa: KeahlianMahasiswa) {
        keahlian_id = keahlianMahasiswa.id.toString()
        binding.etNamaKeahlian.setText(keahlianMahasiswa.nama_skill)
        var level_keahlian = keahlianMahasiswa.level_skill
        level_keahlian = level_keahlian
        setupSpinner(level_keahlian)
    }

    private fun setupSpinner(levelKeahlian: String?) {

        val arrayLevelSkill = arrayOf(
            levelKeahlian, "Beginner", "Advanced", "Competent", "Proficient", "Expert"
        )
        val spinnerLevelSkill = binding.spLevelKeahlian
        val arrayAdapterLevelSkill =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayLevelSkill)
        spinnerLevelSkill.adapter = arrayAdapterLevelSkill

        spinnerLevelSkill?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    level_keahlian = arrayLevelSkill[position].toString()
//                Toast.makeText(applicationContext, arrayJenisPengalaman[position], Toast.LENGTH_SHORT).show()
                }
            }
    }
}