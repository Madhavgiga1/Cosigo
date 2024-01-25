package com.example.cosigo.data

import com.example.cosigo.data.network.OpenAppApi
import com.example.cosigo.models.Basic
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val openAppApi: OpenAppApi){

    suspend fun getData():Response<Basic>{
        return openAppApi.getData()
    }
}