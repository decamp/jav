/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.nio.ByteBuffer;


public final class JavSampleFormat {

    /**
     * @return name of fmt, or null if not recognized.
     */
    public static native String getSampleFormatName( int fmt );


    /**
     * @return a sample format corresponding to name, or AV_SAMPLE_FMT_NONE on error.
     */
    public static native int getSampleFormat( String name );


    /**
     * @return the planar<->packed alternative form of the given sample format, or
     *         AV_SAMPLE_FMT_NONE on error. If the passed sample_fmt is already in the
     *         requested planar/packed format, the format returned is the same as the
     *         input.
     */
    public static native int getAltSampleFormat( int fmt, int planar );


    /**
     * Generate a string corresponding to the sample format with
     * sample_fmt, or a header if sample_fmt is negative.
     *
     * @param buf Direct nativeBuffer to receive string.
     * @param fmt the number of the sample format to print the
     *            corresponding info string, or a negative value to print the
     *            corresponding header.
     * @return true if succesful, false if fmt is unknown or other errors.
     */
    public static boolean getSampleFormatString( ByteBuffer buf, int fmt ) {
        long n = nGetSampleFormatString( buf, buf.position(), buf.remaining(), fmt );
        return n != 0;
    }


    /**
     * @param sampleFormat the sample format
     * @return number of bytes per sample or zero if unknown for the given sample format.
     */
    public static native int getBytesPerSample( int sampleFormat );


    /**
     * Check if the sample format is planar.
     *
     * @param format the sample format to inspect
     * @return true if sample format is planar, false if interleaved.
     */
    public static native boolean isFormatPlanar( int format );


    /**
     * Get the required nativeBuffer size for the given audio parameters.
     *
     * @param chanNum   the number of channels
     * @param sampNum    the number of samples in a single channel
     * @param sampFmt    the sample format
     * @param align        nativeBuffer size alignment (0 = default, 1 = no alignment)
     * @param optLineSize  [OUTPUT] receives calculated linesize. May be NULL.
     * @return required nativeBuffer size, or negative error code on failure
     */
    public static native int getBufferSize( int chanNum,
                                            int sampNum,
                                            int sampFmt,
                                            int align,
                                            int[] optLineSize );

    /**
     * Fill plane data pointers and linesize for samples with sample
     * format sampFmt.
     *
     * The outBufOffsets is filled with the offets to the audio sample data planes:
     * for planar, set the start point of each channel's data within the nativeBuffer,
     * for packed, set the start point of the entire nativeBuffer only.
     *
     * The value pointed to by linesize is set to the aligned size of each
     * channel's data nativeBuffer for planar layout, or to the aligned size of the
     * nativeBuffer for all channels for packed layout.
     *
     * The nativeBuffer in buf must be big enough to contain all the samples
     * (use av_samples_get_buffer_size() to compute its minimum size),
     * otherwise the audio_data pointers will point to invalid data.
     *
     * @see bits.jav.Jav
     * The documentation for AVSampleFormat describes the data layout.
     *
     * @param buf            Buffer of type uint8_t*.
     * @param chanNum        Number of channels.
     * @param sampNum        Number of samples per channel.
     * @param sampFmt        Sample format.
     * @param align          Buffer size alignment (0 = default, 1 = no alignmnet)
     * @param outDataPtrs    [OUT] Array of type uint8_t*[] to receive nativeBuffer pointers for each channel.
     *                       For planar formats, this must be uint8_t*[chanNum] or larger.
     *                       For packed formats, this may be uint8_t*[1] or larger.
     * @param optLineSize    [OUT] Array of length 1 to receive line size. May be NULL.
     *
     * @return error code
     */
    public static int fillArrays( ByteBuffer buf,
                                  int chanNum,
                                  int sampNum,
                                  int sampFmt,
                                  int align,
                                  ByteBuffer outDataPtrs,
                                  int[] optLineSize )
    {
        return fillArrays( JavMem.nativeAddress( buf ) + buf.position(),
                           chanNum,
                           sampNum,
                           sampFmt,
                           align,
                           JavMem.nativeAddress( outDataPtrs ) + outDataPtrs.position(),
                           optLineSize );
    }


    /**
     * Fill plane data pointers and linesize for samples with sample
     * format sampFmt.
     *
     * The outBufOffsets is filled with the offets to the audio sample data planes:
     * for planar, set the start point of each channel's data within the nativeBuffer,
     * for packed, set the start point of the entire nativeBuffer only.
     *
     * The value pointed to by linesize is set to the aligned size of each
     * channel's data nativeBuffer for planar layout, or to the aligned size of the
     * nativeBuffer for all channels for packed layout.
     *
     * The nativeBuffer in buf must be big enough to contain all the samples
     * (use av_samples_get_buffer_size() to compute its minimum size),
     * otherwise the audio_data pointers will point to invalid data.
     *
     * @see bits.jav.Jav
     * The documentation for AVSampleFormat describes the data layout.
     *
     * @param bufPtr         Buffer of type uint8_t*.
     * @param chanNum        Number of channels.
     * @param sampNum        Number of samples per channel.
     * @param sampFmt        Sample format.
     * @param align          Buffer size alignment (0 = default, 1 = no alignmnet)
     * @param outDataPtr     [OUT] Array of type uint8_t*[] to receive nativeBuffer pointers for each channel.
     *                       For planar formats, this must be uint8_t*[chanNum] or larger.
     *                       For packed formats, this may be uint8_t*[1] or larger.
     * @param optLineSize    [OUT] Array of length 1 to receive line size. May be NULL.
     *
     * @return error code
     */
    public static native int fillArrays( long bufPtr,
                                         int chanNum,
                                         int sampNum,
                                         int sampFmt,
                                         int align,
                                         long outDataPtr,
                                         int[] optLineSize );

    /**
     * Copy samples from src to dst.
     *
     * @param srcPtr     [uint8_t**]-type pointer to source nativeBuffer array.
     * @param srcOff     Offset into source data, in samples-per-channel.
     * @param chanNum    Number of audio channels.
     * @param sampNum    Number of samples per channel to copy.
     * @param sampFmt    Audio sample format.
     * @param dstPtr     [uint8_t**]-type pointer to destination nativeBuffer array.
     * @param dstOff     Offset into dest data, in samples-per-channel.
     * @return error code
     */
    public static native int copy( long srcPtr,
                                   int srcOff,
                                   int chanNum,
                                   int sampNum,
                                   int sampFmt,
                                   long dstPtr,
                                   int dstOff );

    /**
     * Fill an audio nativeBuffer with silence.
     *
     * @param dataPtr  [uint8_t**]-type pointer to data array.
     * @param dataOff  Offset into data in samples-per-channel.
     * @param chanNum  Number of audio channels.
     * @param sampNum  Number of samples to fill.
     * @param sampFmt  Audio sample format.
     */
    public static native int setSilence( long dataPtr,
                                         int dataOff,
                                         int chanNum,
                                         int sampNum,
                                         int sampFmt );


    private static native long nGetSampleFormatString( ByteBuffer buf, int off, int len, int fmt );


    private JavSampleFormat() {}

}
