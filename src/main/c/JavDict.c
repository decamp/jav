/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavDict.h"
#include "libavutil/opt.h"
#include <stdio.h>

JNIEXPORT jlong JNICALL Java_bits_jav_util_JavDict_nGet
( JNIEnv *env, jclass clazz, jlong pointer, jstring jkey, jlong prevPointer, jint flags )
{
  AVDictionary *dict = *(AVDictionary**)&pointer;
  AVDictionaryEntry *prevEntry = *(AVDictionaryEntry**)&prevPointer;
  const char *ckey = NULL;
  
  if( jkey != NULL ) {
    ckey = (*env)->GetStringUTFChars( env, jkey, NULL );
  }
  
  AVDictionaryEntry *entry = av_dict_get( dict, ckey, prevEntry, flags );
	
  if( jkey != NULL ) {
    (*env)->ReleaseStringUTFChars( env, jkey, ckey );
  }
	
  return *(jlong*)&entry;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavDict_nCount
( JNIEnv* env, jclass clazz, jlong pointer )
{
  AVDictionary* dict = *(AVDictionary**)&pointer;
  return av_dict_count( dict );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavDict_nSet
( JNIEnv* env, jclass clazz, jlong pointer, jstring key, jstring value, jint flags, jlongArray joutPointer )
{
  AVDictionary* dict = *(AVDictionary**)&pointer;
  const char* ckey   = NULL;
  const char* cvalue = NULL;
  jlong* outPointer;
  
  if( key != NULL ) {
    ckey = (*env)->GetStringUTFChars( env, key, NULL );
  }
  
  if( value != NULL ) {
    cvalue = (*env)->GetStringUTFChars( env, value, NULL );
  }
  
  jint err = av_dict_set( &dict, ckey, cvalue, flags );
  
  if( key != NULL ) {
    (*env)->ReleaseStringUTFChars( env, key, ckey );
  }
  
  if( value != NULL ) {
    (*env)->ReleaseStringUTFChars( env, value, cvalue );
  }
  
  outPointer    = (*env)->GetLongArrayElements(env, joutPointer, 0);
  outPointer[0] = *(jlong*)&dict;
  (*env)->ReleaseLongArrayElements(env, joutPointer, outPointer, 0);
  
  return err;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavDict_nCopy
(JNIEnv* env, jclass clazz, jlong pointer, jlong srcPointer, jint flags )
{
  AVDictionary* dict = *(AVDictionary**)&pointer;
  AVDictionary* src  = *(AVDictionary**)&pointer;
  av_dict_copy( &dict, src, flags );
  
  return *(jlong*)&dict;
}



JNIEXPORT void JNICALL Java_bits_jav_util_JavDict_nFree
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVDictionary* dict = *(AVDictionary**)&pointer;
  av_dict_free( &dict );
}



JNIEXPORT jstring JNICALL Java_bits_jav_util_JavDict_nKey
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVDictionaryEntry* e = *(AVDictionaryEntry**)&pointer;
  if( e == NULL || e->key == NULL ) {
    return NULL;
	}
	
  return (*env)->NewStringUTF( env, e->key );
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavDict_nValue
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVDictionaryEntry* e = *(AVDictionaryEntry**)&pointer;
  if( e == NULL || e->value == NULL ) {
    return NULL;
	}
  return (*env)->NewStringUTF( env, e->value );
}



