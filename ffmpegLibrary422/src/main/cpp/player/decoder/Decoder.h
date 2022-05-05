//
// Created by liyang on 2022/5/5.
//

#ifndef AV_DECODER_H
#define AV_DECODER_H

class Decoder {
public:
    Decoder() {};

    ~Decoder() {};

    virtual void Start() = 0;

    virtual void Pause() = 0;

    virtual void Stop() = 0;

    virtual float GetDuration() = 0;

    virtual void SeekToPosition(float position) = 0;

    virtual float GetCurrentPosition() = 0;

};

#endif //AV_DECODER_H
