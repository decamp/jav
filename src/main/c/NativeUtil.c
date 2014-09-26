/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "NativeUtil.h"
#include <string.h>


JNIEXPORT void JNICALL Java_bits_jav_util_NativeUtil_copy
( JNIEnv *env, jclass clazz, jlong srcPtr, jlong dstPtr, jint len )
{
	memcpy( *(char**)&dstPtr, *(const char**)&srcPtr, len );
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_NativeUtil_nativeAddress
( JNIEnv *env, jclass clazz, jobject buf )
{
	if( buf ) {
		return (jlong)( (*env)->GetDirectBufferAddress( env, buf ) );
	}
	return 0;
}


JNIEXPORT void JNICALL Java_bits_jav_util_NativeUtil_nCopyPointerBuffer
(JNIEnv *env, jclass clazz, jlong srcPtr, jobject dstBuf, jint off, jint len )
{
  char* dst = (*env)->GetDirectBufferAddress( env, dstBuf ) + off;
  memcpy( dst, *(const char**)&srcPtr, len );
}


JNIEXPORT void JNICALL Java_bits_jav_util_NativeUtil_nCopyBufferPointer
(JNIEnv *env, jclass clazz, jobject srcBuf, jint off, jint len, jlong dstPtr )
{
	const char *src = (*env)->GetDirectBufferAddress( env, srcBuf ) + off;
	memcpy( *(char**)&dstPtr, src, len );
}