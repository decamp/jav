/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavSampleFormat.h"
#include "libavutil/samplefmt.h"
#include "libavutil/frame.h"


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavSampleFormat_getSampleFormatName
( JNIEnv *env, jclass clazz, jint fmt )
{
    const char *str = av_get_sample_fmt_name( fmt );
    return str ? (*env)->NewStringUTF( env, str ) : NULL;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_getSampleFormat
( JNIEnv *env, jclass clazz, jstring fmt )
{
    const char *cfmt = ( fmt == NULL ? NULL : (*env)->GetStringUTFChars( env, fmt, NULL ) );
    enum AVSampleFormat ret = av_get_sample_fmt( cfmt );
    if( fmt ) {
        (*env)->ReleaseStringUTFChars( env, fmt, cfmt );
    }
    return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_getAltSampleFormat
( JNIEnv *env, jclass clazz, jint fmt, jint planar )
{
    return av_get_alt_sample_fmt( fmt, planar );
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavSampleFormat_nGetSampleFormatString
( JNIEnv *env, jclass clazz, jobject jbuf, jint bufOff, jint bufSize, jint fmt )
{
    char *cbuf = (*env)->GetDirectBufferAddress( env, jbuf ) + bufOff;
    char *ret  = av_get_sample_fmt_string( cbuf, bufSize, fmt );
    return *(jlong*)&ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_getBytesPerSample
( JNIEnv *env, jclass clazz, jint fmt )
{
    return av_get_bytes_per_sample( fmt );
}


JNIEXPORT jboolean JNICALL Java_bits_jav_util_JavSampleFormat_isFormatPlanar
( JNIEnv *env, jclass clazz, jint fmt )
{
    return av_sample_fmt_is_planar( fmt ) ? JNI_TRUE : JNI_FALSE;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_getBufferSize
(JNIEnv *env, jclass clazz, jint chanNum, jint sampNum, jint sampFmt, jint align, jintArray optLineSize )
{
	int lineSize;
    int ret = av_samples_get_buffer_size( &lineSize, chanNum, sampNum, sampFmt, align );
	if( optLineSize ) {
        jint *arr = (*env)->GetIntArrayElements( env, optLineSize, 0 );
        arr[0] = lineSize;
        (*env)->ReleaseIntArrayElements( env, optLineSize, arr, 0 );
	}
	return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_copy
( JNIEnv *env, jclass clazz, jlong srcPtr, jint srcOff, jint chanNum, jint sampNum, jint sampFmt, jlong dstPtr, jint dstOff )
{
    return av_samples_copy( *(uint8_t***)&dstPtr,
                            *(uint8_t**const*)&srcPtr,
                            dstOff,
                            srcOff,
                            sampNum,
                            chanNum,
                            sampFmt );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_setSilence
( JNIEnv *env, jclass clazz, jlong dataPtr, jint dataOff, jint chanNum, jint sampNum, jint sampFmt )
{
    return av_samples_set_silence( *(uint8_t***)&dataPtr, dataOff, sampNum, chanNum, sampFmt );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavSampleFormat_fillArrays
(
    JNIEnv *env,
    jclass clazz,
    jlong bufPtr,
    jint chanNum,
    jint sampNum,
    jint sampFmt,
    jint align,
    jlong outDataPtr,
    jintArray optLineSize
)
{
    int lineSize;
    int err = av_samples_fill_arrays( *(uint8_t***)&outDataPtr,
                                      &lineSize,
                                      *(const uint8_t**)&bufPtr,
                                      chanNum,
                                      sampNum,
                                      sampFmt,
                                      align );

    if( err == 0 && optLineSize != NULL ) {
          jint *arr = (*env)->GetIntArrayElements( env, optLineSize, 0 );
          arr[0] = lineSize;
          (*env)->ReleaseIntArrayElements( env, optLineSize, arr, 0 );
    }

    return err;
}
