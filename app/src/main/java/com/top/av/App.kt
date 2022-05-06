package com.top.av

import android.os.Environment
import com.top.arch.BuildConfig
import com.top.arch.base.BaseApplication
import com.top.arch.logger.*
import com.top.arch.logger.impl.FormatStrategy
import com.top.arch.utils.AssetsUtils
import java.io.File

class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        copyAsset()
    }

    private fun copyAsset() {
        AssetsUtils.copyFolderFromAssetsToSD(
            this,
            "fonts",
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "fonts/"
        )
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