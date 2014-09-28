/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.codec;

import bits.jav.util.*;


/**
 * Wrapper for AVCodec object.
 */
public class JavCodec implements NativeObject {

    
    public static JavCodec findEncoder( int id ) {
        long pointer = nFindEncoder( id );
        return pointer == 0 ? null : new JavCodec( pointer );
    }
    
    
    public static JavCodec findDecoder( int id ) {
        long pointer = nFindDecoder( id );
        return pointer == 0 ? null : new JavCodec( pointer );
    }

    
    
    private long mPointer;
    
    
    JavCodec( long pointer ) {
        mPointer = pointer;
    }
    
    
    
    public int type() {
        return nType( mPointer );
    }
    
    
    public int id() {
        return nId( mPointer );
    }
    
    
    /**
     * Codec capabilities.
     * see CODEC_CAP_*
     */
    public int capabilities() {
        return nCapabilities( mPointer );
    }

    /**
     * @return number of supported framerates supported by this codec, or 0 if any framerate.
     */
    public int supportedFrameratesCount() {
        return nSupportedFramerateCount( mPointer ); 
    }
    
    /**
     * @param idx Framerate option index. {@code 0 <= idx < supportedFrameratesCount()}
     * @return frame rate supported by this code.
     */
    public Rational supportedFramerate( int idx ) {
        return Rational.fromNativeLong( nSupportedFramerate( mPointer, idx ) );
    }
    
    /**
     * @return number of pixel formats supported by this codec
     */
    public int pixelFormatCount() {
        return nPixelFormatCount( mPointer );
    }
    
    /**
     * @param idx Pixel format option index: {@code 0 <= idx < pixelFormatCount()}
     * @return sample rate supported by this codec.
     */
    public int pixelFormat( int idx ) {
        return nPixelFormat( mPointer, idx );
    }
    
    /**
     * @return number of sample rates supported by this codec
     */
    public int sampleRateCount() {
        return nSampleRateCount( mPointer );
    }
    
    /**
     * @param idx Sample rate option index: {@code 0 <= idx < sampleRateCount()}
     * @return sample rate supported by this codec.
     */
    public int sampleRate( int idx ) {
        return nSampleRate( mPointer, idx );
    }
    
    /**
     * @return number of sample formats supported by this codec
     */
    public int sampleFormatCount() {
        return nSampleFormatCount( mPointer );
    }

    /**
     * @param idx Sample format option index: {@code 0 <= idx < sampleFormatCount()}
     * @return sample format supported by this codec, defined in JavConstants.AV_SAMPLE_FMT_*.
     */
    public int sampleFormat( int idx ) {
        return nSampleFormat( mPointer, idx );
    }

    /**
     * @return number of channel layouts supported by this codec.
     */
    public int channelLayoutCount() {
        return nChannelLayoutCount( mPointer );
    }
    
    /**
     * @param idx Channel layout option index: {@code 0 <= idx < channelLayoutCount()}
     * @return channel layout supported by this codec.
     */
    public long channelLayout( int idx ) {
        return nChannelLayout( mPointer, idx );
    }
    
    /**
     * JavClass for private context.
     */
    public JavClass privClass() {
        return new OpaqueJavClass( nPrivClass( mPointer ) );
    }
    
    
    
    public long pointer() {
        return mPointer;
    }

    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }
    
    
    
    private static native long nFindEncoder( int id );
    private static native long nFindDecoder( int id );
    
    private static native int  nType( long pointer );
    private static native int  nId( long pointer );
    private static native int  nCapabilities( long pointer );
    private static native int  nSupportedFramerateCount( long pointer );
    private static native long nSupportedFramerate( long pointer, int idx );
    private static native int  nPixelFormatCount( long pointer );
    private static native int  nPixelFormat( long pointer, int idx );
    private static native int  nSampleRateCount( long pointer );
    private static native int  nSampleRate( long pointer, int idx );
    private static native int  nSampleFormatCount( long pointer );
    private static native int  nSampleFormat( long pointer, int idx );    
    private static native int  nChannelLayoutCount( long pointer );
    private static native long nChannelLayout( long pointer, int idx );
    
    private static native long nPrivClass( long pointer );
    
}
