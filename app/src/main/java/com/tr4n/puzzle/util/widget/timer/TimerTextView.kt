package com.tr4n.puzzle.util.widget.timer

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import java.util.*
import java.util.concurrent.TimeUnit

class TimerTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    private var timer = Timer()
    private var endTime = 0L
    private var interval = DEFAULT_INTERVAL.toLong()
    private var isCanceled = false

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopTimer()
    }

    fun setInterval(interval: Long) {
        if (interval >= 0) {
            this.interval = interval
            stopTimer()
            startTimer()
        }
    }

    fun setEndTime(endTime: Long) {
        if (endTime >= 0) {
            this.endTime = endTime
            stopTimer()
            startTimer()
        }
    }

    private fun startTimer() {
        if (endTime == 0L) {
            return
        }
        if (isCanceled) {
            timer = Timer()
            isCanceled = false
        }
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                handler?.post {
                    text = getDurationBreakdown(endTime - System.currentTimeMillis())
                }
            }
        }, 0, interval)
    }

    private fun stopTimer() {
        timer.cancel()
        isCanceled = true
    }

    private fun getDurationBreakdown(diff: Long): String {
        var millis = diff
        if (millis < 0) {
            return "00:00:00"
        }
        val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d", hours, minutes, seconds)
        //return "${getWithLeadZero(hours)}:${getWithLeadZero(minutes)}:${getWithLeadZero(seconds)}"
    }

    companion object {
        private const val DEFAULT_INTERVAL = 1000
    }
}
