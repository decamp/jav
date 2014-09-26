/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavFormatContext.h"
#include "libavformat/avformat.h"

#include <stdio.h>


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nAlloc
(JNIEnv* env, jclass clazz)
{
  AVFormatContext* f = avformat_alloc_context();
  return *(jlong*)&f;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nStreamCount
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVFormatContext**)&pointer).nb_streams;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nStreamPointer
(JNIEnv* env, jclass clazz, jlong pointer, jint index)
{
  return *(jlong*)&(**(AVFormatContext**)&pointer).streams[index];
}


JNIEXPORT jstring JNICALL Java_bits_jav_format_JavFormatContext_nFilename__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFormatContext* format = *(AVFormatContext**)&pointer;
  return (*env)->NewStringUTF( env, format->filename );
}


JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nFilename__JLjava_lang_String_2
(JNIEnv* env, jclass clazz, jlong pointer, jstring filename ) 
{
  AVFormatContext* format = *(AVFormatContext**)&pointer;
  
  if( filename == NULL ) {
    format->filename[0] = 0;
  }else{
    const char* cname = (*env)->GetStringUTFChars( env, filename, NULL );
    snprintf( format->filename, sizeof( format->filename), "%s", cname );
    (*env)->ReleaseStringUTFChars( env, filename, cname );
  }
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nMetadata__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return *((jlong*)&c->metadata);
}


JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nMetadata__JJ
(JNIEnv* env, jclass clazz, jlong pointer, jlong dictPointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  AVDictionary* d    = *(AVDictionary**)&dictPointer;
  
  if( c == NULL ) {
    return;
  }

  if( c->metadata != NULL ) {
    if( c->metadata == d ) {
      return;
    }
    av_dict_free( &c->metadata );
  }

  c->metadata = d;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nDuration
(JNIEnv *env, jclass clazz, jlong pointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return *((jlong*)&c->duration);
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nDurationEstimationMethod
(JNIEnv *env, jclass clazz, jlong pointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return av_fmt_ctx_get_duration_estimation_method( c );
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nAvio
(JNIEnv *env, jclass clazz, jlong pointer )
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return *(jlong*)&c->pb;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nFindBestStream
(JNIEnv* env, jclass clazz, jlong pointer, jint type, jint wantedNum, jint related, jint flags ) 
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return av_find_best_stream( c, type, wantedNum, related, NULL, flags );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nOpenInput
( JNIEnv *env, jclass clazz, jlong pointer, jstring jpath )
{
  const char* path = (*env)->GetStringUTFChars(env, jpath, NULL);
  AVFormatContext* formatContext = *(AVFormatContext**)&pointer;
  int err;
  
  //Open file
  err = avformat_open_input( &formatContext, path, NULL, NULL );
  (*env)->ReleaseStringUTFChars( env, jpath, path );
	if( err < 0 ) {
		return err;
	}
	
	//Retrieve stream information.
  err = avformat_find_stream_info( formatContext, NULL );
  if( err < 0 ) {
    avformat_free_context( formatContext );
    return err;
  }
  
  return 0;
}


JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nCloseInput
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  avformat_close_input( &c );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nReadFrame
(JNIEnv* env, jclass clazz, jlong pointer, jlong packetPointer)
{
  AVFormatContext* format = *(AVFormatContext**)&pointer;
  AVPacket* packet        = *(AVPacket**)&packetPointer;
  return av_read_frame( format, packet );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nSeekFrame
(JNIEnv* env, jclass clazz, jlong pointer, jint stream, jlong timestamp, jint flags)
{
  AVFormatContext* format = *(AVFormatContext**)&pointer;
  int64_t t = (int64_t)timestamp;
  return av_seek_frame(format, stream, t, flags);
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nOutputFormat__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOutputFormat* ret = (**(AVFormatContext**)&pointer).oformat;
  return *(jlong*)&ret;
}


JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nOutputFormat__JJ
(JNIEnv* env, jclass clazz, jlong pointer, jlong ofPointer)
{
  AVOutputFormat* ofmt = *(AVOutputFormat**)&ofPointer;
  (**(AVFormatContext**)&pointer).oformat = ofmt;
}


JNIEXPORT jlong JNICALL Java_bits_jav_format_JavFormatContext_nNewStream
(JNIEnv* env, jclass clazz, jlong pointer, jlong codecPointer)
{
  AVFormatContext* f  = *(AVFormatContext**)&pointer;
  AVCodec* c          = *(AVCodec**)&codecPointer;
  AVStream* ret       = avformat_new_stream( f, c );
  
	if( ret ) {
		avcodec_get_context_defaults3( ret->codec, c );
		ret->codec->codec_id = c->id;
		//if( f->oformat && ( f->oformat->flags & AVFMT_GLOBALHEADER ) ) {
		//	ret->codec->flags |= CODEC_FLAG_GLOBAL_HEADER;
		//}
	}
	
  return *(jlong*)&ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nOpenOutput
( JNIEnv *env, jclass clazz, jlongArray jptr, jstring jpath, jlong fmtPtr, jstring jfmtName )
{
	AVFormatContext *c;
	const char *path     = jpath ? (*env)->GetStringUTFChars( env, jpath, NULL ) : NULL;
	AVOutputFormat* ofmt = *(AVOutputFormat**)&fmtPtr;
	const char *fmtName  = jfmtName ? (*env)->GetStringUTFChars( env, jfmtName, NULL ) : NULL;
	
	int err = avformat_alloc_output_context2( &c, ofmt, fmtName, path );
	
	if( path ) {
		(*env)->ReleaseStringUTFChars( env, jpath, path );
	}
	if( fmtName ) {
		(*env)->ReleaseStringUTFChars( env, jfmtName, fmtName );
	}
		
	if( err < 0 ) {
		return err;
	}
	
	jlong *ptr = (*env)->GetLongArrayElements( env, jptr, NULL );
	ptr[0] = (jlong)c;
	(*env)->ReleaseLongArrayElements( env, jptr, ptr, 0 );
	
	return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nOpenOutputFile
( JNIEnv *env, jclass clazz, jlong ptr, jstring jpath )
{
	AVFormatContext *c = *(AVFormatContext**)&ptr;
	const char *path = (*env)->GetStringUTFChars( env, jpath, NULL );
	int err = avio_open( &c->pb, path, AVIO_FLAG_WRITE );
	(*env)->ReleaseStringUTFChars( env, jpath, path );
	return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nWriteHeader
(JNIEnv* env, jclass clazz, jlong pointer ) 
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return avformat_write_header( c, NULL );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nWriteFrame
(JNIEnv* env, jclass clazz, jlong pointer, jlong packetPointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  AVPacket* p        = *(AVPacket**)&packetPointer;
  
  return av_write_frame( c, p );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nWriteInterleavedFrame
(JNIEnv* env, jclass clazz, jlong pointer, jlong packetPointer)
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  AVPacket* p        = *(AVPacket**)&packetPointer;
  
  return av_interleaved_write_frame( c, p );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nWriteTrailer
(JNIEnv* env, jclass clazz, jlong pointer ) 
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
  return av_write_trailer( c );
}


JNIEXPORT jint JNICALL Java_bits_jav_format_JavFormatContext_nOutFormatFlags
( JNIEnv *env, jclass clazz, jlong pointer )
{
	AVFormatContext *c = *(AVFormatContext**)&pointer;
	return c->oformat ? c->oformat->flags : 0;
}

JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nCloseOutputFile
( JNIEnv *env, jclass clazz, jlong pointer )
{
	AVFormatContext *c = *(AVFormatContext**)&pointer;
	if( c->pb ) {
		avio_close( c->pb );
	}
}


JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nFreeContext
( JNIEnv *env, jclass clazz, jlong pointer )
{
  AVFormatContext* c = *(AVFormatContext**)&pointer;
	avformat_free_context( c );
  //if( !c || !c->pb ) {
  //  return 0;
	//}
  //return avio_close( c->pb );
}


// JNIEXPORT void JNICALL Java_bits_jav_format_JavFormatContext_nFlushOutput
// (JNIEnv* env, jclass clazz, jlong pointer)
// {
//   AVFormatContext* c = *(AVFormatContext**)&pointer;
//   if( c && c->pb ) {
//     avio_flush( c->pb );
//   }
// }
