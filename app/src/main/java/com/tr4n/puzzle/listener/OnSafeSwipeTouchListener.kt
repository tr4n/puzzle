package com.tr4n.puzzle.listener

import android.content.Context
import android.os.SystemClock

abstract class OnSafeSwipeTouchListener(context: Context) : OnSwipeTouchListener(context) {

    private var lastClickTime = 0L

    final override fun onSwipeRight() {
        if (SystemClock.elapsedRealtime() - lastClickTime < BLOCK_MILLIS) return
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeSwipeRight()
    }

    final override fun onSwipeLeft() {
        if (SystemClock.elapsedRealtime() - lastClickTime < BLOCK_MILLIS) return
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeSwipeLeft()
    }

    final override fun onSwipeTop() {
        if (SystemClock.elapsedRealtime() - lastClickTime < BLOCK_MILLIS) return
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeSwipeTop()
    }

    final override fun onSwipeBottom() {
        if (SystemClock.elapsedRealtime() - lastClickTime < BLOCK_MILLIS) return
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeSwipeBottom()
    }

    final override fun onClick() {
        if (SystemClock.elapsedRealtime() - lastClickTime < BLOCK_MILLIS) return
        lastClickTime = SystemClock.elapsedRealtime()
        onSafeClick()
    }

    open fun onSafeSwipeRight() {}
    open fun onSafeSwipeLeft() {}
    open fun onSafeSwipeTop() {}
    open fun onSafeSwipeBottom() {}
    open fun onSafeClick() {}

    companion object {
        private const val BLOCK_MILLIS = 200L
    }
}