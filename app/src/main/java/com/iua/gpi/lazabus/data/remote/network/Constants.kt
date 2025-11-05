package com.iua.gpi.lazabus.data.remote.network

object ApiRoutes {
    const val SERVER_URL = "http://10.0.2.2:3000"

    private const val API = "/api"
    private const val API_VERSION = "/v1"
    const val BASE_URL = "$SERVER_URL$API$API_VERSION/"

    const val PARADA = "paradas"
    const val RUTA = "rutas"
    const val VIAJE = "viajes"
}