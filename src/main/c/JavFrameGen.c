/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include <stdio.h>
#include "libavcodec/avcodec.h"
#include "JavFrame.h"

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nWidth__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).width;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nWidth__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).width = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nHeight__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).height;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nHeight__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).height = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nNbSamples__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).nb_samples;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nNbSamples__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).nb_samples = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nFormat__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).format;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nFormat__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).format = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nKeyFrame__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).key_frame;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nKeyFrame__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).key_frame = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nPictType__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).pict_type;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nPictType__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).pict_type = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nPts__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVFrame**)&pointer).pts;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nPts__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVFrame**)&pointer).pts = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nPktPts__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVFrame**)&pointer).pkt_pts;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nPktPts__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVFrame**)&pointer).pkt_pts = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nPktDts__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVFrame**)&pointer).pkt_dts;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nPktDts__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVFrame**)&pointer).pkt_dts = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nCodedPictureNumber__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).coded_picture_number;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nCodedPictureNumber__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).coded_picture_number = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nDisplayPictureNumber__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).display_picture_number;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDisplayPictureNumber__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).display_picture_number = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nQuality__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).quality;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nQuality__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).quality = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nRepeatPict__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).repeat_pict;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nRepeatPict__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).repeat_pict = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nInterlacedFrame__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).interlaced_frame;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nInterlacedFrame__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).interlaced_frame = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nTopFieldFirst__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).top_field_first;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nTopFieldFirst__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).top_field_first = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nPaletteHasChanged__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).palette_has_changed;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nPaletteHasChanged__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).palette_has_changed = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nSampleRate__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).sample_rate;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nSampleRate__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).sample_rate = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nChannelLayout__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVFrame**)&pointer).channel_layout;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nChannelLayout__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVFrame**)&pointer).channel_layout = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nDecodeErrorFlags__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).decode_error_flags;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDecodeErrorFlags__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).decode_error_flags = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nChannels__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVFrame**)&pointer).channels;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nChannels__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVFrame**)&pointer).channels = val; 
}

