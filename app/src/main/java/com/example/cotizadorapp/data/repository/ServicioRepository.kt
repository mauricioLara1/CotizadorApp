package com.example.cotizadorapp.data.repository

import com.example.cotizadorapp.data.db.DetalleServicioDao
import com.example.cotizadorapp.data.db.ServicioDao
import com.example.cotizadorapp.model.DetalleServicioEntity
import com.example.cotizadorapp.model.ServicioEntity

/**
 * El repositorio actúa como intermediario entre el ViewModel y la base de datos.
 * Aquí se implementa la lógica de negocio que combina los DAO.
 */
class ServicioRepository(
    private val servicioDao: ServicioDao,
    private val detalleDao: DetalleServicioDao
) {

    /**
     * Inserta un servicio y luego todos sus detalles asociados.
     */
    suspend fun insertarServicioConDetalles(servicio: ServicioEntity, detalles: List<DetalleServicioEntity>) {
        val servicioId = servicioDao.insert(servicio)
        detalles.forEach { detalle ->
            detalleDao.insert(detalle.copy(servicioId = servicioId))
        }
    }

    /**
     * Devuelve todos los servicios cuyo nombre de cliente coincida (parcialmente).
     */
    suspend fun getServiciosPorCliente(nombreCliente: String): List<ServicioEntity> {
        return servicioDao.getByCliente(nombreCliente)
    }

    /**
     * Devuelve los detalles asociados a un servicio específico.
     */
    suspend fun getDetallesPorServicio(servicioId: Long): List<DetalleServicioEntity> {
        return detalleDao.getByServicio(servicioId)
    }
}
