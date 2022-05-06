package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.SurfaceHolder
import android.view.View
import com.google.android.exoplayer2.Player
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityVideoPlayerBinding
import com.top.ffmpeg.player.FFMediaPlayer
import com.top.ffmpeg.player.FFMediaPlayer.VIDEO_RENDER_ANWINDOW
import com.top.ffmpeg.player.LeoSurfaceView

class VideoPlayerActivity:BaseActivity<ActivityVideoPlayerBinding>(),
    SurfaceHolder.Callback {

    private var mMediaPlayer: FFMediaPlayer? = null
    private val mSurfaceView: LeoSurfaceView? = null
    private val mVideoPath =
        Environment.getExternalStorageDirectory().absolutePath + "/in.mp4"

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, VideoPlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_video_player
    }

    override fun init(root: View?) {

        mDataBinding.leoSurfaceView.holder.addCallback(this)
    }

    override fun surfaceCreated(p0: SurfaceHolder) {
        mMediaPlayer = FFMediaPlayer()
        //mMediaPlayer.addEventCallback(this)
        mMediaPlayer!!.init(mVideoPath, VIDEO_RENDER_ANWINDOW, p0.surface)
    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
        mMediaPlayer!!.play()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {
        mMediaPlayer!!.stop()
        mMediaPlayer!!.unInit()
    }

}