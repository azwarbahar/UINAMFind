package com.azwar.uinamfind.ui.loker

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityEditLokerBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditLokerActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var perusahaan: Perusahaan
    private lateinit var recruiter: Recruiter
    private lateinit var loker: Loker

    private var jenis_pekerjaan: String = ""
    private var lokasi_pilih: String = ""
    private var gaji_tersedia: String = "Tidak"
    private var lamar_mudah: String = "Ya"


    private lateinit var binding: ActivityEditLokerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()
        loadDataAkun(id)
        loker = intent.getParcelableExtra("loker")!!
        initData(loker)

        binding.cbSalary.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                gaji_tersedia = "Ya"
                salaryTrue()
            } else {
                gaji_tersedia = "Tidak"
                salaryFalse()
            }
        }
        binding.cbLamaraMudah.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                linkTrue()
                lamar_mudah = "Tidak"
            } else {
                linkFalse()
                lamar_mudah = "Ya"
            }
        }

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }

        binding.imgBack.setOnClickListener { finish() }

        binding.llHapus.setOnClickListener {
            clickDelete()
        }
    }

    private fun clickDelete() {
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Delete loker")
            .setContentText("Yakin ingin menghapus loker ini?")
            .setConfirmButton(
                "Ok"
            ) { sweetAlertDialog ->
                sweetAlertDialog.dismiss()
                startDelete()
            }
            .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                it.dismiss()
            })
            .show()

    }

    private fun startDelete() {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.deleteLoker(loker.id.toString())
            ?.enqueue(object : Callback<Responses.ResponseLoker> {
                override fun onResponse(
                    call: Call<Responses.ResponseLoker>,
                    response: Response<Responses.ResponseLoker>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@EditLokerActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Loker berhasil dihapus..")
                                .setConfirmButton(
                                    "Ok"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@EditLokerActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Gagal..")
                                .setContentText(message)
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@EditLokerActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText("Server tidak merespon")
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseLoker>, t: Throwable) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@EditLokerActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Maaf..")
                        .setContentText("Terjadi Kesalahan Sistem")
                        .show()
                }

            })


    }

    private fun clickSimpan() {

        var isAllClear = false

        var posisi = binding.etPosisiPekerjaan.text.toString()
        var jenis = jenis_pekerjaan
        var lokasi = binding.etLokasi.text.toString()
        var jobdesk = binding.etJobdesk.text.toString()
        var deskripsi = binding.etDeskripsi.text.toString()

        var min_gaji = ""
        var max_gaji = ""
        if (gaji_tersedia.equals("Ya")) {
            min_gaji = binding.etMinGaji.text.toString()
            max_gaji = binding.etMaxGaji.text.toString()
        } else {
            isAllClear = true
        }
        var link_lamar = ""
        if (lamar_mudah.equals("Tidak")) {
            link_lamar = binding.etLinkLamaran.text.toString()
        } else {
            isAllClear = true
        }

        if (posisi.isEmpty()) {
            binding.etPosisiPekerjaan.error = "Lengkapi"
            binding.etPosisiPekerjaan.requestFocus()
            isAllClear = false
        } else {
            isAllClear = true
        }
        if (jenis.equals("")) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Jenis Pekerjaan")
                .setContentText("Silahkan lengkapi Jenis Pekerjaan")
                .show()
            binding.spJenisPekerjaan.requestFocus()
            isAllClear = false
        } else {
            isAllClear = true
        }
        if (lokasi.isEmpty()) {
            binding.etLokasi.error = "Lengkapi"
            binding.etLokasi.requestFocus()
            isAllClear = false
        } else {
            isAllClear = true
        }
        if (jobdesk.isEmpty()) {
            binding.etJobdesk.error = "Lengkapi"
            binding.etJobdesk.requestFocus()
            isAllClear = false
        } else {
            isAllClear = true
        }
        if (deskripsi.isEmpty()) {
            binding.etDeskripsi.error = "Lengkapi"
            binding.etDeskripsi.requestFocus()
            isAllClear = false
        } else {
            isAllClear = true
        }
        if (gaji_tersedia.equals("Ya")) {
            if (min_gaji.equals("")) {
                binding.etMinGaji.error = "Lengkapi"
                binding.etMinGaji.requestFocus()
                isAllClear = false
            } else if (max_gaji.equals("")) {
                binding.etMaxGaji.error = "Lengkapi"
                binding.etMaxGaji.requestFocus()
                isAllClear = false
            } else {
                isAllClear = true
            }
        }
        if (lamar_mudah.equals("Tidak")) {
            if (link_lamar.equals("")) {
                binding.etLinkLamaran.error = "Lengkapi"
                binding.etLinkLamaran.requestFocus()
                isAllClear = false
            } else {
                isAllClear = true
            }
        }

        if (isAllClear) {
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Edit loker")
                .setContentText("Yakin ingin mengirim perubahan loker ini?")
                .setConfirmButton(
                    "Ok"
                ) { sweetAlertDialog ->
                    sweetAlertDialog.dismiss()
                    sendData(
                        posisi,
                        jobdesk,
                        deskripsi,
                        lokasi,
                        jenis,
                        gaji_tersedia,
                        max_gaji,
                        min_gaji,
                        lamar_mudah,
                        link_lamar
                    )
                }
                .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                    it.dismiss()
                })
                .show()
        }


    }

    private fun sendData(
        posisi: String,
        jobdesk: String,
        deskripsi: String,
        lokasi: String,
        jenis: String,
        gajiTersedia: String,
        maxGaji: String,
        minGaji: String,
        lamarMudah: String,
        linkLamar: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateLoker(
            loker.id.toString(),
            posisi,
            jobdesk,
            deskripsi,
            lokasi,
            jenis,
            gajiTersedia,
            maxGaji,
            minGaji,
            lamarMudah,
            linkLamar
        )?.enqueue(object : Callback<Responses.ResponseLoker> {
            override fun onResponse(
                call: Call<Responses.ResponseLoker>,
                response: Response<Responses.ResponseLoker>
            ) {
                dialogProgress.dismiss()
                val pesanRespon = response.message()
                if (response.isSuccessful) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (kode.equals("1")) {
                        SweetAlertDialog(
                            this@EditLokerActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Loker berhasil diubah..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@EditLokerActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@EditLokerActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseLoker>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@EditLokerActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }

    private fun loadDataAkun(id: String) {

        ApiClient.instances.getRecruiterId(id)
            ?.enqueue(object : Callback<Responses.LoginRecruiterResponse> {
                override fun onResponse(
                    call: Call<Responses.LoginRecruiterResponse>,
                    response: Response<Responses.LoginRecruiterResponse>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    recruiter = response.body()?.result_recruiter!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataAkun(recruiter)
                        } else {
                            Toast.makeText(this@EditLokerActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@EditLokerActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.LoginRecruiterResponse>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@EditLokerActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    private fun linkTrue() {
        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.grey_hint)
        binding.etLinkLamaran.backgroundTintList = colorStateList
        binding.etLinkLamaran.setText("")
        binding.etLinkLamaran.isEnabled = true
    }

    private fun linkFalse() {
        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.grey_hint)
        binding.etLinkLamaran.backgroundTintList = colorStateList
        binding.etLinkLamaran.setText("")
        binding.etLinkLamaran.isEnabled = false
    }

    private fun salaryTrue() {
        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.grey_hint)
        binding.etMinGaji.backgroundTintList = colorStateList
        binding.etMinGaji.setText("")
        binding.etMinGaji.isEnabled = true
        binding.etMaxGaji.backgroundTintList = colorStateList
        binding.etMaxGaji.setText("")
        binding.etMaxGaji.isEnabled = true
    }

    private fun salaryFalse() {
        val colorStateList = ColorStateList.valueOf(com.azwar.uinamfind.R.color.grey_hint)
        binding.etMinGaji.backgroundTintList = colorStateList
        binding.etMinGaji.setText("")
        binding.etMinGaji.isEnabled = false


        binding.etMaxGaji.backgroundTintList = colorStateList
        binding.etMaxGaji.setText("")
        binding.etMaxGaji.isEnabled = false
    }


    private fun initDataAkun(recruiter: Recruiter) {
        var perusahaan_id = recruiter.id_perusahaan
        loadPerusahaan(perusahaan_id)
    }

    private fun loadPerusahaan(perusahaanId: String?) {
        ApiClient.instances.getPerusahaanId(perusahaanId)?.enqueue(object :
            Callback<Responses.ResponsePerusahaan> {
            override fun onResponse(
                call: Call<Responses.ResponsePerusahaan>,
                response: Response<Responses.ResponsePerusahaan>
            ) {
                if (response.isSuccessful) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode

                    if (kode.equals("1")) {
                        perusahaan = response.body()?.result_perusahaan!!
                    } else {
                    }
                } else {
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePerusahaan>,
                t: Throwable
            ) {
            }

        })
    }


    private fun initData(loker: Loker) {

        binding.etPosisiPekerjaan.setText(loker.posisi)
        lokasi_pilih = loker.lokasi.toString()
        setupAutoCompletLokasi(lokasi_pilih)
        setupSpinnerInit(loker.jenis_pekerjaan)

        binding.etDeskripsi.setText(loker.deskripsi)
        binding.etJobdesk.setText(loker.jobdesk)

        if (loker.gaji_tersedia.equals("Ya")) {
            gaji_tersedia = "Ya"
            binding.cbSalary.isChecked = true
            binding.etMinGaji.setText(loker.gaji_min)
            binding.etMaxGaji.setText(loker.gaji_max)
        } else {
            gaji_tersedia = "Tidak"
            binding.cbSalary.isChecked = false
        }

        if (loker.lamar_mudah.equals("Tidak")) {
            lamar_mudah = "Tidak"
            binding.cbLamaraMudah.isChecked = true
            binding.etLinkLamaran.setText(loker.link_lamar)
        } else {
            binding.cbLamaraMudah.isChecked = false
            lamar_mudah = "Ya"
        }


    }

    private fun setupSpinnerInit(jenisPekerjaan: String?) {
        val arrayJenisPekerjaan =
            arrayOf(
                jenisPekerjaan,
                "Purnawaktu", "Paruh Waktu", "Wiraswasta",
                "Pekerjaan Lepas", "Kontrak", "Magang"
            )
        val spinnerJenisPekerjaan = binding.spJenisPekerjaan
        val arrayAdapterJenisPekerjaan =
            ArrayAdapter(this, R.layout.simple_list_item_1, arrayJenisPekerjaan)
        spinnerJenisPekerjaan.adapter = arrayAdapterJenisPekerjaan
        spinnerJenisPekerjaan?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    jenis_pekerjaan = arrayJenisPekerjaan[position].toString()
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
                }
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
        ac_lokasi.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            lokasi_pilih = arg0.getItemAtPosition(arg2).toString()
        })
    }
}