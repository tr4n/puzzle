package com.tr4n.puzzle.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tr4n.puzzle.R
import com.tr4n.puzzle.base.recyclerview.BaseDiffUtilItemCallback
import com.tr4n.puzzle.base.recyclerview.BindAbleAdapter
import com.tr4n.puzzle.base.recyclerview.DataBindingListener
import com.tr4n.puzzle.base.recyclerview.SimpleBindingAdapter
import com.tr4n.puzzle.data.type.Category
import com.tr4n.puzzle.extension.navigationBarHeight
import com.tr4n.puzzle.extension.statusBarHeight
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Suppress("UNCHECKED_CAST", "UNCHECKED_CAST")
@BindingAdapter(value = ["adapter", "items"], requireAll = false)
fun <T> RecyclerView.setAdapterData(
    itemAdapter: RecyclerView.Adapter<*>? = null,
    items: List<T>?
) {
    itemAdapter?.let {
        adapter = it
    }
    (adapter as? BindAbleAdapter<T>)?.setItems(items ?: emptyList())
}

@BindingAdapter(
    value = ["simpleData", "layoutId", "listener", "diffUtil"],
    requireAll = false
)
fun <T> RecyclerView.setSimpleAdapter(
    data: List<T>?,
    @LayoutRes layoutId: Int,
    listener: DataBindingListener? = null,
    diffUtilItemCallback: DiffUtil.ItemCallback<T>? = null
) {
    val diffUtil = diffUtilItemCallback ?: BaseDiffUtilItemCallback()
    if (adapter as? SimpleBindingAdapter<*> == null) {
        adapter = SimpleBindingAdapter(layoutId, diffUtil)
    }

    (adapter as? SimpleBindingAdapter<T>)?.apply {
        setItems(data ?: emptyList())
        this.listener = listener
    }
}

@BindingAdapter("gridSpanCount")
fun RecyclerView.setGridLayoutManagerSpanCount(spanCount: Int?) {
    layoutManager = GridLayoutManager(context, spanCount ?: 1)
}

@BindingAdapter(value = ["textResource", "date", "format"], requireAll = false)
fun TextView.setTextResource(
    @StringRes
    resId: Int? = null,
    date: Date? = null,
    format: String? = null
) {

    resId?.let {
        text = context.getString(resId)
    }
    date?.let {
        val pattern = format ?: "MM/dd/yyyy HH:mm"
        text = SimpleDateFormat(pattern, Locale.getDefault()).format(it)
    }
}

@BindingAdapter("relativeTime")
fun TextView.setRelativeTime(millis: Long?) {
    val now = System.currentTimeMillis()
    text = when {
        millis == null || millis == 0L -> context.getString(R.string.never)
        abs(now - millis) <= DateUtils.MINUTE_IN_MILLIS -> context.getString(R.string.recent)
        else -> DateUtils.getRelativeTimeSpanString(millis, now, DateUtils.MINUTE_IN_MILLIS)
    }
}


@BindingAdapter(value = ["resource", "url", "backgroundResource"], requireAll = false)
fun ImageView.setImageResource(resId: Int? = null, url: String? = null, bgRes: Int? = null) {
    try {
        //  setImageResource(R.drawable.img_no_image)
        resId?.let(::setImageResource)
        bgRes?.let(::setBackgroundResource)
        if (!url.isNullOrBlank()) {
            Glide.with(this)
                .load(url)
                .into(this)
        }
    } catch (e: Exception) {
        // Resource not found
        e.printStackTrace()
    }
}

@BindingAdapter("bitmap")
fun ImageView.setBindingImageBitmap(bitmap: Bitmap?) {
    kotlin.runCatching {
        setImageBitmap(bitmap)
    }
}

@BindingAdapter("bgColor")
fun View.bindBackgroundColor(@ColorInt color: Int) {
    background = null
    setBackgroundColor(Color.BLACK)
    setBackgroundColor(color)
}

@BindingAdapter("isVisible")
fun View.setVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("isInvisible")
fun View.setInvisible(isInvisible: Boolean) {
    this.isInvisible = isInvisible
}

@BindingAdapter(value = ["widthPercent", "heightPercent"], requireAll = false)
fun View.setLayoutPercent(widthPercent: Float?, heightPercent: Float?) {
    updateLayoutParams<ViewGroup.LayoutParams> {
        widthPercent?.let {
            width = (Constant.screenWidth * it).toInt()
        }
        heightPercent?.let {
            height = (Constant.screenHeight * it).toInt()
        }
    }
}

@BindingAdapter(value = ["ratioBaseWidth", "ratioBaseHeight"], requireAll = false)
fun View.setRatio(ratioBaseWidth: Float?, ratioBaseHeight: Float?) {
    updateLayoutParams<ViewGroup.LayoutParams> {
        when {
            ratioBaseWidth != null -> height = (width * ratioBaseWidth).toInt()
            ratioBaseHeight != null -> width = (height * ratioBaseHeight).toInt()
        }
    }
}

