package com.example.cotizadorapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "servicio",
    foreignKeys = [
        ForeignKey(
            entity = PerfilEntity::class,
            parentColumns = ["perfilId"],
            childColumns = ["perfilId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["perfilId"])]
)
data class ServicioEntity(
    @PrimaryKey(autoGenerate = true)
    val servicioId: Long = 0L,
    val perfilId: Long, // FK hacia Perfil
    val cliente: String,
    val descripcion: String,
    val costoTotal: Double,
    val pagado: Boolean = false
)
