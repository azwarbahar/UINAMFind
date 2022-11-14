package com.azwar.uinamfind.ui.loker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityTambahLokerBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahLokerActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityTambahLokerBinding

    private lateinit var perusahaan: Perusahaan
    private lateinit var recruiter: Recruiter

    private var jenis_pekerjaan: String = ""
    private var lokasi_pilih: String = ""
    private var gaji_tersedia: String = "Tidak"
    private var lamar_mudah: String = "Tidak"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()

        loadDataAkun(id)
        setupSpinnerInit()
        setupAutoCompletLokasi()

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
                lamar_mudah = "Ya"
            } else {
                linkFalse()
                lamar_mudah = "Tidak"
            }
        }

        binding.tvSimpan.setOnClickListener {
            clickSimpan()
        }

        binding.imgBack.setOnClickListener { finish() }


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
        if (lamar_mudah.equals("Ya")) {
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
        if (lamar_mudah.equals("Ya")) {
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
                .setTitleText("Buat loker")
                .setContentText("Yakin ingin mengirim loker ini?")
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
                        id,
                        perusahaan.id,
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
        recruiter_id: String,
        perusahaan_id: String?,
        lamarMudah: String,
        linkLamar: String
    ) {
        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.addLoker(
            posisi,
            jobdesk,
            deskripsi,
            lokasi,
            jenis,
            gajiTersedia,
            maxGaji,
            minGaji,
            recruiter_id,
            perusahaan_id,
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
                            this@TambahLokerActivity,
                            SweetAlertDialog.SUCCESS_TYPE
                        )
                            .setTitleText("Success..")
                            .setContentText("Loker berhasil dibuat..")
                            .setConfirmButton(
                                "Ok"
                            ) { sweetAlertDialog ->
                                sweetAlertDialog.dismiss()
                                finish()
                            }
                            .show()
                    } else {
                        SweetAlertDialog(
                            this@TambahLokerActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@TambahLokerActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(call: Call<Responses.ResponseLoker>, t: Throwable) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@TambahLokerActivity, SweetAlertDialog.ERROR_TYPE)
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
                            Toast.makeText(this@TambahLokerActivity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(
                            this@TambahLokerActivity,
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
                        this@TambahLokerActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

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

    private fun setupAutoCompletLokasi() {
        val ac_lokasi = binding.etLokasi
        var nama_lokasi = resources.getStringArray(com.azwar.uinamfind.R.array.nama_lokasi)
        val adapter: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.select_dialog_item, nama_lokasi)
        ac_lokasi.threshold = 1
        ac_lokasi.setAdapter(adapter)
//        if (lokasi != null) {
//            ac_lokasi.setText(lokasi)
//        }
        ac_lokasi.setOnItemClickListener(AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
            lokasi_pilih = arg0.getItemAtPosition(arg2).toString()
        })
    }

    private fun setupSpinnerInit() {
        val arrayJenisPekerjaan =
            arrayOf(
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
                    jenis_pekerjaan = arrayJenisPekerjaan[position]
//                Toast.makeText(
//                    applicationContext,
//                    getString(R.string.selected_item) + " " + arrayJenisKelamin[position],
//                    Toast.LENGTH_SHORT
//                ).show()
                }
            }
    }
}