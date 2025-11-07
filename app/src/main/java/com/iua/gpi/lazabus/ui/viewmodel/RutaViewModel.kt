package com.iua.gpi.lazabus.ui.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.data.remote.model.Parada
import com.iua.gpi.lazabus.data.remote.model.Ruta
import com.iua.gpi.lazabus.data.remote.model.RutaOptima
import com.iua.gpi.lazabus.data.remote.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

class RutaViewModel : ViewModel() {


    var rutaOptima: RutaOptima? = null
        private set
    var rutas: List<Ruta> = emptyList()
        private set
    /**
     * Calcular ruta óptima
     */
    fun calcularRutaOptima(
        olat: Double,
        olng: Double,
        dlat: Double,
        dlng: Double,
        onResult: (RutaOptima) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitClient.api
                    .calcularRutaOptima(olat, olng, dlat, dlng)
                    .await()

                rutaOptima = result
                onResult(result)

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
        onResult: (List<Ruta>) -> Unit, onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = RetrofitClient.api
                    .calcularRutas(olat, olng, dlat, dlng)
                    .await()

                rutas = result
                onResult(result)

            } catch (e: Exception) {
                onError(e)
            }
        }
    }
}
