package com.tr4n.puzzle.data.database.converter

import androidx.room.TypeConverter
import com.tr4n.puzzle.extension.formatDate
import com.tr4n.puzzle.extension.toDate
import java.util.*

class DateStringConverter {
    @TypeConverter
    fun toDate(dateString: String?): Date? = dateString?.toDate("dd/MM/yyyy", Locale.ROOT)

    @TypeConverter
    fun fromDate(date: Date?) = date?.let {
        "dd/MM/yyyy".formatDate(it)
    }
}
