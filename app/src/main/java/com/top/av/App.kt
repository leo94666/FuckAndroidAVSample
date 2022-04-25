package com.top.av

import android.app.Application
import com.top.arch.base.BaseApplication

class App: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun TAG(): String {
        return "ffmpegLibrary422"
    }
}