package com.example.cotizadorapp.data.db

import androidx.room.*
import com.example.cotizadorapp.model.CotizacionEntity


@Dao
interface CotizacionDao {
    @Query("SELECT * FROM cotizaciones ORDER BY fecha DESC")
    suspend fun getAll(): List<CotizacionEntity>


    @Insert
    suspend fun insert(c: CotizacionEntity): Long


    @Update
    suspend fun update(c: CotizacionEntity)


    @Delete
    suspend fun delete(c: CotizacionEntity)


    @Query("SELECT * FROM cotizaciones WHERE id = :id")
    suspend fun getById(id: Long): CotizacionEntity?
}