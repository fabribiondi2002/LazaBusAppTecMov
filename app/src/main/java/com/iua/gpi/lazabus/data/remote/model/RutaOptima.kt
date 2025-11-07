package com.iua.gpi.lazabus.data.remote.model

data class RutaOptima(

    val ruta: Ruta,
    val paradaOrigen: ParadaCompleta,
    val paradaDestino: ParadaCompleta,
    val distanciaOrigen: Double,
    val distanciaDestino: Double
)

data class ParadaCompleta(
    val id_parada: String,
    val nombre: String,
    val lon: Double,
    val lat: Double,
    val RutaParada: RutaParadaInfo
)

data class RutaParadaInfo(
    val orden: String
)
