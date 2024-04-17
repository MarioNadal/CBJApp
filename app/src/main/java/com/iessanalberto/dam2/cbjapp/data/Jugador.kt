package com.iessanalberto.dam2.cbjapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Entity(tableName = "jugadores")
data class Jugador(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val apellidos: String = "",
    val equipo: String = ""
)

@Entity(
    tableName = "asistencias",
    foreignKeys = [
        ForeignKey(
            entity = Jugador::class,
            parentColumns = ["id"],
            childColumns = ["jugadorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Asistencia(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val jugadorId: Int,
    val fecha: Date,
    val presente: Boolean
)