/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavPacket.h"
#include "libavcodec/avcodec.h"

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nAlloc
(JNIEnv* env, jclass clazz)
{
  AVPacket* packet = (AVPacket*)av_mallocz( sizeof(AVPacket) );
  av_init_packet( packet );
  return *(jlong*)&packet;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nInit
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVPacket* packet = *(AVPacket**)&pointer;
  av_init_packet( packet );
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nFree
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVPacket* packet = *(AVPacket**)&pointer;
  if(packet->data) {
    av_free_packet( packet );
  }
  
  free(packet);
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nFreeData
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVPacket* packet = *(AVPacket**)&pointer;
  if( packet->data ) {
    av_free_packet( packet );
  }
}


JNIEXPORT int JNICALL Java_bits_jav_codec_JavPacket_nAllocData
(JNIEnv* env, jclass clazz, jlong pointer, jint size)
{
  AVPacket* packet = *(AVPacket**)&pointer;
  if( packet->data ) {
    av_free_packet( packet );
  }
  
  return av_new_packet( packet, size );
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nBuf__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVBufferRef *ref = (*(AVPacket**)&pointer)->buf;
    if( ref == NULL ) {
        return 0;
    }
    ref = av_buffer_ref( ref );
    return *(jlong*)&ref;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nBuf__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong bufPointer )
{
  AVPacket *packet = *(AVPacket**)&pointer;
  av_buffer_unref( &packet->buf );
  if( bufPointer ) {
    packet->buf = av_buffer_ref( *(AVBufferRef**)&bufPointer );
  }
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nBufSize
(JNIEnv *env, jclass clazz, jlong pointer)
{
  AVBufferRef *ref = (*(AVPacket**)&pointer)->buf;
  return ref ? ref->size : 0;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nData__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return *(jlong*)&(**(AVPacket**)&pointer).data;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nData__JJ
(JNIEnv *env, jclass clazz, jlong pointer, jlong dataPointer )
{
	(**(AVPacket**)&pointer).data = *(void**)&dataPointer;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nSize__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVPacket**)&pointer).size;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nSize__JI
(JNIEnv* env, jclass clazz, jlong pointer, jint size)
{
  (**(AVPacket**)&pointer).size = size;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nMoveData
(JNIEnv *env, jclass clazz, jlong pointer, jint n)
{
	AVPacket *p = *(AVPacket**)&pointer;
	p->data += n;
	p->size -= n;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nResetData
(JNIEnv *env, jclass clazz, jlong pointer)
{
  AVPacket *p = *(AVPacket**)&pointer;
  if( p->buf ) {
    p->data = p->buf->data;
    p->size = p->buf->size;
    return p->size;
  }
  return p->size;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nPts__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (**(AVPacket**)&pointer).pts;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nPts__JJ
( JNIEnv* env, jclass clazz, jlong pointer, jlong pts )
{
  (**(AVPacket**)&pointer).pts = pts;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nDts__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (**(AVPacket**)&pointer).dts;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nDts__JJ
( JNIEnv* env, jclass clazz, jlong pointer, jlong dts )
{
  (**(AVPacket**)&pointer).dts = dts;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nDuration__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (**(AVPacket**)&pointer).duration;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_Duration__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint duration )
{
  (**(AVPacket**)&pointer).duration = duration;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nStreamIndex__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (**(AVPacket**)&pointer).stream_index;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nStreamIndex__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint stream_index )
{
  (**(AVPacket**)&pointer).stream_index = stream_index;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavPacket_nFlags__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
  return (**(AVPacket**)&pointer).flags;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nFlags__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint flags )
{
  (**(AVPacket**)&pointer).flags = flags;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nPos__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    return (**(AVPacket**)&pointer).pos;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nPos__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong pos ) 
{
    (**(AVPacket**)&pointer).pos = pos;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavPacket_nConvergenceDuration__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    return (**(AVPacket**)&pointer).convergence_duration;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavPacket_nConvergenceDuration__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong cd ) 
{
    (**(AVPacket**)&pointer).convergence_duration = cd;
}

