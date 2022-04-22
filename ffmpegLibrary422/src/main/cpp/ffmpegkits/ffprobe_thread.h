#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "../fftools/ffmpeg.h"
#include <pthread.h>
#include <string.h>

int ffprobe_thread_run_cmd(int cmdnum,char **argv);

void ffprobe_thread_exit(int ret);

void ffprobe_thread_callback(void (*cb)(int ret));

void ffprobe_thread_cancel();