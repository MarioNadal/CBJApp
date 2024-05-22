package com.iessanalberto.dam2.cbjapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import android.app.Application
import androidx.navigation.NavType
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.navArgument
import com.example.loginfactoriaproyectos.navigation.AppScreens
import com.example.loginfactoriaproyectos.viewmodels.LoginScreenUiState
import com.example.loginfactoriaproyectos.viewmodels.LoginScreenViewModel
import com.iessanalberto.dam2.cbjapp.screens.HomeScreen
import com.iessanalberto.dam2.cbjapp.screens.LoginScreen

import com.iessanalberto.dam2.cbjapp.screens.PasarListaScreen
import com.iessanalberto.dam2.cbjapp.viewmodels.PasarListaScreenViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // Este es el gestor de rutas para poder ir a la pantalla que queramos en cualquier momento
    NavHost(navController = navController, startDestination = AppScreens.LoginScreen.route) {
        // Esta es la pantalla del Login donde el usuario podrá verificarse para entrar a la aplicación
        composable(route = AppScreens.LoginScreen.route) {
            LoginScreen(
                navController,
                loginScreenViewModel = LoginScreenViewModel()
            )
        }
        composable(route = AppScreens.HomeScreen.route + "/{text}",
            arguments = listOf(navArgument(name = "text") {type= NavType.StringType})
        ){ it.arguments?.getString("text")?.let { it1 -> HomeScreen(navController, text = it1) } }
        composable(
            route = AppScreens.PasarListaScreen.route + "/{text}",
            arguments = listOf(navArgument(name = "text") { type = NavType.StringType })
        ) { backStackEntry ->
            val text = backStackEntry.arguments?.getString("text")
            val viewModel = rememberPasarListaScreenViewModel()
            PasarListaScreen(navController = navController, viewModel = viewModel, equipo = text ?: "")
        }
    }
}

@Composable
fun rememberPasarListaScreenViewModel(): PasarListaScreenViewModel {
    val application = LocalContext.current.applicationContext as Application
    return remember(application) {
        PasarListaScreenViewModel(application)
    }
}