#include "JavPixelFormat.h"
#include "libavutil/pixdesc.h"

JNIEXPORT jint JNICALL Java_bits_jav_util_JavPixelFormat_get
( JNIEnv *env, jclass clazz, jstring fmt )
{
    const char *cfmt = ( fmt == NULL ? NULL : (*env)->GetStringUTFChars( env, fmt, NULL ) );
    enum AVPixelFormat ret = av_get_pix_fmt( cfmt );
    if( fmt ) {
        (*env)->ReleaseStringUTFChars( env, fmt, cfmt );
    }
    return ret;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavPixelFormat_getName
( JNIEnv *env, jclass clazz, jint fmt )
{
    const char *str = av_get_pix_fmt_name( fmt );
    return str ? (*env)->NewStringUTF( env, str ) : NULL;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavPixelFormat_getString
( JNIEnv *env, jclass clazz, jint fmt )
{
    char buf[1024];
    buf[0] = 0;
    av_get_pix_fmt_string( buf, 1024, fmt );
    buf[1023] = 0;
    return (*env)->NewStringUTF( env, buf );
}
