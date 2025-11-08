package com.iua.gpi.lazabus.data.remote.network

import com.iua.gpi.lazabus.data.remote.model.Parada
import com.iua.gpi.lazabus.data.remote.model.Ruta
import com.iua.gpi.lazabus.data.remote.model.RutaOptima
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET(ApiRoutes.PARADA)
    fun getParadas(): Call<List<Parada>>

    @GET("${ApiRoutes.PARADA}/{id}")
    fun getParadaPorId(
        @Path("id") id: Int
    ): Call<Parada>

    @GET("${ApiRoutes.RUTA}/calcular-ruta-optima")
    fun calcularRutaOptima(
        @Query("olat") olat: Double,
        @Query("olng") olng: Double,
        @Query("dlat") dlat: Double,
        @Query("dlng") dlng: Double
    ): Call<RutaOptima>
    @GET("${ApiRoutes.RUTA}/calcular-rutas")
    fun calcularRutas(
        @Query("olat") olat: Double,
        @Query("olng") olng: Double,
        @Query("dlat") dlat: Double,
        @Query("dlng") dlng: Double
    ): Call<List<Ruta>>


}
