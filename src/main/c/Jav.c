/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

#include "Jav.h"
#include "JavCore.h"
#include "libavformat/avformat.h"
#include "libavutil/log.h"

JavaVM* jav_jvm;

//JNIEXPORT jint JNICALL JNI_OnLoad( JavaVM *jvm, void *reserved )
//{
//    jav_jvm = jvm;
//    return 0;
//}


JNIEXPORT void JNICALL Java_bits_jav_Jav_nInit
( JNIEnv* env, jclass clazz )
{
    int err = (*env)->GetJavaVM(env, &jav_jvm);
    av_register_all();
    avcodec_register_all();
    av_log_set_level( AV_LOG_FATAL );
}