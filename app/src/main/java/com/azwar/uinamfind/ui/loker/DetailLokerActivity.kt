package com.azwar.uinamfind.ui.loker

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailLokerBinding
import com.azwar.uinamfind.ui.ShowPhotoActivity
import com.azwar.uinamfind.ui.akun.adpter.LamaranMahasiswaBaruAdapter
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat


class DetailLokerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLokerBinding
    private var lamar_mudah = ""
    private var link_lamar = ""
    private lateinit var loker: Loker

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""
    private var recruiter_id: String = ""
    private var role: String = ""

    private var alreadyLamaran = false
    private lateinit var lamarans: List<Lamaran>

    private lateinit var perusahaan: Perusahaan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLokerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        recruiter_id = sharedPref.getString(Constanta.ID_RECRUITER).toString()
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()

        loker = intent.getParcelableExtra("loker")!!
        initData(loker)

        if (role.equals("user")) {
            loadPelamar(id, loker.id.toString())
        }

        val toolbar_loker_detail = binding.toolbarLokerDetail
        setSupportActionBar(toolbar_loker_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_chevron_left_24)

        binding.rlBrnLamar.setOnClickListener {

            if (role.equals("user")) {
                if (!alreadyLamaran) {
                    if (lamar_mudah.equals("Ya")) {
                        val intent = Intent(this, LamarLokerActivity::class.java)
                        intent.putExtra("loker", loker)
                        startActivity(intent)
                    } else {
                        if (link_lamar.equals("-") || link_lamar.equals("")) {
                            Toast.makeText(this, "Link tidak terdeteksi", Toast.LENGTH_SHORT)
                        } else {
                            val defaultBrowser = Intent.makeMainSelectorActivity(
                                Intent.ACTION_MAIN,
                                Intent.CATEGORY_APP_BROWSER
                            )
                            defaultBrowser.data = Uri.parse(link_lamar)
                            startActivity(defaultBrowser)
                        }
                    }
                }

            } else {
                SweetAlertDialog(
                    this,
                    SweetAlertDialog.ERROR_TYPE
                )
                    .setTitleText("Maaf..")
                    .setContentText("Akses tidak diizinkan!")
                    .show()
            }
        }

        binding.fabShareDetailLoker.setOnClickListener {

            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "Hi, ada loker nih " + loker.posisi.toString() + ",  ayo cek di :\n" +
                        "https://uinamfind.com/loker/" + loker.slug.toString()
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
        }

        binding.imgLogoKantorDetailLoker.setOnClickListener {
            var foto = perusahaan.foto
            var foto_intent = BuildConfig.BASE_URL + "upload/perusahaan/" + foto
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }

        binding.imgLogoPerusahaanDetailLoker.setOnClickListener {
            var foto = perusahaan.foto
            var foto_intent = BuildConfig.BASE_URL + "upload/perusahaan/" + foto
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }


    }

    private fun initData(loker: Loker) {

        binding.tvJudulDetailLoker.setText(loker.posisi)
        binding.tvLokasiDetailLoker.setText(loker.lokasi)
        binding.tvJenisDetailLoker.setText(loker.jenis_pekerjaan)
        var gaji_tercantum = loker.gaji_tersedia
        if (gaji_tercantum.equals("Ya")) {
            binding.tvRangeGajiDetailLoker.setText(
                moneyFormatConvert(loker.gaji_min.toString()) + " - " + moneyFormatConvert(
                    loker.gaji_max.toString()
                )
            )
        } else {
            binding.tvRangeGajiDetailLoker.setText("Salary tidak dicantumkan")
        }
        binding.tvTanggalTayangDetailLoker.setText("Tayang pada " + convertDate(loker.created_at))
        binding.tvDeskripsiDetailLoker.setText(loker.deskripsi)
        binding.tvJobdesDetailLoker.setText(loker.jobdesk)

        lamar_mudah = loker.lamar_mudah!!
        if (lamar_mudah.equals("Ya")) {
            binding.tvBtnLamarLoker.setText("Lamar Sekarang")
        } else {
            binding.tvBtnLamarLoker.setText("Kunjungi Untuk Melamar")
        }
        link_lamar = loker.link_lamar!!
        loadPerusahaan(loker.perusahaan_id)
    }

    private fun moneyFormatConvert(value: String): String? {
        val nilai = value.toDouble()
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(nilai)
    }

    private fun convertDate(date: String?): String {
//        2022-07-30 03:40:52
//        val parser = SimpleDateFormat("dd-MM-yyyy")
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val formatter = SimpleDateFormat("dd MMM yyyy")
        val output = formatter.format(parser.parse(date))
        return output
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
                    perusahaan = response.body()?.result_perusahaan!!
                    var foto_perusahaan = perusahaan.foto
                    if (foto_perusahaan != null) {
                        Glide.with(this@DetailLokerActivity)
                            .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto_perusahaan)
                            .into(binding.imgLogoKantorDetailLoker)
                        Glide.with(this@DetailLokerActivity)
                            .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto_perusahaan)
                            .into(binding.imgLogoPerusahaanDetailLoker)
                    }
                    binding.tvNamaKantorDetailLoker.setText(perusahaan.nama)
                    binding.tvNamaPerusahaanDetailLoker.setText(perusahaan.nama)
                    binding.tvJumlahKariawanDetailLoker.setText(perusahaan.ukuran_kariawan + " Kariawan")
                    binding.tvDeskripsiPerusahaanDetailLoker.setText(perusahaan.deskripsi)
                }
            }

            override fun onFailure(call: Call<Responses.ResponsePerusahaan>, t: Throwable) {
            }

        })
    }

    private fun onLamaranReady() {
        binding.tvBtnLamarLoker.setText("Selesai Melamar")
        binding.tvBtnLamarLoker.setTextColor(ContextCompat.getColor(this, R.color.grey));
        binding.rlBrnLamar.setBackground(
            ContextCompat.getDrawable(
                this,
                R.drawable.bg_grey_trans_corner
            )
        );
    }

    private fun onLamaranNoReady() {
        binding.tvBtnLamarLoker.setText("Lamar Sekarang")
        binding.tvBtnLamarLoker.setTextColor(ContextCompat.getColor(this, R.color.white));
        binding.rlBrnLamar.setBackground(
            ContextCompat.getDrawable(
                this,
                R.drawable.bg_primary_corner_16
            )
        );
    }

    private fun loadPelamar(mahasiswa_id: String?, loker_id: String?) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.getLamaranMahasiswaId(mahasiswa_id, loker_id)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    dialogProgress.dismiss()
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            lamarans = response.body()?.lamaran_data!!
                            if (lamarans.size > 0) {
                                alreadyLamaran = true
                                onLamaranReady()
                            } else {
                                alreadyLamaran = false
                                onLamaranNoReady()
                            }
                        } else {
                            alreadyLamaran = false
                            onLamaranNoReady()
                        }
                    } else {
                        alreadyLamaran = false
                        onLamaranNoReady()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
                    dialogProgress.dismiss()
                    alreadyLamaran = false
                    onLamaranNoReady()
                }
            })

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}