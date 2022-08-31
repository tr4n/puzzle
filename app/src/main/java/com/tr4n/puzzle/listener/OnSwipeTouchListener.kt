package com.tr4n.puzzle.listener

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

abstract class OnSwipeTouchListener(context: Context) : View.OnTouchListener {

    private val gestureDetector = GestureDetector(
        context,
        GestureListener(
            ::onSwipeRight,
            ::onSwipeLeft,
            ::onSwipeTop,
            ::onSwipeBottom,
            ::onClick
        )
    )

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        view.performClick()
        return gestureDetector.onTouchEvent(event)
    }

    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}
    open fun onSwipeTop() {}
    open fun onSwipeBottom() {}
    open fun onClick() {}

    private class GestureListener(
        private val onSwipeRight: () -> Unit,
        private val onSwipeLeft: () -> Unit,
        private val onSwipeTop: () -> Unit,
        private val onSwipeBottom: () -> Unit,
        private val onClick: () -> Unit
    ) : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean = true

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            onClick()
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean =
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                when {
                    abs(diffX) > abs(diffY) -> {
                        if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) onSwipeRight() else onSwipeLeft()
                            true
                        } else false
                    }
                    abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD -> {
                        if (diffY > 0) onSwipeBottom() else onSwipeTop()
                        true
                    }
                    else -> false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                false
            }

        companion object {
            private const val SWIPE_THRESHOLD = 100
            private const val SWIPE_VELOCITY_THRESHOLD = 100
        }
    }
}
