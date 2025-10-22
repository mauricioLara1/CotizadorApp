package com.example.cotizadorapp.data.db

import androidx.room.*

import com.example.cotizadorapp.model.DetalleServicioEntity

@Dao
interface DetalleServicioDao {
    @Query("SELECT * FROM detalle_servicio WHERE servicioId = :servicioId")
    suspend fun getByServicio(servicioId: Long): List<DetalleServicioEntity>

    @Insert
    suspend fun insert(detalle: DetalleServicioEntity): Long

    @Update
    suspend fun update(detalle: DetalleServicioEntity)

    @Delete
    suspend fun delete(detalle: DetalleServicioEntity)
}
