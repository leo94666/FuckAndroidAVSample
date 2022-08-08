package com.top.av.webrtc

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import org.webrtc.*
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnection.RTCConfiguration
import org.webrtc.audio.JavaAudioDeviceModule
import java.io.IOException
import java.util.*


data class SdpResponse(var code: Int, var id: String, var sdp: String, var type: String)

class WebRTCClient(var context: Context, var surfaceView: SurfaceViewRenderer) {


    private var mPeerConnectionFactory: PeerConnectionFactory? = null
    private var rootEglBase: EglBase? = null

    private var ICEServers = LinkedList<IceServer>()

    private var mLocalMediaStream: MediaStream? = null
    private var localAudioTrack: AudioTrack? = null
    private var audioSource: AudioSource? = null

    private var localPeer: Peer? = null


    init {
        try {
            rootEglBase = EglBase.create()
            surfaceView.init(rootEglBase?.eglBaseContext, object : RendererCommon.RendererEvents {
                override fun onFirstFrameRendered() {

                }

                override fun onFrameResolutionChanged(p0: Int, p1: Int, p2: Int) {

                }
            })
            val options: PeerConnectionFactory.InitializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(context)
                    .setEnableInternalTracer(false)
                    .createInitializationOptions()

            PeerConnectionFactory.initialize(options)


            val videoDecoderFactory: VideoDecoderFactory =
                DefaultVideoDecoderFactory(rootEglBase?.eglBaseContext)
            val videoEncoderFactory: VideoEncoderFactory =
                DefaultVideoEncoderFactory(rootEglBase?.eglBaseContext, true, true)

            mPeerConnectionFactory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(videoDecoderFactory)
                .setVideoEncoderFactory(videoEncoderFactory)
                .createPeerConnectionFactory()

            //创建媒体流
            mLocalMediaStream = mPeerConnectionFactory?.createLocalMediaStream("ARDAMS")
            //采集音频
            audioSource = mPeerConnectionFactory?.createAudioSource(createAudioConstraints())
            localAudioTrack = mPeerConnectionFactory?.createAudioTrack("ARDAMSa0", audioSource)

            //添加Track
            mLocalMediaStream?.addTrack(localAudioTrack)

            //创建端Peer
            localPeer = Peer()

            //localPeer?.mPeerConnection?.addStream(mLocalMediaStream)
            localPeer?.mPeerConnection?.createOffer(object : SdpObserver {
                override fun onCreateSuccess(sdp: SessionDescription?) {
                    Log.v(TAG, "PeerConnectionManager  sdp创建成功       \n" + sdp?.description)
                    localPeer?.mPeerConnection?.setLocalDescription(this, sdp)
                    localPeer?.mPeerConnection?.addStream(mLocalMediaStream)

                    quest(sdp?.description!!)
                }

                override fun onSetSuccess() {}

                override fun onCreateFailure(p0: String?) {}

                override fun onSetFailure(p0: String?) {}

            }, offerOrAnswerConstraint())

            Log.i(TAG, "=========init========")
        } catch (e: Exception) {
            Log.i(TAG, "=========init failure========$e")
        }
    }


