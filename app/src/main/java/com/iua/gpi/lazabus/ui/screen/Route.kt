package com.iua.gpi.lazabus.ui.screen

/**
 * Clase sellada que define las rutas de navegación de la aplicación.
 */
sealed class Route(val route: String) {
    object SplashRoute : Route("splash")
    object MainRoute : Route ("main_screen")
    object HistorialRoute : Route("historial")
}