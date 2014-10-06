/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import bits.util.ref.AbstractRefable;
import bits.util.ref.Refable;


/**
 * AVBufferPool is an API for a lock-free thread-safe pool of AVBuffers.
 * <p>
 * Frequently allocating and freeing large buffers may be slow. AVBufferPool is
 * meant to solve this in cases when the caller needs a set of buffers of the
 * same size (the most obvious use case being buffers for raw video or audio
 * frames).
 * <p>
 * At the beginning, the user must call JavBufferPool.create() to create the
 * nativeBuffer pool. Whenever a nativeBuffer is needed, call pool.get() to
 * get a reference to a new nativeBuffer, similar to JavBufferFrame.alloc(). This new
 * reference works in all aspects the same way as the one created by
 * JavBufferFrame.alloc(), however, when the last reference to this nativeBuffer is
 * unreferenced, it is returned to the pool instead of being freed and will be
 * reused for subsequent av_buffer_pool_get() calls.
 * <p>
 * When the caller is done with the pool and no longer needs to allocate any new
 * buffers, pool.deref() must be called to mark the pool as freeable.
 * Once all the buffers are released, it will automatically be freed.
 * <p>
 * Allocating and releasing buffers with this API is thread-safe as long as
 * either the default alloc callback is used, or the user-supplied one is
 * thread-safe.
 */
public class JavBufferPool extends AbstractRefable implements NativeObject {

    /**
     * Allocate and initialize a nativeBuffer pool.
     *
     * @param size size of each nativeBuffer in this pool
     * @return newly created nativeBuffer pool on success, NULL on error.
     */
    public static JavBufferPool init( int size ) {
        assert( size >= 0 );
        long p = nInit( size );
        if( p == 0 ) {
            throw new RuntimeException( "Memory allocation failed or invalid argument." );
        }
        return new JavBufferPool( p );
    }


    private long mPointer;


    JavBufferPool( long pointer ) {
        mPointer = pointer;
    }


    /**
     * Allocate a new AVBuffer, reusing an old nativeBuffer from the pool when available.
     * This function may be called simultaneously from multiple threads.
     *
     * @return a reference to the new nativeBuffer on success, NULL on error.
     */
    public synchronized JavBufferRef get() {
        assert mPointer != 0;
        long ptr = nGet( mPointer );
        return ptr == 0L ? null : new JavBufferRef( ptr );
    }

    @Override
    public long pointer() {
        return 0;
    }

    @Override
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }

    @Override
    protected synchronized void freeObject() {
        long ptr = mPointer;
        mPointer = 0L;
        if( ptr != 0L ) {
            nUninit( ptr );
        }
    }

    @Override
    protected void finalize() throws Throwable {
        freeObject();
        super.finalize();
    }


    private static native long nInit( int size );
    private static native long nGet( long pointer );
    private static native void nUninit( long pointr );

}