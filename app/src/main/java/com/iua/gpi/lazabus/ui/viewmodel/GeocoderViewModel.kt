package com.iua.gpi.lazabus.ui.viewmodel

import android.location.Address
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.service.interf.GeocodeServiceI
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.io.IOException


data class GeocoderState(
    val coordenadas: String = "Introduce una ubicación para buscar",
    val location : Address? = null,
    val isLoading: Boolean = false,
    val ultimaBusqueda: String? = null
)

private const val TAG = "GeocoderViewModel"

@HiltViewModel
class GeocoderViewModel @Inject constructor(
    // Hilt inyecta tu implementación de GeocodeServiceI
    private val geocodeService: GeocodeServiceI
) : ViewModel() {

    // Estado reactivo para la UI (Composable)
    private val _geocoderState = MutableStateFlow(GeocoderState())
    val geocoderState: StateFlow<GeocoderState> = _geocoderState

    /**
     * Busca las coordenadas para una ubicación dada y actualiza el estado.
     * * @param ubicacion El nombre del lugar a geocodificar.
     */
    fun buscarUbicacion(ubicacion: String) {
        if (ubicacion.isBlank()) return


        _geocoderState.update {
            it.copy(
                isLoading = true,
                coordenadas = "Buscando coordenadas para '$ubicacion'...",
                ultimaBusqueda = ubicacion
            )
        }

        // Lanza la operación asíncrona
        viewModelScope.launch {

            // Llama a tu función suspendida en el servicio
            val result = geocodeService.getCoordinatesForLocation(ubicacion)

            // Procesa el resultado
            _geocoderState.update { currentState ->
                result.fold(
                    onSuccess = { address ->
                        val direccionCompleta = address.getAddressLine(0) // <--- ESTA ES LA DIRECCIÓN REAL

                        val coords = formatAddress(address)
                        Log.i(TAG, "Ubicación encontrada: $direccionCompleta (Lat=${address.latitude}, Lon=${address.longitude})")

                        currentState.copy(
                            coordenadas = coords,
                            location = address,
                            isLoading = false
                        )
                    },
                    onFailure = { error ->
                        val mensajeError = when (error) {
                            is NoSuchElementException -> "No se encontraron resultados para '${currentState.ultimaBusqueda}'."
                            is IOException -> "Error de red: Servicio no disponible."
                            else -> "Error: ${error.localizedMessage}"
                        }
                        Log.e(TAG, "Fallo al geocodificar para ${currentState.ultimaBusqueda}: $mensajeError", error)
                        currentState.copy(
                            coordenadas = mensajeError,
                            location = null,
                            isLoading = false
                        )
                    }
                )

            }
        }
    }

    // Función de utilidad para formatear la Address
    private fun formatAddress(address: Address): String {
        val lat = String.format("%.6f", address.latitude)
        val lon = String.format("%.6f", address.longitude)
        return "Latitud: $lat, Longitud: $lon"
    }
}