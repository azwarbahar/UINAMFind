package com.azwar.uinamfind.ui.saya

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.FragmentSayaBinding
import com.azwar.uinamfind.ui.ShowPhotoActivity
import com.azwar.uinamfind.ui.lembaga.adapter.SosmedLembagaAdapter
import com.azwar.uinamfind.ui.saya.adapter.KeahlianMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PendidikanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PengalamanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.keahlian.AddKeahlianMahasiswaActivity
import com.azwar.uinamfind.ui.saya.keahlian.ListKeahlianMahasiswaActivity
import com.azwar.uinamfind.ui.saya.organisasi.AddOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.organisasi.ListOrganisasiMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pendidikan.AddPendidikanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pendidikan.ListPendidikanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.AddPengalamanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.pengalaman.ListPengalamanMahasiswaActivity
import com.azwar.uinamfind.ui.saya.tentang.EditTentangMahasiswaActivity
import com.azwar.uinamfind.ui.sosmed.AddSosmedMahasiswaActivity
import com.azwar.uinamfind.ui.sosmed.ListSosmedMahasiswaActivity
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SayaFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var sharedPref: PreferencesHelper
    private var _sayaBinding: FragmentSayaBinding? = null
    private val sayaBinding get() = _sayaBinding!!

    private lateinit var swipe_saya: SwipeRefreshLayout

    private val PROFILE_IMAGE_REQ_CODE = 101
    private val HEADER_IMAGE_REQ_CODE = 102

    // models
    private lateinit var user: User

    private lateinit var sosmedList: List<Sosmed>

    // adapter
    private lateinit var organisasiMahasiswaAdapter: OrganisasiMahasiswaAdapter
    private lateinit var pengalamanMahasiswaAdapter: PengalamanMahasiswaAdapter
    private lateinit var keahlianMahasiswaAdapter: KeahlianMahasiswaAdapter
    private lateinit var pendidikanMahasiswaAdapter: PendidikanMahasiswaAdapter
    private lateinit var sosmedLembagaAdapter: SosmedLembagaAdapter

    private var id: String = ""
    private var tentang_user: String = ""
    private var username: String = ""

    private lateinit var dialogProgress: SweetAlertDialog

    private lateinit var bottomSheetBehaviorMenu: BottomSheetBehavior<RelativeLayout>

    companion object {
        fun newInstance() = SayaFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _sayaBinding = FragmentSayaBinding.inflate(inflater, container, false)
        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        // Bottom Sheet Menu
        bottomSheetBehaviorMenu =
            BottomSheetBehavior.from(sayaBinding.continerBottomSheetMenuSaya)

        sayaBinding.imgLableMahasiswa.visibility = View.GONE

        sayaBinding.rlEditProfilDetailMahasiswa.setOnClickListener {
            val intent_edit_profil = Intent(context, EditProfilMahasiswaActivity::class.java)
            startActivity(intent_edit_profil)
        }

        //Sosmed
        sayaBinding.imgAddSosmed.setOnClickListener {
            val intent = Intent(context, AddSosmedMahasiswaActivity::class.java)
            intent.putExtra("kategori", "Mahasiswa")
            startActivity(intent)
        }
        sayaBinding.imgEditSosmed.setOnClickListener {
            val intent = Intent(context, ListSosmedMahasiswaActivity::class.java)
            intent.putExtra("kategori", "Mahasiswa")
            startActivity(intent)
        }

        //organisasi
        sayaBinding.imgAddOrganisasiSaya.setOnClickListener {
            val intent = Intent(context, AddOrganisasiMahasiswaActivity::class.java)
            startActivity(intent)
        }
        sayaBinding.imgEditOrganisasiSaya.setOnClickListener {
            val intent = Intent(context, ListOrganisasiMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Pengalaman
        sayaBinding.imgAddPengalamanSaya.setOnClickListener {
            val intent = Intent(context, AddPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        sayaBinding.imgEditPengalamanSaya.setOnClickListener {
            val intent = Intent(context, ListPengalamanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Keahlian
        sayaBinding.imgAddKeahlianSaya.setOnClickListener {
            val intent = Intent(context, AddKeahlianMahasiswaActivity::class.java)
            startActivity(intent)
        }

        sayaBinding.imgEditKeahlianSaya.setOnClickListener {
            val intent = Intent(context, ListKeahlianMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Pendidikan
        sayaBinding.imgAddPendidikanSaya.setOnClickListener {
            val intent = Intent(context, AddPendidikanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        sayaBinding.imgEditPendidikanSaya.setOnClickListener {
            val intent = Intent(context, ListPendidikanMahasiswaActivity::class.java)
            startActivity(intent)
        }

        // Tetnag
        sayaBinding.imgEditTentangSaya.setOnClickListener {
            val intent = Intent(context, EditTentangMahasiswaActivity::class.java)
            intent.putExtra("tentang", tentang_user)
            startActivity(intent)
        }


        // logo back trans in card
        val img_back_logo_card = sayaBinding.imgBaclLogoCard
        img_back_logo_card.alpha = 0.3f

        // show progress loading

        dialogProgress = SweetAlertDialog(activity, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        swipe_saya = sayaBinding.swipeSaya
        swipe_saya.setOnRefreshListener(this)
        swipe_saya.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_saya.post(Runnable {
            loadData()
        })

        sayaBinding.imgMoreSaya.setOnClickListener {
            showDialogMore()
        }

        sayaBinding.imgPhotoCardDetailMahasiswa.setOnClickListener {
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

        sayaBinding.imgHeaderCardDetailMahasiswa.setOnClickListener {
            val pilihan = arrayOf(
                "Ganti Gambar",
                "Lihat Gambar"
            )
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Opsi")
            builder.setItems(pilihan) { dialog, which ->
                when (which) {
                    0 -> openImagePicker(HEADER_IMAGE_REQ_CODE)
                    1 -> openPreviewImage("Header")
                }
            }
            val dialog = builder.create()
            dialog.show()
        }

        sayaBinding.imgCloseLihatCv.setOnClickListener {
            sayaBinding.llLihatCvOnline.visibility = View.GONE
        }

        sayaBinding.llLihatCvOnline.setOnClickListener {

            val defaultBrowser = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_BROWSER
            )
            defaultBrowser.data = Uri.parse("https://uinamfind.com/$username")
            startActivity(defaultBrowser)
        }

        return sayaBinding.root
    }

    private fun setSosmed(id: String?, kategori: String) {

        ApiClient.instances.getSosmedKategori(id, kategori)?.enqueue(object :
            Callback<Responses.ResponseSosmed> {
            override fun onResponse(
                call: Call<Responses.ResponseSosmed>,
                response: Response<Responses.ResponseSosmed>
            ) {
                if (response.isSuccessful) {
                    sosmedList = response.body()?.sosmed_data!!
                    val rv_sosmed = sayaBinding.rvSosmed
                    rv_sosmed.layoutManager =
                        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    sosmedLembagaAdapter = SosmedLembagaAdapter(sosmedList)
                    rv_sosmed.adapter = sosmedLembagaAdapter
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseSosmed>, t: Throwable) {

            }

        })

    }

    private fun openPreviewImage(s: String) {

        var foto = user.foto
        var header = user.foto_sampul
        if (s.equals("Profil")) {
            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto

            val intent = Intent(context, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        } else {
            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + header

            val intent = Intent(context, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }

    }

    private fun openImagePicker(s: Int) {
        if (s == HEADER_IMAGE_REQ_CODE) {
            ImagePicker.with(this)
                .galleryOnly()
                .crop()
                .compress(2048)
                .maxResultSize(1000, 1000)
                .start(HEADER_IMAGE_REQ_CODE)
        } else {
            ImagePicker.with(this)
                .galleryOnly()
                .cropSquare()
                .compress(2048)
                .maxResultSize(1000, 1000)
                .start(PROFILE_IMAGE_REQ_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val uri: Uri = data?.data!!
            if (requestCode == PROFILE_IMAGE_REQ_CODE) {
                sayaBinding.imgPhotoCardDetailMahasiswa.setImageURI(null)
                sayaBinding.imgPhotoCardDetailMahasiswa.setImageURI(uri)
                startUploadPhoto("Profil", uri)
            } else if (requestCode == HEADER_IMAGE_REQ_CODE) {
                startUploadPhoto("Header", uri)
                sayaBinding.imgHeaderCardDetailMahasiswa.setImageURI(null)
                sayaBinding.imgHeaderCardDetailMahasiswa.setImageURI(uri)
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

        ApiClient.instances.updatePhoto(keterangan, user_id, foto_send)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
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
                            loadDataSaya(id)
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

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    Toast.makeText(activity, t.message, Toast.LENGTH_SHORT)
                        .show()

                    dialogProgress.dismiss()
                }

            })

    }

    private fun showDialogMore() {
        var dialogMoreFragment = DialogMoreFragment()
        dialogMoreFragment.show(getParentFragmentManager(), "")
    }

    private fun loadData() {
        loadDataSaya(id)
        laodDataOrganisasi(id)
        loadDataPengalaman(id)
        loadDataKeahlian(id)
        loadDataPendidikan(id)
        setSosmed(id, "Mahasiswa")
    }

    private fun loadDataPendidikan(id: String) {
        ApiClient.instances.getPendidikanUser(id)?.enqueue(object :
            Callback<Responses.ResponsePendidikanMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponsePendidikanMahasiswa>,
                response: Response<Responses.ResponsePendidikanMahasiswa>
            ) {
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                val data = response.body()?.pendidikan_data
                if (response.isSuccessful) {
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            val rv_pendidikan = sayaBinding.rvPendidikanDetailMahasiswa
                            rv_pendidikan.layoutManager = LinearLayoutManager(activity)
                            pendidikanMahasiswaAdapter = PendidikanMahasiswaAdapter(data)

                            val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    context!!, R.drawable.divider
                                )
                            )
                            rv_pendidikan.addItemDecoration(dividerItemDecoration)
                            rv_pendidikan.adapter = pendidikanMahasiswaAdapter
                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePendidikanMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDataKeahlian(id: String) {
        ApiClient.instances.getKeahlianUser(id)?.enqueue(object :
            Callback<Responses.ResponseKeahlianMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                response: Response<Responses.ResponseKeahlianMahasiswa>
            ) {
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                val data = response.body()?.keahlian_data
                if (response.isSuccessful) {
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            val rv_keahlian = sayaBinding.rvKeahlianDetailMahasiswa
                            rv_keahlian.layoutManager =
                                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                            keahlianMahasiswaAdapter = KeahlianMahasiswaAdapter(data)
//                            val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
//                                ContextCompat.getDrawable(
//                                    context!!, R.drawable.divider
//                                )
//                            )
//                            rv_keahlian.addItemDecoration(dividerItemDecoration)
                            rv_keahlian.adapter = keahlianMahasiswaAdapter
                        } else {
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDataPengalaman(id: String) {
        ApiClient.instances.getPengalamanUser(id)?.enqueue(object :
            Callback<Responses.ResponsePengalamanMahasiswa> {
            override fun onResponse(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                response: Response<Responses.ResponsePengalamanMahasiswa>
            ) {
                val pesanRespon = response.message()
                val message = response.body()?.pesan
                val kode = response.body()?.kode
                val data = response.body()?.pengalaman_data
                if (response.isSuccessful) {
                    if (kode.equals("1")) {
                        if (data!!.size > 0) {
                            val rv_pengalaman = sayaBinding.rvPengalamanDetailMahasiswa
                            rv_pengalaman.layoutManager = LinearLayoutManager(activity)
                            pengalamanMahasiswaAdapter = PengalamanMahasiswaAdapter(data)

                            val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
                                ContextCompat.getDrawable(
                                    context!!, R.drawable.divider
                                )
                            )
                            rv_pengalaman.addItemDecoration(dividerItemDecoration)
                            rv_pengalaman.adapter = pengalamanMahasiswaAdapter
                        } else {

                        }
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun laodDataOrganisasi(id: String) {

        ApiClient.instances.getOrganisasiUser(id)
            ?.enqueue(object : Callback<Responses.ResponseOrganisasiMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    response: Response<Responses.ResponseOrganisasiMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val data = response.body()?.organisasi_data

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            if (data!!.size > 0) {
                                val rv_org = sayaBinding.rvOrganisasiMahasiswaDetailMahasiswa
                                rv_org.layoutManager = LinearLayoutManager(activity)
                                organisasiMahasiswaAdapter = OrganisasiMahasiswaAdapter(data)

                                val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        context!!, R.drawable.divider
                                    )
                                )
                                rv_org.addItemDecoration(dividerItemDecoration)
                                rv_org.adapter = organisasiMahasiswaAdapter
                            } else {

                            }
                        } else {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    t: Throwable
                ) {
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun loadDataSaya(id: String) {
        ApiClient.instances.getMahasiswaID(id)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    swipe_saya.isRefreshing = false
                    dialogProgress.dismiss()
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    user = response.body()?.result_mahasiswa!!
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            initDataUser(user)
                        } else {
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    dialogProgress.dismiss()
                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun initDataUser(user: User) {
        val lable = user.status_kemahasiswaan
        if (lable.equals("Lulus")) {
            sayaBinding.imgLableMahasiswa.visibility = View.VISIBLE
        } else {
            sayaBinding.imgLableMahasiswa.visibility = View.GONE
        }

        var nim = user.nim

        // username
        var text_username = sayaBinding.tvToolbarSaya
        username = user.username.toString()
        if (username == null) {
            text_username.text = nim
        } else {
            text_username.text = username
        }

        // nama
        var nama_depan = user.nama_depan
        var nama_belakang = user.nama_belakang
        var text_nama_lengkap = sayaBinding.tvNamaCardDetailMahasiswa
        if (nama_depan == null) {
            text_nama_lengkap.text = nim
        } else if (nama_belakang == null) {
            var nama_lengkap = user.nama_depan
            text_nama_lengkap.text = nama_lengkap
        } else {
            var nama_lengkap = user.nama_depan + " " + user.nama_belakang
            text_nama_lengkap.text = nama_lengkap
        }

        // tentang saya
        tentang_user = user.tentang_user.toString()
        var text_tentang_saya = sayaBinding.tvTentangSayaDetailMahasiswa
        if (tentang_user.isEmpty() || tentang_user.equals("null")) {
            text_tentang_saya.setTypeface(text_tentang_saya.getTypeface(), Typeface.ITALIC)
            text_tentang_saya.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                resources.getDimension(R.dimen.dimens_fint_12)
            )
            text_tentang_saya.setTextColor(Color.parseColor("#ABABAB"))
            text_tentang_saya.text = "Belum diatur"
        } else {
            text_tentang_saya.text = tentang_user
        }

        // jurusan
        var text_jurusan = sayaBinding.tvJurusanCardDetailMahasiswa
        var jurusan = user.jurusan
        var fakultas = user.fakultas
        text_jurusan.text = jurusan + ", " + fakultas

        // lokasi
        var text_lokasi = sayaBinding.tvLokasiCardDetailMahasiswa
        var lokasi = user.lokasi
        text_lokasi.text = lokasi

        var foto = user.foto
        if (foto !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + foto)
                .into(sayaBinding.imgPhotoCardDetailMahasiswa)
        } else {

        }

        val sampul = user.foto_sampul
        if (sampul !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "upload/photo/" + sampul)
                .into(sayaBinding.imgHeaderCardDetailMahasiswa)
        } else {

        }

        // motto
        loadMotto(id)

    }

    private fun loadMotto(id: String) {
        ApiClient.instances.getMottoId(id)
            ?.enqueue(object : Callback<Responses.ResponseMotto> {
                override fun onResponse(
                    call: Call<Responses.ResponseMotto>,
                    response: Response<Responses.ResponseMotto>
                ) {
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val motto = response.body()?.result_motto
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                            val motto_ = motto?.motto_profesional
                            if (motto_ == null) {
                                text_motto.text = "  -"
                            } else {
                                text_motto.text = motto_
                            }
                        } else {
//                            Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                            val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                            text_motto.text = "  -"
                        }
                    } else {
//                        Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                        val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                        text_motto.text = "  -"
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
//                    Toast.makeText(activity, "Gagal Load Motto", Toast.LENGTH_SHORT).show()
                    val text_motto = sayaBinding.tvMottoCardDetailMahasiswa
                    text_motto.text = "  -"
                }

            })
    }

    override fun onRefresh() {
        loadData()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

}