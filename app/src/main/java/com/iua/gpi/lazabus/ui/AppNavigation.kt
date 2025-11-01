package com.iua.gpi.lazabus.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iua.gpi.lazabus.ui.component.Greeting
import com.iua.gpi.lazabus.ui.screen.Route
import com.iua.gpi.lazabus.ui.screen.SplashScreen
import kotlinx.coroutines.delay

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
                navController.navigate(Route.GreetingRoute.route) {
                    popUpTo(Route.SplashRoute.route) { inclusive = true }
                    launchSingleTop = true
                }
            })
        }

        composable(Route.GreetingRoute.route)
        {
            Greeting("LazaBus")
        }
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