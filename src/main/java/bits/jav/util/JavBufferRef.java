/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.nio.ByteBuffer;
import bits.jav.JavException;
import bits.util.ref.*;


/**
 * Wrapper around AVBufferRef object.
 * <p>
 * AVBufferRef uses its own reference counting that is incompatible
 * with {@link bits.util.ref.Refable}. JavBufferRef holds a single reference
 * to the native AVBufferRef object, and uses its own reference counter
 * for the java object. When the java reference counter reaches zero,
 * the java object becomes invalid, and the reference to the native
 * object is released. If the JavBufferRef has a pool, then it will
 * added to that pool only after all references to the native object have 
 * been released.
 * <p>
 * TODO: Add pool support.
 * 
 * @author decamp
 */
public class JavBufferRef implements NativeObject, Refable {
    
    /**
     * Allocate an AVBuffer of the given size using av_malloc().
     *
     * @return an AVBufferRef of given size or NULL when out of memory
     */
    public static JavBufferRef alloc( int size, boolean clear ) {
        long p = clear ? nAllocZ( size ) : nAlloc( size );
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }
        return new JavBufferRef( p, null );
    }
    
    /**
     * Create JavBufferRef instance for native AVBufferRef instance. Note
     * that this method constructs a new native AVBufferRef object.
     * 
     * @param pointer to AVBufferRef.
     * @return JavBufferRef instance with provided pointer.
     */
    public static JavBufferRef wrap( long pointer ) {
        return new JavBufferRef( nRef( pointer ), null );
    }
    
    /**
     * Create an AVBuffer from an existing array.
     *
     * If this function is successful, data is owned by the AVBuffer. The caller may
     * only access data through the returned AVBufferRef and references derived from
     * it.
     * If this function fails, data is left untouched.
     * @param buf    Directly allocated ByteBuffer object, with position() and limit() set as desired.
     * @param flags  a combination of AV_BUFFER_FLAG_*
     *
     * @return an AVBufferRef referring to data on success, NULL on failure.
     */
    public static JavBufferRef wrap( ByteBuffer buf, int flags ) {
        long p = nWrap( buf, buf.position(), buf.remaining(), flags );
        if( p == 0 ) {
            throw new IllegalArgumentException();
        }
        return new JavBufferRef( p, null );
    }
    
    
    
    private long mPointer;
    
    private final ByteBuffer mWrappedBuf;
    private int mRefCount = 1;
    
    
    JavBufferRef( long pointer, ByteBuffer wrappedBuf ) {
        mPointer    = pointer;
        mWrappedBuf = wrappedBuf;
    }
        
    
    /**
     * The data buffer. It is considered writable if and only if
     * this is the only reference to the buffer, in which case
     * av_buffer_is_writable() returns 1.
     */
    public long data() {
        return nData( mPointer );
    }
    
    /**
     * Size of data in bytes.
     */
    public int size() {
        return nSize( mPointer );
    }
    
    /**
     * @return 1 if the caller may write to the data referred to by buf (which is
     * true if and only if buf is the only reference to the underlying AVBuffer).
     * Return 0 otherwise.
     * A positive answer is valid until av_buffer_ref() is called on buf.
     */
    public boolean isWritable() {
        return nIsWritable( mPointer ) != 0;
    }
    
    /**
     * Create a writable reference from a given buffer reference, avoiding data copy
     * if possible.On success, buf is either left untouched, or it is unreferenced
     * and a new writable AVBufferRef is written in its place. On failure, buf is left untouched.
     *
     * @return 0 on success, a negative AVERROR on failure.
     */
    public int makeWritable() {
        return nMakeWritable( mPointer );
    }

    
    public boolean ref() {
        synchronized( this ) {
            if( mPointer == 0L ) {
                return false;
            }
            mRefCount++;
            return true;
        }
    }
    
    
    public void deref() {
        long ptr = 0L;
        
        synchronized( this ) {
            if( --mRefCount > 0 ) {
                return;
            }
            mRefCount = 0;
            if( mPointer == 0L ) {
                return;
            }
            
            ptr = mPointer;
            mPointer = 0L;
        }
        
        nUnref( ptr );
    }
    
    
    public int refCount() {
        return mRefCount;
    }

    /**
     * @return the number of native references to tis BufferRef.
     */
    public int nativeRefCount() {
        return nRefCount( mPointer );
    }
    
    /**
     * Reallocate a given buffer. On success, the underlying buffer will be
     * unreferenced and a new reference with the required size will be
     * written in its place. On failure buf will be left untouched.
     *
     * @param size required new buffer size.
     * @return JavBufferRef with requested size. May be this, or different object.
     *
     * @note the buffer is actually reallocated with av_realloc() only if it was
     * initially allocated through av_buffer_realloc(NULL) and there is only one
     * reference to it (i.e. the one passed to this function). In all other cases
     * a new buffer is allocated and the data is copied.
     * 
     * @throws JavException on error.
     */
    public JavBufferRef realloc( int size ) throws JavException {
        long[] p = { mPointer };
        int ret  = nRealloc( p, size );
        if( ret != 0 ) {
            throw new JavException( ret );
        }
        
        long ptr = p[0];
        if( ptr == mPointer ) {
            return this;
        }
        
        return new JavBufferRef( ptr, null );
    }

    /**
     * Don't mess with this.
     */
    public long buffer() {
        return nBuffer( mPointer );
    }

    /**
     * Don't mess with this.
     */
    public ByteBuffer wrappedBuffer() {
        return mWrappedBuf;
    }
    
    
    
    @Override
    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        long p = 0L;
        synchronized( this ) {
            p = mPointer;
            mPointer = 0L;
        }
        if( p != 0L ) {
            nUnref( p );
        }            
        super.finalize();
    }
    
    
    
    private static native long nAlloc( int size );
    private static native long nAllocZ( int size );
    private static native long nWrap( ByteBuffer buf, int off, int size, int flags );
    private static native int nRealloc( long[] pointer, int size );
    
    private static native long nRef( long pointer );
    private static native void nUnref( long pointer );
    private static native int nRefCount( long pointer );
    
    private static native int nIsWritable( long pointer );
    private static native int nMakeWritable( long pointer );
    
    private static native long nBuffer( long pointer );
    private static native long nData( long pointer );
    private static native int nSize( long pointer );

}
