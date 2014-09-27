/* DO NOT EDIT THIS FILE - it is machine generated */
#include <JavaVM/jni.h>
/* Header for class bits_jav_util_JavBufferRef */

#ifndef _Included_bits_jav_util_JavBufferRef
#define _Included_bits_jav_util_JavBufferRef
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nAlloc
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nAlloc
  (JNIEnv *, jclass, jint);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nAllocZ
 * Signature: (I)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nAllocZ
  (JNIEnv *, jclass, jint);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nWrap
 * Signature: (Ljava/nio/ByteBuffer;III)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nWrap
  (JNIEnv *, jclass, jobject, jint, jint, jint);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nRealloc
 * Signature: ([JI)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nRealloc
  (JNIEnv *, jclass, jlongArray, jint);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nRef
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nRef
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nUnref
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_bits_jav_util_JavBufferRef_nUnref
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nRefCount
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nRefCount
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nIsWritable
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nIsWritable
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nMakeWritable
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nMakeWritable
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nBuffer
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nBuffer
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nData
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavBufferRef_nData
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavBufferRef
 * Method:    nSize
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_util_JavBufferRef_nSize
  (JNIEnv *, jclass, jlong);

#ifdef __cplusplus
}
#endif
#endif