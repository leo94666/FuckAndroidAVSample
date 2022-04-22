#include "libavcodec/avcodec.h"
#include "ffprobe_thread.h"
#include "../log/android_log.h"
#include "../fftools/ffmpeg.h"

pthread_t ntid;
char **argvs = NULL;
int num = 0;


void *thread(void *arg) {   //执行
    int result = ffmpeg_execute(num, argvs);
    ffprobe_thread_exit(result);
    return ((void *) 0);
}

/**
 * 新建子线程执行ffmpeg命令
 */
int ffprobe_thread_run_cmd(int cmdnum, char **argv) {
    num = cmdnum;
    argvs = argv;
    int temp = pthread_create(&ntid, NULL, thread, NULL);
    if (temp != 0) {
        LOGE("can't create thread: %s ", strerror(temp));
        return 1;
    }
    LOGI("create thread succes: %s ", strerror(temp));
    return 0;
}

static void (*ffmpeg_callback)(int ret);

/**
 * 注册线程回调
 */
void ffprobe_thread_callback(void (*cb)(int ret)) {
    ffmpeg_callback = cb;
}

/**
 * 退出线程
 */
void ffprobe_thread_exit(int ret) {
    if (ffmpeg_callback) {
        ffmpeg_callback(ret);
    }
    pthread_exit("ffmpeg_thread_exit");
}

/**
 * 取消线程
 */
void ffprobe_thread_cancel() {
    void *ret = NULL;
    pthread_join(ntid, &ret);
}
