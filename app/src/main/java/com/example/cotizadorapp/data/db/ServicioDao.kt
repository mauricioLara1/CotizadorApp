package com.example.cotizadorapp.data.db

import com.example.cotizadorapp.model.PerfilEntity
import com.example.cotizadorapp.model.ServicioEntity
import androidx.room.*

@Dao
interface ServicioDao {
    @Query("SELECT * FROM servicio")
    suspend fun getAll(): List<ServicioEntity>
    @Query("SELECT * FROM servicio WHERE perfilId = :perfilId")
    suspend fun getByPerfil(perfilId: Long): List<ServicioEntity>
    @Query("SELECT * FROM servicio WHERE cliente LIKE '%' || :nombreCliente || '%'")
    suspend fun getByCliente(nombreCliente: String): List<ServicioEntity>
    @Insert
    suspend fun insert(servicio: ServicioEntity): Long
    @Update
    suspend fun update(servicio: ServicioEntity)
    @Delete
    suspend fun delete(servicio: ServicioEntity)
}
