 /*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import bits.jav.codec.JavCodecContext;
import bits.jav.util.*;

/**
 * Wrapper for AVStream structure.
 */
public final class JavStream implements NativeObject {
    
    
    private long mPointer;
    private JavCodecContext mCodec;
    
    
    JavStream( long pointer ) {
        mPointer = pointer;
        
        long p = nCodecPointer( mPointer );
        if( p == 0 ) {
            mCodec = null;
        } else {
            mCodec = JavCodecContext.wrap( p );
            mCodec.setReleaseMethod( ReleaseMethod.OWNER );
        }
    }


    /**
     * @return stream index in AVFormatContext
     */
    public int index() {
        return nIndex(mPointer);
    }

    /**
     * Format-specific stream ID.
     * decoding: set by libavformat
     * encoding: set by the user, replaced by libavformat if left unset
     */
    public int id() {
        return nId(mPointer);
    }

    /**
     * Codec context associated with this stream. Allocated and freed by
     * libavformat.
     *
     * - decoding: The demuxer exports codec information stored in the headers
     *             here.
     * - encoding: The user sets codec information, the muxer writes it to the
     *             output. Mandatory fields as specified in AVCodecContext
     *             documentation must be set even if this AVCodecContext is
     *             not actually used for encoding.
     */
    public JavCodecContext codecContext() {
        return mCodec;
    }

    /**
     * This is the fundamental unit of time (in seconds) in terms
     * of which frame timestamps are represented. For fixed-fps content,
     * time base should be 1/framerate and timestamp increments should be 1.
     * 
     * @return time base of stream
     */
    public Rational timeBase() {
        return Rational.fromNativeLong( nTimeBase( mPointer ) );
    }

    /**
     * Decoding: pts of the first frame of the stream in presentation order, in stream time base.
     * Only set this if you are absolutely 100% sure that the value you set
     * it to really is the pts of the first frame.
     * This may be undefined (AV_NOPTS_VALUE).
     * @note The ASF header does NOT contain a correct start_time the ASF
     * demuxer must NOT set this.
     */
    public long startTime() {
        return nStartTime(mPointer);
    }

    /**
     * Decoding: duration of the stream, in stream time base.
     * If a source file does not specify a duration, but does specify
     * a bitrate, this value will be estimated from bitrate and file size.
     */
    public long duration() {
        return nDuration(mPointer);
    }

    /**
     * @return number of frames in this stream if known or 0
     */
    public long nbFrames() {
        return nNbFrames( mPointer );
    }

    /**
     * Selects which packets can be discarded at will and do not need to be demuxed.
     *
     * @return an enum value from Jav.AVDISCARD_*
     */
    public int discard() {
        return nDiscard( mPointer );
    }

    /**
     * Selects which packets can be discarded at will and do not need to be demuxed.
     *
     * @param discard  An enum value from Jav.AVDISCARD_*
     */
    public void discard( int discard ) {
        nDiscard( mPointer, discard );
    }


    public JavDict metadata() {
        long p = nMetadata( mPointer );
        if( p == 0L ) {
            return null;
        }
        JavDict dict = new JavDict( p );
        dict.setReleaseMethod( ReleaseMethod.OWNER );
        return dict;
    }

    @Override
    public long pointer() {
        return mPointer;
    }

    @Override
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }
    

    private static native int  nIndex( long pointer );
    private static native int  nId( long pointer );
    private static native long nCodecPointer( long pointer );
    private static native long nFrameRate( long pointer );
    private static native long nTimeBase( long pointer );
    private static native long nStartTime( long pointer );
    private static native long nDuration( long pointer );
    private static native long nNbFrames( long pointer );
    private static native int  nDiscard( long pointer );
    private static native void nDiscard( long pointer, int discard );

    private static native long nMetadata( long pointer );



    @Deprecated public Rational frameRate() {
        return Rational.fromNativeLong( nFrameRate( mPointer ) );
    }


}
