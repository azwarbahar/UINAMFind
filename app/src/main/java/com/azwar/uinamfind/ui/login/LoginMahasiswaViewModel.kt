package com.azwar.uinamfind.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.azwar.uinamfind.data.repository.LoginRepository
import com.azwar.uinamfind.utils.Resource

class LoginMahasiswaViewModel(
    val loginRepository: LoginRepository
) : ViewModel() {
    var nim = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    val login: MutableLiveData<Resource<LoginRepository>> = MutableLiveData()

//    fun getLogin() =



}