package com.tr4n.puzzle.extension

import com.tr4n.puzzle.util.Constant
import java.text.DecimalFormat
import java.util.*

private const val FORMAT_DECIMAL_NUMBER = "#.#"
private const val SPACE = " "

private val decimalFormat = DecimalFormat(FORMAT_DECIMAL_NUMBER)

fun Number.formatWithUnit(unit: String): String = decimalFormat.format(this) + SPACE + unit

fun Number.toPercentage(): String = decimalFormat.format(this) + Constant.PERCENT

fun Long.toDateString(format: String, locale: Locale = Locale.getDefault()): String =
    format.formatDate(Date(this), locale)

fun Long.toTimeInToday(locale: Locale = Locale.getDefault()) =
    Date(this@toTimeInToday).getHourMinute().run {
        Date().setTimeOfDay(hour = first, minute = second)
    }

fun Int.toPercent(total: Int): Float = if (total == 0) 0f else this * 100f / total
fun Float.toPercent(total: Float): Float = if (total == 0f) 0f else this * 100f / total
