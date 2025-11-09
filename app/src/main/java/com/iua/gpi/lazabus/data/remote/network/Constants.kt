package com.iua.gpi.lazabus.data.remote.network

object ApiRoutes {
    const val SERVER_URL = "http://192.168.0.101:3000"

    private const val API = "/api"
    private const val API_VERSION = "/v1"
    const val BASE_URL = "$SERVER_URL/"

    const val PARADA = "paradas"
    const val RUTA = "rutas"
    const val VIAJE = "viajes"
}