package com.example.cotizadorapp.ui.navigation


sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object NuevaCotizacion : NavRoutes("nueva_cotizacion")
    object Detalle : NavRoutes("detalle/{id}") {
        fun createRoute(id: Int) = "detalle/$id"
    }
    object Historial : NavRoutes("historial")
}
