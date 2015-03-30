/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

#include "JavFrame.h"
#include "JavCore.h"
#include "libavcodec/avcodec.h"

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nAllocFrame
(JNIEnv* env, jclass clazz)
{
  AVFrame* f = av_frame_alloc();
  return *(jlong*)&f;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nFree
(JNIEnv* env, jclass clazz, jlong pointer)
{
  av_frame_unref( *(AVFrame**)&pointer );
  av_free( *(AVFrame**)&pointer );
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nUnref
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

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nDataElem__JI
(JNIEnv* env, jclass clazz, jlong pointer, jint layer)
{
  return *(jlong*)&((**(AVFrame**)&pointer).data[layer]);
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDataElem__JIJ
(JNIEnv* env, jclass clazz, jlong pointer, jint layer, jlong dataPointer)
{
  (**(AVFrame**)&pointer).data[layer] = *(uint8_t**)&dataPointer;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nDataElem__J_3J
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
    return *(jlong*)&(**(AVFrame**)&pointer).extended_data;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedData__JJ
(JNIEnv* env, jclass clazz, jlong pointer, jlong val)
{
    (**(AVFrame**)&pointer).extended_data = *(uint8_t***)&val;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nExtendedDataElem__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint layer )
{
	return *(jlong*)&(**(AVFrame**)&pointer).extended_data[layer];
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedDataElem__JIJ
( JNIEnv *env, jclass clazz, jlong pointer, jint layer, jlong dataPointer )
{
  (**(AVFrame**)&pointer).extended_data[layer] = *(uint8_t**)&dataPointer;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nLineSize__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    int *arr = (**(AVFrame**)&pointer).linesize;
    return *(jlong*)&arr;
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


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nBuf
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return *(jlong*)&frame->buf;
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nBufElem__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    AVBufferRef *ref  = frame->buf[idx];
    if( ref == NULL ) {
        return 0;
    }
    ref = av_buffer_ref( ref );
    return *(jlong*)&ref;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nBufElem__JIJ
( JNIEnv *env, jclass clazz, jlong pointer, jint idx, jlong refPtr )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    av_buffer_unref( &frame->buf[idx] );
    if( refPtr ) {
        frame->buf[idx] = av_buffer_ref( *(AVBufferRef**)&refPtr );
    }
}


JNIEXPORT jobject JNICALL Java_bits_jav_codec_JavFrame_nJavaBufElem
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return jav_buffer_unwrap_bytebuffer( frame->buf[idx] );
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nBufElemSize
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVBufferRef *ref = (**(AVFrame**)&pointer).buf[idx];
    return ref ? ref->size : 0;
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nBufElemReset
(JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVBufferRef *ref = (**(AVFrame**)&pointer).buf[idx];
    if( ref ) {
        ref->data = ref->buffer->data;
        ref->size = ref->buffer->size;
        return ref->size;
    }

    return 0;
}

JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nExtendedBuf__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return *(jlong*)&frame->extended_buf;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedBuf__JJ
( JNIEnv *env, jclass clazz, jlong pointer, jlong extBufPtr )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    frame->extended_buf = *(AVBufferRef***)&extBufPtr;
}


JNIEXPORT jlong JNICALL Java_bits_jav_codec_JavFrame_nExtendedBufElem__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    AVBufferRef *ref  = frame->extended_buf[idx];
    if( ref == NULL ) {
        return 0;
    }
    ref = av_buffer_ref( ref );
    return *(jlong*)&ref;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nExtendedBufElem__JIJ
( JNIEnv *env, jclass clazz, jlong pointer, jint idx, jlong refPtr )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    av_buffer_unref( &frame->extended_buf[idx] );
    if( refPtr ) {
        frame->extended_buf[idx] = av_buffer_ref( *(AVBufferRef**)&refPtr );
    }
}


JNIEXPORT jobject JNICALL Java_bits_jav_codec_JavFrame_nJavaExtendedBufElem
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return jav_buffer_unwrap_bytebuffer( frame->extended_buf[idx] );
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nExtendedBufElemSize
( JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVBufferRef *ref = (**(AVFrame**)&pointer).extended_buf[idx];
    return ref ? ref->size : 0;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nExtendedBufElemReset
(JNIEnv *env, jclass clazz, jlong pointer, jint idx )
{
    AVBufferRef *ref = (**(AVFrame**)&pointer).extended_buf[idx];
    if( ref ) {
        ref->data = ref->buffer->data;
        ref->size = ref->buffer->size;
        return ref->size;
    }

    return 0;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nNbExtendedBuf__J
( JNIEnv *env, jclass clazz, jlong pointer )
{
    return (*(AVFrame*)&pointer).nb_extended_buf;
}


JNIEXPORT void JNICALL Java_bits_jav_codec_JavFrame_nNbExtendedBuf__JI
( JNIEnv *env, jclass clazz, jlong pointer, jint val )
{
    (*(AVFrame*)&pointer).nb_extended_buf = val;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nNbAllBufs
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    int ret = 0;
    for(; ret < AV_NUM_DATA_POINTERS; ret++ ) {
        if( frame->buf[ret] == NULL ) {
            return ret;
        }
    }

    for( int i = 0; i < frame->nb_extended_buf; i++ ) {
        if( frame->extended_buf[ret] ) {
            ret++;
        }
    }

    return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nAllBufsMinSize
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    int ret = 0;

    for( int i = 0; i < AV_NUM_DATA_POINTERS; i++ ) {
        AVBufferRef *ref = frame->buf[ret];
        if( ref ) {
            if( i == 0 ) {
                ret = ref->size;
            } else if( ref->size < ret ) {
                ret = ref->size;
            }
        } else {
            return ret;
        }
    }

    for( int i = 0; i < frame->nb_extended_buf; i++ ) {
        AVBufferRef *ref = frame->extended_buf[i];
        if( !ref ) {
            return ret;
        }

        if( ref->size < ret ) {
            ret = ref->size;
        }
    }

    return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nIsWritable
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return av_frame_is_writable( frame );
}

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nMakeWritable
( JNIEnv *env, jclass clazz, jlong pointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    return av_frame_make_writable( frame );
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nCopy
( JNIEnv *env, jclass clazz, jlong pointer, jlong srcPointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    const AVFrame *source = *(AVFrame**)&srcPointer;
    return av_frame_copy( frame, source );
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nCopyProps
( JNIEnv *env, jclass clazz, jlong pointer, jlong srcPointer )
{
    AVFrame *frame = *(AVFrame**)&pointer;
    const AVFrame *source = *(AVFrame**)&srcPointer;
    return av_frame_copy_props( frame, source );
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
(JNIEnv *env, jclass clazz, jlong pointer, jint w, jint h, jint pixFmt, jobject buf, jint bufOff, jint bufLen )
{
	AVFrame *pic = *(AVFrame**)&pointer;
	AVBufferRef *ref = NULL;

	int err;

	if( buf ) {
	    ref = jav_buffer_wrap_bytebuffer( env, buf, bufOff, bufLen, 0 );
		err = avpicture_fill( (AVPicture*)pic, ref->data, pixFmt, w, h );
	} else {
		err = avpicture_fill( (AVPicture*)pic, NULL, pixFmt, w, h );
	}

	if( err >= 0 ) {
	    pic->buf[0] = ref;
		pic->width  = w;
		pic->height = h;
		pic->format = pixFmt;
	} else {
	    av_buffer_unref( &ref );
    }

	return err;
}


#include <stdio.h>

JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nFillAudioFrame
( JNIEnv *env,
  jclass clazz,
  jlong pointer,
  jint chanNum,
  jint sampNum,
  jint sampFmt,
  jint align,
  jobject buf,
  jint bufOff,
  jint bufLen)
{
    AVFrame* frame   = *(AVFrame**)&pointer;
    AVBufferRef *ref = jav_buffer_wrap_bytebuffer( env, buf, bufOff, bufLen, 0 );
    frame->nb_samples = sampNum;

    int err = avcodec_fill_audio_frame( frame, chanNum, sampFmt, ref->data, ref->size, align );

    if( err >= 0 ) {
        frame->buf[0]   = ref;
        frame->channels = chanNum;
        frame->format   = sampFmt;
    } else {
        av_buffer_unref( &ref );
    }

    return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_codec_JavFrame_nComputeVideoBufferSize
(JNIEnv* env, jclass clazz, jint w, jint h, jint pixelFormat)
{
    return avpicture_get_size(pixelFormat, w, h);
}
