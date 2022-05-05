//
// Created by liyang on 2022/5/5.
//

#include "VideoDecoder.h"

void VideoDecoder::OnDecoderReady() {
    m_VideoWidth=GetCodecContext()->width;
    m_VideoHeight=GetCodecContext()->height;
}

void VideoDecoder::OnDecoderDone() {

}

void VideoDecoder::OnFrameAvailable(AVFrame *frame) {

}
