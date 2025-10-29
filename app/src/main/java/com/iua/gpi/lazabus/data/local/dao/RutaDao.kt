package com.iua.gpi.lazabus.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iua.gpi.lazabus.data.local.entity.RuntaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RutaDao {
    @Insert
    suspend fun insert(ruta: RuntaEntity)

    @Update
    suspend fun update(ruta: RuntaEntity)

    @Delete
    suspend fun delete(ruta: RuntaEntity)

    @Query("SELECT * FROM ruta ORDER BY nombre ASC")
    fun getAllItems(): Flow<List<RuntaEntity>> // Ejemplo con Flow para observar cambios
}