package com.iua.gpi.lazabus.service.interf


import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationServiceI {
    /**
     * Emite una secuencia de actualizaciones de la ubicación actual.
     * Ya no necesita el Context como argumento, lo obtiene del constructor.
     */
    fun getLocationUpdates(): Flow<Location?>
}