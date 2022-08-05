package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.google.android.exoplayer2.*
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityExoPlayerBinding
import kotlinx.android.synthetic.main.activity_exo_player.*


class ExoPlayerActivity:BaseActivity<ActivityExoPlayerBinding>() {


    private var player: ExoPlayer? = null


    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ExoPlayerActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun init(root: View?) {

        player = ExoPlayer.Builder(this).build()

        player_view.player = player

        //val mediaItem = MediaItem.fromUri("http://124.223.98.45/live/test.live.ts")
        val mediaItem = MediaItem.fromUri("http://124.223.98.45/live/test.live.flv")
        //val mediaItem = MediaItem.fromUri("rtmp://124.223.98.45/live/test")

        player!!.setMediaItem(mediaItem)



        player!!.playWhenReady = true
        player!!.prepare()


    }

    override fun getLayout(): Int {
        return R.layout.activity_exo_player
    }
}