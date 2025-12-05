package com.iua.gpi.lazabus.service.interf


import android.location.Location
import kotlinx.coroutines.flow.Flow
/**
 * Archivo que contiene las interfaces para inyectar dependencias de Location.
 */
interface LocationServiceI {
    fun getLocationUpdates(): Flow<Location?>
}