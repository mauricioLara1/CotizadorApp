package com.example.cotizadorapp.data.repository


import com.example.cotizadorapp.data.db.CotizacionDao
import com.example.cotizadorapp.model.CotizacionEntity


class CotizacionRepository(private val dao: CotizacionDao) {
    suspend fun getAll() = dao.getAll()
    suspend fun getById(id: Long) = dao.getById(id)
    suspend fun insert(c: CotizacionEntity) = dao.insert(c)
    suspend fun update(c: CotizacionEntity) = dao.update(c)
    suspend fun delete(c: CotizacionEntity) = dao.delete(c)
}