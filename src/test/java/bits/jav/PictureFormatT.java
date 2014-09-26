/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import static bits.jav.Jav.*;
import bits.jav.codec.JavCodecContext;
import bits.jav.util.Rational;


/**
 * @author decamp
 */
public class PictureFormatT {


    public static PictureFormatT fromCodecContext( JavCodecContext cc ) {
        return new PictureFormatT( cc.width(), cc.height(), cc.pixelFormat(), cc.sampleAspectRatio() );
    }



    /**
     * Merges two PictureFormat objects by replacing the undefined values in
     * {@code dest} with values from {@code source}. For example, if
     * {@code source} is a fully defined PictureFormat and {@code dest}
     * defines only the {@code pixelFormat} value, the result from merging them
     * will be a PictureFormat with the size of {@code source} and the
     * {@code pixelFormat} of {@code code}.
     * 
     * @param source
     *            Source PictureFormat. May be {@code null}.
     * @param dest
     *            Destination PictureFormat that overwrites source values. May
     *            be {@code null}.
     * @return PictureFormat containing merged values taken from input.
     */
    public static PictureFormatT merge( PictureFormatT source, PictureFormatT dest ) {
        if( source == null )
            return dest == null ? new PictureFormatT() : dest;

        if( dest == null )
            return source;

        return new PictureFormatT( dest.mWidth >= 0 ? dest.mWidth : source.mWidth,
                                   dest.mHeight >= 0 ? dest.mHeight : source.mHeight,
                                   dest.mPixelFormat != AV_PIX_FMT_NONE ? dest.mPixelFormat : source.mPixelFormat,
                                   dest.mSampleAspect != null ? dest.mSampleAspect : source.mSampleAspect );
    }


    private final int mWidth;
    private final int mHeight;
    private final int mPixelFormat;
    private final Rational mSampleAspect;

    /**
     * @param width
     *            Width of picture, or -1 if undefined.
     * @param height
     *            Height of picture, or -1 if undefined.
     * @param pixelFormat
     *            PixelFormat of picture, or -1 (PIX_FMT_NONE) if undefined.
     */
    public PictureFormatT( int width, int height, int pixelFormat ) {
        this( width, height, pixelFormat, null );
    }

    /**
     * @param width
     *            Width of picture, or -1 if undefined.
     * @param height
     *            Height of picture, or -1 if undefined.
     * @param pixelFormat
     *            PixelFormat of picture, or -1 (PIX_FMT_NONE) if undefined.
     * @param sampleAspect
     *            Aspect ratio of samples (pixels).
     */
    public PictureFormatT( int width, int height, int pixelFormat, Rational sampleAspect ) {
        mWidth = width >= 0 ? width : -1;
        mHeight = height >= 0 ? height : -1;
        mPixelFormat = pixelFormat;
        mSampleAspect = sampleAspect == null ? null : sampleAspect.reduce();
    }


    private PictureFormatT() {
        this( -1, -1, AV_PIX_FMT_NONE, null );
    }



    /**
     * @return picture width in pixels, or -1 if undefined.
     */
    public int width() {
        return mWidth;
    }

    /**
     * @return picture height in pixels, or -1 if undefined.
     */
    public int height() {
        return mHeight;
    }

    /**
     * @return true iff width() and height() are both defined and thus
     *         non-negative.
     */
    public boolean hasSize() {
        return mWidth >= 0 && mHeight >= 0;
    }

    /**
     * @return PIX_FMT value, or -1 (PIX_FMT_NONE) if undefined.
     */
    public int pixelFormat() {
        return mPixelFormat;
    }

    /**
     * @return aspect ratio of samples (pixels), or null if undefined.
     */
    public Rational sampleAspect() {
        return mSampleAspect;
    }



    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof PictureFormatT) )
            return false;

        PictureFormatT p = (PictureFormatT)obj;

        if( mWidth != p.mWidth ||
            mHeight != p.mHeight ||
            mPixelFormat != p.mPixelFormat )
        {
            return false;
        }

        return (mSampleAspect == p.mSampleAspect || mSampleAspect != null && mSampleAspect.equals( p.mSampleAspect ));
    }


    @Override
    public int hashCode() {
        return mWidth ^ (mHeight << 16) ^ (mHeight >>> 16) ^ mPixelFormat;
    }


    public String toString() {
        StringBuilder s = new StringBuilder( "PictureFormat [" );
        s.append( mWidth );
        s.append( " X " );
        s.append( mHeight );
        s.append( ", fmt: " );
        s.append( mPixelFormat );

        if( mSampleAspect != null ) {
            s.append( ", aspect: " );
            s.append( mSampleAspect.num() );
            s.append( "/" ).append( mSampleAspect.den() );
        }

        s.append( "]" );
        return s.toString();
    }

}
