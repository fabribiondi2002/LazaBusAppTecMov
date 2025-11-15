package com.iua.gpi.lazabus.service

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import com.iua.gpi.lazabus.service.interf.LocationServiceI
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Singleton

@Singleton
class LocationService (private val context: Context
) : LocationServiceI {

    private val TAG = "LocationService"

    @SuppressLint("MissingPermission")
    override fun getLocationUpdates(): Flow<Location?> = callbackFlow {

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 🔥 CHECK DE PERMISOS ANTES DE TOCAR EL LOCATION MANAGER
        val fine = android.content.pm.PackageManager.PERMISSION_GRANTED ==
                androidx.core.content.ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_FINE_LOCATION
                )

        val coarse = android.content.pm.PackageManager.PERMISSION_GRANTED ==
                androidx.core.content.ContextCompat.checkSelfPermission(
                    context, android.Manifest.permission.ACCESS_COARSE_LOCATION
                )

        if (!fine && !coarse) {
            Log.e(TAG, "No hay permisos de ubicación → deteniendo flujo")
            trySend(null)
            close()     // Cierra el flow para evitar más llamadas
            return@callbackFlow
        }

        // Última ubicación conocida
        val ultima = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (ultima != null) trySend(ultima)

        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                trySend(location)
            }
        }

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ->
                LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ->
                LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (provider != null) {
            locationManager.requestLocationUpdates(provider, 5000L, 0f, listener)
        } else {
            trySend(null)
        }

        awaitClose {
            locationManager.removeUpdates(listener)
        }
    }.distinctUntilChanged()

}