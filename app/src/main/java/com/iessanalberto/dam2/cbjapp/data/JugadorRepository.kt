package com.iessanalberto.dam2.cbjapp.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import java.util.Date

class JugadorRepository(private val jugadorDao: JugadorDao) {

    fun getJugadores(): Flow<List<Jugador>> {
        return jugadorDao.getJugadores()
    }

    fun getJugadoresByGrupo(equipo: String): Flow<List<Jugador>> {
        return jugadorDao.getJugadoresByEquipo(equipo)
    }

    suspend fun insertarJugador(jugador: Jugador) {
        jugadorDao.insertJugador(jugador)
    }

    suspend fun deleteJugador(jugador: Jugador){
        jugadorDao.deleteJugador(jugador)
    }


    suspend fun updateJugador(jugador: Jugador) {
        jugadorDao.updateJugador(jugador)
    }

    fun getJugadorById(id: Int): Flow<Jugador?> {
        return jugadorDao.getJugadorById(id)
    }

    suspend fun insertAsistencia(asistencia: Asistencia) {
        jugadorDao.insertAsistencia(asistencia)
    }

    suspend fun updateAsistencia(asistencia: Asistencia) {
        jugadorDao.updateAsistencia(asistencia)
    }

    suspend fun getAsistencia(jugadorId: Int, fecha: Date): Asistencia? {
        return jugadorDao.getAsistencia(jugadorId, fecha)
    }

    suspend fun getAsistencias(jugadorId: Int): List<Asistencia> {
        return jugadorDao.getAsistencias(jugadorId)
    }


}