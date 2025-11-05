package com.iua.gpi.lazabus.data.remote.network

import com.iua.gpi.lazabus.data.remote.service.ParadaService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitClient {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

//    val paradaService: ParadaService by lazy {
//        retrofit.create(ParadaService::class.java)
//    }
}