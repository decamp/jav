/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.swresample;

import bits.jav.Jav;
import bits.jav.JavException;
import bits.jav.codec.JavFrame;
import bits.jav.util.*;

import java.nio.ByteBuffer;


/**
 *
 * Libswresample (lswr) is a library that handles audio resampling, sample
 * format conversion and mixing.
 *
 * Interaction with lswr is done through SwrContext, which is
 * allocated with swr_alloc() or swr_alloc_set_opts(). It is opaque, so all parameters
 * must be set with the @ref avoptions API.
 *
 * For example the following code will setup conversion from planar float sample
 * format to interleaved signed 16-bit integer, downsampling from 48kHz to
 * 44.1kHz and downmixing from 5.1 channels to stereo (using the default mixing
 * matrix):
 * { @code
 * SwrContext *swr = swr_alloc();
 * av_opt_set_channel_layout(swr, "in_channel_layout",  AV_CH_LAYOUT_5POINT1, 0);
 * av_opt_set_channel_layout(swr, "out_channel_layout", AV_CH_LAYOUT_STEREO,  0);
 * av_opt_set_int(swr, "in_sample_rate",     48000,                0);
 * av_opt_set_int(swr, "out_sample_rate",    44100,                0);
 * av_opt_set_sample_fmt(swr, "in_sample_fmt",  AV_SAMPLE_FMT_FLTP, 0);
 * av_opt_set_sample_fmt(swr, "out_sample_fmt", AV_SAMPLE_FMT_S16,  0);
 * }
 *
 * Once all values have been set, it must be initialized with swr_init(). If
 * you need to change the conversion parameters, you can change the parameters
 * as described above, or by using swr_alloc_set_opts(), then call swr_init()
 * again.
 *
 * The conversion itself is done by repeatedly calling swr_convert().
 * Note that the samples may get buffered in swr if you provide insufficient
 * output space or if sample rate conversion is done, which requires "future"
 * samples. Samples that do not require future input can be retrieved at any
 * time by using swr_convert() (in_count can be set to 0).
 * At the end of conversion the resampling buffer can be flushed by calling
 * swr_convert() with NULL in and 0 in_count.
 *
 * The delay between input and output, can at any time be found by using
 * swr_get_delay().
 *
 * The following code demonstrates the conversion loop assuming the parameters
 * from above and caller-defined functions get_input() and handle_output():
 * { @code
 * uint8_t **input;
 * int in_samples;
 *
 * while (get_input(&input, &in_samples)) {
 *     uint8_t *output;
 *     int out_samples = av_rescale_rnd(swr_get_delay(swr, 48000) +
 *                                      in_samples, 44100, 48000, AV_ROUND_UP);
 *     av_samples_alloc(&output, NULL, 2, out_samples,
 *                      AV_SAMPLE_FMT_S16, 0);
 *     out_samples = swr_convert(swr, &output, out_samples,
 *                                      input, in_samples);
 *     handle_output(output, out_samples);
 *     av_freep(&output);
 * }
 * }
 *
 * When the conversion is finished, the conversion
 * context and everything associated with it must be freed with swr_free().
 * There will be no memory leak if the data is not completely flushed before
 * swr_free().
 *
 * @author Philip DeCamp
 */
public class SwrContext implements JavClass {

