package com.azwar.uinamfind.ui.akun

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Lamaran
import com.azwar.uinamfind.data.models.Loker
import com.azwar.uinamfind.data.models.Perusahaan
import com.azwar.uinamfind.data.models.Recruiter
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentAkunBinding
import com.azwar.uinamfind.ui.akun.adpter.LamaranMahasiswaBaruAdapter
import com.azwar.uinamfind.ui.akun.adpter.LokerRecruiterAdapter
import com.azwar.uinamfind.ui.loker.TambahLokerActivity
import com.azwar.uinamfind.ui.perusahaan.EditPerusahaanActivity
import com.azwar.uinamfind.ui.perusahaan.TambahPerusahaanActivity
import com.azwar.uinamfind.utils.Constanta
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AkunFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private var _binding: FragmentAkunBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPref: PreferencesHelper

    // model
    private lateinit var perusahaan: Perusahaan
    private lateinit var recruiter: Recruiter
    lateinit var loker: List<Loker>

    private lateinit var lamaran_baru: List<Lamaran>

    private lateinit var lokerRecruiterAdapter: LokerRecruiterAdapter
    private lateinit var lamaranMahasiswaBaruAdapter: LamaranMahasiswaBaruAdapter

    private var id: String = ""

    private lateinit var swipe_akun: SwipeRefreshLayout

    private val PROFILE_IMAGE_REQ_CODE = 101
    private val HEADER_IMAGE_REQ_CODE = 102

    companion object {
        fun newInstance() = AkunFragment()
    }

    private lateinit var dialogProgress: SweetAlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAkunBinding.inflate(inflater, container, false)
        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_RECRUITER).toString()
        binding.rlPerusahaan.visibility = View.GONE

        // show progress loading

        dialogProgress = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        swipe_akun = binding.swipeAkun
        swipe_akun.setOnRefreshListener(this)
        swipe_akun.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_akun.post(Runnable {
            loadDataAkun(id)
            loadLokerRecruiter(id)
            loadLamaranBaru(id)
        })

        binding.imgAddLoker.setOnClickListener {
            val intent = Intent(context, TambahLokerActivity::class.java)
            startActivity(intent)
        }

        binding.imgEditPerusahaan.setOnClickListener {
            val intent = Intent(context, EditPerusahaanActivity::class.java)
            intent.putExtra("perusahaan", perusahaan)
            startActivity(intent)
        }

        binding.tvBelumDiaturPerusahaan.setOnClickListener {
            val intent = Intent(context, TambahPerusahaanActivity::class.java)
            startActivity(intent)
        }

        binding.rlEditProfilRecruiter.setOnClickListener {
            val intent = Intent(context, EditProfilRecruiterActivity::class.java)
            intent.putExtra("recruiter", recruiter)
            startActivity(intent)
        }

        binding.rlPengaturanRecruiter.setOnClickListener {
            val intent = Intent(context, PengaturanRecruiterActivity::class.java)
            startActivity(intent)
        }

        binding.imgPhotoRecruiter.setOnClickListener {
            val pilihan = arrayOf(
                "Ganti Gambar",
                "Lihat Gambar"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Opsi")
            builder.setItems(pilihan) { dialog, which ->
                when (which) {
                    0 -> openImagePicker(PROFILE_IMAGE_REQ_CODE)
                    1 -> openPreviewImage("Profil")
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.imgHeaderRecruiter.setOnClickListener {
            val pilihan = arrayOf(
                "Ganti Gambar",
                "Lihat Gambar"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Opsi")
            builder.setItems(pilihan) { dialog, which ->
                when (which) {
                    0 -> openImagePicker(HEADER_IMAGE_REQ_CODE)
                    1 -> openPreviewImage("Profil")
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        return binding.root
    }

    private fun openPreviewImage(s: String) {

    }

    private fun openImagePicker(s: Int) {
        if (s == HEADER_IMAGE_REQ_CODE) {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(HEADER_IMAGE_REQ_CODE)
        } else {
            ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .compress(1024)
                .maxResultSize(620, 620)
                .start(PROFILE_IMAGE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                binding.imgPhotoRecruiter.setImageURI(null)
                binding.imgPhotoRecruiter.setImageURI(uri)
                startUploadPhoto("Profil", uri)
            } else if (requestCode == HEADER_IMAGE_REQ_CODE) {
                startUploadPhoto("Header", uri)
                binding.imgHeaderRecruiter.setImageURI(null)
                binding.imgHeaderRecruiter.setImageURI(uri)
            }
        }
    }

    private fun startUploadPhoto(ket: String, uri: Uri) {

        val dialogProgress = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        val file = File(uri.path)
        val foto = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val foto_send = MultipartBody.Part.createFormData("foto", file.name, foto)
        val keterangan = RequestBody.create("text/plain".toMediaTypeOrNull(), ket)
        val user_id = RequestBody.create("text/plain".toMediaTypeOrNull(), id)

        ApiClient.instances.updatePhotoRecruiter(keterangan, user_id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseRecruiter> {
                override fun onResponse(
                    call: Call<Responses.ResponseRecruiter>,
                    response: Response<Responses.ResponseRecruiter>
                ) {
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            Toast.makeText(activity, "Success upload photo!", Toast.LENGTH_SHORT)
                                .show()
                        } else {

                            Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(activity, pesanRespon, Toast.LENGTH_SHORT)
                            .show()
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseRecruiter>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

    }

    private fun loadLamaranBaru(id: String) {
        ApiClient.instances.getLamaranStatusRecruiterId("New", id)
            ?.enqueue(object : Callback<Responses.ResponseLamaran> {
                override fun onResponse(
                    call: Call<Responses.ResponseLamaran>,
                    response: Response<Responses.ResponseLamaran>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            binding.tvLamabarnBaruKosong.visibility = View.GONE
                            binding.rvLamaranMahasiswa.visibility = View.VISIBLE
                            lamaran_baru = response.body()?.lamaran_data!!
                            if (lamaran_baru.size > 0) {
                                val rv_lamaran_baru = binding.rvLamaranMahasiswa
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(
                                        activity,
                                        LinearLayoutManager.HORIZONTAL,
                                        false
                                    )
                                rv_lamaran_baru.layoutManager = layoutManagerLoker
                                lamaranMahasiswaBaruAdapter =
                                    LamaranMahasiswaBaruAdapter(lamaran_baru)
                                rv_lamaran_baru.adapter = lamaranMahasiswaBaruAdapter
                                binding.tv1.setText("Lamaran Baru (" + lamaran_baru.size + ")")
                            } else {
                                if (lamaran_baru.size < 1) {
                                    binding.tvLamabarnBaruKosong.visibility = View.VISIBLE
                                    binding.rvLamaranMahasiswa.visibility = View.GONE
                                    binding.tv1.setText("Lamaran Baru (0)")
                                }
                            }
                        } else {
                            if (lamaran_baru.size < 1) {
                                binding.tvLamabarnBaruKosong.visibility = View.VISIBLE
                                binding.rvLamaranMahasiswa.visibility = View.GONE
                                binding.tv1.setText("Lamaran Baru (0)")
                            }
                        }

                    } else {
                        if (lamaran_baru.size < 1) {
                            binding.tvLamabarnBaruKosong.visibility = View.VISIBLE
                            binding.rvLamaranMahasiswa.visibility = View.GONE
                            binding.tv1.setText("Lamaran Baru (0)")
                        }
                    }

                }

                override fun onFailure(call: Call<Responses.ResponseLamaran>, t: Throwable) {
//                    if (lamaran_baru.size < 1) {
                    binding.tvLamabarnBaruKosong.visibility = View.VISIBLE
                    binding.rvLamaranMahasiswa.visibility = View.GONE
                    binding.tv1.setText("Lamaran Baru (0)")
//                    }
                }
            })
    }

    private fun loadLokerRecruiter(id: String) {

        ApiClient.instances.getLokerRecruiterId(id)
            ?.enqueue(object : Callback<Responses.ResponseLoker> {
                override fun onResponse(
                    call: Call<Responses.ResponseLoker>,
                    response: Response<Responses.ResponseLoker>
                ) {
                    if (response.isSuccessful) {
                        val pesanRespon = response.message()
                        val message = response.body()?.pesan
                        val kode = response.body()?.kode

                        if (kode.equals("1")) {
                            loker = response.body()?.loker_data!!
                            if (loker.size > 0) {
                                val rv_loker = binding.rvLokerRecruiter
                                val layoutManagerLoker: RecyclerView.LayoutManager =
                                    LinearLayoutManager(activity)
                                rv_loker.layoutManager = layoutManagerLoker
                                lokerRecruiterAdapter = LokerRecruiterAdapter(loker)
                                rv_loker.adapter = lokerRecruiterAdapter

                                binding.tv1ListLoker.setText("Postingan Loker (" + loker.size + ")")
                            } else {

                                binding.tv1ListLoker.setText("Postingan Loker (0)")
                            }
                        } else {
                            binding.tv1ListLoker.setText("Postingan Loker (0)")
                        }

                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponseLoker>,
                    t: Throwable
                ) {
                    binding.tv1ListLoker.setText("Postingan Loker (0)")
                    // do something
//                swipe_loker.isRefreshing = false
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
                    swipe_akun.isRefreshing = false
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    recruiter = response.body()?.result_recruiter!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataAkun(recruiter)
                        } else {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.LoginRecruiterResponse>,
                    t: Throwable
                ) {
                    swipe_akun.isRefreshing = false
                    dialogProgress.dismiss()
                    Toast.makeText(
                        activity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
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
                        binding.tvBelumDiaturPerusahaan.visibility = View.GONE
                        binding.rlPerusahaan.visibility = View.VISIBLE
                        perusahaan = response.body()?.result_perusahaan!!
                        initPerusahaan(perusahaan)
                    } else {
                        binding.tvBelumDiaturPerusahaan.visibility = View.VISIBLE
                        binding.rlPerusahaan.visibility = View.GONE
                    }
                } else {
                    binding.tvBelumDiaturPerusahaan.visibility = View.VISIBLE
                    binding.rlPerusahaan.visibility = View.GONE
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePerusahaan>,
                t: Throwable
            ) {
                binding.tvBelumDiaturPerusahaan.visibility = View.VISIBLE
                binding.rlPerusahaan.visibility = View.GONE
            }

        })
    }

    private fun initPerusahaan(perusahaan: Perusahaan) {
        var nama_perusahaan = perusahaan.nama.toString()
        var industri = perusahaan.industri.toString()
        var lokasi_perusahaan = perusahaan.lokasi.toString()

        binding.tvNamaPerusahaan.text = nama_perusahaan
        binding.tvIndusreiPerusahaan.text = industri
        binding.tvLokasiPerusahaan.text = lokasi_perusahaan + ", Indonesia"

        var foto = perusahaan.foto
        if (foto !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/perusahaan/" + foto)
                .into(binding.imgPerusahaanRecruiter)
        } else {

        }

    }

    private fun initDataAkun(recruiter: Recruiter) {

        val nama = recruiter.nama.toString()
        var motto = recruiter.motto.toString()
        var lokasi = recruiter.lokasi.toString()

        binding.tvNamaRecruiter.text = nama
        binding.tvMottoRecruiter.text = motto
        binding.tvLokasi.text = lokasi + ", Indonesia"


        var foto = recruiter.foto
        if (foto !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                .into(binding.imgPhotoRecruiter)
        } else {

        }

        val sampul = recruiter.foto_sampul
        if (sampul !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/photo/" + sampul)
                .into(binding.imgHeaderRecruiter)
        } else {

        }


        var perusahaan_id = recruiter.id_perusahaan
        if (perusahaan_id == null || perusahaan_id.equals("")) {
            binding.tvBelumDiaturPerusahaan.visibility = View.VISIBLE
            binding.rlPerusahaan.visibility = View.GONE
        } else {
            binding.tvBelumDiaturPerusahaan.visibility = View.GONE
            binding.rlPerusahaan.visibility = View.VISIBLE
            loadPerusahaan(perusahaan_id)
        }

    }

    override fun onRefresh() {
        loadDataAkun(id)
        loadLokerRecruiter(id)
        loadLamaranBaru(id)
    }


}