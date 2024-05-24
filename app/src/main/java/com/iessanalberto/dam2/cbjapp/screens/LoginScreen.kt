package com.iessanalberto.dam2.cbjapp.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.loginfactoriaproyectos.navigation.AppScreens
import com.example.loginfactoriaproyectos.viewmodels.LoginScreenViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.iessanalberto.dam2.cbjapp.R

@Composable
fun LoginScreen(navController: NavController, loginScreenViewModel: LoginScreenViewModel){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
            LoginScreenBodyContent(navController, loginScreenViewModel)
    }
}

@Composable
fun LoginScreenBodyContent(navController: NavController, loginScreenViewModel: LoginScreenViewModel) {
    val loginScreenuiState by loginScreenViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var passwordVisible by remember { mutableStateOf(false) }


    val auth: FirebaseAuth = Firebase.auth

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Image(painter = painterResource(id = R.drawable.logocbj),
            contentDescription = "Logo Punto violeta", modifier = Modifier.size(150.dp))
        Spacer(modifier = Modifier.height(50.dp))
        OutlinedTextField(value = loginScreenuiState.correo,
            onValueChange = {loginScreenViewModel.onChanged(correoUi = it, passwordUi = loginScreenuiState.password)},
            label = { Text(text = "Correo")})
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = loginScreenuiState.password, 
            onValueChange = {loginScreenViewModel.onChanged(correoUi = loginScreenuiState.correo, passwordUi = it)},
            label = { Text(text = "Contraseña")},
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(R.drawable.ojocontrasena),
                        contentDescription = "Toggle password visibility"
                    )
                }
            }
            )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            if(loginScreenViewModel.validarCorreoContrasena() == 1) {
                Toast.makeText(context, "Ambos campos deben estar rellenados", Toast.LENGTH_SHORT).show()
            }else if(loginScreenViewModel.validarCorreoContrasena() == 2) {
                Toast.makeText(context, "El correo debe contener un @", Toast.LENGTH_SHORT).show()
            }else if(loginScreenViewModel.validarCorreoContrasena() == 3){
                Toast.makeText(context,"La contraseña debe tener entre 9 y 30 carácteres", Toast.LENGTH_SHORT).show()
            }else if(loginScreenViewModel.validarCorreoContrasena() == 4){
                Toast.makeText(context, "La contraseña no contiene un simbolo (@,#,$,% o &)", Toast.LENGTH_SHORT).show()            }else if(loginScreenViewModel.validarCorreoContrasena() == 5){
            }else if(loginScreenViewModel.validarCorreoContrasena() == 5){
                Toast.makeText(context, "La contraseña debe tener una mayuscula, una minuscula y un número", Toast.LENGTH_SHORT).show()            }
            else {
                auth.signInWithEmailAndPassword(
                    loginScreenuiState.correo,
                    loginScreenuiState.password
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navController.navigate(AppScreens.HomeScreen.route + "/" + loginScreenuiState.correo)
                        Toast.makeText(context, "Conectado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No ha ido bien", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }) {
            Text(text = "Acceder")
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}
