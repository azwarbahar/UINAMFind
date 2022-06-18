package com.azwar.uinamfind.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azwar.uinamfind.data.repository.LoginRepository

class LoginMahasiswaViewModel(
    val loginRepository: LoginRepository
) : ViewModel() {
    var nim = MutableLiveData<String>()
    var password = MutableLiveData<String>()




}