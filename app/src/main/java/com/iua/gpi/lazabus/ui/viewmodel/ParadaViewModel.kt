package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.data.remote.model.Parada
import com.iua.gpi.lazabus.data.remote.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

class ParadaViewModel : ViewModel() {

    var paradas: List<Parada> = emptyList()
        private set

    fun cargarParadas(onResult: (List<Parada>) -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getParadas().await()
                paradas = response
                onResult(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }
    fun cargaParadaPorId(
        id: Int,
        onResult: (Parada) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getParadaPorId(id).await()
                onResult(response)
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

}
