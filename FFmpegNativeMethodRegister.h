//
// Created by liyang on 2022/4/8.
//

#ifndef AV_FFMPEGNATIVEMETHODREGISTER_H
#define AV_FFMPEGNATIVEMETHODREGISTER_H


#include <jni.h>
#include <libavutil/ffversion.h>
#include <libavutil/bprint.h>
#include <pthread.h>
#include <libavutil/mem.h>
#include <stdatomic.h>
#include "fftools/ffmpeg.h"

#define NATIVE_FFMPEG_CLASS_NAME "com/top/ffmpeg/ffmpeg/FFmpegNative"

#define SESSION_MAP_SIZE 1000
# define LogType 1
# define StatisticsType 2

/** Holds the id of the current session */
__thread volatile long globalSessionId = 0;

static atomic_short sessionMap[SESSION_MAP_SIZE];
static atomic_int sessionInTransitMessageCountMap[SESSION_MAP_SIZE];

struct CallBackData {
    int type;                 // 1 (log callback) or 2 (statistics callback)
    long sessionId;           // session identifier

    int logLevel;             // log level
    AVBPrint logData;         // log data

    int statisticsFrameNumber;        // statistics frame number
    float statisticsFps;              // statistics fps
    float statisticsQuality;          // statistics quality
    int64_t statisticsSize;           // statistics size
    int statisticsTime;               // statistics time
    double statisticsBitrate;         // statistics bitrate
    double statisticsSpeed;           // statistics speed

    struct CallBackData *next;
};

struct CallBackData *callBackDataHead;
struct CallBackData *callBackDataTail;

/** Fields that control the handling of SIGNALs */
volatile int handleSIGQUIT = 1;
volatile int handleSIGINT = 1;
volatile int handleSIGTERM = 1;
volatile int handleSIGXCPU = 1;
volatile int handleSIGPIPE = 1;

/** Global reference to the virtual machine running */
static JavaVM *globalVm;

/** Global reference of Config class in Java */
static jclass configClass;

/** Global reference of log redirection method in Java */
static jmethodID logMethod;

/** Global reference of statistics redirection method in Java */
static jmethodID statisticsMethod;

/** Global reference of safOpen method in Java */
static jmethodID safOpenMethod;

/** Global reference of safClose method in Java */
static jmethodID safCloseMethod;

/** Global reference of String class in Java */
static jclass stringClass;

/** Global reference of String constructor in Java */
static jmethodID stringConstructor;

/** Full name of the Config class */
const char *configClassName = "com/top/ffmpeg/ffmpeg/FFmpegNative";

/** Full name of String class */
const char *stringClassName = "java/lang/String";

/** Redirection control variables */
static pthread_mutex_t lockMutex;
static pthread_mutex_t monitorMutex;
static pthread_cond_t monitorCondition;

pthread_t callBackThread;
int redirectionEnabled;
int configuredLogLevel = AV_LOG_INFO;

static const char *avutil_log_get_level_str(int level) {
    switch (level) {
//        case AV_LOG_STDERR:
//            return "stderr";
        case AV_LOG_QUIET:
            return "quiet";
        case AV_LOG_DEBUG:
            return "debug";
        case AV_LOG_VERBOSE:
            return "verbose";
        case AV_LOG_INFO:
            return "info";
        case AV_LOG_WARNING:
            return "warning";
        case AV_LOG_ERROR:
            return "error";
        case AV_LOG_FATAL:
            return "fatal";
        case AV_LOG_PANIC:
            return "panic";
        default:
            return "";
    }
}

static void
avutil_log_format_line(void *avcl, int level, const char *fmt, va_list vl, AVBPrint part[4],
                       int *print_prefix) {
    int flags = av_log_get_flags();
    AVClass *avc = avcl ? *(AVClass **) avcl : NULL;
    av_bprint_init(part + 0, 0, 1);
    av_bprint_init(part + 1, 0, 1);
    av_bprint_init(part + 2, 0, 1);
    av_bprint_init(part + 3, 0, 65536);

    if (*print_prefix && avc) {
        if (avc->parent_log_context_offset) {
            AVClass **parent = *(AVClass ***) (((uint8_t *) avcl) +
                                               avc->parent_log_context_offset);
            if (parent && *parent) {
                av_bprintf(part + 0, "[%s @ %p] ",
                           (*parent)->item_name(parent), parent);
            }
        }
        av_bprintf(part + 1, "[%s @ %p] ",
                   avc->item_name(avcl), avcl);
    }

    if (*print_prefix && (level > AV_LOG_QUIET) && (flags & AV_LOG_PRINT_LEVEL))
        av_bprintf(part + 2, "[%s] ", avutil_log_get_level_str(level));

    av_vbprintf(part + 3, fmt, vl);

    if (*part[0].str || *part[1].str || *part[2].str || *part[3].str) {
        char lastc = part[3].len && part[3].len <= part[3].size ? part[3].str[part[3].len - 1] : 0;
        *print_prefix = lastc == '\n' || lastc == '\r';
    }
}

