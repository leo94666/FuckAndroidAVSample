package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.top.arch.base.BaseActivity
import com.top.arch.utils.DataCenter
import com.top.av.R
import com.top.av.databinding.ActivityVideoPlayerBackBinding
import com.top.av.databinding.ActivityVideoPlayerBinding

class VideoPlayerActivity_back:BaseActivity<ActivityVideoPlayerBackBinding>(), Player.Listener {

    lateinit var player: ExoPlayer

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, VideoPlayerActivity_back::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_video_player_back
    }

    override fun init(root: View?) {
        player = ExoPlayer.Builder(this).build()
        mDataBinding.exoplayer.player=player
        val mediaItem: MediaItem = MediaItem.fromUri("/sdcard/1.mp4")
        val mediaItem2: MediaItem = MediaItem.fromUri("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4")

        player.setMediaItem(mediaItem)
        player.addMediaItem(mediaItem2)

        player.prepare()
        player.play()
        player.addListener(this)
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        player.next()
    }
}