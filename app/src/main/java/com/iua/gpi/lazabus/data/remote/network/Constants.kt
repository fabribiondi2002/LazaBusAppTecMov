package com.iua.gpi.lazabus.data.remote.network
/**
 * Archivo que contiene las rutas del API REST.
 * @param SERVER_URL representa la url del servidor.
 */
object ApiRoutes {
    //const val SERVER_URL = "http://10.0.2.2:3000"
    const val SERVER_URL = "http://192.168.0.221:3000"
    const val BASE_URL = "$SERVER_URL/"

    const val PARADA = "paradas"
    const val RUTA = "rutas"
}