package com.iua.gpi.lazabus.data.remote.network

import com.iua.gpi.lazabus.data.remote.model.Parada
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET(ApiRoutes.PARADA)
    fun getParadas(): Call<List<Parada>>

    @GET("${ApiRoutes.PARADA}/{id}")
    fun getParadaPorId(
        @Path("id") id: Int
    ): Call<Parada>

}
