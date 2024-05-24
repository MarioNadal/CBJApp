package com.example.loginfactoriaproyectos.viewmodels

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginScreenViewModel {
    private val _uiState = MutableStateFlow(LoginScreenUiState())
    val uiState: StateFlow<LoginScreenUiState> = _uiState.asStateFlow()

    fun onChanged(correoUi: String, passwordUi: String){
        _uiState.update {
                currentState -> currentState.copy(correo = correoUi, password = passwordUi)
        }
    }

    fun validarCorreoContrasena(): Int{
        // Verificar que la contraseña tenga al menos una mayúscula, una minúscula y un número
        val regexMayuscMinus = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+\$")
        //Campos en blanco
        if(_uiState.value.correo.isEmpty()||_uiState.value.password.isEmpty()){
            return 1
        }else if(!_uiState.value.correo.contains("@")){
            return 2
        }
        //Campos con longitud incorrecta
        else if(_uiState.value.password.length<9||_uiState.value.password.length>30){
            return 3
        }
        //Campo contraseña sin simbolo
        else if(!_uiState.value.password.contains("@") && !_uiState.value.password.contains("#") && !_uiState.value.password.contains("$") && !_uiState.value.password.contains("%") && !_uiState.value.password.contains("&")){
            return 4
        }
        //Contraseña con un formato invalido ya que no tiene como minimo una mayuscula, una minuscula y un número
        else if(!_uiState.value.password.matches(regexMayuscMinus)){
            return 5
        }
        else{
            return 6
        }
    }

}