package com.azwar.uinamfind.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.azwar.uinamfind.data.response.Responses
import com.azwar.uinamfind.database.server.ApiClient
import com.azwar.uinamfind.databinding.ActivityDaftarTamuBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarTamuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDaftarTamuBinding
    private var isEmailFiled = true
    private var isCBCheck = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDaftarTamuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llBtnKembaliDaftarTamu.setOnClickListener {
            finish()
        }

        binding.rlBtnSubmitDaftarTamu.setOnClickListener {
            clickSubmit()
        }

        binding.tvLoginDaftarTamu.setOnClickListener {
            finish()
        }

        binding.tieEmailDaftarTamu.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email: String = s.toString()
                val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                if (!isValid) {
                    binding.tilEmailDaftarTamu.isHelperTextEnabled = true
                    binding.tilEmailDaftarTamu.isErrorEnabled = true
                    binding.tilEmailDaftarTamu.error = "Perbaiki format email!"
                    isEmailFiled = true
                } else {
                    isEmailFiled = false
                    binding.tilEmailDaftarTamu.isErrorEnabled = false
                    checkEmail(email)
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        binding.cbDaftarTamu.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                isCBCheck = true
            } else {
                isCBCheck = false
            }
        }


    }

    private fun checkEmail(email: String) {

        ApiClient.instances.checkEmailRecruiter(email)
            ?.enqueue(object : Callback<Responses.ResponseRecruiter> {
                override fun onResponse(
                    call: Call<Responses.ResponseRecruiter>,
                    response: Response<Responses.ResponseRecruiter>
                ) {
                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode
                    if (response.isSuccessful) {
                        var recruiters = response.body()?.recruiter_data
                        if (recruiters != null) {
                            if (recruiters.size > 0) {
                                isEmailFiled = true
                                binding.tilEmailDaftarTamu.isHelperTextEnabled = true
                                binding.tilEmailDaftarTamu.isErrorEnabled = true
                                binding.tilEmailDaftarTamu.error = "Email sudah digunakan!"
                            } else {
                                isEmailFiled = false
                                binding.tilEmailDaftarTamu.isErrorEnabled = false
                            }
                        } else {
                            isEmailFiled = false
                            binding.tilEmailDaftarTamu.isErrorEnabled = false
                        }
                    } else {
                        isEmailFiled = false
                        binding.tilEmailDaftarTamu.isErrorEnabled = false
                    }


                }

                override fun onFailure(call: Call<Responses.ResponseRecruiter>, t: Throwable) {
                    isEmailFiled = false
                    binding.tilEmailDaftarTamu.isErrorEnabled = false
                }

            })


    }

    private fun clickSubmit() {

        val nama = binding.tieNamaDaftarTamu.text.toString()
        val email = binding.tieEmailDaftarTamu.text.toString()
        val password = binding.tiePasswordDaftarTamu.text.toString()
        val password_ulang = binding.tieUlangiPasswordDaftarTamu.text.toString()

        if (nama.isEmpty()) {
            binding.tilNamaDaftarTamu.error = "Lengkapi"
            binding.tilNamaDaftarTamu.focusable
        } else if (email.isEmpty()) {
            binding.tilNamaDaftarTamu.isErrorEnabled = false
            binding.tilEmailDaftarTamu.error = "Lengkapi"
            binding.tilEmailDaftarTamu.focusable
        } else if (isEmailFiled) {
            binding.tilEmailDaftarTamu.focusable
        } else if (password.isEmpty()) {
            binding.tilEmailDaftarTamu.isErrorEnabled = false
            binding.tilPasswordDaftarTamu.error = "Lengkapi"
            binding.tilPasswordDaftarTamu.focusable
        } else if (password_ulang.isEmpty()) {
            binding.tilPasswordDaftarTamu.isErrorEnabled = false
            binding.tilUlangiPasswordDaftarTamu.error = "Lengkapi"
            binding.tilUlangiPasswordDaftarTamu.focusable
        } else if (!password.equals(password_ulang)) {
            SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Uups..")
                .setContentText("Password yang diulang tidak sama!")
                .show()
            binding.tilUlangiPasswordDaftarTamu.focusable
        } else if (!isCBCheck) {
            binding.tilUlangiPasswordDaftarTamu.isErrorEnabled = false
            SweetAlertDialog(this@DaftarTamuActivity, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Uups..")
                .setContentText("Cheklis Privacy Policy terlebih dahulu!")
                .show()
        } else {
            setRegist(nama, email, password)
        }


    }

    private fun setRegist(nama: String, email: String, password: String) {

        // show progress loading
        val dialogProgress = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        dialogProgress.progressHelper.barColor = Color.parseColor("#A5DC86")
        dialogProgress.titleText = "Loading"
        dialogProgress.setCancelable(false)
        dialogProgress.show()

        ApiClient.instances.registRecruiter(nama, email, password)
            ?.enqueue(object : Callback<Responses.ResponseRecruiter> {
                override fun onResponse(
                    call: Call<Responses.ResponseRecruiter?>,
                    response: Response<Responses.ResponseRecruiter?>
                ) {
                    dialogProgress.dismiss()

                    val pesanRespon = response.message()
                    val message = response.body()?.pesan
                    val kode = response.body()?.kode

                    if (response.isSuccessful) {
                        if (kode.equals("1")) {
                            resetForm()
                            SweetAlertDialog(
                                this@DaftarTamuActivity,
                                SweetAlertDialog.SUCCESS_TYPE
                            )
                                .setTitleText("Success..")
                                .setContentText("Daftar Sebagai Recruiter Berhasil!")
                                .setConfirmButton(
                                    "Mulia Login"
                                ) { sweetAlertDialog ->
                                    sweetAlertDialog.dismiss()
                                    finish()
                                }
                                .show()
                        } else {

                            SweetAlertDialog(
                                this@DaftarTamuActivity,
                                SweetAlertDialog.ERROR_TYPE
                            )
                                .setTitleText("Uups..")
                                .setContentText(message)
                                .show()

                        }
                    } else {
                        SweetAlertDialog(this@DaftarTamuActivity, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Uups..")
                            .setContentText(pesanRespon)
                            .show()
                    }
                }

                override fun onFailure(
                    call: Call<Responses.ResponseRecruiter?>,
                    t: Throwable
                ) {
                    dialogProgress.dismiss()
                    SweetAlertDialog(this@DaftarTamuActivity, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Uups..")
                        .setContentText(t.localizedMessage)
                        .show()
                }

            })


    }

    private fun resetForm() {

        binding.tilNamaDaftarTamu.isErrorEnabled = false
        binding.tieNamaDaftarTamu.setText("")

//        binding.tilEmailDaftarTamu.isErrorEnabled = false
//        binding.tieEmailDaftarTamu.setText("")

        binding.tilPasswordDaftarTamu.isErrorEnabled = false
        binding.tiePasswordDaftarTamu.setText("")

        binding.tilUlangiPasswordDaftarTamu.isErrorEnabled = false
        binding.tieUlangiPasswordDaftarTamu.setText("")

        isEmailFiled = true

        binding.cbDaftarTamu.isChecked = false
        isCBCheck = false
    }
}