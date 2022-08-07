package com.top.av.ui

import android.util.Log
import android.view.View
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.webrtc.WebRTCClient
import com.top.av.databinding.ActivityWebRtcBinding
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

class WebRTCActivity : BaseActivity<ActivityWebRtcBinding>(), SdpObserver {

    override fun init(root: View?) {


        val webRTCClient = WebRTCClient(this)



//        val url = "http://124.223.98.45/elab/api/webrtc"
//        val okHttpClient = OkHttpClient.Builder().build()
//
//        val body =
//            RequestBody.create(MediaType.get("Content-Type: text/plain;charset=UTF-8"), "本机sdp")
//
//        val request: Request = Request.Builder()
//            .url(url)
//            .post(body) //默认就是GET请求，可以不写
//            .build()
//
//        val call: Call = okHttpClient.newCall(request)
//
//        call.enqueue(object :Callback{
//            override fun onFailure(call: Call, e: IOException) {
//
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                var sdp = SessionDescription(SessionDescription.Type.fromCanonicalForm("answer"),"")
//
//            }
//
//        })



    }

    override fun getLayout(): Int {
        return R.layout.activity_web_rtc
    }

    override fun onCreateSuccess(p0: SessionDescription?) {
        Log.i("", "")

    }

    override fun onSetSuccess() {
        Log.i("", "")
    }

    override fun onCreateFailure(p0: String?) {
        Log.i("", "")
    }

    override fun onSetFailure(p0: String?) {
        Log.i("", "")
    }
}