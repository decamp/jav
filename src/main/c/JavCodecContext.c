/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavCodecContext.h"
#include "libavcodec/avcodec.h"
#include <stdio.h>

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nAlloc
(JNIEnv* env, jclass clazz, jlong codecPointer)
{
  AVCodecContext* ret = avcodec_alloc_context3( *(AVCodec**)&codecPointer );
  return *(jlong*)&ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nOpen
(JNIEnv* env, jclass clazz, jlong pointer, jlong codecPointer)
{
  AVCodecContext* context = *(AVCodecContext**)&pointer;
  AVCodec* codec          = *(AVCodec**)&codecPointer;
  int err;
	
  //Open codec
	err = avcodec_open2( context, codec, NULL );
	
  if( err < 0 ) {
		//char str[64];
		//av_strerror( err, str, 64 );
		//printf( "CODEC FAILED : 0x%08X  %s\n", err, str );
        //return 0x03000002; //ERROR_CODEC_OPEN_FAILED;
		return err;
	}
   
  return 0;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nClose
( JNIEnv* env, jclass clazz, jlong pointer )
{
  AVCodecContext* context = *(AVCodecContext**)&pointer;
 	int err;
  
	if( context->codec ) {
		err = avcodec_close( context );
		if( err < 0 ) {
			//return 0x03000003; //ERROR_CODEC_CLOSE_FAILED
			return err;
		}
	} else {
		return 0xBCBEBC02; // ERROR_CODEC_ALREADY_CLOSED
	}
  
  return 0;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nDecodeVideo
(JNIEnv* env, jclass clazz, jlong pointer, jlong packetPointer, jlong framePointer, jintArray gotFrameOut)
{
  AVCodecContext* codec = *(AVCodecContext**)&pointer;
  AVPacket* packet      = *(AVPacket**)&packetPointer;
  AVFrame* frame        = *(AVFrame**)&framePointer;
  jint* gotFrame        = (*env)->GetIntArrayElements(env, gotFrameOut, NULL);
  
  int err = avcodec_decode_video2(codec, frame, gotFrame, packet);
  
  (*env)->ReleaseIntArrayElements(env, gotFrameOut, gotFrame, 0);
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nEncodeVideo
(JNIEnv* env, jclass clazz, jlong pointer, jlong framePointer, jlong packetPointer, jintArray gotPacketOut)
{
  AVCodecContext* codec = *(AVCodecContext**)&pointer;
  AVFrame* frame        = *(AVFrame**)&framePointer;
  AVPacket* packet      = *(AVPacket**)&packetPointer;
  jint* gotPacket       = (*env)->GetIntArrayElements( env, gotPacketOut, NULL );
  
  int err = avcodec_encode_video2( codec, packet, frame, gotPacket );
  
  (*env)->ReleaseIntArrayElements( env, gotPacketOut, gotPacket, 0 );
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nDecodeAudio
(JNIEnv* env, jclass clazz, jlong pointer, jlong packetPointer, jlong framePointer, jintArray gotFrameOut)
{
  AVCodecContext* codec = *(AVCodecContext**)&pointer;
  AVPacket* packet      = *(AVPacket**)&packetPointer;
  AVFrame* frame        = *(AVFrame**)&framePointer;
  jint* gotFrame        = (*env)->GetIntArrayElements( env, gotFrameOut, NULL );
  
  int err = avcodec_decode_audio4( codec, frame, gotFrame, packet );
  
  (*env)->ReleaseIntArrayElements( env, gotFrameOut, gotFrame, 0 );
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nEncodeAudio
(JNIEnv* env, jclass clazz, jlong pointer, jlong framePointer, jlong packetPointer, jintArray gotPacketOut)
{
  AVCodecContext* codec = *(AVCodecContext**)&pointer;
  AVFrame* frame        = *(AVFrame**)&framePointer;
  AVPacket* packet      = *(AVPacket**)&packetPointer;
  jint* gotPacket       = (*env)->GetIntArrayElements( env, gotPacketOut, NULL );
  
  int err = avcodec_encode_audio2( codec, packet, frame, gotPacket );
  
  (*env)->ReleaseIntArrayElements( env, gotPacketOut, gotPacket, 0 );
  return err;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nFlush
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVCodecContext* codec = *(AVCodecContext**)&pointer;
  avcodec_flush_buffers( codec );
}


JNIEXPORT jstring JNICALL Java_bits_jav_codec_JavCodecContext_nCodecName
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVCodecContext* c = *(AVCodecContext**)&pointer;
  if( !c || !c->codec || !c->codec->name ) {
    return NULL;
  }
  return (*env)->NewStringUTF( env, c->codec->name );
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nTimeBase__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVCodecContext* c = *(AVCodecContext**)&pointer;
	return (((int64_t)c->time_base.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)c->time_base.den & 0xFFFFFFFFLL);  
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nTimeBase__JII
(JNIEnv* env, jclass clazz, jlong pointer, jint num, jint den)
{
  AVCodecContext* c = *(AVCodecContext**)&pointer;
	c->time_base.num = num;
	c->time_base.den = den;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nSampleAspectRatio__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVCodecContext* c = *(AVCodecContext**)&pointer;
  return (((int64_t)c->sample_aspect_ratio.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)c->sample_aspect_ratio.den & 0xFFFFFFFFLL);  
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nPrivDataPointer
( JNIEnv *env, jclass clazz, jlong pointer )
{
	AVCodecContext *c = *(AVCodecContext**)&pointer;
	return *(jlong*)&c->priv_data;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodecContext_nExtraData__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVCodecContext *c = *(AVCodecContext**)&pointer;
    return *(jlong*)&c->extradata;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodecContext_nExtraDataSize
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVCodecContext *c = *(AVCodecContext**)&pointer;
    return (jint)c->extradata_size;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nExtraData__JJI
( JNIEnv *env, jclass clazz, jlong pointer, jlong bufPtr, jint len )
{
    AVCodecContext *c = *(AVCodecContext**)&pointer;
    c->extradata      = *(void**)&bufPtr;
    c->extradata_size = (int)len;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavCodecContext_nSampleAspectRatio__JII
(JNIEnv* env, jclass clazz, jlong pointer, jint num, jint den)
{
  AVCodecContext* c = *(AVCodecContext**)&pointer;
  c->sample_aspect_ratio = (AVRational){ num, den };
}


  
