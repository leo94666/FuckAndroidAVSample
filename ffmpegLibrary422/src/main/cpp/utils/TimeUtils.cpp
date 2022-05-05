//
// Created by liyang on 2022/5/5.
//

#include <ctime>
#include "TimeUtils.h"

 long long TimeUtils::GetSysCurrentTime() {
    struct timeval time;
    gettimeofday(&time, NULL);
    long long curTime = ((long long)(time.tv_sec))*1000+time.tv_usec/1000;
    return curTime;
}