static void avutil_log_sanitize(uint8_t *line) {
    while (*line) {
        if (*line < 0x08 || (*line > 0x0D && *line < 0x20))
            *line = '?';
        line++;
    }
}

void mutexInit() {
    pthread_mutexattr_t attributes;
    pthread_mutexattr_init(&attributes);
    pthread_mutexattr_settype(&attributes, PTHREAD_MUTEX_RECURSIVE_NP);
    pthread_mutex_init(&lockMutex, &attributes);
    pthread_mutexattr_destroy(&attributes);
}

void monitorInit() {
    pthread_mutexattr_t attributes;
    pthread_mutexattr_init(&attributes);
    pthread_mutexattr_settype(&attributes, PTHREAD_MUTEX_RECURSIVE_NP);

    pthread_condattr_t cattributes;
    pthread_condattr_init(&cattributes);
    pthread_condattr_setpshared(&cattributes, PTHREAD_PROCESS_PRIVATE);

    pthread_mutex_init(&monitorMutex, &attributes);
    pthread_mutexattr_destroy(&attributes);

    pthread_cond_init(&monitorCondition, &cattributes);
    pthread_condattr_destroy(&cattributes);
}

void mutexUnInit() {
    pthread_mutex_destroy(&lockMutex);
}

void monitorUnInit() {
    pthread_mutex_destroy(&monitorMutex);
    pthread_cond_destroy(&monitorCondition);
}

void mutexLock() {
    pthread_mutex_lock(&lockMutex);
}

void mutexUnlock() {
    pthread_mutex_unlock(&lockMutex);
}

void monitorWait(int milliSeconds) {
    struct timeval tp;
    struct timespec ts;
    int rc;

    rc = gettimeofday(&tp, NULL);
    if (rc) {
        return;
    }

    ts.tv_sec = tp.tv_sec;
    ts.tv_nsec = tp.tv_usec * 1000;
    ts.tv_sec += milliSeconds / 1000;
    ts.tv_nsec += (milliSeconds % 1000) * 1000000;
    ts.tv_sec += ts.tv_nsec / 1000000000L;
    ts.tv_nsec = ts.tv_nsec % 1000000000L;

    pthread_mutex_lock(&monitorMutex);
    pthread_cond_timedwait(&monitorCondition, &monitorMutex, &ts);
    pthread_mutex_unlock(&monitorMutex);
}

void monitorNotify() {
    pthread_mutex_lock(&monitorMutex);
    pthread_cond_signal(&monitorCondition);
    pthread_mutex_unlock(&monitorMutex);
}


/**
 * Adds a session id to the session map.
 *
 * @param id session id
 */
void addSession(long id) {
    atomic_store(&sessionMap[id % SESSION_MAP_SIZE], 1);
}

/**
 * Removes head of callback data list.
 */
struct CallBackData *callbackDataRemove() {
    struct CallBackData *currentData;

    mutexLock();

    if (callBackDataHead == NULL) {
        currentData = NULL;
    } else {
        currentData = callBackDataHead;

        struct CallBackData *nextHead = currentData->next;
        if (nextHead == NULL) {
            if (callBackDataHead != callBackDataTail) {
                LOGE("Head and tail callback data pointers do not match for single callback data element. This can cause memory leak.");
            } else {
                callBackDataTail = NULL;
            }
            callBackDataHead = NULL;

        } else {
            callBackDataHead = nextHead;
        }
    }

    mutexUnlock();

    return currentData;
}

/**
 * Removes a session id from the session map.
 *
 * @param id session id
 */
void removeSession(long id) {
    atomic_store(&sessionMap[id % SESSION_MAP_SIZE], 0);
}

/**
 * Adds a cancel session request to the session map.
 *
 * @param id session id
 */
void cancelSession(long id) {
    atomic_store(&sessionMap[id % SESSION_MAP_SIZE], 2);
}

/**
 * Checks whether a cancel request for the given session id exists in the session map.
 *
 * @param id session id
 * @return 1 if exists, false otherwise
 */
