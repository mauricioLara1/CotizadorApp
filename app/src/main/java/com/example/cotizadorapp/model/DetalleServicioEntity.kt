package com.example.cotizadorapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "detalle_servicio",
    foreignKeys = [
        ForeignKey(
            entity = ServicioEntity::class,
            parentColumns = ["servicioId"],
            childColumns = ["servicioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["servicioId"])]
)
data class DetalleServicioEntity(
    @PrimaryKey(autoGenerate = true)
    val detalleId: Long = 0L,
    val servicioId: Long, // FK hacia Servicio
    val descripcion: String,
    val costo: Double
)
