package com.example.cotizadorapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey(autoGenerate = true)
    val perfilId: Long = 0L,
    val nombre: String,
    val tel: String,
    val email: String,
    val clave: String
)
