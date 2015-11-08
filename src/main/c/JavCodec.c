/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

#include "JavCodec.h"
#include "libavcodec/avcodec.h"
#include "libavutil/rational.h"


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodec_nFindEncoder
( JNIEnv* env, jclass clazz, jint id )
{
  AVCodec* codec = avcodec_find_encoder( id );
  if( codec == NULL ) {
    return 0;
  }
  
  return *(jlong*)&codec;
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodec_nFindDecoder
( JNIEnv* env, jclass clazz, jint id )
{
  AVCodec* codec = avcodec_find_decoder( id );
  if( codec == NULL ) {
    return 0;
  }
    
  return *(jlong*)&codec;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nType
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVCodec**)&pointer).type;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nId
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVCodec**)&pointer).id;
}



JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nCapabilities
(JNIEnv *env, jclass clazz, jlong pointer)
{
	return (**(AVCodec**)&pointer).capabilities;
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nSupportedFramerateCount
( JNIEnv *env, jclass clazz, jlong pointer )
{
	AVCodec* codec = *(AVCodec**)&pointer;
	const AVRational* rat = codec->supported_framerates;
	int i = 0;
	while( rat != NULL && ( rat->num != 1 || rat->den != 1 ) ) {
		i++;
		rat++;
	}
	return i;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodec_nSupportedFramerate
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
	const AVRational* rat = (**(AVCodec**)&pointer).supported_framerates;

  while( 1 ) {
		if( rat == NULL || ( rat->num == 1 && rat->den == 1 ) ) {
			return 0L;
		}
		if( idx == 0 ) {
			return ( ((int64_t)rat->num << 32LL) & 0xFFFFFFFF00000000LL ) | 
				       ((int64_t)rat->den          & 0x00000000FFFFFFFFLL ); 
		}
		idx--;
		rat++;
	}
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nPixelFormatCount
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const enum AVPixelFormat* p = codec->pix_fmts;
  
  if( p == NULL ) {
    return 0;
  }
    
  int i = 0;
  while( *p != -1 ) {
    i++;
    p++;
  }
  
  return i;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nPixelFormat
(JNIEnv* env, jclass clazz, jlong pointer, jint idx)
{
  AVCodec* codec              = *(AVCodec**)&pointer;
  const enum AVPixelFormat* p = codec->pix_fmts;
  
  if( p == NULL )
    return -1;
    
  while( idx > 0 && *p != -1 ) {
    idx--;
    p++;
  }
  
  return *p;
}



JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nSampleRateCount
( JNIEnv* env, jclass clazz, jlong pointer )
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const int* p   = codec->supported_samplerates;
  
  if( p == NULL ) 
    return 0;
  
  int i = 0;
  while( *p != 0 ) {
    i++;
    p++;
  }
  
  return i;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nSampleRate
( JNIEnv* env, jclass clazz, jlong pointer, jint idx ) 
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const int* p = codec->supported_samplerates;
  
  if( p == NULL )
    return 0;
  
  while( idx > 0 && *p != 0 ) {
    idx--;
    p++;
  }
  
  return *p;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nSampleFormatCount
( JNIEnv* env, jclass clazz, jlong pointer ) 
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const enum AVSampleFormat* p   = codec->sample_fmts;
  
  if( p == NULL ) 
    return 0;
  
  int i = 0;
  while( *p != -1 ) {
    i++;
    p++;
  }
  
  return i;
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nSampleFormat
( JNIEnv* env, jclass clazz, jlong pointer, jint idx )
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const enum AVSampleFormat* p = codec->sample_fmts;
  
  if( p == NULL )
    return -1;
    
  while( idx > 0 && *p != -1 ) {
    idx--;
    p++;
  }
  
  return *p;
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavCodec_nChannelLayoutCount
( JNIEnv* env, jclass clazz, jlong pointer )
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const uint64_t* p = codec->channel_layouts;
  
  if( p == NULL )
    return 0;
    
  int ret = 0;
  while( *p != 0 ) {
    ret++;
    p++;
  }
  
  return ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodec_nChannelLayout
( JNIEnv* env, jclass clazz, jlong pointer, jint idx ) 
{
  AVCodec* codec = *(AVCodec**)&pointer;
  const uint64_t* p = codec->channel_layouts;
  
  if( p == NULL )
    return 0;
    
  while( idx > 0 && *p != 0 ) {
    idx--;
    p++;
  }
  
  return *p;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavCodec_nPrivClass
( JNIEnv *env, jclass clazz, jlong pointer )
{
	AVCodec* codec = *(AVCodec**)&pointer;
	return (jlong)codec->priv_class;
}
