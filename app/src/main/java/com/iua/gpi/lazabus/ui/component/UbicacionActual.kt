package com.iua.gpi.lazabus.ui.component

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Deprecated("Cambiado por ViewModel el 08/11/2025")
@SuppressLint("MissingPermission")
@Composable
fun UbicacionActual() {
    val context = LocalContext.current

    //permisos
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permisos ->
        val permitido = permisos[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permisos[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (permitido) {
            obtenerUbicacion(context) { location ->
                Log.i("UbicacionActual", "Latitud: ${location.latitude}, Longitud: ${location.longitude}")
            }
        } else {
            Log.w("UbicacionActual", "Permisos de ubicación denegados")
        }
    }

    LaunchedEffect(Unit) {
        val finePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarsePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (finePermission == PackageManager.PERMISSION_GRANTED || coarsePermission == PackageManager.PERMISSION_GRANTED) {
            obtenerUbicacion(context) { location ->
                Log.i("UbicacionActual", "Latitud: ${location.latitude}, Longitud: ${location.longitude}")
            }
        } else {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

/**
 * Función auxiliar que usa LocationManager (API nativa) para obtener la ubicación actual
 */
@SuppressLint("MissingPermission")
private fun obtenerUbicacion(context: Context, onLocationObtained: (Location) -> Unit) {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // obtiene la última ubicacion conocida
    val ultima = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        ?: locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

    if (ultima != null) {
        Log.i("UbicacionActual", "Última ubicación conocida → Lat: ${ultima.latitude}, Lon: ${ultima.longitude}")
        onLocationObtained(ultima)
        return
    }

    //Si no hay ultima ubicacion, escucha nuevas actualizaciones
    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.i("UbicacionActual", "Nueva ubicación → Lat: ${location.latitude}, Lon: ${location.longitude}")
            onLocationObtained(location)
            locationManager.removeUpdates(this)
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
        Log.i("UbicacionActual", "Escuchando actualizaciones del proveedor: $provider")
        locationManager.requestLocationUpdates(provider, 2000L, 0f, listener)
    } else {
        Log.w("UbicacionActual", "No hay proveedores de ubicación activos")
    }
}
