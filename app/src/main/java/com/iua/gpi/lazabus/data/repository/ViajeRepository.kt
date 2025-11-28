package com.iua.gpi.lazabus.data.repository

import com.iua.gpi.lazabus.data.local.dao.ViajeDao
import com.iua.gpi.lazabus.data.local.entity.ViajeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ViajeRepository @Inject constructor(private val viajeDao: ViajeDao) {

    val historial: Flow<List<ViajeEntity>> = viajeDao.obtenerViajes()

    suspend fun guardarViaje(viaje: ViajeEntity) {
        viajeDao.insertarViaje(viaje)
    }

    suspend fun borrarHistorial() {
        viajeDao.borrarViajes()
    }
}
