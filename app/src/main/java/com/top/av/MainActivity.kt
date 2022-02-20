package com.top.av

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.top.ffmpeg.Cmd
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv.text = Cmd.version()
    }
}