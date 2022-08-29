package com.tr4n.puzzle.di

import android.content.Context

object App {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }
}
