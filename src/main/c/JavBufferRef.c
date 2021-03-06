/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */
#include "JavBufferRef.h"
#include "JavCore.h"
#include "libavutil/mem.h"
#include "libavutil/buffer.h"


void releaseJavaRef
( void *opaque, uint8_t *data )
{
	jobject ref = (jobject)opaque;
	JNIEnv *env;
    (*jav_jvm)->AttachCurrentThread( jav_jvm, (void **)&env, NULL );
    (*env)->DeleteGlobalRef( env, ref );
}


AVBufferRef *jav_buffer_wrap_bytebuffer
( JNIEnv *env, jobject buf, jint bufOff, jint bufLen, jint flags )
{
	uint8_t *data = (*env)->GetDirectBufferAddress( env, buf ) + bufOff;
    jobject ref = (*env)->NewGlobalRef( env, buf );
	return av_buffer_create( data, bufLen, &releaseJavaRef, ref, flags );
}


jobject jav_buffer_unwrap_bytebuffer( AVBufferRef *ref ) {
    if( !ref || !ref->buffer || ref->buffer->free != &releaseJavaRef ) {
        return NULL;
    }
    return (jobject)ref->buffer->opaque;
}



JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nAlloc
( JNIEnv *env, jclass clazz, jint size )
{
	AVBufferRef *ret = av_buffer_alloc( size );
	return *(long*)&ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nAllocZ
( JNIEnv *env, jclass clazz, jint size )
{
	AVBufferRef *ret = av_buffer_allocz( size );
	return *(long*)&ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nWrap
( JNIEnv *env, jclass clazz, jobject buf, jint bufOff, jint bufLen, jint flags )
{
    AVBufferRef *ret = jav_buffer_wrap_bytebuffer( env, buf, bufOff, bufLen, flags );
	return *(jlong*)&ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nBuffer
( JNIEnv *env, jclass clazz, jlong ptr )
{
	AVBuffer *buf = (**(AVBufferRef**)&ptr).buffer;
	return *(jlong*)&buf;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nData
(JNIEnv *env, jclass clazz, jlong ptr)
{
	uint8_t *data = (**(AVBufferRef**)&ptr).data;
	return *(jlong*)&data;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nSize
(JNIEnv *env, jclass clazz, jlong ptr)
{
	return (**(AVBufferRef**)&ptr).size;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nRef
( JNIEnv *env, jclass clazz, jlong ptr )
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	ref = av_buffer_ref( ref );
	return *(jlong*)&ref;
}


JNIEXPORT void JNICALL Java_bits_jav_util_JavBufferRef_nUnref
(JNIEnv *env, jclass clazz, jlong ptr)
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	av_buffer_unref( &ref );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nRefCount
(JNIEnv *env, jclass clazz, jlong ptr)
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	return av_buffer_get_ref_count( ref );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nIsWritable
(JNIEnv *env, jclass clazz, jlong ptr)
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	return av_buffer_is_writable( ref );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nMakeWritable
( JNIEnv *env, jclass clazz, jlong ptr )
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	return av_buffer_make_writable( &ref );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nRealloc
( JNIEnv *env, jclass clazz, jlongArray jarr, jint size )
{
	jlong *ptr = (*env)->GetLongArrayElements( env, jarr, NULL );
	int err = av_buffer_realloc( (AVBufferRef**)ptr, size );
	(*env)->ReleaseLongArrayElements( env, jarr, ptr, 0 );
	return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nReset
( JNIEnv *env, jclass clazz, jlong ptr )
{
	AVBufferRef *ref = *(AVBufferRef**)&ptr;
	ref->data = ref->buffer->data;
	ref->size = ref->buffer->size;
	return ref->size;
}



JNIEXPORT jobject JNICALL Java_bits_jav_util_JavBufferRef_nJavaByteBuffer
( JNIEnv *env, jclass clazz, jlong ptr )
{
    return jav_buffer_unwrap_bytebuffer( *(AVBufferRef**)&ptr );
}
