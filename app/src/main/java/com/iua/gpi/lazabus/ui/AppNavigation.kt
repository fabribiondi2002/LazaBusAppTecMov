package com.iua.gpi.lazabus.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iua.gpi.lazabus.ui.component.Greeting
import com.iua.gpi.lazabus.ui.screen.Route

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.GreetingRoute.route // Usamos el 'route' del objeto
    ) {
        composable(Route.GreetingRoute.route) { Greeting("LazaBus") }
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