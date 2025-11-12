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

        // Intenta obtener la última ubicación conocida al iniciar el Flow
        val ultima = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if (ultima != null) {
            Log.i(TAG, "Última ubicación conocida → Lat: ${ultima.latitude} Lon: ${ultima.longitude}")
            trySend(ultima)
        }

        // Escucha nuevas actualizaciones
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
               // Log.i(TAG, "Nueva ubicación → Lat: ${location.latitude} Lon: ${location.longitude}")
                trySend(location) // Envía la nueva ubicación al Flow
            }
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        }

        val provider = when {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            else -> null
        }

        if (provider != null) {
            Log.i(TAG, "Escuchando actualizaciones del proveedor: $provider")
            // Solicita actualizaciones: cada 5 segundos (5000L) o 0 metros de distancia
            locationManager.requestLocationUpdates(provider, 5000L, 0f, listener)
        } else {
            Log.w(TAG, "No hay proveedores de ubicación activos")
            trySend(null) // Envía null si no hay proveedores
        }

        // Cleanup: Esto se ejecuta cuando el Flow deja de ser observado (ej. ViewModel se limpia)
        awaitClose {
            Log.d(TAG, "Deteniendo actualizaciones de ubicación")
            locationManager.removeUpdates(listener)
        }
    }.distinctUntilChanged()
}