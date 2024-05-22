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
}