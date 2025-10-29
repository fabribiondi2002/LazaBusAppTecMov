package com.iua.gpi.lazabus.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ruta")
data class RuntaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val nombre : String,
    val descripcion: String
)