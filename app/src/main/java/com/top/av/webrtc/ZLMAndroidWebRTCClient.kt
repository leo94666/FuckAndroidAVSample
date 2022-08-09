package com.top.av.webrtc

import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import org.checkerframework.checker.units.qual.m
import org.webrtc.*
import org.webrtc.audio.JavaAudioDeviceModule
import java.util.*

class ZLMAndroidWebRTCClient(var context: Context, var surfaceViewRenderer: SurfaceViewRenderer) :
    RendererCommon.RendererEvents, DefaultLifecycleObserver {

    private var TAG = "ZLMAndroidWebRTCClient"


    private var mEGLBaseContext: EglBase.Context = EglBase.create().eglBaseContext
    private var mContext: Context = context


    private var mSurfaceViewRenderer: SurfaceViewRenderer = surfaceViewRenderer

    private var mPeerConnectionFactory: PeerConnectionFactory? = null

    private var mLocalMediaStream: MediaStream? = null
    private var mLocalAudioTrack: AudioTrack? = null
    private var mAudioSource: AudioSource? = null

    private var mLocalPeer: Peer? = null


    init {
        mPeerConnectionFactory = createConnectionFactory()

        mSurfaceViewRenderer.init(mEGLBaseContext, this)
        surfaceViewRenderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_BALANCED)

        surfaceViewRenderer.setEnableHardwareScaler(true)

        //创建媒体流
        mLocalMediaStream = mPeerConnectionFactory?.createLocalMediaStream("ARDAMS")
        //采集音频
        mAudioSource = mPeerConnectionFactory?.createAudioSource(createAudioConstraints())
        mLocalAudioTrack = mPeerConnectionFactory?.createAudioTrack("ARDAMSa0", mAudioSource)

        //添加Tracks
        mLocalMediaStream?.addTrack(mLocalAudioTrack)
    }

    fun createLocalPeer(sdp: (String?) -> Unit): Peer? {
        if (null == mLocalPeer) {
            mLocalPeer = Peer {
                sdp.invoke(it)
            }
        }
        return mLocalPeer
    }

    fun setRemoteDescription(sdp: SessionDescription) {
        mLocalPeer?.setRemoteDescription(sdp)
    }

    private fun createConnectionFactory(): PeerConnectionFactory? {

        val options = PeerConnectionFactory.InitializationOptions.builder(mContext)
            .setEnableInternalTracer(false)
            .createInitializationOptions()

        PeerConnectionFactory.initialize(options)

        val videoEncoderFactory = DefaultVideoEncoderFactory(
            mEGLBaseContext,
            true,
            true
        )

        val videoDecoderFactory = DefaultVideoDecoderFactory(mEGLBaseContext)


        return PeerConnectionFactory.builder()
            .setAudioDeviceModule(JavaAudioDeviceModule.builder(mContext).createAudioDeviceModule())
            .setVideoEncoderFactory(videoEncoderFactory)
            .setVideoDecoderFactory(videoDecoderFactory)
            .createPeerConnectionFactory()
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

    inner class Peer(var sdp: (String?) -> Unit = {}) : PeerConnection.Observer, SdpObserver {

        var mPeerConnection: PeerConnection? = null

        init {
            mPeerConnection = createPeerConnection()
            mPeerConnection?.createOffer(this, offerOrAnswerConstraint())
        }

        //初始化 RTCPeerConnection 连接管道
        private fun createPeerConnection(): PeerConnection? {
            if (mPeerConnectionFactory == null) {
                mPeerConnectionFactory = createConnectionFactory()
            }
            // 管道连接抽象类实现方法
            val ICEServers = LinkedList<PeerConnection.IceServer>()
            val rtcConfig = PeerConnection.RTCConfiguration(ICEServers)
            return mPeerConnectionFactory?.createPeerConnection(rtcConfig, this)

        }

        fun setRemoteDescription(sdp: SessionDescription) {
            mPeerConnection?.setRemoteDescription(this, sdp)
        }

        override fun onCreateSuccess(sessionDescription: SessionDescription?) {
            mPeerConnection?.setLocalDescription(this, sessionDescription)
            mPeerConnection?.addStream(mLocalMediaStream)
            sdp.invoke(sessionDescription?.description)
        }

        override fun onSetSuccess() {

        }

        override fun onCreateFailure(p0: String?) {

        }

        override fun onSetFailure(p0: String?) {

        }


        override fun onSignalingChange(signalingState: PeerConnection.SignalingState?) {
            Log.i(TAG, "onSignalingChange ============> " + signalingState.toString())
        }

        override fun onIceConnectionChange(iceConnectionState: PeerConnection.IceConnectionState?) {
            Log.i(TAG, "onIceConnectionChange ============> " + iceConnectionState.toString())

        }

        override fun onIceConnectionReceivingChange(p0: Boolean) {
            Log.i(TAG, "onIceConnectionReceivingChange ============> $p0")

        }

        override fun onIceGatheringChange(iceGatheringState: PeerConnection.IceGatheringState?) {
            Log.i(TAG, "onIceGatheringChange ============> ${iceGatheringState.toString()}")
        }

        override fun onIceCandidate(iceCandidate: IceCandidate?) {
            Log.i(TAG, "onIceCandidate ============> ${iceCandidate.toString()}")
        }

        override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
            Log.i(TAG, "onIceCandidatesRemoved ============> ${p0.toString()}")

        }

        override fun onAddStream(mediaStream: MediaStream?) {
            Log.i(TAG, "onAddStream ============> ${mediaStream.toString()}")

            val remoteVideoTrack = mediaStream?.videoTracks?.get(0)
            remoteVideoTrack?.setEnabled(true)
            remoteVideoTrack?.addSink(surfaceViewRenderer)
        }

        override fun onRemoveStream(mediaStream: MediaStream?) {
            Log.i(TAG, "onRemoveStream ============> ${mediaStream.toString()}")

        }

        override fun onDataChannel(dataChannel: DataChannel?) {
            Log.i(TAG, "onDataChannel ============> ${dataChannel.toString()}")

        }

        override fun onRenegotiationNeeded() {
            Log.i(TAG, "onRenegotiationNeeded ============>")

        }

        override fun onAddTrack(rtpReceiver: RtpReceiver?, p1: Array<out MediaStream>?) {
            Log.i(TAG, "onAddTrack ============>" + rtpReceiver.toString())

        }
    }

    override fun onFirstFrameRendered() {
        Log.i(TAG, "onFirstFrameRendered ============>")

    }

    override fun onFrameResolutionChanged(frameWidth: Int, frameHeight: Int, rotation: Int) {
        Log.i(TAG, "onFrameResolutionChanged ============> $frameWidth:$frameHeight:$rotation")
    }


    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        surfaceViewRenderer.release()
        mLocalPeer?.mPeerConnection?.dispose()
        mAudioSource?.dispose()
        mPeerConnectionFactory?.dispose()
    }

}
