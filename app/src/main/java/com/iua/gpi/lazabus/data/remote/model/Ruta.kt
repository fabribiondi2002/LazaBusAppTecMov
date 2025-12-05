package com.iua.gpi.lazabus.data.remote.model

/**
 * Data class de la entidad Ruta para el API REST.
 * @param nombre representa el nombre o numero de la ruta
 * @param descripcion representa el nombre de la empresa que ofrece la ruta
 * @param paradas representa la lista de paradas de la ruta
 */
data class Ruta(
    val id_ruta: String,
    val nombre: String,
    val descripcion: String,
    val paradas: List<ParadaCompleta>
)