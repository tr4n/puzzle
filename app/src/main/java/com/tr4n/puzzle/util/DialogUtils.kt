package com.tr4n.puzzle.util

import android.content.Context
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.viewbinding.ViewBinding
import com.tr4n.puzzle.R

object DialogUtils {

    fun createDialog(
        context: Context,
        title: String,
        message: String,
        positionText: String = context.getString(android.R.string.ok),
        negativeText: String = context.getString(android.R.string.cancel),
        onBuild: AlertDialog.Builder.() -> Unit = {},
        onPositionClick: () -> Unit = {},
        onNegativeClick: () -> Unit = {}
    ): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> onPositionClick() }
            .setNegativeButton(android.R.string.cancel) { _, _ -> onNegativeClick() }
            .apply {
                onBuild()
            }
            .create()
    }

    fun <T : ViewBinding> createCustomDialog(
        binding: T,
        onBuild: AlertDialog.Builder.() -> Unit = {}
    ): AlertDialog {
        val context = binding.root.context
        val alert = AlertDialog.Builder(context)
            .setView(binding.root)
            .apply {
                onBuild()
            }
            .setOnKeyListener { _, keyCode, _ -> // Prevent dialog close on back press button
                keyCode == KeyEvent.KEYCODE_BACK
            }
            .create()
        alert.window?.apply {
            setLayout(
                (Constant.screenWidth * 0.9f).toInt(),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(R.color.colorTransparent)
        }
        return alert
    }
}
