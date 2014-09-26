/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */


#include "JavException.h"
#include "libavutil/error.h"

JNIEXPORT jstring JNICALL Java_bits_jav_JavException_errStr
( JNIEnv *env, jclass clazz, jint code )
{
	char str[AV_ERROR_MAX_STRING_SIZE];
	int err = av_strerror( code, str, AV_ERROR_MAX_STRING_SIZE );
	if( err < 0 ) {
		return NULL;
	} else {
		return (*env)->NewStringUTF( env, str );
	}
}