int cancelRequested(long id) {
    if (atomic_load(&sessionMap[id % SESSION_MAP_SIZE]) == 2) {
        return 1;
    } else {
        return 0;
    }
}

/**
 * Resets the number of messages in transmit for this session.
 *
 * @param id session id
 */
void resetMessagesInTransmit(long id) {
    atomic_store(&sessionInTransitMessageCountMap[id % SESSION_MAP_SIZE], 0);
}

void *callbackThreadFunction() {
    JNIEnv *env;
    jint getEnvRc = (globalVm)->GetEnv(reinterpret_cast<void **>(globalVm), JNI_VERSION_1_6);
    if (getEnvRc != JNI_OK) {
        if (getEnvRc != JNI_EDETACHED) {
            LOGE("Callback thread failed to GetEnv for class %s with rc %d.\n", configClassName,
                 getEnvRc);
            return NULL;
        }

        if ((*globalVm).AttachCurrentThread(reinterpret_cast<JNIEnv **>(globalVm), NULL) != 0) {
            LOGE("Callback thread failed to AttachCurrentThread for class %s.\n", configClassName);
            return NULL;
        }
    }

    LOGD("Async callback block started.\n");

    while (redirectionEnabled) {

        struct CallBackData *callbackData = callbackDataRemove();
        if (callbackData != NULL) {
            if (callbackData->type == LogType) {
                // LOG CALLBACK
//                int size = callbackData->logData.len;
//                jbyteArray byteArray = (jbyteArray) (*env).NewByteArray(size);
//                (*env).SetByteArrayRegion(byteArray, 0, size, callbackData->logData.str);
//                (*env)->CallStaticVoidMethod(env, configClass, logMethod,
//                                             (jlong) callbackData->sessionId,
//                                             callbackData->logLevel, byteArray);
//                (*env)->DeleteLocalRef(env, byteArray);
//
//                // CLEAN LOG DATA
//                av_bprint_finalize(&callbackData->logData, NULL);

            } else {
                // STATISTICS CALLBACK
                (*env).CallStaticVoidMethod(configClass, statisticsMethod,
                                            (jlong) callbackData->sessionId,
                                            callbackData->statisticsFrameNumber,
                                            callbackData->statisticsFps,
                                            callbackData->statisticsQuality,
                                            callbackData->statisticsSize,
                                            callbackData->statisticsTime,
                                            callbackData->statisticsBitrate,
                                            callbackData->statisticsSpeed);

            }

            atomic_fetch_sub(
                    &sessionInTransitMessageCountMap[callbackData->sessionId % SESSION_MAP_SIZE],
                    1);

            // CLEAN STRUCT
            callbackData->next = NULL;
            av_free(callbackData);

        } else {
            monitorWait(100);
        }
    }
    (*globalVm).DetachCurrentThread();
    LOGD("Async callback block stopped.\n");
    return NULL;
}

/**
 * Used by saf protocol; is expected to be called from a Java thread, therefore we don't need attach/detach
 */
int saf_open(int safId) {
    JNIEnv *env = NULL;
    (*globalVm).GetEnv(reinterpret_cast<void **>(globalVm), JNI_VERSION_1_6);
    return (*env).CallStaticIntMethod(configClass, safOpenMethod, safId);
}

/**
 * Used by saf protocol; is expected to be called from a Java thread, therefore we don't need attach/detach
 */
int saf_close(int fd) {
    JNIEnv *env = NULL;
    (*globalVm).GetEnv(reinterpret_cast<void **>(globalVm), JNI_VERSION_1_6);
    return (*env).CallStaticIntMethod(configClass, safCloseMethod, fd);
}

/**
 * Adds statistics data to the end of callback data list.
 */
void statisticsCallbackDataAdd(int frameNumber, float fps, float quality, int64_t size, int time,
                               double bitrate, double speed) {

    // CREATE DATA STRUCT FIRST
    struct CallBackData *newData = (struct CallBackData *) av_malloc(sizeof(struct CallBackData));
    newData->type = StatisticsType;
    newData->sessionId = globalSessionId;
    newData->statisticsFrameNumber = frameNumber;
    newData->statisticsFps = fps;
    newData->statisticsQuality = quality;
    newData->statisticsSize = size;
    newData->statisticsTime = time;
    newData->statisticsBitrate = bitrate;
    newData->statisticsSpeed = speed;

    newData->next = NULL;

    mutexLock();

    // INSERT IT TO THE END OF QUEUE
    if (callBackDataTail == NULL) {
        callBackDataTail = newData;

        if (callBackDataHead != NULL) {
            LOGE("Dangling callback data head detected. This can cause memory leak.");
        } else {
            callBackDataHead = newData;
        }
    } else {
        struct CallBackData *oldTail = callBackDataTail;
        oldTail->next = newData;

        callBackDataTail = newData;
    }

    mutexUnlock();

    monitorNotify();

    atomic_fetch_add(&sessionInTransitMessageCountMap[globalSessionId % SESSION_MAP_SIZE], 1);
}


