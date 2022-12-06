package com.azwar.uinamfind.ui.mahasiswa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.azwar.uinamfind.BuildConfig
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.Sosmed
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDetailMahasiswaBinding
import com.azwar.uinamfind.ui.ShowPhotoActivity
import com.azwar.uinamfind.ui.chat.RoomChatActivity
import com.azwar.uinamfind.ui.lembaga.adapter.SosmedLembagaAdapter
import com.azwar.uinamfind.ui.saya.DialogMoreFragment
import com.azwar.uinamfind.ui.saya.adapter.KeahlianMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.OrganisasiMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PendidikanMahasiswaAdapter
import com.azwar.uinamfind.ui.saya.adapter.PengalamanMahasiswaAdapter
import com.azwar.uinamfind.utils.Constanta
import com.azwar.uinamfind.utils.ui.DividerItemDecorator
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


class DetailMahasiswaActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityDetailMahasiswaBinding

    private lateinit var sharedPref: PreferencesHelper

    // adapter
    private lateinit var organisasiMahasiswaAdapter: OrganisasiMahasiswaAdapter
    private lateinit var pengalamanMahasiswaAdapter: PengalamanMahasiswaAdapter
    private lateinit var keahlianMahasiswaAdapter: KeahlianMahasiswaAdapter
    private lateinit var pendidikanMahasiswaAdapter: PendidikanMahasiswaAdapter
    private lateinit var sosmedLembagaAdapter: SosmedLembagaAdapter

    private lateinit var sosmedList: List<Sosmed>

    private lateinit var swipe_detail: SwipeRefreshLayout

    private lateinit var mahasiswa: User

    private var user_id: String = ""
    private var role: String = ""
    private var id: String = ""
    private var username: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMahasiswaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        role = sharedPref.getString(Constanta.ROLE).toString()


        mahasiswa = intent.getParcelableExtra("mahasiswa")!!

