/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.nio.*;


/**
* @author decamp
* @deprecated {@see bits.jav.JavMem}
*/
@Deprecated public class NativeUtil {

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
     * @throws nothing!   This method will just crash the JVM with a segfault or an
     *                    illegal access exception.
     */
    public static void copy( long srcPointer, ByteBuffer dst ) {
        nCopyPointerBuffer( srcPointer, dst, dst.position(), dst.remaining() );
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
     * @throws nothing!   This method will just crash the JVM with a segfault or an
     *                    illegal access exception.
     */
    public static void copy( ByteBuffer src, long dstPointer ) {
        nCopyBufferPointer( src, src.position(), src.remaining(), dstPointer );
        src.position( src.limit() );
    }

    /**
     * Copies block of memory from one pointer to another.
     * <p>
     * This is an excellent method to call to crash the JVM. Use responsibly.
     *
     * @param srcPointer  Native memory address where data will be read from.
     * @param dstPointer  Native memory address where data will be written to.
     * @param len         Number of bytes to copy.
     * @throws nothing!   This method will just crash the JVM with a segfault or an
     *                    illegal access exception.
     */
    public static native void copy( long srcPointer, long dstPointer, int len );

    /**
     * @param src   Directly allocated ByteBuffer.
     * @return native memory address of src.
     */
    public static native long nativeAddress( ByteBuffer src );



    private static native void nCopyPointerBuffer( long srcPtr, ByteBuffer dst, int pos, int len );

    private static native void nCopyBufferPointer( ByteBuffer src, int pos, int len, long dstPtr );

}