    /**
     * Allocates SwrContext without configuration or initalization.
     *
     * @see #allocAndInit
     * @see #config
     * @see #initialize()
     * @see #release()
     */
    public static SwrContext alloc() {
        long p = nAlloc();
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }
        return new SwrContext( p );
    }

    /**
     * Allocate SwrContext, configures common parameters and initializes.
     * Equivalent to calling alloc(), configure(), and then init().
     * <p>
     * @param srcChanLayout   input channel layout (AV_CH_LAYOUT_*)
     * @param srcSampFormat input sample format (AV_SAMPLE_FMT_*).
     * @param srcSampRate   input sample rate (frequency in Hz)
     * @param dstChanLayout   output channel layout (AV_CH_LAYOUT_*)
     * @param dstSampFormat output sample format (AV_SAMPLE_FMT_*).
     * @param dstSampRate   output sample rate (frequency in Hz)
     * @return NULL on error, allocated context otherwise
     *
     * @see #alloc()
     * @see #config
     * @see #initialize()
     * @see #release()
     */
    public static SwrContext allocAndInit( long srcChanLayout,
                                           int srcSampFormat,
                                           int srcSampRate,
                                           long dstChanLayout,
                                           int dstSampFormat,
                                           int dstSampRate )
                                           throws JavException
    {
        long p = nAlloc();
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }

        SwrContext ret = new SwrContext( p );
        int err = ret.config( srcChanLayout, srcSampFormat, srcSampRate, dstChanLayout, dstSampFormat, dstSampRate );
        if( err != Jav.AVERROR_NONE ) {
            ret.release();
            return null;
        }

        err = ret.initialize();
        if( err != Jav.AVERROR_NONE ) {
            ret.release();
            throw new JavException( err );
        }

        return ret;
    }



    private long mPointer;
    private boolean mInit = false;


    private SwrContext( long pointer ) {
        mPointer = pointer;
    }


    /**
     * Allocate SwrContext if needed and set/reset common parameters.
     * Equivalent to calling alloc(), configure(), and then init().
     * <p>
     * @param srcChanLayout   input channel layout (AV_CH_LAYOUT_*)
     * @param srcSampFormat input sample format (AV_SAMPLE_FMT_*).
     * @param srcSampRate   input sample rate (frequency in Hz)
     * @param dstChanLayout   output channel layout (AV_CH_LAYOUT_*)
     * @param dstSampFormat output sample format (AV_SAMPLE_FMT_*).
     * @param dstSampRate   output sample rate (frequency in Hz)
     * @return NULL on error, allocated context otherwise
     *
     * @see #alloc()
     * @see #initialize()
     * @see #release()
     */
    public int config( long srcChanLayout,
                       int srcSampFormat,
                       int srcSampRate,
                       long dstChanLayout,
                       int dstSampFormat,
                       int dstSampRate )
    {
        if( mInit ) {
            throw new IllegalStateException( "Cannot call configure() on initialized " + getClass().getName() );
        }

        int err;
        err = JavOption.setChannelLayout( this, "in_channel_layout", srcChanLayout, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setChannelLayout( this, "out_channel_layout", dstChanLayout, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "in_sample_rate", srcSampRate, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "out_sample_rate", dstSampRate, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setSampleFormat( this, "in_sample_fmt", srcSampFormat, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setSampleFormat( this, "out_sample_fmt", dstSampFormat, 0 );

        return err;
    }


    /**
     * Initialize context after user parameters have been set.
     * After initialize is called, SwrContext MUST NOT be reconfigured.
     */
    public int initialize() {
        return nInit( mPointer );
    }

    /**
     * Convert audio.
     * <p>
     * src and srcNumSamps may be set to 0 to flush out last few samples at end.
     * <p>
     * If more input is provided than output space, then the input will be buffered.
     * You can avoid this buffering by providing more output space than input.
     * Converting will run directly without copying whenever possible.
     *
     * @param src         Source frame.
     * @param srcSampNum  Number of samples per channel to convert.
     * @param dst         Destination frame.
     * @param dstSampNum  Available space in destination buffer, in samples pre channel.
     *
     * @return number of samples output per channel
     */
    public int convert( JavFrame src, int srcSampNum, JavFrame dst, int dstSampNum ) {
        return convert( src == null ? 0L : src.extendedData(), srcSampNum, dst.extendedData(), dstSampNum );
    }

    /**
     * Convert audio. Will work with planar or packed data.
     * <p>
     * src and srcNumsamps may be set to 0 to flush out last few samples at end.
     * <p>
     * If more input is provided than output space, then the input will be buffered.
     * You can avoid this buffering by providing more output space than input.
     * Converting will run directly without copying whenever possible.
     *
     * @param srcPtr       Source buffer of type uint8_t**.
     * @param srcSampNum   Number of samples per channel to convert.
     * @param dstPtr       Destination buffer of type uint8_t**.
     * @param dstSampNum   Available space in destination buffer, in samples pre channel.
     *
     * @return number of samples output per channel
     */
    public int convert( long srcPtr, int srcSampNum, long dstPtr, int dstSampNum ) {
        return nConvert( mPointer, srcPtr, srcSampNum, dstPtr, dstSampNum );
    }

    /**
     * Converts packed (or single-channel) audio buffer>
     *
     * @param src       Direct byte buffer containing source audio.
     * @param srcLen    Number of samples in source buffer.
     * @param dst       Direct byte buffer to receive destination audio.
     * @param dstLen    Number of available samples in destination buffer.
     * @return number of samples output per channel.
     */
    public int convertPacked( ByteBuffer src, int srcOff, int srcLen, ByteBuffer dst, int dstOff, int dstLen ) {
        return nConvertPacked( mPointer, src, srcOff, srcLen, dst, dstOff, dstLen );
    }

    /**
     * Convert the next timestamp from input to output
     * timestamps are in 1/(in_sample_rate * out_sample_rate) units.
     * <p>
     * Note:  There are 2 slightly differently behaving modes.
     *       First is when automatic timestamp compensation is not used, (min_compensation >= FLT_MAX)
     *              in this case timestamps will be passed through with delays compensated
     *       Second is when automatic timestamp compensation is used, (min_compensation < FLT_MAX)
     *              in this case the output timestamps will match output sample numbers
     *
     * @param pts   timestamp for the next input sample, INT64_MIN if unknown
     * @return the output timestamp for the next output sample
     */
    public long nextPts( long pts ) {
        return nNextPts( mPointer, pts );
    }

    /**
     * Activate resampling compensation.
     */
    public int setCompensation( int sampleDelta, int compensationDistance ){
        return nSetCompensation( mPointer, sampleDelta, compensationDistance );
    }

    /**
     * Set a customized input channel mapping. SwrContext MUST NOT be initialized yet.
     *
     * @param channelMap customized input channel mapping (array of channel
     *                   indexes, -1 for a muted channel)
     * @return AVERROR error code in case of failure.
     */
    public int setChannelMapping( int[] channelMap ) {
        return nSetChannelMapping( mPointer, channelMap );
    }

    /**
     * Set a customized remix matrix. This SwrContext MUST NOT be initialized yet.
    *
     * @param matrix  remix coefficients; matrix[i + stride * o] is
     *                the weight of input channel i in output channel o
     * @param stride  offset between lines of the matrix
     * @return  AVERROR error code in case of failure.
     */
    public int setMatrix( double[] matrix, int stride ) {
        return nSetMatrix( mPointer, matrix, stride );
    }

    /**
     * Drops the specified number of output samples.
     */
    public int dropOutput( int count ) {
        return nDropOutput( mPointer, count );
    }

    /**
     * Injects the specified number of silence samples.
     */
    public int injectSilence( int count ) {
        return nInjectSilence( mPointer, count );
    }

    /**
     * Gets the delay the next input sample will experience relative to the next output sample.
     *
     * Swresample can buffer data if more input has been provided than available
     * output space, also converting between sample rates needs a delay.
     * This function returns the sum of all such delays.
     * The exact delay is not necessarily an integer value in either input or
     * output sample rate. Especially when downsampling by a large value, the
     * output sample rate may be a poor choice to represent the delay, similarly
     * for upsampling and the input sample rate.
     *
     * @param base  timebase in which the returned delay will be
     *              if its set to 1 the returned delay is in seconds
     *              if its set to 1000 the returned delay is in milli seconds
     *              if its set to the input sample rate then the returned delay is in input samples
     *              if its set to the output sample rate then the returned delay is in output samples
     *              an exact rounding free delay can be found by using LCM(in_sample_rate, out_sample_rate)
     * @return      the delay in 1/base units.
     */
    public long getDelay( long base ) {
        return nGetDelay( mPointer, base );
    }



    public void release() {
        long p = mPointer;
        mPointer = 0;
        if( p != 0 ) {
            nFree( p );
        }
    }

    @Override
    public long pointer() {
        return mPointer;
    }

    @Override
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }


    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }



    private static native long nAlloc();
    private static native void nFree( long pointer );
    private static native int  nInit( long pointer );

    private static native int nConvert( long ptr, long srcPtr, int srcNumSamps, long dstPtr, int dstNumSamps );
    private static native int nConvertPacked( long ptr, ByteBuffer src, int srcOff, int srcLen, ByteBuffer dst, int dstOff, int dstLen );

    private static native long nNextPts( long ptr, long pts );
    private static native int  nSetCompensation( long ptr, int sampleDelta, int compensationDistance );
    private static native int  nSetChannelMapping( long ptr, int[] map );
    private static native int  nSetMatrix( long ptr, double[] matrix, int stride );
    private static native int  nDropOutput( long ptr, int count );
    private static native int  nInjectSilence( long ptr, int count );
    private static native long nGetDelay( long ptr, long base );

}
