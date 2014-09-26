/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

#include "JavFrame.h"
#include "libavcodec/avcodec.h"

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nAllocFrame
(JNIEnv* env, jclass clazz)
{
  AVFrame* f = avcodec_alloc_frame();
  return *(jlong*)&f;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nFree
(JNIEnv* env, jclass clazz, jlong pointer)
{
  av_frame_unref( *(AVFrame**)&pointer );
  av_free( *(AVFrame**)&pointer );
}

JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nFreeData
(JNIEnv *env, jclass clazz, jlong pointer)
{
	av_frame_unref( *(AVFrame**)&pointer );
}



JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nData
(JNIEnv* env, jclass clazz, jlong pointer)
{
    uint8_t** arr = (**(AVFrame**)&pointer).data;
    return *(jlong*)&arr;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nDataPointer__JI
(JNIEnv* env, jclass clazz, jlong pointer, jint layer)
{
  return *(jlong*)&((**(AVFrame**)&pointer).data[layer]);
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDataPointer__JIJ
(JNIEnv* env, jclass clazz, jlong pointer, jint layer, jlong dataPointer)
{
  (**(AVFrame**)&pointer).data[layer] = *(uint8_t**)&dataPointer;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDataPointers
(JNIEnv* env, jclass clazz, jlong pointer, jlongArray jarr)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  jlong* vals    = (*env)->GetLongArrayElements(env, jarr, 0);
  int i;
  for( i = 0; i < AV_NUM_DATA_POINTERS; i++ ) {
    vals[i] = *(jlong*)&(frame->data[i]);
  }
  (*env)->ReleaseLongArrayElements(env, jarr, vals, 0);
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nExtendedData__J
( JNIEnv* env, jclass clazz, jlong pointer)
{
    AVFrame* frame = *(AVFrame**)&pointer;
    return *(jlong*)&(**(AVFrame**)&pointer).extended_data;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedData__JJ
(JNIEnv* env, jclass clazz, jlong pointer, jlong val)
{
    (**(AVFrame**)&pointer).extended_data = *(uint8_t***)&val;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nExtendedDataPointer__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint layer )
{
	return *(jlong*)&(**(AVFrame**)&pointer).extended_data[layer];
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedDataPointer__JIJ
( JNIEnv *env, jclass clazz, jlong pointer, jint layer, jlong dataPointer )
{
  (**(AVFrame**)&pointer).extended_data[layer] = *(uint8_t**)&dataPointer;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nLineSize__JI
(JNIEnv* env, jclass clazz, jlong pointer, jint layer)
{
  return (**(AVFrame**)&pointer).linesize[layer];
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nLineSize__JII
(JNIEnv* env, jclass clazz, jlong pointer, jint layer, jint lineSize)
{
  (**(AVFrame**)&pointer).linesize[layer] = lineSize;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nLineSizes
(JNIEnv* env, jclass clazz, jlong pointer, jintArray jarr)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  jint* vals = (*env)->GetIntArrayElements(env, jarr, 0);
  int i;
  for( i = 0; i < AV_NUM_DATA_POINTERS; i++ ) {
    vals[i] = frame->linesize[i];
  }
  (*env)->ReleaseIntArrayElements(env, jarr, vals, 0);
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nSampleAspectRatio__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFrame* c = *(AVFrame**)&pointer;
  return (((int64_t)c->sample_aspect_ratio.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)c->sample_aspect_ratio.den & 0xFFFFFFFFLL);  
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nSampleAspectRatio__JII
(JNIEnv* env, jclass clazz, jlong pointer, jint num, jint den)
{
  AVFrame* c = *(AVFrame**)&pointer;
	c->sample_aspect_ratio.num = num;
	c->sample_aspect_ratio.den = den;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nError__JI
(JNIEnv* env, jclass clazz, jlong pointer, jint index)
{
  return (**(AVFrame**)&pointer).error[index];
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nError__JIJ
(JNIEnv* env, jclass clazz, jlong pointer, jint index, jlong err)
{
  (**(AVFrame**)&pointer).error[index] = err;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nErrors
(JNIEnv* env, jclass clazz, jlong pointer, jlongArray jarr)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  jlong* vals = (*env)->GetLongArrayElements(env, jarr, 0);
  int i;
  for( i = 0; i < AV_NUM_DATA_POINTERS; i++ ) {
    vals[i] = frame->error[i];
  }
  (*env)->ReleaseLongArrayElements(env, jarr, vals, 0);
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nBestEffortTimestamp
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  return av_frame_get_best_effort_timestamp( frame );
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nPktPos
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  return av_frame_get_pkt_pos( frame );
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nPktDuration
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  return av_frame_get_pkt_duration( frame );
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nMetadata__J
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  return *(jlong*)&(frame->metadata);
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nMetadata__JJ
(JNIEnv* env, jclass clazz, jlong pointer, jlong dictPointer)
{
  AVFrame* frame = *(AVFrame**)&pointer;
  frame->metadata = *(AVDictionary**)&dictPointer;
}



JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nFillVideoFrame
(JNIEnv *env, jclass clazz, jlong pointer, jint w, jint h, jint pixFmt, jobject jbuf, jint jbufOff )
{
	AVFrame *pic = *(AVFrame**)&pointer;
	int err;
	if( jbuf ) {
		uint8_t *buf = (*env)->GetDirectBufferAddress( env, jbuf ) + jbufOff;		
		err = avpicture_fill( (AVPicture*)pic, buf, pixFmt, w, h );
	} else {
		err = avpicture_fill( (AVPicture*)pic, NULL, pixFmt, w, h );
	}
	
	if( err >= 0 ) {
		pic->width  = w;
		pic->height = h;
		pic->format = pixFmt;
		if( jbuf ) {
			pic->type = FF_BUFFER_TYPE_USER;
		}
	}
	
	return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nFillVideoFrameManually
(JNIEnv *env, jclass clazz, jlong ptr, jint w, jint h, jint pixFmt, jint d, jobject jbuf, jintArray joffs, jintArray jlineSizes)
{
	if( d > AV_NUM_DATA_POINTERS ) {
		return -1;
	}

	AVFrame *pic = *(AVFrame**)&ptr;
	jint *lineSizes = (*env)->GetIntArrayElements( env, jlineSizes, 0 );
	
	if( jbuf ) {
		uint8_t *buf = (*env)->GetDirectBufferAddress( env, jbuf );
		jint* offs   = (*env)->GetIntArrayElements( env, joffs, 0 );
		
		int i;
		for( i = 0; i < d; i++ ) {
			pic->data[i]     = buf + offs[i];
			pic->linesize[i] = lineSizes[i];
		}
		
		(*env)->ReleaseIntArrayElements( env, joffs, offs, 0 );
		pic->type = FF_BUFFER_TYPE_USER;
		
	} else {
		int i;
		for( i = 0; i < d; i++ ) {
			pic->linesize[i] = lineSizes[i];
		}
	}
	
	(*env)->ReleaseIntArrayElements( env, jlineSizes, lineSizes, 0 );
	pic->width  = w;
	pic->height = h;
	pic->format = pixFmt;
	pic->extended_data = pic->data;
	
	return 0;
}


#include <stdio.h>

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nFillAudioFrame
(JNIEnv* env, jclass clazz, jlong pointer, jint channels, jint sampleFmt, jint align, jobject userBuf, jint pos, jint remain)
{
    AVFrame* frame = *(AVFrame**)&pointer;
    uint8_t* data  = (*env)->GetDirectBufferAddress( env, userBuf );

    int err = avcodec_fill_audio_frame( frame, channels, sampleFmt, data + pos, remain, align );
    if( err >= 0 ) {
        frame->type     = FF_BUFFER_TYPE_USER;
        frame->channels = channels;
        frame->format   = sampleFmt;
    }

    return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nComputeVideoBufferSize
(JNIEnv* env, jclass clazz, jint w, jint h, jint pixelFormat)
{
    return avpicture_get_size(pixelFormat, w, h);
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nComputeAudioBufferSize
(JNIEnv *env, jclass clazz, jint channels, jint samplesPerChannel, jint sampleFmt, jint align, jintArray optLineSize )
{
	int lineSize;
  int ret = av_samples_get_buffer_size( &lineSize, channels, samplesPerChannel, sampleFmt, align );
	if( optLineSize ) {
		jint *arr = (*env)->GetIntArrayElements( env, optLineSize, 0 );
		arr[0] = lineSize;
		(*env)->ReleaseIntArrayElements( env, optLineSize, arr, 0 );
	}
	return ret;
}

