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
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityAddKeahlianMahasiswaBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddKeahlianMahasiswaActivity : AppCompatActivity() {
    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var level_keahlian: String = ""

    private lateinit var binding: ActivityAddKeahlianMahasiswaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKeahlianMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        setupSpinner()

        binding.imgBackAddKeahlian.setOnClickListener { finish() }

        binding.rlSimpanPerubahan.setOnClickListener {
            clickSimpan()
        }
    }

    private fun clickSimpan() {
        val nama_keahlian = binding.etNamaKeahlian.text

        if (nama_keahlian.isEmpty()) {
            binding.etNamaKeahlian.error = "Lengkapi"
        } else {
            startAddData(id, nama_keahlian, level_keahlian)
        }
    }

    private fun startAddData(id: String, namaKeahlian: Editable?, levelKeahlian: String) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.addKeahlianUser(id, namaKeahlian.toString(), levelKeahlian)
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
                                this@AddKeahlianMahasiswaActivity,
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
                                this@AddKeahlianMahasiswaActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@AddKeahlianMahasiswaActivity,
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
                    SweetAlertDialog(this@AddKeahlianMahasiswaActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }

            })

    }

    private fun setupSpinner() {
        val arrayLevelSkill = arrayOf(
            "Beginner", "Advanced", "Competent", "Proficient", "Expert"
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
                    level_keahlian = arrayLevelSkill[position]
//                Toast.makeText(applicationContext, arrayJenisPengalaman[position], Toast.LENGTH_SHORT).show()
                }
            }
    }
}