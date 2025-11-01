package com.iua.gpi.lazabus.ui.screen

sealed class Route(val route: String) {
    // Pantalla sin argumentos
    object SplashRoute : Route("splash")
    object GreetingRoute : Route("greeting_screen")
    object MainRoute : Route ("main_screen")

    /**
    // Pantalla con argumento obligatorio
    data class Detail(val userId: String) : Screen("detail_screen/{userId}") {
        // Función para construir la ruta final con el argumento
        fun createRoute(): String {
            return "detail_screen/$userId"
        }
    }

    // Pantalla con argumento opcional
    object Settings : Screen("settings_screen?showHelp={showHelp}") {
        fun createRoute(showHelp: Boolean = false): String {
            return "settings_screen?showHelp=$showHelp"
        }
    }
    */
}