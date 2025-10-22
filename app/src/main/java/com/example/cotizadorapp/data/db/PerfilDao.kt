package com.example.cotizadorapp.data.db

import androidx.room.*
import com.example.cotizadorapp.model.PerfilEntity


@Dao
interface PerfilDao {
    @Query("SELECT * FROM perfil")
    suspend fun getAll(): List<PerfilEntity>

    @Insert
    suspend fun insert(perfil: PerfilEntity): Long

    @Update
    suspend fun update(perfil: PerfilEntity)

    @Delete
    suspend fun delete(perfil: PerfilEntity)
}
