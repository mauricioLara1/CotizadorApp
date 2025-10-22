package com.example.cotizadorapp.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotizadorapp.data.db.AppDatabase
import com.example.cotizadorapp.data.repository.ServicioRepository
import com.example.cotizadorapp.model.DetalleServicioEntity
import com.example.cotizadorapp.model.ServicioEntity
import com.example.cotizadorapp.model.PerfilEntity
import com.example.cotizadorapp.pdf.PdfGenerator
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ServicioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServicioRepository
    private val db: AppDatabase = AppDatabase.getInstance(application)
    private val context = application.applicationContext

    private val pdfGenerator = PdfGenerator()

    init {
        repository = ServicioRepository(db.servicioDao(), db.detalleServicioDao())

        // ✅ Crear perfil por defecto si no existe
        viewModelScope.launch(Dispatchers.IO) {
            val perfilDao = db.perfilDao()
            if (perfilDao.getAll().isEmpty()) {
                val perfilDefecto = PerfilEntity(
                    nombre = "Yon Mauricio Lara",
                    tel = "561-951-6824",
                    email = "jml.17lara@gmail.com",
                    clave = "1234"
                )
                perfilDao.insert(perfilDefecto)
            }
        }
    }

    /**
     * Guarda un servicio, sus detalles y genera el PDF
     */
    fun guardarServicioConDetalles(servicio: ServicioEntity, detalles: List<DetalleServicioEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            val perfilDao = db.perfilDao()
            val perfiles = perfilDao.getAll()
            val perfilPorDefecto = perfiles.firstOrNull()

            if (perfilPorDefecto != null) {
                // 🔹 Guardar servicio con ID de perfil
                val servicioConPerfil = servicio.copy(perfilId = perfilPorDefecto.perfilId)
                repository.insertarServicioConDetalles(servicioConPerfil, detalles)

                // 🔹 Generar el PDF después de guardar
                val archivo = pdfGenerator.generarPdf(context, perfilPorDefecto, servicioConPerfil, detalles)

                // 🔹 Notificar en la UI
                withContext(Dispatchers.Main) {
                    if (archivo != null) {
                        Toast.makeText(
                            context,
                            "PDF creado: ${archivo.absolutePath}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Error al generar PDF",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    suspend fun obtenerServiciosPorCliente(nombreCliente: String): List<ServicioEntity> {
        return withContext(Dispatchers.IO) {
            repository.getServiciosPorCliente(nombreCliente)
        }
    }

    suspend fun obtenerDetallesPorServicio(servicioId: Long): List<DetalleServicioEntity> {
        return withContext(Dispatchers.IO) {
            repository.getDetallesPorServicio(servicioId)
        }
    }

    suspend fun obtenerPerfil(): PerfilEntity? {
        return withContext(Dispatchers.IO) {
            db.perfilDao().getAll().firstOrNull()
        }
    }

}
