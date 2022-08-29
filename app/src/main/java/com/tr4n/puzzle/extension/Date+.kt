package com.tr4n.puzzle.extension

import android.text.format.DateUtils
import com.tr4n.puzzle.extension.getCalendar
import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)

fun Date.format(formatDate: String, zone: TimeZone = TimeZone.getDefault()): String? =
    SimpleDateFormat(formatDate, Locale.getDefault())
        .apply { timeZone = zone }
        .format(this)

fun Date.addDeltaTime(hour: Int = 0, minute: Int = 0, second: Int = 0, millis: Long = 0): Date {
    val milliseconds = time + hour * DateUtils.HOUR_IN_MILLIS +
            minute * DateUtils.MINUTE_IN_MILLIS +
            second * DateUtils.SECOND_IN_MILLIS + millis
    return Date(milliseconds)
}

fun Date.setTimeOfDay(hour: Int = 0, minute: Int = 0, second: Int = 0, millis: Int = 0): Date =
    getCalendar(TimeZone.getDefault()).apply {
        time = this@setTimeOfDay
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, second)
        set(Calendar.MILLISECOND, millis)
    }.time

fun Date.getHourMinute(): Pair<Int, Int> {
    val calender = getCalendar(TimeZone.getDefault()).apply {
        time = this@getHourMinute
    }
    return calender.get(Calendar.HOUR_OF_DAY) to calender.get(Calendar.MINUTE)
}
