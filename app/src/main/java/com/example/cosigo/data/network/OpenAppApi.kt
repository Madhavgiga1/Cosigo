package com.example.cosigo.data.network

import com.example.cosigo.models.Basic
import retrofit2.Response
import retrofit2.http.GET

interface OpenAppApi {
    @GET("dashboardNew")
    suspend fun getData():Response<Basic>
}