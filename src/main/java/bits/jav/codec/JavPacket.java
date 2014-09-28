/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.codec;

import bits.jav.util.*;
import bits.util.ref.*;

/**
 * This structure stores compressed data. It is typically exported by demuxers
 * and then passed as input to decoders, or received as output from encoders and
 * then passed to muxers.
 *
 * <p>For video, it should typically contain one compressed frame. For audio it may
 * contain several compressed frames.
 *
 * <p>AVPacket is one of the few structs in FFmpeg, whose size is a part of public
 * ABI. Thus it may be allocated on stack and no new fields can be added to it
 * without libavcodec and libavformat major bump.
 *
 * <p>The semantics of data ownership depends on the buf or destruct (deprecated)
 * fields. If either is set, the packet data is dynamically allocated and is
 * valid indefinitely until av_free_packet() is called (which in turn calls
 * av_buffer_unref()/the destruct callback to free the data). If neither is set,
 * the packet data is typically backed by some static buffer somewhere and is
 * only valid for a limited time (e.g. until the next read call when demuxing).
 *
 * <p>The side data is always allocated with av_malloc() and is freed in
 * av_free_packet().
 */

public final class JavPacket extends AbstractRefable implements NativeObject {

    
    public static JavPacket alloc() {
        return alloc( null );
    }
    
    
    public static JavPacket alloc( ObjectPool<? super JavPacket> optPool ) {
        long p = nAlloc();
        if( p == 0 ) {
            throw new OutOfMemoryError();
        }
        return new JavPacket( p, optPool );     
    }
    
    
    
    private long mPointer;
        
    
    JavPacket( long pointer, ObjectPool<? super JavPacket> pool ) {
        super( pool );
        mPointer = pointer;
    }
    
    
    
    public void allocData( int size ) {
        nAllocData( mPointer, size );
    }
    
    /**
     * Sets default values, without touching data pointer or data pointer siez.
     */
    public void init() {
        nInit( mPointer );
    }

    
    public void freeData() {
        nFreeData(mPointer);
    }
    
    
    public long dataPointer() {
        return nData( mPointer );
    }

    
    public void dataPointer( long ptr ) {
        nData( mPointer, ptr );
    }

    /**
     * Shifts the data pointer by adding n to the poniter
     * and subtracting n from size.
     */
    public void moveDataPointer( int n ) {
        nMoveData( mPointer, n );
    }
    
    
    public int size() {
        return nSize( mPointer );
    }
    
    
    public void size( int size ) {
        nSize( mPointer, size );
    }

    /**
     * Presentation timestamp in AVStream->time_base units; the time at which
     * the decompressed packet will be presented to the user.
     * Can be AV_NOPTS_VALUE if it is not stored in the file.
     * pts MUST be larger or equal to dts as presentation cannot happen before
     * decompression, unless one wants to view hex dumps. Some formats misuse
     * the terms dts and pts/cts to mean something different. Such timestamps
     * must be converted to true pts/dts before they are stored in AVPacket.
     */
    public long pts() {
        return nPts( mPointer );
    }
    
    
    public void pts( long pts ) {
        nPts( mPointer, pts );
    }
    
    /**
     * Decompression timestamp in AVStream->time_base units; the time at which
     * the packet is decompressed.
     * Can be AV_NOPTS_VALUE if it is not stored in the file.
     */
    public long dts() {
        return nDts( mPointer );
    }
    
    
    public void dts( long dts ) {
        nDts( mPointer, dts );
    }
    
    /**
     * Duration of this packet in AVStream->time_base units, 0 if unknown.
     * Equals next_pts - this_pts in presentation order.
     */
    public int duration() {
        return nDuration( mPointer );
    }

    
    public void duration( int dur ) {
        nDuration( mPointer, dur );
    }
    
    
    public int streamIndex() {
        return nStreamIndex( mPointer );
    }
    
    
    public void streamIndex( int idx ) {
        nStreamIndex( mPointer, idx );
    }

    /**
     * A combination of AV_PKT_FLAG values
     */
    public int flags() {
        return nFlags(mPointer);
    }
    
    
    public void flags( int flags ) {
        nFlags( mPointer, flags );
    }

    /**
     * @return byte position in stream, -1 if unknown
     */
    public long pos() {
        return nPos( mPointer );
    }


    public void pos( long pos ) {
        nPos( mPointer, pos );
    }

    /**
     * Time difference in AVStream->time_base units from the pts of this
     * packet to the point at which the output from the decoder has converged
     * independent from the availability of previous frames. That is, the
     * frames are virtually identical no matter if decoding started from
     * the very first frame or from this keyframe.
     * Is AV_NOPTS_VALUE if unknown.
     * This field is not the display duration of the current packet.
     * This field has no meaning if the packet does not have AV_PKT_FLAG_KEY
     * set.
     *
     * The purpose of this field is to allow seeking in streams that have no
     * keyframes in the conventional sense. It corresponds to the
     * recovery point SEI in H.264 and match_time_delta in NUT. It is also
     * essential for some types of subtitle streams to ensure that all
     * subtitles are correctly displayed after seeking.
     */
    public long convergenceDuration() {
        return nConvergenceDuration( mPointer );
    }


    public void convergenceDuration( long cd ) {
        nConvergenceDuration( mPointer, cd );
    }



    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }
    
        
    
    @Deprecated public long decodeTime() {
        return nDts( mPointer );
    }
    
    
    @Deprecated public long presentTime() {
        return nPts( mPointer );
    }
        
    
    @Override
    protected void finalize() throws Throwable {
        freeObject();
        super.finalize();
    }
    
    
    protected synchronized void freeObject() {
        long p = mPointer;
        mPointer = 0;
        if( p == 0 ) {
            return;
        }
        nFree( p );
    }
    
    
    
    private static native long nAlloc();
    private static native void nInit( long pointer );
    private static native void nFree( long pointer );
    private static native void nFreeData( long pointer );
    private static native int  nAllocData( long pointer, int size );
    
    private static native long nData( long pointer );
    private static native void nData( long pointer, long dataPointer );
    private static native void nMoveData( long pointer, int n );
    private static native int  nSize( long pointer );
    private static native void nSize( long pointer, int size );
    private static native long nPts( long pointer );
    private static native void nPts( long pointer, long pts );
    private static native long nDts( long pointer );
    private static native void nDts( long pointer, long dts );
    private static native int  nDuration( long pointer );
    private static native void nDuration( long pointer, int duration );
    private static native int  nStreamIndex( long pointer );
    private static native void nStreamIndex( long pointer, int idx );
    private static native int  nFlags( long pointer );
    private static native void nFlags( long pointer, int flags );
    private static native long nPos( long pointer  );
    private static native void nPos( long pointer, long pos );
    private static native long nConvergenceDuration( long pointer  );
    private static native void nConvergenceDuration( long pointer, long cd );
        
    /**
     * Unused for now.
     */
    private static native long nBuf( long pointer );
    private static native void nBuf( long pointer, long bufPointer );
    
}
