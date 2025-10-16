package com.example.cotizadorapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cotizaciones")
data class CotizacionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cliente: String,
    val fecha: Long, // epoch millis
    val itemsJson: String, // JSON de List<Item>
    val total: Double,
    val pdfPath: String? = null // ruta local del PDF si ya se generó
)