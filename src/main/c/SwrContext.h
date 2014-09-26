/* DO NOT EDIT THIS FILE - it is machine generated */
#include <JavaVM/jni.h>
/* Header for class bits_jav_swresample_SwrContext */

#ifndef _Included_bits_jav_swresample_SwrContext
#define _Included_bits_jav_swresample_SwrContext
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nAlloc
 * Signature: ()J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nAlloc
  (JNIEnv *, jclass);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nFree
 * Signature: (J)V
 */
JNIEXPORT void JNICALL Java_bits_jav_swresample_SwrContext_nFree
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nInit
 * Signature: (J)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nInit
  (JNIEnv *, jclass, jlong);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nConvert
 * Signature: (JJIJI)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nConvert
  (JNIEnv *, jclass, jlong, jlong, jint, jlong, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nConvertPacked
 * Signature: (JLjava/nio/ByteBuffer;IILjava/nio/ByteBuffer;II)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nConvertPacked
  (JNIEnv *, jclass, jlong, jobject, jint, jint, jobject, jint, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nNextPts
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nNextPts
  (JNIEnv *, jclass, jlong, jlong);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nSetCompensation
 * Signature: (JII)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetCompensation
  (JNIEnv *, jclass, jlong, jint, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nSetChannelMapping
 * Signature: (J[I)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetChannelMapping
  (JNIEnv *, jclass, jlong, jintArray);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nSetMatrix
 * Signature: (J[DI)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nSetMatrix
  (JNIEnv *, jclass, jlong, jdoubleArray, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nDropOutput
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nDropOutput
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nInjectSilence
 * Signature: (JI)I
 */
JNIEXPORT jint JNICALL Java_bits_jav_swresample_SwrContext_nInjectSilence
  (JNIEnv *, jclass, jlong, jint);

/*
 * Class:     bits_jav_swresample_SwrContext
 * Method:    nGetDelay
 * Signature: (JJ)J
 */
JNIEXPORT jlong JNICALL Java_bits_jav_swresample_SwrContext_nGetDelay
  (JNIEnv *, jclass, jlong, jlong);

#ifdef __cplusplus
}
#endif
#endif
