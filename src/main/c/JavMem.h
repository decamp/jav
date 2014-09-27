/* DO NOT EDIT THIS FILE - it is machine generated */
#include <JavaVM/jni.h>
/* Header for class bits_jav_util_JavMem */

#ifndef _Included_bits_jav_util_JavMem
#define _Included_bits_jav_util_JavMem
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     bits_jav_util_JavMem
 * Method:    malloc
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_malloc
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    mallocz
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_mallocz
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    realloc
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_realloc
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    reallocf
 * Signature: (JJJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_reallocf
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    reallocArray
 * Signature: (JJJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_reallocArray
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    strdup
 * Signature: (J)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_strdup
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    memdup
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_memdup
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    free
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_bits_jav_util_JavMem_free
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    copy
 * Signature: (JJJ)V
 */
JNIEXPORT void JNICALL Java_bits_jav_util_JavMem_copy
  (JNIEnv *, jclass, jlong, jlong, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    nativeAddress
 * Signature: (Ljava/nio/ByteBuffer;)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_util_JavMem_nativeAddress
  (JNIEnv *, jclass, jobject);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    nCopyPointerToBuffer
 * Signature: (JLjava/nio/ByteBuffer;IJ)V
 */
JNIEXPORT void JNICALL Java_bits_jav_util_JavMem_nCopyPointerToBuffer
  (JNIEnv *, jclass, jlong, jobject, jint, jlong);

/*
 * Class:     bits_jav_util_JavMem
 * Method:    nCopyBufferToPointer
 * Signature: (Ljava/nio/ByteBuffer;IJJ)V
 */
JNIEXPORT void JNICALL Java_bits_jav_util_JavMem_nCopyBufferToPointer
  (JNIEnv *, jclass, jobject, jint, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif