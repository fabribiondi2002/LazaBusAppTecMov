package com.iua.gpi.lazabus.ui.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.service.interf.LocationServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationService: LocationServiceI // Se inyecta la interfaz del servicio
) : ViewModel() {

    // StateFlow para exponer el estado de la ubicación a la UI
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    init {
        //Iniciar la recolección del Flow de ubicaciones al crear el ViewModel
        collectLocationUpdates()
    }

    private fun collectLocationUpdates() {
        // Ejecuta la recolección en el ámbito del ViewModel,
        // esto asegura que se detiene cuando el ViewModel se limpia (onCleared)
        viewModelScope.launch {
            // Llama al servicio y comienza a escuchar el Flow
            locationService.getLocationUpdates()
                // El valor emitido por el servicio (Location?) se asigna al StateFlow
                .collect { location ->
                    _currentLocation.value = location
                }
        }
    }

    /* El método onCleared() se llama automáticamente y,
    gracias a viewModelScope.launch, detiene la recolección del Flow.
     Al detenerse la recolección, se ejecuta el 'awaitClose' en el LocationService,
    y este a su vez llama a locationManager.removeUpdates(listener),
    deteniendo el consumo de batería. */
}