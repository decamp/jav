/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "SwrContext.h"
#include "libswresample/swresample.h"
#include "libavcodec/avcodec.h"


JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nAlloc
(JNIEnv* env, jclass clazz)
{
    struct SwrContext* context = swr_alloc();
    return *(jlong*)&context;
}


JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nInit
( JNIEnv* env, jclass clazz, jlong pointer )
{
    struct SwrContext* context = *(struct SwrContext**)&pointer;
    return swr_init( context );
}


JNIEXPORT void JNICALL Java_bits_jav_swresample_SwrContext_nFree
( JNIEnv* env, jclass clazz, jlong pointer )
{
    struct SwrContext* context = *(struct SwrContext**)&pointer;
    swr_free( &context );
}


#include <stdio.h>

JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nConvert
( JNIEnv *env, jclass clazz, jlong ptr, jlong srcPtr, jint srcLen, jlong dstPtr, jint dstLen )
{
    struct SwrContext *context = *(struct SwrContext**)&ptr;
    const uint8_t **src = *(const uint8_t***)&srcPtr;
    uint8_t **dst = *(uint8_t***)&dstPtr;
    return swr_convert( context, dst, dstLen, src, srcLen );
}


JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nConvertPacked
( JNIEnv *env, jclass clazz, jlong ptr, jobject srcBuf, jint srcOff, jint srcLen, jobject dstBuf, jint dstOff, jint dstLen )
{
    uint8_t *in  = srcBuf ? (*env)->GetDirectBufferAddress( env, srcBuf ) + srcOff : 0;
    uint8_t *out = (*env)->GetDirectBufferAddress( env, dstBuf ) + dstOff;
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_convert( context, &out, dstLen, (const uint8_t**)&in, srcLen );
}


JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nNextPts
( JNIEnv *env, jclass clazz, jlong ptr, jlong pts)
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_next_pts( context, pts );
}

JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetCompensation
( JNIEnv *env, jclass clazz, jlong ptr, jint sampleDelta, jint compDist )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_set_compensation( context, sampleDelta, compDist );
}


JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetChannelMapping
( JNIEnv *env, jclass clazz, jlong ptr, jintArray mapArr )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    jint *map = (*env)->GetIntArrayElements( env, mapArr, NULL );
    int err = swr_set_channel_mapping( context, map );
    (*env)->ReleaseIntArrayElements( env, mapArr, map, 0 );
    return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetMatrix
( JNIEnv *env, jclass clazz, jlong ptr, jdoubleArray matrixArr, jint stride )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    jdouble *matrix = (*env)->GetDoubleArrayElements( env, matrixArr, NULL );
    int err = swr_set_matrix( context, matrix, stride );
    (*env)->ReleaseDoubleArrayElements( env, matrixArr, matrix, 0 );
    return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nDropOutput
( JNIEnv *env, jclass clazz, jlong ptr, jint count )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_drop_output( context, count );
}



JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nInjectSilence
( JNIEnv *env, jclass clazz, jlong ptr, jint count )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_inject_silence( context, count );
}



JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nGetDelay
( JNIEnv *env, jclass clazz, jlong ptr, jlong base )
{
    struct SwrContext* context = *(struct SwrContext**)&ptr;
    return swr_get_delay( context, base );
}
