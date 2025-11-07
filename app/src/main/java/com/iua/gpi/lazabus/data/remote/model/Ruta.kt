package com.iua.gpi.lazabus.data.remote.model

data class Ruta(
    val id_ruta: String,
    val nombre: String,
    val descripcion: String,
    val paradas: List<ParadaCompleta>
)