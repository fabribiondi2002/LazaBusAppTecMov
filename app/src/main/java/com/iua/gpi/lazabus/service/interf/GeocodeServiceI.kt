package com.iua.gpi.lazabus.service.interf

import android.location.Address

/**
 * Archivo que contiene las interfaces para inyectar dependencias de geocoder.
 */
interface GeocodeServiceI {
    suspend fun getCoordinatesForLocation(locationName: String): Result<Address>
}