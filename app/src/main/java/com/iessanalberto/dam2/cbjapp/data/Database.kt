package com.iessanalberto.dam2.cbjapp.data

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@androidx.room.Database(entities = [Jugador::class, Asistencia::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Database : RoomDatabase() {
    abstract fun jugadorDao(): JugadorDao

    companion object{
        @Volatile
        private var Instance: Database? = null

        fun getDatabase(context: Context): Database{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, Database::class.java, "database")
                    .fallbackToDestructiveMigration() // Esto elimina la base de datos anterior si hay cambios
                    .build().also { Instance = it }
            }
        }
    }
}
