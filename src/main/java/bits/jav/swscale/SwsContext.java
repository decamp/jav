/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.swscale;

import bits.jav.*;
import bits.jav.codec.*;
import bits.jav.util.*;

import static bits.jav.JavException.assertNoErr;
import static bits.jav.Jav.*;


/**
 * SwsContext is used for performing image scaling and pixel format conversions.
 * To use a SwsContext, you must do the following:
 * <ol>
 * <li>newInstance()          - Create new instance </li>
 * <li>setColorSpaceDetails() - Configure color space.</li>
 * <li>configure()            - Configure the conversion by specifying input and output formats. </li>
 * <li>initialize()           - Initializes the SwsContext, after which it may not be reconfigured. </li>
 * <li>convert()              - Convert frames as you desire.<li>
 * </ol>
 * 
 * SwsContext is not thread-safe.
 * 
 * 
 * A note on scaler algorithm performance that I found online:
 * 
 * Tests results for downsample from 640x480 to 320x240
 * and back again.  Test used mmx+mmx2 on E6400 intel.
 * 
 * ALG:                  ERR (sum-squared-diff)   TIME
 * SWS_POINT             3769328057                1.1034
 * SWS_FAST_BILINEAR     2636583475                1.2289
 * SWS_BILINEAR          2155004550                1.7151
 * SWS_BICUBLIN          2039237301                2.2899
 * SWS_BICUBIC           1980996866                2.6026
 * SWS_SPLINE            1951467852                6.719
 * SWS_SINC              1902084412               13.2753
 *
 */
