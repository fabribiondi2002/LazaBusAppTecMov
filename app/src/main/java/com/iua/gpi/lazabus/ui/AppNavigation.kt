package com.iua.gpi.lazabus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iua.gpi.lazabus.ui.screen.HistorialScreen
import com.iua.gpi.lazabus.ui.screen.MainScreen
import com.iua.gpi.lazabus.ui.screen.Route
import com.iua.gpi.lazabus.ui.screen.SplashScreen

/**
 * Navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SplashRoute.route
    ) {
        // Splash
        composable(Route.SplashRoute.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Route.MainRoute.route)
            })
        }
        // Main
        composable(Route.MainRoute.route) {
            MainScreen(
                onOpenHistorial = {
                    navController.navigate(Route.HistorialRoute.route)
                }
            )
        }
        // Historial
        composable(Route.HistorialRoute.route) {
            HistorialScreen(onBack = { navController.popBackStack() })
        }    }
}