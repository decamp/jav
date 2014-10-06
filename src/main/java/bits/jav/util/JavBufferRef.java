/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import bits.jav.JavException;
import static bits.jav.Jav.assertOkay;


/**
 * AVBuffer is an API for reference-counted data buffers.
 *
 * <p>There are two core objects in this API -- AVBuffer and AVBufferRef. AVBuffer
 * represents the data buffer itself; it is opaque and not meant to be accessed
 * by the caller directly, but only through AVBufferRef. However, the caller may
 * e.g. compare two AVBuffer pointers to check whether two different references
 * are describing the same data buffer. AVBufferRef represents a single
 * reference to an AVBuffer and it is the object that may be manipulated by the
 * caller directly.
 *
 * <p>There are two functions provided for creating a new AVBuffer with a single
 * reference -- av_buffer_alloc() to just allocate a new buffer, and
 * av_buffer_create() to wrap an existing array in an AVBuffer. From an existing
 * reference, additional references may be created with av_buffer_ref().
 * Use av_buffer_unref() to free a reference (this will automatically free the
 * data once all the references are freed).
 *
 * <p>The convention throughout this API and the rest of FFmpeg is such that the
 * buffer is considered writable if there exists only one reference to it (and
 * it has not been marked as read-only). The av_buffer_is_writable() function is
 * provided to check whether this is true and av_buffer_make_writable() will
 * automatically create a new writable buffer when necessary.
 * Of course nothing prevents the calling code from violating this convention,
 * however that is safe only when all the existing references are under its
 * control.
 *
 * <p>Note: Referencing and unreferencing the buffers is thread-safe and thus
 * may be done from multiple threads simultaneously without any need for
 * additional locking.
 *
 * <p>Note: Two different references to the same buffer can point to different
 * parts of the buffer (i.e. their AVBufferRef.data will not be equal).
 */