//        binding.cvCardDetailMahasiswa.setOnClickListener {
//            binding.cvCardDetailMahasiswa.setDrawingCacheEnabled(true)
//            val b: Bitmap = binding.cvCardDetailMahasiswa.getDrawingCache()
//            SaveImage(b)
//            b.compress(CompressFormat.JPEG, 100, FileOutputStream("/some/location/image.jpg"))
//        }

        binding.imgBaclLogoCard.alpha = 0.3f

        swipe_detail = binding.swipeDetail
        swipe_detail.setOnRefreshListener(this)
        swipe_detail.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_blue_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_green_dark
        )
        swipe_detail.post(Runnable {
            initData(mahasiswa)
        })

        binding.imgMoreDetailMahasiswa.setOnClickListener {
            showDialogMore()
        }

        binding.rlKirimPesanDetailMahasiswa.setOnClickListener {

            if (role.equals("user")) {
                if (id.equals(user_id)) {

                } else {
                    val intent_room_chat = Intent(this, RoomChatActivity::class.java)
                    intent_room_chat.putExtra("mahasiswa", mahasiswa)
                    startActivity(intent_room_chat)
                }
            } else {

            }

        }

        binding.imgBackDetailMahasiswa.setOnClickListener { finish() }


        binding.imgCloseLihatCv.setOnClickListener {
            binding.llLihatCvOnline.visibility = View.GONE
        }

        binding.llLihatCvOnline.setOnClickListener {

            val defaultBrowser = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_BROWSER
            )
            defaultBrowser.data = Uri.parse("https://uinamfind.com/$username")
            startActivity(defaultBrowser)
        }

        binding.imgPhotoCardDetailMahasiswa.setOnClickListener {
            var foto = mahasiswa.foto
            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }

        binding.imgHeaderCardDetailMahasiswa.setOnClickListener {
            var foto = mahasiswa.foto_sampul
            var foto_intent = BuildConfig.BASE_URL + "upload/photo/" + foto
            val intent = Intent(this, ShowPhotoActivity::class.java)
            intent.putExtra("foto", foto_intent)
            startActivity(intent)
        }

    }

    private fun initData(user: User) {

        user_id = user.id.toString()
        sharedPref.put(Constanta.USER_ID_DETAIL, user_id)

        val lable = user.status_kemahasiswaan
        if (lable.equals("Lulus")) {
            binding.imgLableMahasiswa.visibility = View.VISIBLE
        } else {
            binding.imgLableMahasiswa.visibility = View.GONE
        }

        var nim = user.nim

        // username
        var text_username = binding.tvToolbarDetailMahasiswa
        username = user.username.toString()
        if (username == null) {
            text_username.text = nim
        } else {
            text_username.text = username
        }

        // nama
        var nama_depan = user.nama_depan
        var nama_belakang = user.nama_belakang
        var text_nama_lengkap = binding.tvNamaCardDetailMahasiswa
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
        var tentang_user = user.tentang_user.toString()
        var text_tentang_saya = binding.tvTentangSayaDetailMahasiswa
        if (tentang_user.equals("-") || tentang_user.equals("null")) {
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
        var text_jurusan = binding.tvJurusanCardDetailMahasiswa
        var jurusan = user.jurusan
        var fakultas = user.fakultas
        text_jurusan.text = jurusan + ", " + fakultas

        // lokasi
        var text_lokasi = binding.tvLokasiCardDetailMahasiswa
        var lokasi = user.lokasi
        text_lokasi.text = lokasi

        val foto = user.foto
        if (foto !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/photo/" + foto)
                .into(binding.imgPhotoCardDetailMahasiswa)
        } else {

        }

        val sampul = user.foto_sampul
        if (sampul !== null) {
            Glide.with(this)
                .load(BuildConfig.BASE_URL + "/upload/photo/" + sampul)
                .into(binding.imgHeaderCardDetailMahasiswa)
        } else {

        }

        // motto
        loadMotto(user.id.toString())
        laodDataOrganisasi(user.id.toString())
        loadDataPengalaman(user.id.toString())
        loadDataKeahlian(user.id.toString())
        loadDataPendidikan(user.id.toString())
        setSosmed(user.id.toString(), "Mahasiswa")

    }

    private fun showDialogMore() {
        var dialogMoreMahasiswaFragment = DialogMoreMahasiswaFragment()
        dialogMoreMahasiswaFragment.show(this.getSupportFragmentManager(), "")
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
                    val rv_sosmed = binding.rvSosmed
                    rv_sosmed.layoutManager =
                        LinearLayoutManager(
                            this@DetailMahasiswaActivity,
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    sosmedLembagaAdapter = SosmedLembagaAdapter(sosmedList)
                    rv_sosmed.adapter = sosmedLembagaAdapter
                } else {

                }

            }

            override fun onFailure(call: Call<Responses.ResponseSosmed>, t: Throwable) {

            }

        })

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
                            val rv_pendidikan = binding.rvPendidikanDetailMahasiswa
                            rv_pendidikan.layoutManager =
                                LinearLayoutManager(this@DetailMahasiswaActivity)
                            pendidikanMahasiswaAdapter = PendidikanMahasiswaAdapter(data)

                            val dividerItemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@DetailMahasiswaActivity, R.drawable.divider
                                    )
                                )
                            rv_pendidikan.addItemDecoration(dividerItemDecoration)
                            rv_pendidikan.adapter = pendidikanMahasiswaAdapter
                        }
                    } else {
//                        Toast.makeText(this@DetailMahasiswaActivity, message, Toast.LENGTH_SHORT)
//                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePendidikanMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@DetailMahasiswaActivity,
                    "Server Tidak Merespon",
                    Toast.LENGTH_SHORT
                ).show()
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
                            val rv_keahlian = binding.rvKeahlianDetailMahasiswa
                            rv_keahlian.layoutManager =
                                LinearLayoutManager(
                                    this@DetailMahasiswaActivity,
                                    LinearLayoutManager.HORIZONTAL,
                                    false
                                )
                            keahlianMahasiswaAdapter = KeahlianMahasiswaAdapter(data)
