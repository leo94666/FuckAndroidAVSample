package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityCpuFeatureBinding
import com.top.ffmpeg.ffmpeg.AbiDetect

class CpuFeatureActivity:BaseActivity<ActivityCpuFeatureBinding>() {

    companion object {
        fun start(context: Context) {
            var intent = Intent(context, CpuFeatureActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getLayout(): Int {
        return R.layout.activity_cpu_feature
    }

    override fun init(root: View?) {
        mDataBinding.tvAbi.text =
            String.format(resources.getString(R.string.abi, AbiDetect.getAbi()))
        mDataBinding.tvCpuAbi.text =
            String.format(resources.getString(R.string.cpu_abi, AbiDetect.getCpuAbi()))
    }
}