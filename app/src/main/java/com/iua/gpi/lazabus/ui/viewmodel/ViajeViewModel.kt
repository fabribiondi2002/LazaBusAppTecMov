package com.iua.gpi.lazabus.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iua.gpi.lazabus.data.local.entity.ViajeEntity
import com.iua.gpi.lazabus.data.repository.ViajeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el historial de viajes en la base de datos.
 */
@HiltViewModel
class ViajeViewModel @Inject constructor(
    private val repository: ViajeRepository
) : ViewModel() {

    val historial = repository.historial.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun agregarViaje(
        ruta: String,
        descripcionRuta: String,
        origen: String,
        destino: String,
        paradaOrigen: String,
        paradaDestino: String
    ) {
        viewModelScope.launch {
            repository.guardarViaje(
                ViajeEntity(
                    ruta = ruta,
                    descripcionRuta = descripcionRuta,
                    origen = origen,
                    destino = destino,
                    paradaOrigen = paradaOrigen,
                    paradaDestino = paradaDestino
                )
            )
        }
    }

    fun limpiarHistorial() {
        viewModelScope.launch {
            repository.borrarHistorial()
        }
    }
}