//                            val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
//                                ContextCompat.getDrawable(
//                                    context!!, R.drawable.divider
//                                )
//                            )
//                            rv_keahlian.addItemDecoration(dividerItemDecoration)
                            rv_keahlian.adapter = keahlianMahasiswaAdapter
                        } else {
//                            Toast.makeText(
//                                this@DetailMahasiswaActivity,
//                                message,
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailMahasiswaActivity,
                            pesanRespon,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseKeahlianMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@DetailMahasiswaActivity,
                    "Server Tidak Merespon",
                    Toast.LENGTH_SHORT
                ).show()
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
                            val rv_pengalaman = binding.rvPengalamanDetailMahasiswa
                            rv_pengalaman.layoutManager =
                                LinearLayoutManager(this@DetailMahasiswaActivity)
                            pengalamanMahasiswaAdapter = PengalamanMahasiswaAdapter(data)

                            val dividerItemDecoration: RecyclerView.ItemDecoration =
                                DividerItemDecorator(
                                    ContextCompat.getDrawable(
                                        this@DetailMahasiswaActivity, R.drawable.divider
                                    )
                                )
                            rv_pengalaman.addItemDecoration(dividerItemDecoration)
                            rv_pengalaman.adapter = pengalamanMahasiswaAdapter
                        } else {

                        }
                    } else {
//                        Toast.makeText(this@DetailMahasiswaActivity, message, Toast.LENGTH_SHORT)
//                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@DetailMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponsePengalamanMahasiswa>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@DetailMahasiswaActivity,
                    "Server Tidak Merespon",
                    Toast.LENGTH_SHORT
                ).show()
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
                                val rv_org = binding.rvOrganisasiMahasiswaDetailMahasiswa
                                rv_org.layoutManager =
                                    LinearLayoutManager(this@DetailMahasiswaActivity)
                                organisasiMahasiswaAdapter = OrganisasiMahasiswaAdapter(data)

                                val dividerItemDecoration: RecyclerView.ItemDecoration =
                                    DividerItemDecorator(
                                        ContextCompat.getDrawable(
                                            this@DetailMahasiswaActivity, R.drawable.divider
                                        )
                                    )
                                rv_org.addItemDecoration(dividerItemDecoration)
                                rv_org.adapter = organisasiMahasiswaAdapter
                            } else {

                            }
                        } else {
//                            Toast.makeText(
//                                this@DetailMahasiswaActivity,
//                                message,
//                                Toast.LENGTH_SHORT
//                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DetailMahasiswaActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }

                override fun onFailure(
                    call: Call<Responses.ResponseOrganisasiMahasiswa>,
                    t: Throwable
                ) {
                    Toast.makeText(
                        this@DetailMahasiswaActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }

    private fun loadMotto(id: String) {
        ApiClient.instances.getMottoId(id)
            ?.enqueue(object : Callback<Responses.ResponseMotto> {
                override fun onResponse(
                    call: Call<Responses.ResponseMotto>,
                    response: Response<Responses.ResponseMotto>
                ) {
                    swipe_detail.isRefreshing = false
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    val motto = response.body()?.result_motto
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            val text_motto = binding.tvMottoCardDetailMahasiswa
                            val motto_ = motto?.motto_profesional
                            if (motto_ == null) {
                                text_motto.text = "  -"
                            } else {
                                text_motto.text = motto_
                            }
                        } else {
                            Toast.makeText(
                                this@DetailMahasiswaActivity,
                                "Gagal Load Motto",
                                Toast.LENGTH_SHORT
                            ).show()
                            val text_motto = binding.tvMottoCardDetailMahasiswa
                            text_motto.text = "  -"
                        }
                    } else {
//                        Toast.makeText(
//                            this@DetailMahasiswaActivity,
//                            "Gagal Load Motto",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        val text_motto = binding.tvMottoCardDetailMahasiswa
                        text_motto.text = "  -"
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMotto>, t: Throwable) {
                    swipe_detail.isRefreshing = false
//                    Toast.makeText(
//                        this@DetailMahasiswaActivity,
//                        "Gagal Load Motto",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    val text_motto = binding.tvMottoCardDetailMahasiswa
                    text_motto.text = "  -"
                }

            })
    }

    private fun SaveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val fname = "Image-$n.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            Toast.makeText(this, "sukses", Toast.LENGTH_SHORT)
            val out: FileOutputStream = FileOutputStream(file)
            finalBitmap.compress(CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            Toast.makeText(this, "gagal", Toast.LENGTH_SHORT)
            e.printStackTrace()
        }
    }

    override fun onRefresh() {
        initData(mahasiswa)
    }

}