/**
 * Callback function for FFmpeg statistics.
 *
 * @param frameNumber last processed frame number
 * @param fps frames processed per second
 * @param quality quality of the output stream (video only)
 * @param size size in bytes
 * @param time processed output duration
 * @param bitrate output bit rate in kbits/s
 * @param speed processing speed = processed duration / operation duration
 */
void ffmpegkit_statistics_callback_function(int frameNumber, float fps, float quality, int64_t size,
                                            int time, double bitrate, double speed) {
    statisticsCallbackDataAdd(frameNumber, fps, quality, size, time, bitrate, speed);
}

/** Forward declaration for function defined in fftools_ffmpeg.c */
int ffmpeg_execute(int argc, char **argv);


JNIEXPORT void JNICALL enableNativeRedirection(JNIEnv *env, jobject instance) {
    mutexLock();

    if (redirectionEnabled != 0) {
        mutexUnlock();
        return;
    }
    redirectionEnabled = 1;

    mutexUnlock();

    int rc = pthread_create(&callBackThread, 0,
                            reinterpret_cast<void *(*)(void *)>(callbackThreadFunction), 0);
    if (rc != 0) {
        LOGE("Failed to create callback thread (rc=%d).\n", rc);
        return;
    }

    //av_log_set_callback(ffmpegkit_log_callback_function);
    //set_report_callback(ffmpegkit_statistics_callback_function);

}



JNIEXPORT void JNICALL disableNativeRedirection(JNIEnv *env, jobject instance) {
    mutexLock();

    if (redirectionEnabled != 1) {
        mutexUnlock();
        return;
    }
    redirectionEnabled = 0;

    mutexUnlock();

    //(av_log_default_callback);
    //set_report_callback(NULL);

    monitorNotify();
}

JNIEXPORT void JNICALL setNativeLogLevel(JNIEnv *env, jobject instance, jint level) {
    configuredLogLevel = level;
}

JNIEXPORT jint JNICALL getNativeLogLevel(JNIEnv *env, jobject instance) {
    return configuredLogLevel;
}

JNIEXPORT jstring JNICALL getNativeFFmpegVersion(JNIEnv *env, jobject instance) {
    return env->NewStringUTF(FFMPEG_VERSION);
}

JNIEXPORT jstring JNICALL getNativeVersion(JNIEnv *env, jobject instance) {
    return env->NewStringUTF(FFMPEG_VERSION);
}
#define LIB_NAME "ffmpeg-kit"
JNIEXPORT jint JNICALL nativeFFmpegExecute(JNIEnv *env, jobject instance, jlong id, jobjectArray stringArray) {
    jstring *tempArray = NULL;
    int argumentCount = 1;
    char **argv = NULL;

    // SETS DEFAULT LOG LEVEL BEFORE STARTING A NEW RUN
    av_log_set_level(configuredLogLevel);

    if (stringArray) {
        int programArgumentCount = (*env).GetArrayLength(stringArray);
        argumentCount = programArgumentCount + 1;

        tempArray = (jstring *) av_malloc(sizeof(jstring) * programArgumentCount);
    }

    /* PRESERVE USAGE FORMAT
     *
     * ffmpeg <arguments>
     */
    argv = (char **)av_malloc(sizeof(char*) * (argumentCount));
    argv[0] = (char *)av_malloc(sizeof(char) * (strlen(LIB_NAME) + 1));
    strcpy(argv[0], LIB_NAME);

    // PREPARE ARRAY ELEMENTS
    if (stringArray) {
        for (int i = 0; i < (argumentCount - 1); i++) {
            tempArray[i] = (jstring) (*env).GetObjectArrayElement(stringArray, i);
            if (tempArray[i] != NULL) {
                argv[i + 1] = (char *) (*env).GetStringUTFChars( tempArray[i], 0);
            }
        }
    }

    // REGISTER THE ID BEFORE STARTING THE SESSION
    globalSessionId = (long) id;
    addSession((long) id);
    resetMessagesInTransmit(globalSessionId);
    // RUN
    int returnCode = ffmpeg_execute(argumentCount, argv);
    // ALWAYS REMOVE THE ID FROM THE MAP
    removeSession((long) id);
    // CLEANUP
    if (tempArray) {
        for (int i = 0; i < (argumentCount - 1); i++) {
            (*env).ReleaseStringUTFChars( tempArray[i], argv[i + 1]);
        }
        av_free(tempArray);
    }
    av_free(argv[0]);
    av_free(argv);
    return returnCode;
}

