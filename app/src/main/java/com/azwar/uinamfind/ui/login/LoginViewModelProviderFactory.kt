package com.azwar.uinamfind.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.azwar.uinamfind.data.repository.LoginRepository

class LoginViewModelProviderFactory(
    val loginRepository: LoginRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginMahasiswaViewModel(loginRepository) as T
    }
}