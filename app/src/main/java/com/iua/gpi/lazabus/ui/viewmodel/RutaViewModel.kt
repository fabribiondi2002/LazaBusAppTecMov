package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.data.remote.model.Ruta
import com.iua.gpi.lazabus.data.remote.model.RutaOptima
import com.iua.gpi.lazabus.data.remote.network.RetrofitClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import retrofit2.await
import javax.inject.Inject

/**
 * ViewModel para la ruta optima.
 */
@HiltViewModel
class RutaViewModel @Inject constructor() : ViewModel() {

    private val _rutaOptima = MutableStateFlow<RutaOptima?>(null)
    val rutaOptima = _rutaOptima.asStateFlow()

    val paradasGeoPoints: StateFlow<List<GeoPoint>> = _rutaOptima
        .map { rutaOptima ->
            rutaOptima?.ruta?.paradas?.map {
                GeoPoint(it.lat, it.lon)
            } ?: emptyList()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun limpiarRuta() {
        _rutaOptima.value = null
    }

    fun calcularRutaOptima(
        olat: Double,
        olng: Double,
        dlat: Double,
        dlng: Double,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitClient.api
                    .calcularRutaOptima(olat, olng, dlat, dlng)
                    .await()

                _rutaOptima.value = result
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun finalizarViaje() {
        limpiarRuta()
    }
}
