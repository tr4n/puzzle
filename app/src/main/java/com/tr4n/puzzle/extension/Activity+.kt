package com.tr4n.puzzle.extension

import android.app.Activity
import android.content.Intent
import androidx.annotation.NonNull
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackBar(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(findViewById(android.R.id.content), message, duration).show()
}

fun Activity.startActivity(@NonNull intent: Intent, flags: Int? = null) {
    startActivity(intent.apply {
        flags?.let(intent::setFlags)
    })
}

fun Activity.startActivityForResult(@NonNull intent: Intent, requestCode: Int, flags: Int? = null) {
    startActivityForResult(intent.apply {
        flags?.let(intent::setFlags)
    }, requestCode)
}
