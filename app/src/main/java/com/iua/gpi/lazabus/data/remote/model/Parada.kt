package com.iua.gpi.lazabus.data.remote.model

/**
 * Data class de la entidad Parada para el API REST.
 * @param nombre representa al nombre de la parada
 * @param lat representa la coordenada latitud de la parada
 * @param lon representa la coordenada longitud de la parada
 * */
data class Parada(
    val id_parada: Int,
    val nombre: String,
    val lat: Double,
    val lon: Double
)