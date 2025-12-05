package com.iua.gpi.lazabus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iua.gpi.lazabus.data.local.entity.ViajeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para el historial de viajes.
 */
@Dao
interface ViajeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarViaje(viaje: ViajeEntity)

    @Query("SELECT * FROM viajes ORDER BY fecha DESC")
    fun obtenerViajes(): Flow<List<ViajeEntity>>

    @Query("DELETE FROM viajes")
    suspend fun borrarViajes()
}