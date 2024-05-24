package com.iessanalberto.dam2.cbjapp.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

data class PasarListaScreenUiState(
    var fechaSeleccionada: MutableState<String> = mutableStateOf(LocalDate.now().toString()),
    var playerName: MutableState<String> = mutableStateOf(""),
    var playerApellidos: MutableState<String> = mutableStateOf(""),
    var comentarios: MutableState<String> = mutableStateOf(""),
)
