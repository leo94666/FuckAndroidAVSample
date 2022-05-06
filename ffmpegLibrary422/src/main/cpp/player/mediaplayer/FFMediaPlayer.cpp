//
// Created by liyang on 2022/5/5.
//

#include "FFMediaPlayer.h"
#include "../render/NativeRender.h"

FFMediaPlayer::~FFMediaPlayer() {

}
void FFMediaPlayer::Init(JNIEnv *env, jobject obj, char *url, int videoRenderType, jobject surface) {
    env->GetJavaVM(&m_JavaVM);
    m_JavaObj = env->NewGlobalRef(obj);

    m_VideoDecoder = new VideoDecoder(url);
    //  m_AudioDecoder = new AudioDecoder(url);

    if(videoRenderType == VIDEO_RENDER_OPENGL) {
        // m_VideoDecoder->SetVideoRender(VideoGLRender::GetInstance());
    } else if (videoRenderType == VIDEO_RENDER_ANWINDOW) {
        m_VideoRender = new NativeRender(env, surface);
        m_VideoDecoder->SetVideoRender(m_VideoRender);
    } else if (videoRenderType == VIDEO_RENDER_3D_VR) {
        // m_VideoDecoder->SetVideoRender(VRGLRender::GetInstance());
    }

    // m_AudioRender = new OpenSLRender();
    // m_AudioDecoder->SetAudioRender(m_AudioRender);

    //m_VideoDecoder->SetMessageCallback(this, PostMessage);
    //  m_AudioDecoder->SetMessageCallback(this, PostMessage);
}


void FFMediaPlayer::UnInit() {
    if(m_VideoDecoder) {
        delete m_VideoDecoder;
        m_VideoDecoder = nullptr;
    }

    if(m_VideoRender) {
        delete m_VideoRender;
        m_VideoRender = nullptr;
    }

//    if(m_AudioDecoder) {
//        delete m_AudioDecoder;
//        m_AudioDecoder = nullptr;
//    }
//
//    if(m_AudioRender) {
//        delete m_AudioRender;
//        m_AudioRender = nullptr;
//    }
//
//    VideoGLRender::ReleaseInstance();

    bool isAttach = false;
    GetJNIEnv(&isAttach)->DeleteGlobalRef(m_JavaObj);
    if(isAttach)
        GetJavaVM()->DetachCurrentThread();
}

void FFMediaPlayer::Play() {
    if(m_VideoDecoder)
        m_VideoDecoder->Start();
//
//    if(m_AudioDecoder)
//        m_AudioDecoder->Start();
}

void FFMediaPlayer::Pause() {
    if(m_VideoDecoder)
        m_VideoDecoder->Pause();

//    if(m_AudioDecoder)
//        m_AudioDecoder->Pause();
}

void FFMediaPlayer::Stop() {
    if(m_VideoDecoder)
        m_VideoDecoder->Stop();

//    if(m_AudioDecoder)
//        m_AudioDecoder->Stop();
}

void FFMediaPlayer::SeekToPosition(float position) {
    if(m_VideoDecoder)
        m_VideoDecoder->SeekToPosition(position);

//    if(m_AudioDecoder)
//        m_AudioDecoder->SeekToPosition(position);
}

long FFMediaPlayer::GetMediaParams(int paramType) {
    long value = 0;
    switch(paramType)
    {
//        case MEDIA_PARAM_VIDEO_WIDTH:
//            value = m_VideoDecoder != nullptr ? m_VideoDecoder->GetVideoWidth() : 0;
//            break;
//        case MEDIA_PARAM_VIDEO_HEIGHT:
//            value = m_VideoDecoder != nullptr ? m_VideoDecoder->GetVideoHeight() : 0;
//            break;
//        case MEDIA_PARAM_VIDEO_DURATION:
//            value = m_VideoDecoder != nullptr ? m_VideoDecoder->GetDuration() : 0;
//            break;
    }
    return value;
}


JNIEnv *FFMediaPlayer::GetJNIEnv(bool *isAttach) {
    JNIEnv *env;
    int status;
    if (nullptr == m_JavaVM) {
        return nullptr;
    }
    *isAttach = false;
    status = m_JavaVM->GetEnv((void **)&env, JNI_VERSION_1_4);
    if (status != JNI_OK) {
        status = m_JavaVM->AttachCurrentThread(&env, nullptr);
        if (status != JNI_OK) {
            return nullptr;
        }
        *isAttach = true;
    }
    return env;}

jobject FFMediaPlayer::GetJavaObj() {
    return m_JavaObj;
}

JavaVM *FFMediaPlayer::GetJavaVM() {
    return m_JavaVM;
}