    private fun createConnectionFactory(): PeerConnectionFactory? {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory.InitializationOptions.builder(context)
                .createInitializationOptions()
        )
        val encoderFactory: VideoEncoderFactory
        val decoderFactory: VideoDecoderFactory
        encoderFactory = DefaultVideoEncoderFactory(
            rootEglBase?.eglBaseContext,
            true,
            true
        )
        decoderFactory = DefaultVideoDecoderFactory(rootEglBase?.eglBaseContext)
        val options = PeerConnectionFactory.Options()
        return PeerConnectionFactory.builder()
            .setOptions(options)
            .setAudioDeviceModule(JavaAudioDeviceModule.builder(context).createAudioDeviceModule())
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .createPeerConnectionFactory()
    }

    private fun offerOrAnswerConstraint(): MediaConstraints {
        val mediaConstraints = MediaConstraints()
        val keyValuePairs = ArrayList<MediaConstraints.KeyValuePair>()
        keyValuePairs.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        keyValuePairs.add(
            MediaConstraints.KeyValuePair(
                "OfferToReceiveVideo",
                "true"
            )
        )
        mediaConstraints.mandatory.addAll(keyValuePairs)
        return mediaConstraints
    }


    private fun createAudioConstraints(): MediaConstraints {
        val audioConstraints = MediaConstraints()
        audioConstraints.mandatory.add(
            MediaConstraints.KeyValuePair(
                "googEchoCancellation",
                "true"
            )
        )
        audioConstraints.mandatory.add(
            MediaConstraints.KeyValuePair(
                "googAutoGainControl",
                "false"
            )
        )
        audioConstraints.mandatory.add(
            MediaConstraints.KeyValuePair(
                "googHighpassFilter",
                "true"
            )
        )
        audioConstraints.mandatory.add(
            MediaConstraints.KeyValuePair(
                "googNoiseSuppression",
                "true"
            )
        )
        return audioConstraints
    }


    private fun quest(sdp: String) {
        val url = "http://124.223.98.45/elab/api/webrtc?app=live&stream=test&type=play"
        val okHttpClient = OkHttpClient.Builder()
            .build()

        val mediaType = MediaType.parse("text/plain; charset=utf-8")
        val body =
            RequestBody
                .create(mediaType, sdp)

        val request: Request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        val call: Call = okHttpClient.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.i(TAG, "=========onCreateSuccess========")
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()
                val sdpResponse = Gson().fromJson(body, SdpResponse::class.java)

                try {
                    val sdp = SessionDescription(
                        SessionDescription.Type.fromCanonicalForm(sdpResponse.type),
                        sdpResponse.sdp
                    )
                    //remotePeer = Peer()
                    localPeer?.mPeerConnection?.setRemoteDescription(object : SdpObserver {
                        override fun onCreateSuccess(p0: SessionDescription?) {

                        }

                        override fun onSetSuccess() {

                        }

                        override fun onCreateFailure(p0: String?) {

                        }

                        override fun onSetFailure(p0: String?) {

                        }

                    }, sdp)
                } catch (e: Exception) {
                    Log.e(TAG, "==============$e")
                }
            }

        })
    }

    inner class Peer : PeerConnection.Observer {

        var mPeerConnection: PeerConnection? = null

        init {
            this.mPeerConnection = createPeerConnection()
        }


        //初始化 RTCPeerConnection 连接管道
        private fun createPeerConnection(): PeerConnection? {
            if (mPeerConnectionFactory == null) {
                mPeerConnectionFactory = createConnectionFactory()
            }
            // 管道连接抽象类实现方法
            val rtcConfig = RTCConfiguration(ICEServers)
            return mPeerConnectionFactory?.createPeerConnection(rtcConfig, this)

        }


        override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
            Log.i(TAG, "=========onSignalingChange========")

        }

        override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
            Log.i(TAG, "=========onIceConnectionChange========")

        }

        override fun onIceConnectionReceivingChange(p0: Boolean) {
            Log.i(TAG, "=========onIceConnectionReceivingChange========")
        }

        override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
            Log.i(TAG, "=========onIceGatheringChange========")
        }

        override fun onIceCandidate(p0: IceCandidate?) {
            Log.i(TAG, "=========onIceCandidate========")
        }

        override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
            Log.i(TAG, "=========onIceCandidatesRemoved========")
        }

        override fun onAddStream(mediaStream: MediaStream?) {
            Log.i(TAG, "=========onAddStream========" + mediaStream?.id)

            val remoteVideoTrack = mediaStream?.videoTracks?.get(0)

            remoteVideoTrack?.setEnabled(true)

            remoteVideoTrack?.addSink(surfaceView)
        }

        override fun onRemoveStream(p0: MediaStream?) {
            Log.i(TAG, "=========onRemoveStream========")

        }

        override fun onDataChannel(p0: DataChannel?) {
            Log.i(TAG, "=========onDataChannel========")

        }

        override fun onRenegotiationNeeded() {
            Log.i(TAG, "=========onRenegotiationNeeded========")

        }

        override fun onAddTrack(p0: RtpReceiver?, p1: Array<out MediaStream>?) {
            Log.i(TAG, "=========onAddTrack========")

        }

    }
}