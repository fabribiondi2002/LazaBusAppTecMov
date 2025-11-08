package com.iua.gpi.lazabus.service.interf

import android.location.Address

interface GeocodeServiceI {
    suspend fun getCoordinatesForLocation(locationName: String): Result<Address>
}