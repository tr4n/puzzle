package com.tr4n.puzzle.util

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.tr4n.puzzle.databinding.DialogLoadingBinding


/**
 * show loading dialog common app
 * @return a dialog
 */
class LoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val binding = DialogLoadingBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
    }

    override fun show() {
        kotlin.runCatching {
            super.show()
        }
    }
}

