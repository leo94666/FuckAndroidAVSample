//
// Created by liyang on 2022/5/5.
//

#include "Player.h"
#include "FFMediaPlayer.h"

void
Player::Init(JNIEnv *env, jobject obj, char *url, int playerType, int renderType, jobject surface) {
    m_MediaPlayer = new FFMediaPlayer();
    if (m_MediaPlayer) m_MediaPlayer->Init(env, obj, url, playerType, renderType, surface);
}

void Player::UnInit() {
    if (m_MediaPlayer) {
        m_MediaPlayer->UnInit();
        delete m_MediaPlayer;
        m_MediaPlayer = nullptr;
    }
}

void Player::Play() {
    if (m_MediaPlayer) {
        m_MediaPlayer->Play();
    }
}

void Player::Pause() {
    if (m_MediaPlayer) {
        m_MediaPlayer->Pause();
    }
}

void Player::Stop() {
    if (m_MediaPlayer) {
        m_MediaPlayer->Stop();
    }
}

void Player::SeekToPosition(float position) {
    if (m_MediaPlayer) {
        m_MediaPlayer->SeekToPosition(position);
    }
}

long Player::GetMediaParams(int paramType) {
    if (m_MediaPlayer) {
        return m_MediaPlayer->GetMediaParams(paramType);
    }
    return 0;
}

void Player::SetMediaParams(int paramType, jobject obj) {
    if (m_MediaPlayer) {
        m_MediaPlayer->SetMediaParams(paramType, obj);
    }
}
