package com.azwar.uinamfind.ui.saya

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.R
import com.azwar.uinamfind.data.models.User
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.local.PreferencesHelper
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDataAkunBinding
import com.azwar.uinamfind.utils.Constanta
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataAkunActivity : AppCompatActivity() {

    private lateinit var sharedPref: PreferencesHelper
    private var id: String = ""

    private lateinit var binding: ActivityDataAkunBinding

    // models
    private lateinit var user: User
    private lateinit var dataUsers: List<User>
    var isUsernameUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataAkunBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferencesHelper(this)
        id = sharedPref.getString(Constanta.ID_USER).toString()
        loadDataSaya(id)

        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var value = s.toString()
                if (value.equals("")) {
                    onInputUsernameIsEmpty()
                } else {
                    loadUsername(value)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                var value = s.toString()
                if (value.equals("")) {
                    onInputUsernameIsEmpty()
                }
            }

        })

        binding.imgBack.setOnClickListener { finish() }

        binding.tvSimpan.setOnClickListener { clickSimpan() }


    }

    private fun clickSimpan() {

        val username = binding.etUsername.text
        val email = binding.etEmail.text
        val telpon = binding.etTelpon.text

        if (username.isEmpty()) {
            binding.etUsername.error = "Lengkapi"
            binding.etUsername.requestFocus()
        } else {
            if (isUsernameUpdate) {
                startUpdateData(username.toString(), email.toString(), telpon.toString())
            } else {
                SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Username")
                    .setContentText("Perbaiki username anda")
                    .show()
                binding.etUsername.requestFocus()
            }
        }
    }

    private fun startUpdateData(username: String, email: String, telpon: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading.."
        dialogProgress.setCancelable(false)
        dialogProgress.show()
        ApiClient.instances.updateAkunMahasiswa(
            id, username, email, telpon
        )?.enqueue(object :
            Callback<Responses.ResponseMahasiswa> {
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

                        SweetAlertDialog(
                            this@DataAkunActivity,
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
                            this@DataAkunActivity,
                            SweetAlertDialog.ERROR_TYPE
                        )
                            .setTitleText("Gagal..")
                            .setContentText(message)
                            .show()
                    }
                } else {
                    SweetAlertDialog(
                        this@DataAkunActivity,
                        SweetAlertDialog.ERROR_TYPE
                    )
                        .setTitleText("Gagal..")
                        .setContentText("Server tidak merespon")
                        .show()
                }
            }

            override fun onFailure(
                call: Call<Responses.ResponseMahasiswa>,
                t: Throwable
            ) {
                dialogProgress.dismiss()
                SweetAlertDialog(this@DataAkunActivity, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("Maaf..")
                    .setContentText("Terjadi Kesalahan Sistem")
                    .show()
            }

        })


    }

    private fun onUsernameLoad() {
        isUsernameUpdate = false
        binding.tvLoadUsername.setTextColor(ContextCompat.getColor(this, R.color.black));
        binding.tvLoadUsername.setText("Loading")
        binding.progressBarUsername.visibility = View.VISIBLE
    }

    private fun onUsernameIsReady() {
        isUsernameUpdate = false
        binding.tvLoadUsername.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tvLoadUsername.setText("Username sudah digunakan")
        binding.progressBarUsername.visibility = View.GONE
    }

    private fun onInputUsernameIsEmpty() {
        isUsernameUpdate = false
        binding.tvLoadUsername.setTextColor(ContextCompat.getColor(this, R.color.red));
        binding.tvLoadUsername.setText("Tidak Boleh Kosong")
        binding.progressBarUsername.visibility = View.GONE
    }

    private fun onUsernameIsEmpty() {
        isUsernameUpdate = true
        binding.tvLoadUsername.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.tvLoadUsername.setText("Username Cocok")
        binding.progressBarUsername.visibility = View.GONE
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
                            Toast.makeText(
                                this@DataAkunActivity,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@DataAkunActivity,
                            "Server Tidak Merespon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    Toast.makeText(
                        this@DataAkunActivity,
                        "Server Tidak Merespon",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })


    }

    private fun loadUsername(username: String) {
        onUsernameLoad()
        ApiClient.instances.getMahasiswaUsername(id, username)
            ?.enqueue(object : Callback<Responses.ResponseMahasiswa> {
                override fun onResponse(
                    call: Call<Responses.ResponseMahasiswa>,
                    response: Response<Responses.ResponseMahasiswa>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            dataUsers = response.body()?.mahasiswa_data!!
                            if (dataUsers.size < 1) {
                                onUsernameIsEmpty()
                            } else {
                                onUsernameIsReady()
                            }
                        } else {
                            onUsernameIsReady()
                        }
                    } else {
                        onUsernameIsReady()
                    }
                }

                override fun onFailure(call: Call<Responses.ResponseMahasiswa>, t: Throwable) {
                    onUsernameIsReady()
                }

            })


    }

    private fun initDataUser(user: User) {

        var userename = user.username.toString();
        var email = user.email.toString();
        var telpon = user.telpon.toString();
        if (userename.equals("") || userename.equals("null")) {

        } else {
            binding.etUsername.setText(user.username.toString())
        }

        if (email.equals("") || email.equals("null")) {

        } else {
            binding.etEmail.setText(user.email.toString())
        }
        if (telpon.equals("") || telpon.equals("null")) {

        } else {
            binding.etTelpon.setText(user.telpon.toString())
        }
    }

}