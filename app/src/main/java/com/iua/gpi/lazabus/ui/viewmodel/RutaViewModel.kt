package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.data.remote.model.Ruta
import com.iua.gpi.lazabus.data.remote.model.RutaOptima
import com.iua.gpi.lazabus.data.remote.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.await

class RutaViewModel : ViewModel() {

    private val _rutaOptima = MutableStateFlow<RutaOptima?>(null)
    val rutaOptima = _rutaOptima.asStateFlow()

    private val _rutas = MutableStateFlow<List<Ruta>>(emptyList())
    val rutas = _rutas.asStateFlow()


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
