/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

/**
 * The start of a wrapper for pixdesc.h.
 */
public class JavPixelFormat {

    /**
     * Return the pixel format corresponding to name.
     *
     * <p>If there is no pixel format with name name, then looks for a
     * pixel format with the name corresponding to the native endian
     * format of name.
     *
     * <p>For example in a little-endian system, first looks for "gray16",
     * then for "gray16le".
     *
     * <p>Finally if no pixel format has been found, returns AV_PIX_FMT_NONE.
     */
    public static native int get( String name );

    /**
     * Return the short name for a pixel format, NULL in case pix_fmt is
     * unknown.
     *
     * @see #get(String)
     * @see #getString(int)
     */
    public static native String getName( int pixFormat );

    /**
     * Print in buf the string corresponding to the pixel format with
     * number pix_fmt, or a header if pix_fmt is negative.
     *
     * @param pixFormat the number of the pixel format to print the
     *                  corresponding info string, or a negative value
     *                  to print corresponding header.
     */
    public static native String getString( int pixFormat );

}
