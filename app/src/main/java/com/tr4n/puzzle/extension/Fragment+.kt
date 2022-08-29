package com.tr4n.puzzle.extension

import android.os.Handler
import android.os.Looper

fun delayTask(duration: Long = 1000, func: () -> Unit = {}) {
    Handler(Looper.getMainLooper()).postDelayed(func, duration)
}
