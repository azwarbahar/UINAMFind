package com.azwar.uinamfind.database.server

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class RemoteDataSource constructor(private val apiService: ApiService) {
    companion object {
        private var instance:RemoteDataSource? = null

        fun getInstance(apiService: ApiService): RemoteDataSource =
            instance ?: synchronized(this){
                RemoteDataSource(apiService).apply { instance = this }
            }
    }

//    fun getAllMahasiswa(): LiveData<ArrayList<Mahasiswa>>{
//        val resultData = MutableLiveData<ArrayList<Mahasiswa>>()
//
//
//
//        return resultData
//
//    }

}