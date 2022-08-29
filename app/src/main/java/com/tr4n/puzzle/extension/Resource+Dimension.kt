package com.tr4n.puzzle.extension

import android.content.res.Resources
import android.util.TypedValue
import android.util.TypedValue.applyDimension

fun Resources.getSP(value: Float) =
    applyDimension(TypedValue.COMPLEX_UNIT_SP, value, displayMetrics)

fun Resources.getDIP(value: Float) =
    applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, displayMetrics)

fun Resources.getPX(value: Float) =
    applyDimension(TypedValue.COMPLEX_UNIT_PX, value, displayMetrics)
