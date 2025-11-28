package com.iua.gpi.lazabus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "viajes")
data class ViajeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ruta: String,
    val descripcionRuta: String,
    val origen: String,
    val destino: String,
    val paradaOrigen: String,
    val paradaDestino: String,
    val fecha: Long = System.currentTimeMillis()
)