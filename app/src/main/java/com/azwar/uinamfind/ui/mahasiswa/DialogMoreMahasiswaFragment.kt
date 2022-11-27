package com.azwar.uinamfind.ui.mahasiswa

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.azwar.uinamfind.BuildConfig
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

class DialogMoreMahasiswaFragment(): BottomSheetDialogFragment() {

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
        val view = inflater.inflate(R.layout.dialog_bottom_sheet_menu_mahasiswa, container, false)

        sharedPref = PreferencesHelper(context)
        id = sharedPref.getString(Constanta.USER_ID_DETAIL).toString()

        val cv_online = view.ll_cv_online
        val bagikan = view.ll_share_profile_dialog

        bagikan.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT, "Hi, Cek profil "+ username +" di UINAM Find dengan Link :\n" +
                        "https://uinamfind.com/" + username + "\n\n\nDownload Aplikasi UINAMFind : http://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "/"
            )
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, null))
        }

        cv_online.setOnClickListener {
            val defaultBrowser = Intent.makeMainSelectorActivity(
                Intent.ACTION_MAIN,
                Intent.CATEGORY_APP_BROWSER
            )
            defaultBrowser.data = Uri.parse("https://uinamfind.com/$username")
            startActivity(defaultBrowser)
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


}