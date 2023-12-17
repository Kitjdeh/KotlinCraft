package com.ncc.kotlincraft.app

import android.app.Application
import android.content.Context

class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
    init {
        instance = this
    }
    companion object{
        private var instance: App? = null
        fun getContext(): Context
        {
            return instance!!.applicationContext
        }
    }
}