package com.example.cotizadorapp.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cotizadorapp.model.PerfilEntity
import com.example.cotizadorapp.model.ServicioEntity
import com.example.cotizadorapp.model.DetalleServicioEntity

/**
 * 🔹 AppDatabase es la clase principal de Room.
 * Aquí se definen:
 * - Las ENTIDADES (tablas)
 * - Los DAOs (interfaces de acceso a datos)
 * - La forma de construir la instancia única de la base de datos
 */
@Database(
    entities = [
        PerfilEntity::class,
        ServicioEntity::class,
        DetalleServicioEntity::class
    ],
    version = 1,
    exportSchema = false // evita errores si no usamos schema export
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 🔹 Definimos las funciones abstractas para cada DAO
     * Estas funciones le dicen a Room qué clases DAO están
     * asociadas a las tablas declaradas arriba.
     */
    abstract fun perfilDao(): PerfilDao
    abstract fun servicioDao(): ServicioDao
    abstract fun detalleServicioDao(): DetalleServicioDao

    /**
     * 🔹 Patrón Singleton (solo una instancia en toda la app)
     * Esto evita fugas de memoria y errores por tener múltiples conexiones.
     */
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * 🔹 Obtiene la instancia de la base de datos o la crea si no existe.
         * Usamos 'synchronized' para que solo un hilo a la vez la inicialice.
         */
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {

                // 🔹 Crea la base de datos física llamada "cotizador_db"
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cotizador_db"
                )
                    // Puedes activar .fallbackToDestructiveMigration() si cambias versiones
                    .build()

                // Guarda la instancia para uso global
                INSTANCE = instance
                instance
            }
    }
}
