/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

#include "JavBufferPool.h"
#include "libavutil/buffer.h"
#include "libavutil/mem.h"

JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferPool_nInit
( JNIEnv *env, jclass clazz, jint size )
{
    AVBufferPool *ret = av_buffer_pool_init( size, NULL );
    return *(jlong*)&ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferPool_nGet
( JNIEnv *env, jclass clazz, jlong ptr )
{
    AVBufferPool *pool = *(AVBufferPool**)&ptr;
    AVBufferRef *ref = av_buffer_pool_get( pool );
    return *(jlong*)&ref;
}


#include <stdio.h>


JNIEXPORT void JNICALL Java_bits_jav_util_JavBufferPool_nUninit
( JNIEnv *env, jclass clazz, jlong ptr )
{
    AVBufferPool *pool = *(AVBufferPool**)&ptr;
    av_buffer_pool_uninit( &pool );
    printf( "### DON\n"); fflush( 0 );
}
