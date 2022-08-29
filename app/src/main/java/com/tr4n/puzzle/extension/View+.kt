package com.tr4n.puzzle.extension

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.SystemClock
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.google.android.material.snackbar.Snackbar
import com.tr4n.puzzle.BuildConfig

fun View.showSnackBar(data: Any, length: Int = Snackbar.LENGTH_LONG) {
    val message = when (data) {
        is Int -> context.getString(data)
        is Throwable -> data.message.toString()
        else -> data.toString()
    }
    Snackbar.make(this, message, length).show()
}

fun View.setOnSafeClickListener(blockInMillis: Long = 500, onClick: (View) -> Any?) {
    var lastClickTime: Long = 0
    this.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        onClick(this)
    }
}

@BindingAdapter("safeClick")
fun View.setOnSafeClickListener(listener: View.OnClickListener? = null) {
    val blockInMillis = 500L
    var lastClickTime = 0L
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        listener?.onClick(this)
    }
}

@BindingAdapter("safeDoubleClick")
fun View.setOnSafeDoubleClickListener(listener: View.OnClickListener? = null) {
    val blockInMillis = 800L
    var lastClickTime = 0L
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < blockInMillis) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        listener?.onClick(this)
    }
}

fun View.OnClickListener.setViewsOnClick(vararg views: View) {
    views.forEach {
        it.setOnClickListener(this)
    }
}

fun View.OnClickListener.setViewsOnSafeClick(vararg views: View) {
    views.forEach { view ->
        view.setOnSafeClickListener {
            onClick(it)
        }
    }
}

fun View.showSnackBar(data: Any) {
    val message = when (data) {
        is Int -> context.getString(data)
        is Throwable -> data.message.toString()
        else -> data.toString()
    }
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.setColorDrawable(drawable: Int, color: Any?) =
    ResourcesCompat.getDrawable(resources, drawable, context?.theme)?.let {
        val wrappedDrawable = DrawableCompat.wrap(it)
        background = wrappedDrawable
        when (color) {
            is Int ->
                DrawableCompat.setTint(wrappedDrawable, resources.getColor(color, context?.theme))
            is String ->
                DrawableCompat.setTint(wrappedDrawable, Color.parseColor(color))
        }
    }

fun View.setColorDrawable(color: Any?) =
    apply {
        when (color) {
            is Int ->
                DrawableCompat.setTint(background, resources.getColor(color, context?.theme))
            is String ->
                DrawableCompat.setTint(background, Color.parseColor(color))
        }
    }

fun View.OnClickListener.listenToViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

fun View.setAnimationResource(
    @AnimRes resId: Int,
    duration: Long = 250
) {
    try {
        val animation = AnimationUtils.loadAnimation(context, resId).apply {
            this.duration = duration
        }
        startAnimation(animation)
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }
}

fun View.setAnimationResource(
    @AnimRes resId: Int,
    duration: Long = 250,
    onStart: () -> Unit = {},
    onRepeat: () -> Unit = {},
    onEnd: () -> Unit = {}
) {
    try {
        val animation = AnimationUtils.loadAnimation(context, resId).apply {
            this.duration = duration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    onStart()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    onEnd()
                }

                override fun onAnimationRepeat(animation: Animation?) {
                    onRepeat()
                }
            })
        }
        startAnimation(animation)
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }
}

fun View.setAnimationResource(
    @AnimRes anim1: Int,
    @AnimRes anim2: Int,
    duration: Long = 500,
    onStart: () -> Unit = {},
    onEnd: () -> Unit = {},
    actionBetween: () -> Unit = {}
) {
    try {
        val eachDuration = duration / 2
        val animation = AnimationUtils.loadAnimation(context, anim1).apply {
            this.duration = eachDuration
            setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    onStart()
                }

                override fun onAnimationEnd(animation: Animation?) {
                    clearAnimation()
                    setAnimationResource(anim2, eachDuration, onEnd = {
                        onEnd()
                    })
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })
        }
        startAnimation(animation)
        delayTask(duration / 3) {
            actionBetween()
        }
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) e.printStackTrace()
    }
}

fun AppCompatImageView.isShowResource(@DrawableRes drawableRes: Int): Boolean =
    drawable.constantState == context.getDrawable(drawableRes)?.constantState

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}


/**
 * Try to hide the keyboard and returns whether it worked
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) {
    }
    return false
}

fun WebView.setBeAbleToGoBack() {
    this.setOnKeyListener(object : View.OnKeyListener {
        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
            if (keyCode == KeyEvent.KEYCODE_BACK && event?.action == MotionEvent.ACTION_UP && this@setBeAbleToGoBack.canGoBack()) {
                this@setBeAbleToGoBack.goBack()
                return true
            }
            return false
        }
    })
}

@SuppressLint("SetJavaScriptEnabled")
fun WebView.configure() {
    requestFocus()
    settings.javaScriptEnabled = true
    settings.setGeolocationEnabled(true)
    isHorizontalScrollBarEnabled = false
    settings.domStorageEnabled = true
    isSoundEffectsEnabled = true
}

fun WebView.enableFitScreen() {
    settings.apply {
        loadWithOverviewMode = true
        useWideViewPort = true
        layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
    }
}
