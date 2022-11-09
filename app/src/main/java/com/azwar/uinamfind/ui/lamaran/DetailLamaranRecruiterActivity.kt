package com.azwar.uinamfind.ui.lamaran

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailLamaranRecruiterBinding
import com.azwar.uinamfind.ui.loker.DetailLokerActivity
import com.azwar.uinamfind.ui.mahasiswa.DetailMahasiswaActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class DetailLamaranRecruiterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLamaranRecruiterBinding

    private lateinit var lamaran: Lamaran
    private lateinit var user: User

    lateinit var loker: Loker

    private var status_lamaran = ""
    private var id_lamaran = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLamaranRecruiterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lamaran = intent.getParcelableExtra("lamaran")!!
        loadLamaran(lamaran.id)

        binding.rlLoker.setOnClickListener {
            val intent = Intent(this, DetailLokerActivity::class.java)
            intent.putExtra("loker", loker)
            startActivity(intent)
        }

        binding.tvNamaPelamar.setOnClickListener {
            val intent = Intent(this, DetailMahasiswaActivity::class.java)
            intent.putExtra("mahasiswa", user)
            startActivity(intent)
        }

        binding.imgBack.setOnClickListener { finish() }

        binding.rlSimpan.setOnClickListener {
            if (!status_lamaran.isEmpty()) {
                if (status_lamaran.equals("New")) {
                    SweetAlertDialog(
                        this@DetailLamaranRecruiterActivity,
                        SweetAlertDialog.WARNING_TYPE
                    )
                        .setTitleText("Tidak Lanjut")
                        .setContentText("Menindaklanjutkan lamaran mahasiswa!")
                        .setConfirmButton("OK", SweetAlertDialog.OnSweetClickListener {
                            it.dismiss()
                        })
                        .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                            it.dismiss()
                            startUpdateLamaran("Review")
                        }).show()
                } else if (status_lamaran.equals("Review")) {
                    SweetAlertDialog(
                        this@DetailLamaranRecruiterActivity,
                        SweetAlertDialog.WARNING_TYPE
                    )
                        .setTitleText("Tidak Lanjut")
                        .setContentText("Menindaklanjutkan lamaran mahasiswa!")
                        .setConfirmButton("OK", SweetAlertDialog.OnSweetClickListener {
                            it.dismiss()
                        })
                        .setCancelButton("Batal", SweetAlertDialog.OnSweetClickListener {
                            it.dismiss()
                            startUpdateLamaran("Finished")
                        }).show()
                } else if (status_lamaran.equals("Finished")) {
                    SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Selesai")
                        .setContentText("Proses lamaran sudah selesai!")
                        .show()
                }
            }
        }
    }

    private fun startUpdateLamaran(s: String) {

        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading"
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.updateLamaranStatus(id_lamaran, s)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    if (response.isSuccessful) {
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode
                        if (kode.equals("1")) {
                            SweetAlertDialog(
                                this@DetailLamaranRecruiterActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success")
                                .setContentText("Berhasil mengupdate lamaran")
                                .setConfirmButton("Ok", SweetAlertDialog.OnSweetClickListener {
                                    it.dismiss()
                                    loadLamaran(id_lamaran)
                                })
                                .show()
                        } else {
                            SweetAlertDialog(
                                this@DetailLamaranRecruiterActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Maaf..")
                                .setContentText(message)
                                .show()
                        }

                    } else {
                        SweetAlertDialog(
                            this@DetailLamaranRecruiterActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Maaf..")
                            .setContentText(pesanRespon)
                            .show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(
                        this@DetailLamaranRecruiterActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Maaf..")
                        .setContentText(t.message)
                        .show()
                }
            })
    }

    private fun initData(lamaran: Lamaran) {
        id_lamaran = lamaran.id.toString()
        var mahasiswa_id = lamaran.mahasiswa_id
        var loker_id = lamaran.loker_id

        var tanggal = lamaran.created_at
        var pesan = lamaran.pesan
        status_lamaran = lamaran.status_lamaran.toString()
        var dokumen_lamaran = lamaran.dokumen_lamaran
        var telpon = lamaran.telpon_pelamar
        var email = lamaran.email_pelamar

        binding.tvEmail.text = email
        binding.tvTelponLamaran.text = telpon
        if (pesan.equals("") || pesan == null) {
            binding.tvPesanLamaran.text = "-"
        } else {
            binding.tvPesanLamaran.text = pesan
        }

        if (dokumen_lamaran == null || dokumen_lamaran.equals("")) {
        } else {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/lamaran/" + dokumen_lamaran)
                .into(binding.imgCv)
        }

        if (status_lamaran.equals("New")) {
            binding.tvBtnLamaran.text = "Review"
        } else if (status_lamaran.equals("Review")) {
            binding.tvBtnLamaran.text = "Selesai"
        } else {
            binding.tvBtnLamaran.text = "Proses Selesai"
        }

        loadMahasiswa(mahasiswa_id)
        loadLoker(loker_id)

        binding.tvTglLamaran.text = convertDate(tanggal)

    }

    private fun loadMahasiswa(mahasiswaId: String?) {

        ApiClient.instances.getMahasiswaID(mahasiswaId)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {

                        user = response.body()?.result_mahasiswa!!
                        var nim = user.nim.toString()

                        var jurusan = user.jurusan
                        binding.tvJurusan.text = jurusan

                        // nama
                        var nama_depan = user.nama_depan.toString()
                        var nama_belakang = user.nama_belakang.toString()
                        var text_nama_lengkap = binding.tvNamaPelamar
                        if (nama_depan == null) {
                            text_nama_lengkap.text = nim
                        } else if (nama_belakang == null) {
                            var nama_lengkap = user.nama_depan
                            text_nama_lengkap.text = nama_lengkap
                        } else {
                            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
                            text_nama_lengkap.text = nama_lengkap
                        }
                        val foto = user.foto.toString()
                        if (foto.equals("-") || foto.isEmpty()) {
                        } else {
                            Glide.with(this@DetailLamaranRecruiterActivity)
                                .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                                .into(binding.imgPhoto)
                        }

                    } else {
                        binding.tvNamaPelamar.text = "Nama Pelamar"
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseMahasiswa>,
                    t: Throwable
                ) {
                    binding.tvNamaPelamar.text = "Nama Pelamar"

                }

            })


    }

    private fun loadLoker(lokerId: String?) {

        ApiClient.instances.getLokerId(lokerId)
            ?.enqueue(object : Callback<Responses.ResponseLoker> {
                override fun onResponse(
                    call: Call<Responses.ResponseLoker>,
                    response: Response<Responses.ResponseLoker>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        loker = response.body()?.result_loker!!
                        binding.tvNamaLoker.text = loker.posisi.toString()
                        var lokasi = loker.lokasi
                        var tgl = loker.created_at
                        binding.tvLokasiLoker.text = lokasi + ", Indonesia"
                        binding.tvTglLamaran.text = "Dibagikan pada " + convertDate(tgl)

                    } else {
                        binding.tvNamaLoker.text = "Nama Loker"
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseLoker>,
                    t: Throwable
                ) {
                    binding.tvNamaLoker.text = "Nama Loker"
                }

            })

    }

    private fun loadLamaran(lamaranId: String?) {

        ApiClient.instances.getLamaranId(lamaranId)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            lamaran = response.body()?.result_lamaran!!
                            initData(lamaran)
                        } else {
                            SweetAlertDialog(
                                this@DetailLamaranRecruiterActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Maaf..")
                                .setContentText("Gagal Memproses data!")
                                .setConfirmButton("Ulang", SweetAlertDialog.OnSweetClickListener {
                                    it.dismiss()
                                    loadLamaran(id_lamaran)
                                })
                                .show()
                        }
                    } else {
                        SweetAlertDialog(
                            this@DetailLamaranRecruiterActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Maaf..")
                            .setContentText("Gagal Memproses data!")
                            .setConfirmButton("Ulang", SweetAlertDialog.OnSweetClickListener {
                                it.dismiss()
                                loadLamaran(id_lamaran)
                            })
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseLamaran>,
                    t: Throwable
                ) {
                    SweetAlertDialog(
                        this@DetailLamaranRecruiterActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Maaf..")
                        .setContentText("Gagal Memproses data!")
                        .setConfirmButton("Ulang", SweetAlertDialog.OnSweetClickListener {
                            it.dismiss()
                            loadLamaran(id_lamaran)
                        })
                        .show()
                }

            })

    }

    private fun convertDate(date: String?): String {
        val parser = SimpleDateFormat("yyyy-mm-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
    }
}