public final class SwsContext implements JavClass {
    
    
    public static SwsContext alloc() {
        long p = nAlloc();
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }
        return new SwsContext( p );
    }
    
    
    public static SwsContext allocAndInit( int srcWidth, 
                                           int srcHeight, 
                                           int srcFormat,
                                           int dstWidth, 
                                           int dstHeight, 
                                           int dstFormat,
                                           int flags )
                                           throws JavException
    {
        long p = nAlloc();
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }
        
        SwsContext ret = new SwsContext( p );
        assertOkay( ret.config( srcWidth, srcHeight, srcFormat, dstWidth, dstHeight, dstFormat, flags ) );
        assertOkay( ret.init() );
        return ret;
    }
    
    
    
    private long mPointer;
    private boolean mColorspaceConfigured = false;
    private boolean mInit                 = false;
    
    
    private SwsContext( long pointer ) {
        mPointer = pointer;
    }


    /**
     * Configures SwsContext.  SHOULD be called, or else initialization may
     * fail.  Alternatively, you may use JavOption directly.
     *
     * @param srcWidth   Width of source image in pixels.
     * @param srcHeight  Height of source image in pixels.
     * @param srcFormat  Pixel format of source image, defined in JavConstants.PIX_FMT_?
     * @param dstWidth   Width of destination image in pixels.
     * @param dstHeight  Height of destination image in pixels.
     * @param dstFormat  Pixel format of dest image, defined in JavConstants.PIX_FMT_?
     * @param flags  Flags for conversion.  Must specify ONE scale algorithm.  See class documentation.
     * @return 0 if successfuly, otherwise ne
     */
    public int config( int srcWidth,
                       int srcHeight,
                       int srcFormat,
                       int dstWidth,
                       int dstHeight,
                       int dstFormat,
                       int flags )
    {
        if( mInit ) {
            throw new IllegalStateException( "Cannot call configure() on initialized " + SwsContext.class.getName() );
        }

        int err;
        err = JavOption.setLong( this, "srcw", srcWidth, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "srch", srcHeight, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "src_format", srcFormat, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "src_range", csRangeValueFor( srcFormat ), 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "dstw", dstWidth, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "dsth", dstHeight, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "dst_format", dstFormat, 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "dst_range", csRangeValueFor( dstFormat ), 0 );
        if( err != 0 ) { return err; }
        err = JavOption.setLong( this, "sws_flags", flags, 0 );
        return err;
    }


    /**
     * Initializes SwsContext to prepare it for frame conversions.
     * After initialization, SwsContext MUST NOT be reconfigured.
     */
    public int init() {
        if( mInit ) {
            throw new IllegalStateException( "Cannot call initialize() on initialized " + SwsContext.class.getName() );
        }

        if( !mColorspaceConfigured ) {
            int[] cs = ColorSpaces.FF_YUV2RGB_COEFFS[ SWS_CS_DEFAULT ];
            setColorSpaceDetails( 0, 0, cs, cs, 0, 1 << 16, 1 << 16 );
        }

        mInit = true;
        return nInit( mPointer );
    }



    /**
     * Converts a source picture to a destination picture.
     * SwsContext must be initialized.
     *
     * @param src   Source frame.
     * @param dst   Pre-allocated and correctly formatted destination frame.
     * @return Height of output slice, or <0 if error.
     */
    public int conv( JavFrame src, JavFrame dst ) {
        return nConvert( mPointer, src.pointer(), 0, src.height(), dst.pointer() );
    }


    /**
     * Converts a source picture to a destination picture.
     * SwsContext must be initialized.
     *
     * @param src   Source frame.
     * @param srcY  Position in source image of slice to process, in rows.
     * @param srcH  Height of source slice to process, in rows.
     * @param dst   Pre-allocated and correctly formatted destination frame.
     * @return Height of output slice, or <0 if error.
     */
    public int conv( JavFrame src, int srcY, int srcH, JavFrame dst ) {
        return nConvert( mPointer, src.pointer(), srcY, srcH, dst.pointer() );
    }

    /**
     * Sets the color space.  By default, this SwsContext wrapper automatically configures
     * the color space.  But if you're super awesome, you can call this before initialization.
     * FFMPEG has almost no documentation on this, so good luck.
     * 
     * @param dstRange   flag indicating the while-black range of the output (1=jpeg / 0=mpeg)
     * @param srcRange   flag indicating the while-black range of the input (1=jpeg / 0=mpeg)
     * @param table4     the yuv2rgb coefficients describing the output yuv space, normally ff_yuv2rgb_coeffs[x]
     * @param invTable4  the yuv2rgb coefficients describing the input yuv space, normally ff_yuv2rgb_coeffs[x]
     * @param brightness 16.16 fixed point brightness correction
     * @param contrast   16.16 fixed point contrast correction
     * @param saturation 16.16 fixed point saturation correction
     * @return -1 if not supported
     */
    public int setColorSpaceDetails( int srcRange, 
                                     int dstRange,
                                     int[] table4,
                                     int[] invTable4,
                                     int brightness, 
                                     int contrast, 
                                     int saturation ) 
    {
        mColorspaceConfigured = false;
        int err = nSetColorSpaceDetails( mPointer, invTable4, srcRange, table4, dstRange, brightness, contrast, saturation );
        if( err < 0 ) {
            return err;
        }
        mColorspaceConfigured = true;
        return 0;
    }

    
    public void release() {
        long p = mPointer;
        mPointer = 0;
        
        if( p != 0 ) {
            nFree( p );
        }
    }
    
    
    public long pointer() {
        return mPointer;
    }

    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }
    
    
    
    @Override
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
    

    private static int csRangeValueFor( int format ) {
        switch( format ) {
        case AV_PIX_FMT_YUV420P:
        case AV_PIX_FMT_YUVJ422P:
        case AV_PIX_FMT_YUVJ444P:
        case AV_PIX_FMT_YUVJ440P:
            return 1;
        }
        
        return 0;
    }


    private static native long nAlloc();
    private static native int  nInit( long pointer );
    private static native int  nConvert( long pointer, long srcPointer, int sourceY, int sourceH, long dstPointer );
    private static native void nFree( long pointer );
    private static native int  nSetColorSpaceDetails( long pointer,
                                                      int[] invTable,
                                                      int srcRange,
                                                      int[] table,
                                                      int dstRange,
                                                      int brightness,
                                                      int contrast,
                                                      int saturation );



    /**
     * Initializes SwsContext to prepare it for frame conversions.
     * After initialization, SwsContext MUST NOT be reconfigured.
     * @deprecated
     */
    public void initialize() throws JavException {
        assertOkay( init() );
    }


    /**
     * Configures SwsContext.  SHOULD be called, or else initialization may
     * fail.  Alternatively, you may use JavOption directly.
     *
     * @param srcWidth   Width of source image in pixels.
     * @param srcHeight  Height of source image in pixels.
     * @param srcFormat  Pixel format of source image, defined in JavConstants.PIX_FMT_?
     * @param dstWidth   Width of destination image in pixels.
     * @param dstHeight  Height of destination image in pixels.
     * @param dstFormat  Pixel format of dest image, defined in JavConstants.PIX_FMT_?
     * @param flags  Flags for conversion.  Must specify ONE scale algorithm.  See class documentation.
     *
     * @deprecated Use config
     */
    public void configure( int srcWidth,
                           int srcHeight,
                           int srcFormat,
                           int dstWidth,
                           int dstHeight,
                           int dstFormat,
                           int flags )
                           throws JavException
    {
        assertOkay( config( srcWidth, srcHeight, srcFormat, dstWidth, dstHeight, dstFormat, flags ) );
    }

    /**
     * Converts a source picture to a destination picture.
     * SwsContext must be initialized.
     *
     * @param sourceY position of slice to convert in source frame
     * @param sourceH of slice to convert
     * @throws JavException
     *
     * @deprecated Use {@link #conv}
     */
    public void convert( JavFrame source, int sourceY, int sourceH, JavFrame dest ) throws JavException {
        int err = nConvert( mPointer, source.pointer(), sourceY, sourceH, dest.pointer() );
        if( err < 0 ) {
            throw new JavException( err, "Failed to convert picture: " + err );
        }
    }

}
