package com.example.cotizadorapp.model

data class Item(
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double
) {
    val subtotal: Double get() = cantidad * precioUnitario
}