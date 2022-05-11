package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.arch.utils.ScreenUtils
import com.top.av.R
import com.top.av.databinding.ActivityANativeWindowVideoPlayerBinding
import com.top.ffmpeg.player.FFMediaPlayer
import com.top.ffmpeg.player.FFMediaPlayer.VIDEO_RENDER_ANWINDOW

public class AWindowNativeVideoPlayerActivity : BaseActivity<ActivityANativeWindowVideoPlayerBinding>(), SurfaceHolder.Callback {

    private var mMediaPlayer: FFMediaPlayer? = null

    // private val mVideoPath = Environment.getExternalStorageDirectory().absolutePath + "/1.mp4"
    private var mVideoPath = Environment.getExternalStorageDirectory().absolutePath + "/1.mp4"

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, AWindowNativeVideoPlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_a_native_window_video_player
    }

    override fun init(root: View?) {
        keepScreenOn()
        //mVideoPath="https://links.jianshu.com/go?to=http%3A%2F%2Fqthttp.apple.com.edgesuite.net%2F1010qwoeiuryfg%2Fsl.m3u8"
        //mVideoPath = "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
        mDataBinding.leoSurfaceView.holder.addCallback(this)
        mDataBinding.leoSurfaceView.setAspectRatio(
            ScreenUtils.getScreenWidth(),
            ScreenUtils.getScreenWidth() / 16 * 9
        )
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        mMediaPlayer = FFMediaPlayer()
        mMediaPlayer!!.init(mVideoPath, VIDEO_RENDER_ANWINDOW, p0.surface)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        mMediaPlayer!!.play()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        mMediaPlayer!!.stop()
        mMediaPlayer!!.unInit()
    }

    fun pause(view: View) {
        mMediaPlayer!!.pause()
    }

}