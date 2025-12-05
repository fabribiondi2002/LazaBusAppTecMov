package com.iua.gpi.lazabus.data.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Archivo que contiene la instancia del API REST.
 * @param api representa la instancia del API REST.
 * @param BASE_URL representa la url base del servidor.
 */

object RetrofitClient {

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ApiRoutes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}