package com.tr4n.puzzle.data.database.converter

import androidx.room.TypeConverter

class StringIntsConverter {

    @TypeConverter
    fun stringToInts(string: String?): List<Int> =
        string?.trim()?.split(SEPARATOR)?.map {
            it.toInt()
        } ?: emptyList()

    @TypeConverter
    fun intsToString(ints: List<Int>): String =
        ints.joinToString(SEPARATOR)

    companion object {
        private const val SEPARATOR = ","
    }
}
