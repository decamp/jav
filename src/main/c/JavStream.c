/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavStream.h"
#include "libavformat/avformat.h"

JNIEXPORT jint JNICALL Java_bits_jav_format_JavStream_nIndex
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).index;
}

JNIEXPORT jint JNICALL Java_bits_jav_format_JavStream_nId
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).id;
}

JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nCodecPointer
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return *(jlong*)&(**(AVStream**)&pointer).codec;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nFrameRate
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return *(jlong*)&(**(AVStream**)&pointer).r_frame_rate;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nTimeBase
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVStream *a = *(AVStream**)&pointer;
  return (((int64_t)a->time_base.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)a->time_base.den & 0xFFFFFFFFLL);  
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nStartTime
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).start_time;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nDuration
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).duration;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nNbFrames
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).nb_frames;
}

JNIEXPORT jint JNICALL Java_bits_jav_format_JavStream_nDiscard__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVStream**)&pointer).discard;    
}

JNIEXPORT void JNICALL Java_bits_jav_format_JavStream_nDiscard__JI
(JNIEnv *env, jclass clazz, jlong pointer, jint discard)
{
  (**(AVStream**)&pointer).discard = discard;
}

JNIEXPORT jlong JNICALL Java_bits_jav_format_JavStream_nMetadata
(JNIEnv *env, jclass clazz, jlong pointer)
{
  AVStream *s = *(AVStream**)&pointer;
  return *((jlong*)&s->metadata);
}
