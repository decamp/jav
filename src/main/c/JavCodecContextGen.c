/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include <stdio.h>
#include "libavcodec/avcodec.h"
#include "JavCodecContext.h"


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nCodecType__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).codec_type;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nCodecType__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).codec_type = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nCodecId__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).codec_id;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nCodecId__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).codec_id = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nBitRate__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).bit_rate;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nBitRate__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).bit_rate = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSize__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).frame_size;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSize__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).frame_size = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFlags__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).flags;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFlags__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).flags = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFlags2__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).flags2;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFlags2__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).flags2 = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nPixFmt__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).pix_fmt;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nPixFmt__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).pix_fmt = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nSampleFmt__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).sample_fmt;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nSampleFmt__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).sample_fmt = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nWidth__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).width;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nWidth__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).width = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nHeight__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).height;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nHeight__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).height = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nChannels__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).channels;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nChannels__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).channels = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nChannelLayout__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVCodecContext**)&pointer).channel_layout;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nChannelLayout__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVCodecContext**)&pointer).channel_layout = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nSampleRate__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).sample_rate;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nSampleRate__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).sample_rate = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nGopSize__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).gop_size;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nGopSize__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).gop_size = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMaxBFrames__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).max_b_frames;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMaxBFrames__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).max_b_frames = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nKeyintMin__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).keyint_min;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nKeyintMin__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).keyint_min = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nProfile__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).profile;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nProfile__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).profile = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nLevel__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).level;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nLevel__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).level = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nBitRateTolerance__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).bit_rate_tolerance;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nBitRateTolerance__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).bit_rate_tolerance = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nGlobalQuality__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).global_quality;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nGlobalQuality__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).global_quality = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nCompressionLevel__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).compression_level;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nCompressionLevel__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).compression_level = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRefcountedFrames__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).refcounted_frames;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRefcountedFrames__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).refcounted_frames = val; 
}

JNIEXPORT jfloat JNICALL Java_bits_jav_codec_JavCodecContext_nQcompress__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jfloat)(**(AVCodecContext**)&pointer).qcompress;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nQcompress__JF
( JNIEnv *env, jclass clazz, jlong pointer, jfloat val )
{
  (**(AVCodecContext**)&pointer).qcompress = val; 
}

JNIEXPORT jfloat JNICALL Java_bits_jav_codec_JavCodecContext_nQblur__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jfloat)(**(AVCodecContext**)&pointer).qblur;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nQblur__JF
( JNIEnv *env, jclass clazz, jlong pointer, jfloat val )
{
  (**(AVCodecContext**)&pointer).qblur = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nQmin__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).qmin;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nQmin__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).qmin = val; 
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nQmax__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).qmax;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nQmax__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).qmax = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMaxQdiff__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).max_qdiff;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMaxQdiff__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).max_qdiff = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nScenechangeThreshold__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).scenechange_threshold;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nScenechangeThreshold__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).scenechange_threshold = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMeRange__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).me_range;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMeRange__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).me_range = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMeCmp__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).me_cmp;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMeCmp__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).me_cmp = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMeSubCmp__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).me_sub_cmp;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMeSubCmp__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).me_sub_cmp = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nCoderType__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).coder_type;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nCoderType__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).coder_type = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nContextModel__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).context_model;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nContextModel__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).context_model = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipThreshold__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).frame_skip_threshold;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipThreshold__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).frame_skip_threshold = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipFactor__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).frame_skip_factor;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipFactor__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).frame_skip_factor = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipExp__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).frame_skip_exp;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipExp__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).frame_skip_exp = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipCmp__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).frame_skip_cmp;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFrameSkipCmp__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).frame_skip_cmp = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nTrellis__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).trellis;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nTrellis__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).trellis = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMinPredictionOrder__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).min_prediction_order;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMinPredictionOrder__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).min_prediction_order = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nMaxPredictionOrder__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).max_prediction_order;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nMaxPredictionOrder__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).max_prediction_order = val; 
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nTimecodeFrameStart__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jlong)(**(AVCodecContext**)&pointer).timecode_frame_start;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nTimecodeFrameStart__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong val )
{
  (**(AVCodecContext**)&pointer).timecode_frame_start = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRcBufferSize__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).rc_buffer_size;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcBufferSize__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).rc_buffer_size = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRcOverrideCount__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).rc_override_count;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcOverrideCount__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).rc_override_count = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRcMaxRate__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).rc_max_rate;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcMaxRate__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).rc_max_rate = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRcMinRate__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).rc_min_rate;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcMinRate__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).rc_min_rate = val; 
}

JNIEXPORT jfloat JNICALL Java_bits_jav_codec_JavCodecContext_nRcMaxAvailableVbvUse__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jfloat)(**(AVCodecContext**)&pointer).rc_max_available_vbv_use;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcMaxAvailableVbvUse__JF
( JNIEnv *env, jclass clazz, jlong pointer, jfloat val )
{
  (**(AVCodecContext**)&pointer).rc_max_available_vbv_use = val; 
}

JNIEXPORT jfloat JNICALL Java_bits_jav_codec_JavCodecContext_nRcMinVbvOverflowUse__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jfloat)(**(AVCodecContext**)&pointer).rc_min_vbv_overflow_use;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcMinVbvOverflowUse__JF
( JNIEnv *env, jclass clazz, jlong pointer, jfloat val )
{
  (**(AVCodecContext**)&pointer).rc_min_vbv_overflow_use = val; 
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nRcInitialBufferOccupancy__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (jint)(**(AVCodecContext**)&pointer).rc_initial_buffer_occupancy;
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nRcInitialBufferOccupancy__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
  (**(AVCodecContext**)&pointer).rc_initial_buffer_occupancy = val; 
}

