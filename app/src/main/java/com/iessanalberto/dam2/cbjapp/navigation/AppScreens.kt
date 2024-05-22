package com.example.loginfactoriaproyectos.navigation

//Objetos posibles a navegar entre pestañas (todas las screens de la app)
sealed class AppScreens(val route: String){
    object LoginScreen: AppScreens (route = "login_screen")

    object AlbertoHomeScreen: AppScreens(route = "alberto_home_screen")
    object AlexHomeScreen: AppScreens(route = "alex_home_screen")
    object MarioHomeScreen: AppScreens(route = "mario_home_screen")
    object PasarListaScreen: AppScreens(route = "pasarList_screen")
}
