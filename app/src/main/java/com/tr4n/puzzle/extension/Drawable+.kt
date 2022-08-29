package com.tr4n.puzzle.extension

import android.graphics.drawable.Drawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

fun Drawable.setTint(color: Int, mode: BlendModeCompat = BlendModeCompat.SRC_IN) {
    colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, mode)
}
