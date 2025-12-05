package com.iua.gpi.lazabus.data.remote.model

/**
 * Data class de la entidad RutaOptima para el API REST.
 * Es la ruta optima entre el origen y el destino.
 * @param ruta representa la ruta optima.
 * @param paradaOrigen representa la parada mas cercana al origen.
 * @param paradaDestino representa la parada mas cercana al destino.
 * @param distanciaOrigen representa la distancia entre la parada mas cercana al origen y el origen.
 * @param distanciaDestino representa la distancia entre la parada mas cercana al destino y el destino.
 */
data class RutaOptima(

    val ruta: Ruta,
    val paradaOrigen: ParadaCompleta,
    val paradaDestino: ParadaCompleta,
    val distanciaOrigen: Double,
    val distanciaDestino: Double
)
/**
 * Data class de la entidad ParadaCompleta para el API REST.
 * Es la parada completa con la informacion de la ruta en la que se encuentra.
 * @param nombre representa el nombre de la parada.
 * @param lat representa la coordenada latitud de la parada
 * @param lon representa la coordenada longitud de la parada
 * @param RutaParadaInfo representa la informacion del orden de la parada en la ruta.
 */
data class ParadaCompleta(
    val id_parada: String,
    val nombre: String,
    val lon: Double,
    val lat: Double,
    val RutaParada: RutaParadaInfo
)

/**
 * Data class de la tabla de relacion entre parada y ruta
 * @param orden representa el orden de la parada en la ruta.
 */
data class RutaParadaInfo(
    val orden: String
)
