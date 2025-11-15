package com.iua.gpi.lazabus.ui.viewmodel

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.service.interf.LocationServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.core.content.ContextCompat
import android.app.Application
import org.osmdroid.util.GeoPoint

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationService: LocationServiceI,
    private val app: Application
) : ViewModel() {

    private val _currentLocation = MutableStateFlow<android.location.Location?>(null)
    val currentLocation: StateFlow<android.location.Location?> = _currentLocation.asStateFlow()
    private val _destinoGeoPoint = MutableStateFlow<GeoPoint?>(null)
    val destinoGeoPoint: StateFlow<GeoPoint?> = _destinoGeoPoint
    private val _origenGeoPoint = MutableStateFlow<GeoPoint?>(null)
    val origenGeoPoint: StateFlow<GeoPoint?> = _origenGeoPoint


    fun setOrigen(lat: Double, lon: Double) {
        _origenGeoPoint.value = GeoPoint(lat, lon)
    }

    fun setDestino(lat: Double, lon: Double) {
        _destinoGeoPoint.value = GeoPoint(lat, lon)
    }

    init {
        // Arranca solamente si realmente hay permisos
        if (hasLocationPermission()) {
            collectLocationUpdates()
        }
    }


    fun onPermissionGranted() {
        collectLocationUpdates()
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            app, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val coarse = ContextCompat.checkSelfPermission(
            app, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fine || coarse
    }

    private fun collectLocationUpdates() {
        viewModelScope.launch {

            locationService.getLocationUpdates().collect { location ->
                _currentLocation.value = location
            }

        }
    }
}
