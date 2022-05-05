package com.top.av

import com.top.arch.BuildConfig
import com.top.arch.base.BaseApplication
import com.top.arch.logger.*
import com.top.arch.logger.impl.FormatStrategy
import com.top.arch.utils.DataCenter

class App: BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        DataCenter.getInternetVideoPath()
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy
            formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(true) // (Optional) Whether to show thread info or not. Default true
                .methodCount(3) // (Optional) How many method line to show. Default 2
                .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
                .logStrategy(LogcatLogStrategy()) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag(TAG) // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
            Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    override fun TAG(): String {
        return "ffmpegLibrary422"
    }
}