JNIEXPORT void JNICALL nativeFFmpegCancel(JNIEnv *env, jobject instance, jlong id) {
    cancel_operation(id);

}

JNIEXPORT void JNICALL nativeFFprobeExecute(JNIEnv *env, jobject instance) {

}

JNIEXPORT int JNICALL registerNewNativeFFmpegPipe(JNIEnv *env, jobject instance, jstring ffmpegPipePath) {
    const char *ffmpegPipePathString = (*env).GetStringUTFChars( ffmpegPipePath, 0);
    return mkfifo(ffmpegPipePathString, S_IRWXU | S_IRWXG | S_IROTH);
}

JNIEXPORT jstring JNICALL getNativeBuildDate(JNIEnv *env, jobject instance) {
    char buildDate[10];
    sprintf(buildDate, "%d", "");
    return (*env).NewStringUTF(buildDate);
}

JNIEXPORT int JNICALL setNativeEnvironmentVariable(JNIEnv *env, jobject instance, jstring variableName, jstring variableValue) {
    const char *variableNameString = (*env).GetStringUTFChars(variableName, 0);
    const char *variableValueString = (*env).GetStringUTFChars(variableValue, 0);

    int rc = setenv(variableNameString, variableValueString, 1);

    (*env).ReleaseStringUTFChars(variableName, variableNameString);
    (*env).ReleaseStringUTFChars(variableValue, variableValueString);
    return rc;
}

JNIEXPORT void JNICALL ignoreNativeSignal(JNIEnv *env, jobject instance, jint signum) {
    if (signum == SIGQUIT) {
        handleSIGQUIT = 0;
    } else if (signum == SIGINT) {
        handleSIGINT = 0;
    } else if (signum == SIGTERM) {
        handleSIGTERM = 0;
    } else if (signum == SIGXCPU) {
        handleSIGXCPU = 0;
    } else if (signum == SIGPIPE) {
        handleSIGPIPE = 0;
    }
}

JNIEXPORT jint JNICALL messagesInTransmit(JNIEnv *env, jobject instance, jlong id) {
    return atomic_load(&sessionInTransitMessageCountMap[id % SESSION_MAP_SIZE]);
}


/** Prototypes of native functions defined by Config class. */
JNINativeMethod ffmpegNativeMethods[] = {
        {"enableNativeRedirection",      "()V",                                     (void *) enableNativeRedirection},
        {"disableNativeRedirection",     "()V",                                     (void *) disableNativeRedirection},
        {"setNativeLogLevel",            "(I)V",                                    (void *) setNativeLogLevel},
        {"getNativeLogLevel",            "()I",                                     (void *) getNativeLogLevel},
        {"getNativeFFmpegVersion",       "()Ljava/lang/String;",                    (void *) getNativeFFmpegVersion},
        {"getNativeVersion",             "()Ljava/lang/String;",                    (void *) getNativeVersion},
        {"nativeFFmpegExecute",          "(J[Ljava/lang/String;)I",                 (void *) nativeFFmpegExecute},
        {"nativeFFmpegCancel",           "(J)V",                                    (void *) nativeFFmpegCancel},
        {"nativeFFprobeExecute",         "(J[Ljava/lang/String;)I",                 (void *) nativeFFprobeExecute},
        {"registerNewNativeFFmpegPipe",  "(Ljava/lang/String;)I",                   (void *) registerNewNativeFFmpegPipe},
        {"getNativeBuildDate",           "()Ljava/lang/String;",                    (void *) getNativeBuildDate},
        {"setNativeEnvironmentVariable", "(Ljava/lang/String;Ljava/lang/String;)I", (void *) setNativeEnvironmentVariable},
        {"ignoreNativeSignal",           "(I)V",                                    (void *) ignoreNativeSignal},
        {"messagesInTransmit",           "(J)I",                                    (void *) messagesInTransmit}
};


#endif //AV_FFMPEGNATIVEMETHODREGISTER_H
