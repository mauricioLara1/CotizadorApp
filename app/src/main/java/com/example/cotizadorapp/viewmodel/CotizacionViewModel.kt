package com.example.cotizadorapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cotizadorapp.data.db.AppDatabase
import com.example.cotizadorapp.data.repository.CotizacionRepository
import com.example.cotizadorapp.model.CotizacionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CotizacionViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: CotizacionRepository


    private val _cotizaciones = MutableStateFlow<List<CotizacionEntity>>(emptyList())
    val cotizaciones: StateFlow<List<CotizacionEntity>> = _cotizaciones


    init {
        val db = AppDatabase.getInstance(application)
        repo = CotizacionRepository(db.cotizacionDao())
        refresh()
    }


    fun refresh() = viewModelScope.launch {
        _cotizaciones.value = repo.getAll()
    }


    fun insert(c: CotizacionEntity, onComplete: (Long) -> Unit = {}) = viewModelScope.launch {
        val id = repo.insert(c)
        refresh()
        onComplete(id)
    }


    fun update(c: CotizacionEntity) = viewModelScope.launch {
        repo.update(c)
        refresh()
    }


    fun delete(c: CotizacionEntity) = viewModelScope.launch {
        repo.delete(c)
        refresh()
    }
}