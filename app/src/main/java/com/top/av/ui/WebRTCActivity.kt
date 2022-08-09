package com.top.av.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.top.arch.base.BaseActivity
import com.top.av.R
import com.top.av.databinding.ActivityWebRtcBinding
import com.top.av.webrtc.SdpResponse
import com.top.av.webrtc.ZLMAndroidWebRTCClient
import okhttp3.*
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import java.io.IOException

class WebRTCActivity : BaseActivity<ActivityWebRtcBinding>() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, WebRTCActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun init(root: View?) {


        //val webRTCClient = WebRTCClient(this, mDataBinding.surfaceView)

        val zlmAndroidWebRTCClient = ZLMAndroidWebRTCClient(this, mDataBinding.surfaceView)

        lifecycle.addObserver(zlmAndroidWebRTCClient)

        zlmAndroidWebRTCClient.createLocalPeer {
            val url = "http://124.223.98.45/elab/api/webrtc?app=live&stream=test&type=play"
            val okHttpClient = OkHttpClient.Builder().build()

            val body = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), it)

            val request: Request = Request.Builder()
                .url(url)
                .post(body) //默认就是GET请求，可以不写
                .build()

            val call: Call = okHttpClient.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }
                override fun onResponse(call: Call, response: Response) {
                    val body = response.body()?.string()
                    val sdpResponse = Gson().fromJson(body, SdpResponse::class.java)

                    try {
                        val sdp = SessionDescription(
                            SessionDescription.Type.fromCanonicalForm(sdpResponse.type),
                            sdpResponse.sdp
                        )
                        zlmAndroidWebRTCClient.setRemoteDescription(sdp)
                    } catch (e: Exception) {

                    }
                }
            })
        }


    }

    override fun getLayout(): Int {
        keepScreenOn()
        return R.layout.activity_web_rtc
    }
}