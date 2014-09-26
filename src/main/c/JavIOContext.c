/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavIOContext.h"
#include "libavformat/avio.h"

JNIEXPORT jlong JNICALL Java_bits_jav_format_JavIOContext_nBuffer
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return *( (jlong*) &(**(AVIOContext**)&ptr).buffer );
}

JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nBufferSize
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).buffer_size;
}

JNIEXPORT jlong JNICALL Java_bits_jav_format_JavIOContext_nBufferPtr
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return *( (jlong*) &(**(AVIOContext**)&ptr).buf_ptr );
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavIOContext_nBufferEnd
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return *( (jlong*) &(**(AVIOContext**)&ptr).buf_end );
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavIOContext_nPos
( JNIEnv *env, jclass clazz, jlong ptr)
{
  return (**(AVIOContext**)&ptr).pos;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavIOContext_nPosPtr
( JNIEnv *env, jclass clazz, jlong ptr)
{
  AVIOContext* c = *(AVIOContext**)&ptr;
  return c->pos + ( c->buf_ptr - c->buffer );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nMustFlush
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).must_flush;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nEofReached
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).eof_reached;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nWriteFlag
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).write_flag;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nMaxPacketSize
( JNIEnv *env, jclass clazz, jlong ptr ) 
{
  return (**(AVIOContext**)&ptr).max_packet_size;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nError
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).error;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nSeekable
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).seekable;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nMaxSize
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).maxsize;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavIOContext_nDirect
( JNIEnv *env, jclass clazz, jlong ptr )
{
  return (**(AVIOContext**)&ptr).direct;
}

