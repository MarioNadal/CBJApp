package com.iessanalberto.dam2.cbjapp.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class Converters {
    @TypeConverter
    fun fromBooleanState(value: MutableState<Boolean>): Boolean {
        return value.value
    }

    @TypeConverter
    fun toBooleanState(value: Boolean): MutableState<Boolean> {
        return mutableStateOf(value)
    }

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    @TypeConverter
    fun fromDate(date: Date): String {
        return dateFormat.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String): Date {
        return dateFormat.parse(dateString)!!
    }
}