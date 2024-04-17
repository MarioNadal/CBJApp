package com.iessanalberto.dam2.cbjapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface JugadorDao {
    @Query("SELECT * FROM jugadores")
    fun getJugadores(): Flow<List<Jugador>>

    @Query("SELECT * FROM jugadores WHERE id = :id")
    fun getJugadorById(id: Int): Flow<Jugador?>

     @Query("SELECT * FROM jugadores WHERE equipo = :equipo")
     fun getJugadoresByEquipo(equipo: String): Flow<List<Jugador>>

     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertJugador(jugador: Jugador)

     @Delete
     suspend fun deleteJugador(jugador: Jugador)

     @Update
     suspend fun updateJugador(jugador: Jugador)

    @Insert
    suspend fun insertAsistencia(asistencia: Asistencia)

    @Update
    suspend fun updateAsistencia(asistencia: Asistencia)

     @Query("SELECT * FROM asistencias WHERE jugadorId = :jugadorId AND fecha = :fecha")
     fun getAsistencia(jugadorId: Int, fecha: Date): Asistencia?

    @Query("SELECT * FROM asistencias WHERE jugadorId = :jugadorId")
    suspend fun getAsistencias(jugadorId: Int): List<Asistencia>
}