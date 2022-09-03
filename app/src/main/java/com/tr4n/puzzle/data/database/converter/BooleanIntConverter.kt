package com.tr4n.puzzle.data.database.converter

import androidx.room.TypeConverter

class BooleanIntConverter {

    @TypeConverter
    fun booleanToInt(boolean: Boolean) = if (boolean) 1 else 0

    @TypeConverter
    fun intToBoolean(int: Int) = int == 1
}
