/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;


import java.nio.ByteBuffer;


/**
 * For native memory allocation. These are not necessarily generic
 * implementations of malloc, free, etc, but the allocators used by
 * in FFMPEG: av_malloc, av_free, etc.
 * <p>
 * Dangerous. None of these methods throw exceptions. Many will crash the JVM if not used correctly.
 */
public final class JavMem {

    /**
     * Allocate a block of size bytes with alignment suitable for all
     * memory accesses (including vectors if available on the CPU).
     * @param size Size in bytes for the memory block to be allocated.
     * @return Pointer to the allocated block, 0 if the block cannot
     * be allocated.
     * @see #mallocz( long )
     */
    public static native long malloc( long size );

    /**
     * Allocate a block of size bytes with alignment suitable for all
     * memory accesses (including vectors if available on the CPU) and
     * zero all the bytes of the block.
     * @param size Size in bytes for the memory block to be allocated.
     * @return Pointer to the allocated block, 0 if it cannot be allocated.
     * @see #malloc( long )
     */
    public static native long mallocz( long size );

    /**
     * Allocate or reallocate a block of memory.
     * If ptr is NULL and size > 0, allocate a new block. If
     * size is zero, free the memory block pointed to by ptr.
     * @param ptr ptr to a memory block already allocated with
     *                realloc() or NULL.
     * @param size    Size in bytes of the memory block to be allocated or
     *                reallocated.
     * @return Pointer to a newly-reallocated block or 0 if the block
     * cannot be reallocated or the function is used to free the memory block.
     * @warning Pointers originating from the av_malloc() family of functions must
     *          not be passed to realloc(). The former can be implemented using
     *          memalign() (or other functions), and there is no guarantee that
     *          pointers from such functions can be passed to realloc() at all.
     *          The situation is undefined according to POSIX and may crash with
     *          some libc implementations.
     */
    public static native long realloc( long ptr, long size );

    /**
     * Allocate or reallocate a block of memory.
     * This function does the same thing as {@link #realloc(long,long)}, except:
     * - It takes two arguments and checks the result of the multiplication for
     *   integer overflow.
     * - It frees the input block in case of failure, thus avoiding the memory
     *   leak with the classic {@code buf = realloc(buf); if (!buf) return -1;}
     */
    public static native long reallocf( long ptr, long elCount, long elSize );

    /**
     * Allocate a block of size * nmemb bytes with av_malloc().
     * @param elCount Number of elements
     * @param elSize  Size of the single element
     * @return Pointer to the allocated block, 0 if the block cannot be allocated.
     * @see #malloc( long )
     */
    public static long mallocArray( long elCount, long elSize ) {
        if( elSize <= 0 || elCount >= Integer.MAX_VALUE / elSize ) {
            return 0L;
        }
        return malloc( elCount * elSize );
    }

    /**
     * Allocate a block of size * nmemb bytes with av_mallocz().
     * @param nmemb Number of elements
     * @param size Size of the single element
     * @return Pointer to the allocated block, NULL if the block cannot
     * be allocated.
     * @see #mallocz(long)
     * @see #mallocArray(long,long)
     */
    public static long malloczArray( long nmemb, long size ) {
        if( size == 0 || nmemb >= Integer.MAX_VALUE / size ) {
            return 0L;
        }
        return mallocz( nmemb * size );
    }

    /**
     * Allocate or reallocate an array.
     * If ptr is NULL and nmemb > 0, allocate a new block. If
     * nmemb is zero, free the memory block pointed to by ptr.
     * @param ptr    Pointer to a memory block already allocated with
     *               {@link #realloc(long,long)} or 0.
     * @param nmemb  Number of elements
     * @param size   Size of the single element
     * @return Pointer to a newly-reallocated block or 0 if the block
     * cannot be reallocated or the function is used to free the memory block.
     * @warning Pointers originating from the av_malloc() family of functions must
     *          not be passed to av_realloc(). The former can be implemented using
     *          memalign() (or other functions), and there is no guarantee that
     *          pointers from such functions can be passed to realloc() at all.
     *          The situation is undefined according to POSIX and may crash with
     *          some libc implementations.
     */
    public static native long reallocArray( long ptr, long nmemb, long size );

