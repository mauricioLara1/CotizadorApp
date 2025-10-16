package com.example.cotizadorapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cotizadorapp.model.CotizacionEntity


@Database(entities = [CotizacionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cotizacionDao(): CotizacionDao


    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null


        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cotizador_db"
                ).build()
                INSTANCE = inst
                inst
            }
    }
}