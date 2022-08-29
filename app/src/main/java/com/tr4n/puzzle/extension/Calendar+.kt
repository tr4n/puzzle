package com.tr4n.puzzle.extension

import java.util.*

fun Calendar.getMonth() = this.get(Calendar.MONTH) + 1

fun Calendar.toTimeSheetCalendar(endDate: Int?) = apply {
    if (getCalendar().get(Calendar.MONTH) == get(Calendar.MONTH)) {
        val endDateNotNull = endDate ?: getActualMaximum(Calendar.DAY_OF_MONTH)
        if (get(Calendar.DATE) > endDateNotNull) add(Calendar.MONTH, 1)
    }
}

fun getCalendar(timeZone: TimeZone = TimeZone.getDefault()): Calendar =
    Calendar.getInstance(timeZone)