    /**
     * Duplicate the nativeBuffer p.
     *
     * @param ptr   Buffer to be duplicated
     * @param size  Size of nativeBuffer.
     * @return Pointer to a newly allocated nativeBuffer containing a
     *         copy of *ptr, or 0 if the nativeBuffer cannot be allocated.
     */
    public static native long memdup( long ptr, long size );

    /**
     * Duplicate a string.
     *
     * @param ptr Pointer to c-string to be duplicated.
     * @return Pointer to a newly-allocated string containing a
     *         copy of *ptr, or 0 if the string cannot be allocated.
     */
    public static native long strdup( long ptr );

    /**
     * Free a memory block which has been allocated with av_malloc(z)() or
     * av_realloc().
     * @param pointer Pointer to the memory block which should be freed.
     * @note pointer = 0L is explicitly allowed.
     * @note It is recommended that you use freep() instead.
     */
    public static native void free( long pointer );

    /**
     * Copies block of memory from native memory address into a directly-allocated
     * ByteBuffer.
     * <p>
     * This is an excellent method to call to crash the JVM. Use responsibly.
     *
     * @param srcPointer  Native memory address where data will be read from.
     * @param dst         Directly-allocated ByteBuffer where data will be written to.
     *                    {@code dst.position()} and {@code dst.limit()}
     *                    should be set to indicate what portion of ByteBuffer to fill.
     */
    public static void copy( long srcPointer, ByteBuffer dst ) {
        nCopyPointerToBuffer( srcPointer, dst, dst.position(), dst.remaining() );
        dst.position( dst.limit() );
    }

    /**
     * Copies block of memory from directly-allocated ByteBuffer to a native memory
     * address.
     * <p>
     * This is an excellent method to call to crash the JVM. Use responsibly.
     *
     * @param src         Directly-allocated ByteBuffer where data will be read from.
     *                    {@code src.position()} and {@code src.limit()}
     *                    should be set to indicate what portion of ByteBuffer to copy.
     * @param dstPointer  Native memory address where data will be written to.
     */
    public static void copy( ByteBuffer src, long dstPointer ) {
        nCopyBufferToPointer( src, src.position(), src.remaining(), dstPointer );
        src.position( src.limit() );
    }

    /**
     * Copies block of memory from one pointer to another.
     * <p>
     * This is an excellent method to call to crash the JVM. Use responsibly.
     *
     * @param srcPointer  Native memory address where data will be read.
     * @param dstPointer  Native memory address where data will be written.
     * @param len         Number of bytes to copy.
     */
    public static native void copy( long srcPointer, long dstPointer, long len );

    /**
     * Copies a block of memory from one pointer to another in reverse order.
     *
     * @param srcPointer  Native memory address where data will be read.
     * @param dstPointer  Native memory address where data will be written.
     * @param chunkNum    Number of chunks to copy
     * @param chunkSize   Size of each chunk in bytes.
     * @throws IllegalArgumentException
     */
    public static native void copyReverse( long srcPointer, long dstPointer, int chunkNum, int chunkSize );

    /**
     * Fills the first {@code size} bytes of a memory area with constant byte {@code val}.
     */
    public static native void memset( long ptr, int val, int size );

    /**
     * @param src Directly allocated ByteBuffer.
     * @return native memory address of src.
     */
    public static native long nativeAddress( ByteBuffer src );



    private JavMem() {}

    private static native void nCopyPointerToBuffer( long srcPtr, ByteBuffer dst, int pos, long len );

    private static native void nCopyBufferToPointer( ByteBuffer src, int pos, long len, long dstPtr );

}
