/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "SwsContext.h"
#include "libswscale/swscale.h"
#include "libavcodec/avcodec.h"


JNIEXPORT jlong JNICALL Java_bits_jav_swscale_SwsContext_nAlloc
(JNIEnv* env, jclass clazz)
{
  struct SwsContext* context = sws_alloc_context();
  return *(jlong*)&context;
}


JNIEXPORT jint JNICALL Java_bits_jav_swscale_SwsContext_nInit
( JNIEnv* env, jclass clazz, jlong pointer )
{
  struct SwsContext* context = *(struct SwsContext**)&pointer;
  return sws_init_context( context, NULL, NULL );
}


JNIEXPORT jint JNICALL Java_bits_jav_swscale_SwsContext_nConvert
(JNIEnv* env, jclass clazz, jlong pointer, jlong srcPointer, jint sourceY, jint sourceH, jlong dstPointer)
{
  struct SwsContext* context = *(struct SwsContext**)&pointer;
  AVFrame* src = *(AVFrame**)&srcPointer;
  AVFrame* dst = *(AVFrame**)&dstPointer;
  
  return sws_scale( context, 
                    (const uint8_t**)src->data,
                    src->linesize,
                    sourceY,
                    sourceH,
                    dst->data,
                    dst->linesize );
}


JNIEXPORT void JNICALL Java_bits_jav_swscale_SwsContext_nFree
(JNIEnv* env, jclass clazz, jlong pointer)
{
  sws_freeContext(*(struct SwsContext**)&pointer);
}


JNIEXPORT jint JNICALL Java_bits_jav_swscale_SwsContext_nSetColorSpaceDetails
( 
  JNIEnv* env, 
  jclass clazz, 
  jlong pointer, 
  jintArray invTable, 
  jint srcRange, 
  jintArray table, 
  jint dstRange, 
  jint brightness, 
  jint contrast, 
  jint saturation
)
{
  struct SwsContext* context = *(struct SwsContext**)&pointer;
  jint* cinvTable            = (*env)->GetIntArrayElements(env, invTable, 0);
  jint* cTable               = (*env)->GetIntArrayElements(env, table, 0);
  int err;
  
  err = sws_setColorspaceDetails(context, cinvTable, srcRange, cTable, dstRange, brightness, contrast, saturation);
  
  (*env)->ReleaseIntArrayElements(env, invTable, cinvTable, 0);
  (*env)->ReleaseIntArrayElements(env, table, cTable, 0);
  
  return err;
}

