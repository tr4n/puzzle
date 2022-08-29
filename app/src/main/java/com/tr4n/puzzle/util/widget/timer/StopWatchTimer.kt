package com.tr4n.puzzle.util.widget.timer

import android.os.Handler
import android.os.Looper
import android.text.format.DateUtils
import java.util.*

class StopWatchTimer {

    private var timer = Timer()
    private val handler by lazy { Handler(Looper.getMainLooper()) }
    private var isCanceled = false

    var startTime = 0
        set(value) {
            if (value >= 0) {
                field = value
                current = value
            }
        }

    var endTime = Int.MAX_VALUE
    var delay = 0L

    var interval = DateUtils.SECOND_IN_MILLIS
        set(value) {
            if (value >= 0) {
                field = value
                stop()
                start()
            }
        }

    var onStepCount: (Int) -> Unit = {}
    var current = 0

    fun start() {
        if (current >= endTime) return
        if (isCanceled) {
            timer = Timer()
            isCanceled = false
        }
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler.post {
                    current++
                    onStepCount(current)
                }
            }
        }, delay, interval)
    }

    fun stop() {
        timer.cancel()
        isCanceled = true
    }
}
