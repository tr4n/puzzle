package com.tr4n.puzzle.extension

import androidx.core.text.HtmlCompat
import java.text.SimpleDateFormat
import java.util.*

fun String.decodeHtml(): String =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()

fun String.formatDate(date: Date, locale: Locale = Locale.getDefault()) =
    SimpleDateFormat(this, locale).format(date)

fun String.toDate(format: String, locale: Locale = Locale.getDefault()): Date? =
    try {
        SimpleDateFormat(format, locale).parse(this)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

fun String.startsWithPrefix(vararg prefix: String): Boolean =
    prefix.any { startsWith(it, ignoreCase = true) }
