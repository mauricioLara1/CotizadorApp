package com.example.cotizadorapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cotizadorapp.ui.screens.*

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavRoutes.Home.route) {
        composable("home") { HomeScreen(navController) }
        composable("new") { NewQuoteScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("details") { DetailScreen(navController, 0L) }
        //composable(NavRoutes.Home.route) { HomeScreen(navController) }
        //composable(NavRoutes.NuevaCotizacion.route) { NewQuoteScreen(navController) }
        //composable(NavRoutes.Historial.route) { HistoryScreen(navController) }
        //composable(NavRoutes.Detalle.route) { backStackEntry ->
        //    val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
        //    DetailScreen(navController, id)
        //}
    }
}
