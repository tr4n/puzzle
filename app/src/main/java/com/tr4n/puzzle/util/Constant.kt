package com.tr4n.puzzle.util

import android.content.res.Resources

object Constant {

    val screenWidth by lazy {
        Resources.getSystem().displayMetrics.widthPixels
    }
    val screenHeight by lazy {
        Resources.getSystem().displayMetrics.heightPixels
    }
    const val PERCENT = "%"
    const val DEFAULT_SIZE = 3
    const val MIN_SIZE = 3
    const val MAX_SIZE = 5
}
