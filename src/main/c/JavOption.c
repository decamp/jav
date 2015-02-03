/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavOption.h"
#include "libavutil/opt.h"


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nFindOption
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jstring unit, jint optFlags, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = name == NULL ? NULL : (*env)->GetStringUTFChars(env, name, NULL);
  const char* cunit = unit == NULL ? NULL : (*env)->GetStringUTFChars(env, unit, NULL);

  const AVOption* opt = av_opt_find( object, cname, cunit, optFlags, searchFlags );

  if( cname ) {
    (*env)->ReleaseStringUTFChars(env, name, cname);
  }
  
  if( cunit ) {
    (*env)->ReleaseStringUTFChars( env, unit, cunit );
  }
  
  return *(jlong*)&opt;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nNextOption
(JNIEnv* env, jclass clazz, jlong objectPointer, jlong optionPointer)
{
  const AVOption* ret = av_opt_next( *(AVClass**)&objectPointer, *(AVOption**)&optionPointer );
  return *(jlong*)&ret;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavOption_nName
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOption* opt = *(AVOption**)&pointer;
  if(opt->name) {
    return (*env)->NewStringUTF(env, opt->name);
  }
  
  return NULL;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavOption_nHelp
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOption* opt = *(AVOption**)&pointer;
  if(opt->help) {
    return (*env)->NewStringUTF(env, opt->help);
  }
  
  return NULL;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nType
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).type;
}


JNIEXPORT jdouble JNICALL Java_bits_jav_util_JavOption_nMinValue
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).min;
}


JNIEXPORT jdouble JNICALL Java_bits_jav_util_JavOption_nMaxValue
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).max;
}



JNIEXPORT jstring JNICALL Java_bits_jav_util_JavOption_nDefaultString
(JNIEnv* env, jclass clazz, jlong pointer)
{
  const char* str = (**(AVOption**)&pointer).default_val.str;

  if( str ) {
    return (*env)->NewStringUTF( env, str );
  } else {
    return NULL;
  }
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nDefaultLong
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).default_val.i64;
}


JNIEXPORT jdouble JNICALL Java_bits_jav_util_JavOption_nDefaultDouble
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).default_val.dbl;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nDefaultRational
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVRational ret = (**(AVOption**)&pointer).default_val.q;
  return (((int64_t)ret.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)ret.den & 0xFFFFFFFFLL);
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nFlags
(JNIEnv* env, jclass clazz, jlong pointer)
{
  return (**(AVOption**)&pointer).flags;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavOption_nUnit
(JNIEnv* env, jclass clazz, jlong pointer)
{
  AVOption* opt = *(AVOption**)&pointer;
  if(opt->unit) {
    return (*env)->NewStringUTF(env, opt->unit);
  }
  
  return NULL;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionString
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jstring value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  const char* cval  = (*env)->GetStringUTFChars(env, value, NULL);
  
  int err = av_opt_set( object, cname, cval, searchFlags );
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  (*env)->ReleaseStringUTFChars(env, value, cval);
  
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionDouble
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jdouble value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int err           = av_opt_set_double( object, cname, value, searchFlags );
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionLong
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jlong value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int err           = av_opt_set_int(object, cname, value, searchFlags);
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}



JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionRational
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint num, jint den, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  AVRational value;
  value.num = num;
  value.den = den;
  int err = av_opt_set_q(object, cname, value, searchFlags);
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavOption_nGetOptionString
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  uint8_t* val      = 0;
  int err           = av_opt_get( object, cname, searchFlags, &val );
    
  (*env)->ReleaseStringUTFChars(env, name, cname);

  if( err != 0 || val == NULL ) {
    if( val != NULL ) {
      av_free( val );
    }
    
    return NULL;
  }
  
  jstring ret = (*env)->NewStringUTF( env, (char*)val );
  av_free( val );
  return ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nGetOptionLong
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  jlong ret         = 0;
  
  av_opt_get_int( object, cname, searchFlags, &ret );
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return ret;
}


JNIEXPORT jdouble JNICALL Java_bits_jav_util_JavOption_nGetOptionDouble
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  jdouble ret       = 0.0;

  av_opt_get_double( object, cname, searchFlags, &ret );
  
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return ret;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nGetOptionRational
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  AVRational ret;
  int err = av_opt_get_q( object, cname, searchFlags, &ret );

  (*env)->ReleaseStringUTFChars(env, name, cname);
  if( err != 0 ) {
    return 0LL;
	}
  
  return (((int64_t)ret.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)ret.den & 0xFFFFFFFFLL);
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nGetOptionPixelFormat
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  jint ret          = av_opt_get_pixel_fmt( object, cname, searchFlags, &ret );
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionPixelFormat
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int err           = av_opt_set_pixel_fmt(object, cname, value, searchFlags);
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nGetOptionSampleFormat
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  jint ret          = av_opt_get_sample_fmt( object, cname, searchFlags, &ret );
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionSampleFormat
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int err           = av_opt_set_sample_fmt(object, cname, value, searchFlags);
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nGetOptionChannelLayout
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  jlong ret         = 0;
  av_opt_get_channel_layout( object, cname, searchFlags, &ret );
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionChannelLayout
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jlong value, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars( env, name, NULL );
  int err           = av_opt_set_channel_layout( object, cname, value, searchFlags );
  (*env)->ReleaseStringUTFChars( env, name, cname );

  return err;
}



JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nGetOptionImageSize
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int w, h;
  int err = av_opt_get_image_size( object, cname, searchFlags, &w, &h );

  (*env)->ReleaseStringUTFChars(env, name, cname);
  if( err != 0 ) {
    return 0LL;
  }

  return (((int64_t)w << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)h & 0xFFFFFFFFLL);
}



JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionImageSize
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint w, jint h, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  int err   = av_opt_set_image_size(object, cname, w, h, searchFlags);
  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}



JNIEXPORT jlong JNICALL Java_bits_jav_util_JavOption_nGetOptionVideoRate
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  AVRational ret;
  int err = av_opt_get_video_rate( object, cname, searchFlags, &ret );

  (*env)->ReleaseStringUTFChars(env, name, cname);
  if( err != 0 ) {
    return 0LL;
  }
  return (((int64_t)ret.num << 32LL) & 0xFFFFFFFF00000000LL) | ((int64_t)ret.den & 0xFFFFFFFFLL);
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavOption_nSetOptionVideoRate
(JNIEnv* env, jclass clazz, jlong objectPointer, jstring name, jint num, jint den, jint searchFlags)
{
  AVClass* object   = *(AVClass**)&objectPointer;
  const char* cname = (*env)->GetStringUTFChars(env, name, NULL);
  AVRational value;
  value.num = num;
  value.den = den;
  int err = av_opt_set_video_rate(object, cname, value, searchFlags);

  (*env)->ReleaseStringUTFChars(env, name, cname);
  return err;
}
