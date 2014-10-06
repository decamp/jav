#include <JavaVM/jni.h>
#include "libavutil/buffer.h"

extern JavaVM* jav_jvm;

struct AVBuffer {
    uint8_t *data; /**< data described by this buffer */
    int      size; /**< size of data in bytes */

    /**
     *  number of existing AVBufferRef instances referring to this buffer
     */
    volatile int refcount;

    /**
     * a callback for freeing the data
     */
    void (*free)(void *opaque, uint8_t *data);

    /**
     * an opaque pointer, to be used by the freeing callback
     */
    void *opaque;

    /**
     * A combination of BUFFER_FLAG_*
     */
    int flags;
};


AVBufferRef *jav_buffer_wrap_bytebuffer( JNIEnv *env, jobject buf, jint bufOff, jint bufLen, jint flags );
jobject jav_buffer_unwrap_bytebuffer( AVBufferRef *ref );