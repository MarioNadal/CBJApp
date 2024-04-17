package com.iessanalberto.dam2.cbjapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginfactoriaproyectos.navigation.AppScreens
import com.iessanalberto.dam2.cbjapp.R



@Composable
fun HomeScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(painter = painterResource(id = R.drawable.logocbj), contentDescription = "Logo CBJ")
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Infantil Femenino")}) {
            Text(text = "Infantil Femenino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Infantil Masculino")}) {
            Text(text = "Infantil Masculino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Cadete Femenino")}) {
            Text(text = "Cadete Femenino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Cadete Masculino")}) {
            Text(text = "Cadete Masculino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Junior Femenino")}) {
            Text(text = "Junior Femenino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Junior Masculino")}) {
            Text(text = "Junior Masculino")
        }
        Button(onClick = {navController.navigate(AppScreens.PasarListaScreen.route + "/" + "Senior Masculino")}) {
            Text(text = "Senior Masculino")
        }
    }
}