public class JavBufferRef implements NativeObject {

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
        return new JavBufferRef( p );
    }

    /**
     * Create an AVBuffer from an existing array.
     * <p>
     * If this function is successful, data is owned by the AVBuffer. The caller may
     * only access data through the returned AVBufferRef and references derived from
     * it. On failure, buf is left untouched.
     *
     * @param buf    Directly allocated ByteBuffer object, with position() and limit() set as desired.
     * @param flags  Union of desired AV_BUFFER_FLAG_* flags.
     *
     * @return an AVBufferRef referencing the byte nativeBuffer.
     * @throws IllegalArgumentException on failer.
     */
    public static JavBufferRef wrap( ByteBuffer buf, int flags ) {
        assert buf != null;
        assert buf.isDirect();

        long p = nWrap( buf, buf.position(), buf.remaining(), flags );
        if( p == 0 ) {
            throw new IllegalArgumentException();
        }
        return new JavBufferRef( p );
    }

    /**
     * Creates a new reference via {@code av_buffer_ref( pointer )}.
     * The new reference is wrapped and returned as a JavBufferRef instance..
     *
     * @param pointer of type AVBufferRef*.
     * @return JavBufferRef wrapping new reference.
     */
    public static JavBufferRef refPointer( long pointer ) {
        return new JavBufferRef( nRef( pointer ) );
    }

    /**
     * Wraps an existing AVBufferRef pointer without creating new referenc.
     *
     * @param pointer of type AVBufferRef*.
     * @return JavBufferRef wrapping existing reference.
     */
    public static JavBufferRef wrapPointer( long pointer ) {
        return new JavBufferRef( pointer );
    }


    private volatile long mPointer;


    JavBufferRef( long pointer ) {
        mPointer    = pointer;
    }

    
    /**
     * The data buffer. It is considered writable if and only if
     * this is the only reference to the nativeBuffer, in which case
     * av_buffer_is_writable() returns 1.
     *
     * @return uint8_t* pointer to data.
     */
    public long data() {
        long p = mPointer;
        return nData( p );
    }

    /**
     * @return size of data in bytes.
     */
    public int size() {
        long p = mPointer;
        return nSize( p );
    }

    /**
     * Create a new reference to an AVBuffer.
     *
     * @return a new AVBufferRef referring to the same AVBuffer as buf or NULL on failure.
     */
    public synchronized JavBufferRef ref() {
        long p = mPointer;
        if( p == 0L ) {
            throw new IllegalStateException( "Buffer already released" );
        }
        p = nRef( p );
        return p == 0L ? null : new JavBufferRef( p );
    }

    /**
     * Free a given reference and automatically free the buffer if there are no more
     * references to it.
     * <p>
     * Object cannot be used after {@code unref()} is called.
     */
    public synchronized void unref() {
        long p = mPointer;
        mPointer = 0L;
        if( p != 0L ) {
            nUnref( p );
        }
    }

    /**
     * @return count of references to data buffer
     */
    public int refCount() {
        return nRefCount( mPointer );
    }

    /**
     * A nativeBuffer is "writable" iff there is only a single reference to the nativeBuffer.
     *
     * @return true if the caller may write to the nativeBuffer. A true value is valid
     *         until ref() is called on buf.
     */
    public boolean isWritable() {
        long p = mPointer;
        return nIsWritable( p ) != 0;
    }

    /**
     * Create a writable reference from a given nativeBuffer reference, avoiding data copy
     * if possible. On success, buf is either left untouched, or is unreferenced
     * and a new writable AVBufferRef is created in its place. On failure, buf is
     * left untouched.
     *
     * @return 0 on success, a negative AVERROR on failure.
     */
    public int makeWritable() {
        return nMakeWritable( mPointer );
    }

    /**
     * Reallocate a given nativeBuffer. On success, the underlying nativeBuffer will be
     * unreferenced and a new reference with the required size will be
     * written in its place. On failure buf will be left untouched and a JavException will
     * be thrown.
     *
     * @param size required new nativeBuffer size.
     * @return JavBufferRef with requested size. May be this, or different object.
     *
     * Note: the nativeBuffer is actually reallocated with av_realloc() only if it was
     * initially allocated through av_buffer_realloc(NULL) and there is only one
     * reference to it (i.e. the one passed to this function). In all other cases
     * a new nativeBuffer is allocated and the data is copied.
     *
     * @throws JavException on error.
     */
    public synchronized JavBufferRef realloc( int size ) throws JavException {
        long[] p = { mPointer };
        assertOkay( nRealloc( p, size ) );

        long ptr = p[0];
        if( ptr == mPointer ) {
            return this;
        }

        mPointer = 0;
        return new JavBufferRef( ptr );
    }

    /**
     * @return true iff this object is backed by a java-allocated ByteBuffer.
     */
    public boolean hasJavaBuffer() {
        return nJavaByteBuffer( mPointer ) != null;
    }

    /**
     * @return a safe copy of the ByteBuffer backing this buf, or null if not backed by java ByteBuffer.
     */
    public ByteBuffer javaBuffer() {
        ByteBuffer b = nJavaByteBuffer( mPointer );
        return b == null ? null : b.duplicate().order( ByteOrder.nativeOrder() );
    }


    @Override
    public long pointer() {
        return mPointer;
    }


    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.USER;
    }

    @Override
    protected void finalize() throws Throwable {
        unref();
        super.finalize();
    }

    /**
     * The native buffer. Should not be accessed directly.
     *
     * @return AVBuffer pointer.
     */
    long buffer() {
        return nBuffer( mPointer );
    }


    private static native long nAlloc( int size );
    private static native long nAllocZ( int size );
    private static native long nWrap( ByteBuffer buf, int off, int size, int flags );

    private static native long nBuffer( long pointer );
    private static native long nData( long pointer );
    private static native int nSize( long pointer );

    private static native long nRef( long pointer );
    private static native void nUnref( long pointer );
    private static native int nRefCount( long pointer );
    
    private static native int nIsWritable( long pointer );
    private static native int nMakeWritable( long pointer );
    private static native int nRealloc( long[] pointer, int size );

    private static native ByteBuffer nJavaByteBuffer( long pointer );

}
