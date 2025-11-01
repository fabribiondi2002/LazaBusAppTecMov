package com.iua.gpi.lazabus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iua.gpi.lazabus.ui.screen.MainScreen
import com.iua.gpi.lazabus.ui.screen.Route
import com.iua.gpi.lazabus.ui.screen.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SplashRoute.route // Usamos el 'route' del objeto
    ) {
        // Splash
        composable(Route.SplashRoute.route) {
            SplashScreen(onTimeout = {
                navController.navigate(Route.MainRoute.route)
            })
        }
        // Main
        composable(Route.MainRoute.route) { MainScreen() }
        /*
         Navegacion con parametros
        composable(
            route = Screen.Detail.route, // Usamos la ruta base con placeholder
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: "Error"
            DetailScreen(userId = userId)
        }**/
    }
}