package com.iessanalberto.dam2.cbjapp.viewmodels

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.iessanalberto.dam2.cbjapp.data.Asistencia
import com.iessanalberto.dam2.cbjapp.data.Database
import com.iessanalberto.dam2.cbjapp.data.Jugador
import com.iessanalberto.dam2.cbjapp.data.JugadorRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class PasarListaScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(PasarListaScreenUiState())
    val uiState: StateFlow<PasarListaScreenUiState> = _uiState.asStateFlow()

    private val jugadorRepository: JugadorRepository

    fun onChanged(fechaSeleccionadaUi: MutableState<String>, playerNameUi: MutableState<String>, playerApellidosUi: MutableState<String>){
        _uiState.update {
                currentState -> currentState.copy(fechaSeleccionada = fechaSeleccionadaUi, playerNameUi, playerApellidosUi )
        }
    }

    fun onChangeComentario(comentarioUi: MutableState<String>){
        _uiState.update {
                currentState -> currentState.copy(comentarios = comentarioUi)
        }
    }

    fun anadirJugador(): Int{
        //Campos en blanco
        if(_uiState.value.playerName.value.isEmpty() || _uiState.value.playerApellidos.value.isEmpty()){
            return 1
        }
        else{
            return 2
        }
    }

    init {
        val jugadorDao = Database.getDatabase(application).jugadorDao()
        jugadorRepository = JugadorRepository(jugadorDao)
    }

    fun getJugadores(): Flow<List<Jugador>> {
        return jugadorRepository.getJugadores()
    }

    fun getJugadoresByGrupo(equipo: String): Flow<List<Jugador>> {
        return jugadorRepository.getJugadoresByGrupo(equipo)
    }


    fun insertJugador(jugador: Jugador, fecha: Date) {
        viewModelScope.launch {
            jugadorRepository.insertarJugador(jugador)
        }
    }

    fun deleteJugador(jugador: Jugador){
        viewModelScope.launch {
            jugadorRepository.deleteJugador(jugador)
        }
    }

    suspend fun getAsistencias(jugadorId: Int): List<Asistencia> {
        // Lógica para obtener las asistencias del repositorio
        return jugadorRepository.getAsistencias(jugadorId)
    }

     suspend fun getAsistenciaPorFecha(jugadorId: Int, fecha: Date): Asistencia?{
        return jugadorRepository.getAsistencia(jugadorId,fecha)
    }

    suspend fun togglePlayerPresence(jugador: Jugador, comentario: String) {
        val jugadorId = jugador.id
        val fecha = Date() // Supongamos que la fecha es la fecha actual

        // Consulta si ya existe una asistencia para esta fecha y jugador
        val asistenciaExistente = jugadorRepository.getAsistencia(jugadorId, fecha)

        if (asistenciaExistente != null) {
            // Si existe, actualiza el estado de asistencia
            jugadorRepository.updateAsistencia(
                Asistencia(
                    id = asistenciaExistente.id,
                    fecha = fecha,
                    presente = !asistenciaExistente.presente,
                    jugadorId = jugadorId
                )
            )
        } else {
            // Si no existe, crea una nueva asistencia
            jugadorRepository.insertAsistencia(
                Asistencia(
                    fecha = fecha,
                    presente = true,
                    jugadorId = jugadorId
                )
            )
        }
    }

    fun registrarAsistencia(jugadorId: Int, fecha: Date, presente: Boolean, comentario: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val asistencia = jugadorRepository.getAsistencia(jugadorId, fecha)
            if (asistencia == null) {
                jugadorRepository.insertAsistencia(Asistencia(fecha = fecha, presente = presente, jugadorId = jugadorId))
            } else {
                jugadorRepository.updateAsistencia(asistencia.copy(presente = presente))
            }
        }
    }

    suspend fun insertAsistencia(jugadorId: Int, fecha: Date, presente: Boolean, comentario: String = "") {
        jugadorRepository.insertAsistencia(Asistencia(fecha = fecha, presente = presente, jugadorId = jugadorId))
    }

}