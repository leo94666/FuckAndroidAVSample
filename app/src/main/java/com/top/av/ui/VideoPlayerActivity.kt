package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity:BaseActivity<ActivityVideoPlayerBinding>() {

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
        title = "播放器"
        mDataBinding.btnNativeWindow.setOnClickListener {
            AWindowNativeVideoPlayerActivity.start(this)
        }
    }

    override fun hideTitleBar() {
        //super.hideTitleBar()
    }
}