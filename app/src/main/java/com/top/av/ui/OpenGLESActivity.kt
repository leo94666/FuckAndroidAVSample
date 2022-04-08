package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityOpenGlBinding

class OpenGLESActivity: BaseActivity<ActivityOpenGlBinding>() {

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, OpenGLESActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_open_gl
    }

    override fun init(root: View?) {

    }
}