@BindingAdapter(
    value = ["startDrawable", "topDrawable", "endDrawable", "bottomDrawable"],
    requireAll = false
)
fun TextView.setDrawables(
    @DrawableRes startRes: Int,
    @DrawableRes topRes: Int,
    @DrawableRes endRes: Int,
    @DrawableRes bottomRes: Int
) {
    setCompoundDrawablesWithIntrinsicBounds(
        startRes,
        topRes,
        endRes,
        bottomRes
    )
}

@BindingAdapter("secondCount")
fun TextView.setSecondCount(count: Int) {
    val minutes = count.div(60)
    val seconds = count.rem(60)
    text = String.format("%d:%02d", minutes, seconds)
}

@BindingAdapter("drawableTintColor")
fun TextView.setDrawableTintColor(color: Int) {
    compoundDrawables.filterNotNull().forEach {
        try {
            it.colorFilter = PorterDuffColorFilter(getColor(context, color), PorterDuff.Mode.SRC_IN)
        } catch (e: Resources.NotFoundException) {
            it.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@BindingAdapter("tintColor")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}

@BindingAdapter("tintRes")
fun ImageView.setImageTintRes(@ColorRes colorRes: Int) {
    try {
        setColorFilter(getColor(context, colorRes), PorterDuff.Mode.SRC_IN)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@BindingAdapter(value = ["imageFile", "decodedSize"], requireAll = false)
fun ImageView.setImageFromFile(file: File?, decodedSize: Int? = null) {
    file ?: return
    val size = decodedSize ?: BitmapUtils.PREVIEW_DECODED_IMAGE_SIZE
    Glide.with(this)
        .load(file)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .centerCrop()
        .override(size)
        .into(this)
}


@BindingAdapter(value = ["fileName", "decodedSize"], requireAll = false)
fun ImageView.setImageFromFile(fileName: String, decodedSize: Int?) {
    val size = decodedSize ?: BitmapUtils.PREVIEW_DECODED_IMAGE_SIZE
    if (fileName.endsWith(".JPG", ignoreCase = true)) {
        val file = FileUtils.getFile(fileName)
        Glide.with(this)
            .load(file)
            .centerCrop()
            .override(size)
            .into(this)
    } else {
        val drawableRes = Category.getImageDrawableRes(fileName) ?: return
        Glide.with(this)
            .load(drawableRes)
            .centerCrop()
            .override(size)
            .into(this)
    }
}

@BindingAdapter("paddingStatusBar")
fun View.isPaddingStatusBar(shouldPadding: Boolean = false) {
    if (shouldPadding) {
        setPadding(0, context.statusBarHeight, 0, 0)
    }
}

@BindingAdapter("paddingNavigationBar")
fun View.isPaddingNavigationBar(shouldPadding: Boolean = false) {
    if (shouldPadding) {
        setPadding(0, 0, 0, context.navigationBarHeight)
    }
}

@BindingAdapter("marginToStatusBar")
fun View.setMarginToStatusBar(dimen: Float) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = dimen.toInt() + context.statusBarHeight
    this.layoutParams = layoutParams
}

@BindingAdapter(
    value = ["layoutMarginStart", "layoutMarginTop", "layoutMarginEnd", "layoutMarginBottom"],
    requireAll = false
)
fun View.setLayoutMargin(start: Float?, top: Float?, end: Float?, bottom: Float?) {
    layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
        start?.let { leftMargin = it.toInt() }
        top?.let { topMargin = it.toInt() }
        end?.let { rightMargin = it.toInt() }
        bottom?.let { bottomMargin = it.toInt() }
    }
}

@BindingAdapter(
    value = ["layoutPaddingStart", "layoutPaddingTop", "layoutPaddingEnd", "layoutPaddingBottom"],
    requireAll = false
)
fun View.setLayoutPadding(start: Float?, top: Float?, end: Float?, bottom: Float?) {
    val pStart = start?.toInt() ?: paddingStart
    val pTop = top?.toInt() ?: paddingTop
    val pEnd = end?.toInt() ?: paddingEnd
    val pBottom = bottom?.toInt() ?: paddingBottom
    setPadding(pStart, pTop, pEnd, pBottom)
}

@BindingAdapter(value = ["layoutHeight", "layoutWidth"], requireAll = false)
fun View.setLayoutWidthHeight(heightDimen: Float?, widthDimen: Float?) {
    layoutParams = layoutParams.apply {
        widthDimen?.let { this.width = it.toInt() }
        heightDimen?.let { this.height = it.toInt() }
    }
}
