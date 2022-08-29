package com.tr4n.puzzle.util

import android.util.Log
import com.tr4n.puzzle.BuildConfig

object LogUtils {
    private const val TAG_DEFAULT = "TOEIC-LOG"
    private val isDebug = BuildConfig.DEBUG

    fun i(obj: Any?, tag: String = TAG_DEFAULT) {
        Log.i(tag, obj.toString())
    }

    fun d(obj: Any?, tag: String = TAG_DEFAULT) {
        Log.d(tag, obj.toString())
    }

    fun e(obj: Any?, tag: String = TAG_DEFAULT) {
        Log.e(tag, obj.toString())
    }

    fun w(obj: Any?, tag: String = TAG_DEFAULT) {
        if (isDebug) Log.w(tag, obj.toString())
    }
}
