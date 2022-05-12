package com.top.ffmpeg.decoder.base;

/**
 * @author leo
 * @version 1.0
 * @className DecodeState
 * @description TODO
 * @date 2022/5/11 16:43
 **/
enum DecodeState {
    START,
    DECODING,
    PAUSE,
    SEEKING,
    FINISH,
    STOP
}
