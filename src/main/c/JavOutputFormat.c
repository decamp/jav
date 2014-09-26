/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavOutputFormat.h"
#include "libavformat/avformat.h"

#include <stdio.h>


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavOutputFormat_nNextOutputFormat
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* f = *(AVOutputFormat**)&pointer;
  f = av_oformat_next( f );
  return *(jlong*)&f;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavOutputFormat_nGuessFormat
(JNIEnv* env, jclass clazz, jstring shortName, jstring fileName, jstring mimeType)
{
  const char* cname = NULL;
  const char* cfile = NULL;
  const char* cmime = NULL;
  
  if( shortName ) {
    cname = (*env)->GetStringUTFChars( env, shortName, NULL );
  }
  
  if( fileName ) {
    cfile = (*env)->GetStringUTFChars( env, fileName, NULL );
  }
  
  if( mimeType ) {
    cmime = (*env)->GetStringUTFChars( env, mimeType, NULL );
  }
  
  AVOutputFormat* ret = av_guess_format( cname, cfile, cmime );
  
  if( cname ) {
    (*env)->ReleaseStringUTFChars( env, shortName, cname );
  }
  
  if( cfile ) {
    (*env)->ReleaseStringUTFChars( env, fileName, cfile );
  }
  
  if( cmime ) {
    (*env)->ReleaseStringUTFChars( env, mimeType, cmime );
  }
  
  return *(jlong*)&ret;
}


JNIEXPORT jstring JNICALL Java_bits_jav_format_JavOutputFormat_nName
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* f = *(AVOutputFormat**)&pointer;
  if( !f->name )
    return NULL;
    
  return (*env)->NewStringUTF( env, f->name );
}


JNIEXPORT jstring JNICALL Java_bits_jav_format_JavOutputFormat_nLongName
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* f = *(AVOutputFormat**)&pointer;
  if( !f->long_name )
    return NULL;
    
  return (*env)->NewStringUTF( env, f->long_name );
}


JNIEXPORT jstring JNICALL Java_bits_jav_format_JavOutputFormat_nMimeType
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* f = *(AVOutputFormat**)&pointer;
  if( !f->mime_type )
    return NULL;
    
  return (*env)->NewStringUTF( env, f->mime_type );
}


JNIEXPORT jstring JNICALL Java_bits_jav_format_JavOutputFormat_nExtensions
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* f = *(AVOutputFormat**)&pointer;
  if( !f->extensions )
    return NULL;
    
  return (*env)->NewStringUTF( env, f->extensions );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavOutputFormat_nAudioCodec
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOutputFormat**)&pointer).audio_codec;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavOutputFormat_nVideoCodec
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOutputFormat**)&pointer).video_codec;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavOutputFormat_nSubtitleCodec
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOutputFormat**)&pointer).subtitle_codec;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavOutputFormat_nFlags
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOutputFormat**)&pointer).flags;
}


