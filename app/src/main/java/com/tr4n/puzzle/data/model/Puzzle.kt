package com.tr4n.puzzle.data.model

import android.graphics.Bitmap

data class Puzzle(
    val index: Int = 0,
    val bitmap: Bitmap,
    var show: Boolean = false
) {

    val alpha
        get() = when {
            index == 0 && show -> 0.35f
            index == 0 -> 0f
            else -> 1f
        }
}
