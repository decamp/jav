#include "JavChannelLayout.h"
#include "libavutil/channel_layout.h"
#include "libavutil/frame.h"


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavChannelLayout_get
( JNIEnv *env, jclass clazz, jstring name )
{
    const char *cname = ( name == NULL ? NULL : (*env)->GetStringUTFChars( env, name, NULL ) );
    uint64_t ret = av_get_channel_layout( cname );
    if( cname ) {
        (*env)->ReleaseStringUTFChars( env, name, cname );
    }
    return *(jlong*)&ret;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavChannelLayout_getString
( JNIEnv *env, jclass clazz, jint numChannels, jlong channelLayout )
{
    char buf[1024];
    av_get_channel_layout_string( buf, 1024, numChannels, channelLayout );
    buf[1023] = 0;
    return (*env)->NewStringUTF( env, buf );
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavChannelLayout_getNbChannels
( JNIEnv *env, jclass clazz, jlong layout )
{
    return av_get_channel_layout_nb_channels( layout );
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavChannelLayout_getDefault
( JNIEnv *env, jclass clazz, jint numChans )
{
    uint64_t ret = av_get_default_channel_layout( numChans );
    return *(jlong*)&ret;
}


JNIEXPORT jint JNICALL Java_bits_jav_util_JavChannelLayout_getChannelIndex
( JNIEnv *env, jclass clazz, jlong layout, jlong channel )
{
    return av_get_channel_layout_channel_index( layout, channel );
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavChannelLayout_extractChannel
( JNIEnv *env, jclass clazz, jlong layout, jint index )
{
    uint64_t ret = av_channel_layout_extract_channel( layout, index );
    return *(jlong*)&ret;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavChannelLayout_getChannelName
( JNIEnv *env, jclass clazz, jlong channel )
{
    const char *str = av_get_channel_name( *(uint64_t*)&channel );
    return str ? (*env)->NewStringUTF( env, str ) : NULL;
}

JNIEXPORT jstring JNICALL Java_bits_jav_util_JavChannelLayout_getChannelDescription
( JNIEnv *env, jclass clazz, jlong channel )
{
    const char *str = av_get_channel_description( *(uint64_t*)&channel );
    return str ? (*env)->NewStringUTF( env, str ) : NULL;
}


JNIEXPORT jlong JNICALL Java_bits_jav_util_JavChannelLayout_getStandardValue
( JNIEnv *env, jclass clazz, jint index )
{
    uint64_t layout;
    const char *name;
    int err = av_get_standard_channel_layout( index, &layout, &name );
    return err ? 0xFFFFFFFFFFFFFFFFLL : *(jlong*)&layout;
}


JNIEXPORT jstring JNICALL Java_bits_jav_util_JavChannelLayout_getStandardName
( JNIEnv *env, jclass clazz, jint index )
{
    uint64_t layout;
    const char *name;
    int err = av_get_standard_channel_layout( index, &layout, &name );
    if( err ) {
        return 0L;
    }
    return name ? (*env)->NewStringUTF( env, name ) : NULL;
}
