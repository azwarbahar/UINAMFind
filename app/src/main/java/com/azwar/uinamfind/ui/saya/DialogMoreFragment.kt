package com.azwar.uinamfind.ui.saya

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.widget.Toast
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.utils.Constanta
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_bottom_sheet_menu_saya.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class DialogMoreFragment() : BottomSheetDialogFragment() {

    private lateinit var sharedPref: PreferencesHelper

    // models
    private lateinit var user: User
    private var id: String = ""
    private var username: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_bottom_sheet_menu_saya, container, false)

        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.ID_USER).toString()
//        val cv_card_mahasiswa = view.cv_card_mahasiswa
        val pengaturan = view.ll_pengaturan_dialog
        val edit = view.ll_edit_profil_dialog
//        val unduh_card = view.ll_simpan_card_dialog
        val bagikan = view.ll_share_profile_dialog

//        onChooseReasonListener = (parentFragment as OnChooseReasonListener?)!!

        pengaturan.setOnClickListener {
            val intent = Intent(context, PengaturanActivity::class.java)
            startActivity(intent)
        }

        edit.setOnClickListener {
            val intent_edit_profil = Intent(context, EditProfilMahasiswaActivity::class.java)
            startActivity(intent_edit_profil)
        }

//        unduh_card.setOnClickListener {
//            try {
//                val output = FileOutputStream(
//                    Environment.getExternalStorageDirectory().toString() + "/path/to/file.png"
//                )
//                viewToBitmap(cv_card_mahasiswa)?.compress(
//                    Bitmap.CompressFormat.PNG,
//                    100,
//                    output
//                )
//                Toast.makeText(activity, "Pesan : Simpan", Toast.LENGTH_SHORT).show()
//                output.close()
//            } catch (e: FileNotFoundException) {
//                Toast.makeText(activity, "Pesan1 : " + e.printStackTrace(), Toast.LENGTH_SHORT)
//                    .show()
//                e.printStackTrace()
//            } catch (e: IOException) {
//                Toast.makeText(activity, "Pesan : " + e.printStackTrace(), Toast.LENGTH_SHORT)
//                    .show()
//                e.printStackTrace()
//            }
//        }

        bagikan.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT, "Hi, Cek profil saya di UINAM Find dengan Link :\n" +
                        "https://uinamfind.com/" + username
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
        }
        loadDataSaya(id)
        return view
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
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
//                        Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
//                    Toast.makeText(activity, "Server Tidak Merespon", Toast.LENGTH_SHORT).show()
                }

            })


    }

    private fun initDataUser(user: User) {
        var nim = user.nim
        username = user.username.toString()
        if (username == null) {
            if (nim != null) {
                username = nim
            }
        }
    }

    fun viewToBitmap(v: View): Bitmap? {
        if (v.getMeasuredHeight() <= 0) {
            val specWidth = MeasureSpec.makeMeasureSpec(0 /* any */, MeasureSpec.UNSPECIFIED)
            v!!.measure(specWidth, specWidth)
            val questionWidth = v!!.measuredWidth
            val questionHeight = v!!.measuredHeight
            val b = Bitmap.createBitmap(
                questionWidth,
                questionHeight,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            v.layout(0, 0, questionWidth, questionHeight)
            v.draw(c)
            return b
        } else {
            val b = Bitmap.createBitmap(
                v.layoutParams.width,
                v.layoutParams.height,
                Bitmap.Config.ARGB_8888
            )
            val c = Canvas(b)
            v.layout(v.left, v.top, v.right, v.bottom)
            v.draw(c)
            return b
        }

//        val specWidth = MeasureSpec.makeMeasureSpec(0 /* any */, MeasureSpec.UNSPECIFIED)
//        view.measure(specWidth, specWidth)
//        val questionWidth = view.measuredWidth
//        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        view.draw(canvas)
//        return bitmap
    }
//    fun loadBitmapFromView(v: View): Bitmap? {
//        if (v.measuredHeight <= 0) {
//            val specWidth = MeasureSpec.makeMeasureSpec(0 /* any */, MeasureSpec.UNSPECIFIED)
//            v.measure(specWidth, specWidth)
//            val questionWidth = v.measuredWidth
//            val b = Bitmap.createBitmap(v.measuredWidth, v.measuredHeight, Bitmap.Config.ARGB_8888)
//            val c = Canvas(b)
//            v.layout(0, 0, v.measuredWidth, v.measuredHeight)
//            v.draw(c)
//            return b
//        }
//    }


//        val b = Bitmap.createBitmap(
//            v.layoutParams.width,
//            v.layoutParams.height,
//            Bitmap.Config.ARGB_8888
//        )
//        val c = Canvas(b)
//        v.layout(v.left, v.top, v.right, v.bottom)
//        v.draw(c)
//        return b


}