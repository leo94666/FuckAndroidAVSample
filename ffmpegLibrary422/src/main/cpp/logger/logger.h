#include "android/log.h"


#define FF_LOG_TAG     "FFmpeg_C_Leo_Log"

#define FF_LOG_VERBOSE     ANDROID_LOG_VERBOSE
#define FF_LOG_DEBUG         ANDROID_LOG_DEBUG
#define FF_LOG_INFO              ANDROID_LOG_INFO
#define FF_LOG_WARN           ANDROID_LOG_WARN
#define FF_LOG_ERROR        ANDROID_LOG_ERROR
#define FF_LOG_FATAL           ANDROID_LOG_FATAL
#define FF_LOG_SILENT         ANDROID_LOG_SILENT


#define VLOG(level, TAG, ...)    ((void)__android_log_vprint(level, TAG, __VA_ARGS__))
#define VLOGV(...)  VLOG(FF_LOG_VERBOSE,   FF_LOG_TAG, __VA_ARGS__)
#define VLOGD(...)  VLOG(FF_LOG_DEBUG,     FF_LOG_TAG, __VA_ARGS__)
#define VLOGI(...)  VLOG(FF_LOG_INFO,      FF_LOG_TAG, __VA_ARGS__)
#define VLOGW(...)  VLOG(FF_LOG_WARN,      FF_LOG_TAG, __VA_ARGS__)
#define VLOGE(...)  VLOG(FF_LOG_ERROR,     FF_LOG_TAG, __VA_ARGS__)


#define ALOG(level, TAG, ...)    ((void)__android_log_print(level, TAG, __VA_ARGS__))
#define ALOGV(...)  ALOG(FF_LOG_VERBOSE,   FF_LOG_TAG, __VA_ARGS__)
#define ALOGD(...)  ALOG(FF_LOG_DEBUG,     FF_LOG_TAG, __VA_ARGS__)
#define ALOGI(...)  ALOG(FF_LOG_INFO,      FF_LOG_TAG, __VA_ARGS__)
#define ALOGW(...)  ALOG(FF_LOG_WARN,      FF_LOG_TAG, __VA_ARGS__)
#define ALOGE(...)  ALOG(FF_LOG_ERROR,     FF_LOG_TAG, __VA_ARGS__)

#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR, FF_LOG_TAG, format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  FF_LOG_TAG, format, ##__VA_ARGS__)



