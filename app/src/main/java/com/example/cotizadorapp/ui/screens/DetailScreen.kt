package com.example.cotizadorapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun DetailScreen(navController: NavController, id: Long) {
    Text(text = "Detalle de cotización ID: $id")
}
