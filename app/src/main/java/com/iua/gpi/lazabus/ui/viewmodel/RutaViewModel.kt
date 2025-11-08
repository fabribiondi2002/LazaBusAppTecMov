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

@HiltViewModel
class RutaViewModel @Inject constructor() : ViewModel() {

    private val _rutaOptima = MutableStateFlow<RutaOptima?>(null)
    val rutaOptima = _rutaOptima.asStateFlow()

    private val _rutas = MutableStateFlow<List<Ruta>>(emptyList())
    val rutas = _rutas.asStateFlow()

    val paradasGeoPoints: StateFlow<List<GeoPoint>> = _rutaOptima
        // Mapeamos el Flow de RutaOptima? a List<GeoPoint>
        .map { rutaOptima ->
            // Si la ruta óptima existe, extraemos la lista de paradas de la ruta anidada.
            rutaOptima?.ruta?.paradas
                // Mapeamos cada ParadaCompleta a un objeto GeoPoint
                ?.map { parada ->
                    // Usar (latitud, longitud) según la convención de GeoPoint
                    GeoPoint(parada.lat, parada.lon)
                }
            // Si la ruta o las paradas son null, devolvemos una lista vacía
                ?: emptyList()
        }
        // 2. Convertimos el Flow resultante en un StateFlow, resolviendo la inferencia de tipo 'R'.
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // El valor inicial es una lista vacía de GeoPoint
        )


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


    fun calcularRutas(
        olat: Double,
        olng: Double,
        dlat: Double,
        dlng: Double,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitClient.api
                    .calcularRutas(olat, olng, dlat, dlng)
                    .await()

                _rutas.value = result
            } catch (e: Exception) {
                onError(e)
            }
        }
    }


    fun getRutaOptima(): RutaOptima? = rutaOptima.value

    fun finalizarViaje() {
        _rutaOptima.value = null
        _rutas.value = emptyList()
    }
}
