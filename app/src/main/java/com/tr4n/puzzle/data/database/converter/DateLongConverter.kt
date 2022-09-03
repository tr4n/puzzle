package com.tr4n.puzzle.data.database.converter

import androidx.room.TypeConverter
import java.util.*

class DateLongConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? = dateLong?.let { Date(it) }

    @TypeConverter
    fun fromDate(date: Date?) = date?.time ?: 0
}
