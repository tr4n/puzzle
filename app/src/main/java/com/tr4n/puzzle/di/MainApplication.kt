package com.tr4n.puzzle.di

import android.app.Application

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        App.init(applicationContext)
    }
}
