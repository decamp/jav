/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import java.io.*;
import java.util.*;

import bits.jav.*;
import bits.jav.codec.*;
import bits.jav.util.*;



/**
 * @author decamp
 */
public final class JavFormatContext implements JavClass {
    
    /**
     * Open an input stream and read the header. The codecs are not opened.
     * The stream must be closed with avformat_close_input().
     *
     * @param file       File to read.
     * @return 0 on success, a negative AVERROR on failure.
     */
    public static JavFormatContext openInput( File file ) throws JavException {
        long pointer = nAlloc();
        if( pointer == 0L ) {
            throw new OutOfMemoryError();
        }
        
        int err = nOpenInput( pointer, file.getAbsolutePath() );
        if( err != 0 ) {
            // NB: the allocated AVFormatContext will be freed if there was a failure.
            throw JavException.fromErr( err );
        }
        
        return new JavFormatContext( pointer, TYPE_INPUT );
    }
    
    /**
     * Allocate an AVFormatContext for an output format.
     * avformat_free_context() can be used to free the context and
     * everything allocated by the framework within it. 
     * <p>
     * This method calls {@code openOutputFile( file )}, so there's no need
     * for a separate call unless you have closed the file with 
     * {@code closeOutputFile()}.
     *
     * @param file           The file to write to. 
     * @param optFormat      Format to use for allocating the context, if NULL
     *                       format_name and filename are used instead
     * @param optFormatName  The name of output format to use for allocating the
     *                       context, if NULL filename is used instead
     *                       
     * @return >= 0 in case of success, a negative AVERROR code in case of failure
     */
    public static JavFormatContext openOutput( File file, JavOutputFormat optFormat, String optFormatName ) throws JavException {
        String path = file == null ? null : file.getAbsolutePath();
        
        long[] pointer = {0};
        int err = nOpenOutput( pointer,
                               path,
                               optFormat == null ? 0L : optFormat.pointer(),
                               optFormatName );
        
        if( err != 0 ) {
            throw JavException.fromErr( err );
        }
        
        if( file != null && ( nOutFormatFlags( pointer[0] ) & Jav.AVFMT_NOFILE ) == 0 ) {
            err = nOpenOutputFile( pointer[0], path );
            if( err < 0 ) {
                nFreeContext( pointer[0] );
                throw JavException.fromErr( err );
            }
        }
        
        return new JavFormatContext( pointer[0], TYPE_OUTPUT );
    }
    
    
    private static final boolean TYPE_INPUT = false;
    private static final boolean TYPE_OUTPUT = true;
    
    private long mPointer;
    private final boolean mType;
    private final List<JavStream> mStreams = new ArrayList<JavStream>();
    private JavIOContext mAvio = null;

    
    private JavFormatContext( long pointer, boolean type ) {
        mPointer = pointer;
        mType    = type;
        
        if( type == TYPE_INPUT ) {
            int len = nStreamCount( mPointer );
            for( int i = 0; i < len; i++ ) {
                long p = nStreamPointer( mPointer, i );
                mStreams.add( new JavStream( p ) );
            }
        }
    }
    
    
    /**
     * Closes lots of things: open codec contexts on all streams, IO, 
     * and frees all contents. This is meant to be a more convenient
     * method of closing everything so that you don't have to remember
     * what weird combination of closeInput(), closeOutputFile(), 
     * freeContext(), etc is needed. 
     */
    public void close() {
        long ptr = mPointer;
        mPointer = 0;
        if( ptr == 0L ) {
            return;
        }
        
        for( JavStream s: mStreams ) {
            s.codecContext().close();
        }
        
        if( mType == TYPE_INPUT ) {
            nCloseInput( ptr );
        } else {
            nCloseOutputFile( ptr );
            nFreeContext( ptr );
        }
    }   
    
    
    public int streamCount() {
        return mStreams.size();
    }
    
    
    public JavStream stream( int idx ) {
        return mStreams.get( idx );
    }
    
    
    public JavStream streamOfType( int type, int idx ) {
        for( int i = 0; i < mStreams.size(); i++ ) {
            JavStream s = mStreams.get( i );
            if( s.codecContext().codecType() == type ) {
                if( idx-- <= 0 ) {
                    return s;
                }
            }
        }
        
        return null;
    }

    /**
     * Find the "best" stream in the file.
     * The best stream is determined according to various heuristics as the most
     * likely to be what the user expects.
     * If the decoder parameter is non-NULL, av_find_best_stream will find the
     * default decoder for the stream's codec; streams for which no decoder can
     * be found are ignored.
     *
     * @param type              stream type: video, audio, subtitles, etc.
     * @param wantedNum         user-requested stream number,
     *                          or -1 for automatic selection
     * @param relatedStream     try to find a stream related (eg. in the same
     *                          program) to this one, or -1 if none
     * @param flags             flags; none are currently defined
     * @return  the non-negative stream number in case of success,
     *          AVERROR_STREAM_NOT_FOUND if no stream with the requested type
     *          could be found,
     *          AVERROR_DECODER_NOT_FOUND if streams were found but no decoder
     * @note  If av_find_best_stream returns successfully and decoder_ret is not
     *        NULL, then *decoder_ret is guaranteed to be set to a valid AVCodec.
     */
    public JavStream bestStream( int type, int wantedNum, int relatedStream, int flags ) {
        int idx = nFindBestStream( mPointer, type, wantedNum, relatedStream, flags );
        return idx < 0 ? null : mStreams.get( idx );
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


    public JavIOContext io() {
        long n = nAvio( mPointer );
        if( n == 0L ) {
            mAvio = null;
            return null;
        }

        if( mAvio != null && mAvio.pointer() == n ) {
            return mAvio;
        }

        mAvio = new JavIOContext( n );
        return mAvio;
    }

    /**
     * Sets metadata for this format context. After this call,
     * {@code metadata} belongs to JavFormatContext and should
     * no longer be access by user in any way.
     */
    public void metadata( JavDict metadata ) {
        if( metadata == null ) {
            nMetadata( mPointer, 0L );
        } else {
            nMetadata( mPointer, metadata.pointer() );
            metadata.setReleaseMethod( ReleaseMethod.OWNER );
        }
    }

    /**
     * Decoding: duration of the stream, in AV_TIME_BASE fractional
     * seconds. Only set this value if you know none of the individual stream
     * durations and also do not set any of them. This is deduced from the
     * AVStream values if not set.
     */
    public long duration() {
        return nDuration( mPointer );
    }

    /**
     * Returns the method used to set ctx->duration.
     *
     * @return AVFMT_DURATION_FROM_PTS, AVFMT_DURATION_FROM_STREAM, or AVFMT_DURATION_FROM_BITRATE.
     */
    public int durationEstimationMethod() {
        return nDurationEstimationMethod( mPointer );
    }


    /***************
     * For Input
     **************/
    
    
    /**
     * @return 0 on success, &lt; 0 on error or EOF.
     */
    public int readPacket( JavPacket out ) {
        return nReadFrame( mPointer, out.pointer() );
    }
    
    /**
     * Seek to keyframe at timestamp.
     * 
     * @param stream     Index of stream for search.  If {@code stream} is -1,
     *                   a default stream is selected and timestamp is automatically
     *                   converted from AV_TIME_BASE units to the stream specific time base. 
     * @param timestamp  Timestamp in the timeBase units of the stream.  Or, if no stream is
     *                   specified, in AV_TIME_BASE units. (See JavConstants). 
     * @param flags      Flags for seek.  See AVSEEK_FLAG_* section in JavConstants.
     *                   They aren't very well documented, unfortunately.
     *                   However, AVSEEK_FLAG_BACKWARD appears to find the keyframe
     *                   with <= timestamp instead of >= timestamp, which is pretty
     *                   useful.
     *
     * @return >= 0 if successful, < 0 on failure.
     *         Not very descriptive, I know. You seem to get a -1
     *         for seeking past the end of the file, but no
     *         error for seeking before the beginning. If you seek
     *         past the end of the file, you seem to get AVERROR_EOF
     *         on trying to read additional packets.
     */
    public int seek( int stream, long timestamp, int flags ) {
        return nSeekFrame( mPointer, stream, timestamp, flags );
    }

    /*
     * TODO: Add this when implemented by FFMPEG
     *
     * Seek to timestamp ts.
     * Seeking will be done so that the point from which all active streams
     * can be presented successfully will be closest to ts and within min/max_ts.
     * Active streams are all streams that have AVStream.discard < AVDISCARD_ALL.
     *
     * If flags contain AVSEEK_FLAG_BYTE, then all timestamps are in bytes and
     * are the file position (this may not be supported by all demuxers).
     * If flags contain AVSEEK_FLAG_FRAME, then all timestamps are in frames
     * in the stream with stream_index (this may not be supported by all demuxers).
     * Otherwise all timestamps are in units of the stream selected by stream_index
     * or if stream_index is -1, in AV_TIME_BASE units.
     * If flags contain AVSEEK_FLAG_ANY, then non-keyframes are treated as
     * keyframes (this may not be supported by all demuxers).
     * If flags contain AVSEEK_FLAG_BACKWARD, it is ignored.
     *
     * @param streamIndex index of the stream which is used as time base reference
     * @param minTime     smallest acceptable timestamp
     * @param time        target timestamp
     * @param maxTime     largest acceptable timestamp
     * @param flags       flags
     * @return >=0 on success, error code otherwise
     *
     * @note This is part of the new seek API which is still under construction.
     *       Thus do not use this yet. It may change at any time, do not expect
     *       ABI compatibility yet!
     *
     */
//    public int seekFile( int streamIndex, long minTime, long time, long maxTime, int flags ) {
//        return nSeekFile( mPointer, streamIndex, minTime, time, maxTime, flags );
//    }


    public void closeInput() {
        long p = mPointer;
        mPointer = 0;
        if( p == 0 ) {
            return;
        }
        nCloseInput( p );
    }
    
    
    
    /***************
     * For Output
     **************/
    
    public JavOutputFormat outputFormat() {
        long p = nOutputFormat( mPointer );
        return p == 0L ? null : new JavOutputFormat( p );
    }
    
    
    public void outputFormat( JavOutputFormat f ) {
        nOutputFormat( mPointer, f == null ? 0L: f.pointer() );
    }
    
    /**
     * Add a new stream to a media file.
     * <p>
     * When demuxing, it is called by the demuxer in read_header(). If the
     * flag AVFMTCTX_NOHEADER is set in s.ctx_flags, then it may also
     * be called in read_packet().
     * <p>
     * When muxing, should be called by the user before avformat_write_header().
     * <p>
     * @param codec If non-NULL, the AVCodecContext corresponding to the new stream
     *              will be initialized to use this codec. This is needed for e.g. codec-specific
     *              defaults to be set, so codec should be provided if it is known.
     * @return newly created stream or NULL on error.
     */
    public JavStream newStream( JavCodec codec ) {
        long p = nNewStream( mPointer, codec.pointer() );
        if( p == 0L ) {
            return null;
        }
        JavStream ret = new JavStream( p );
        mStreams.add( ret );
        
        // Make sure the codec knows if the format uses a global header.
        if( ( nOutFormatFlags( mPointer ) & Jav.AVFMT_GLOBALHEADER ) != 0 ) {
            JavCodecContext cc = ret.codecContext();
            cc.flags( cc.flags() | Jav.CODEC_FLAG_GLOBAL_HEADER );
        }
        
        return ret;
    }
    
    /**
     * Allocate the stream private data and write the stream header to
     * an output media file.
     *
     * @return 0 on success, negative AVERROR on failure.
     *
     * See av_opt_find, av_dict_set, avio_open, av_oformat_next.
     */
    public int writeHeader() {
        return nWriteHeader( mPointer );
    }
    
    /**
     * Write a packet to an output media file.
     *
     * The packet shall contain one audio or video frame.
     * The packet must be correctly interleaved according to the container
     * specification, if not then av_interleaved_write_frame must be used.
     *
     * @param packet The packet, which contains the stream_index, buf/buf_size,
     *               dts/pts, ...
     *               This can be NULL (at any time, not just at the end), in
     *               order to immediately flush data buffered within the muxer,
     *               for muxers that nativeBuffer up data internally before writing it
     *               to the output.
     * @return < 0 on error, = 0 if OK, 1 if flushed and there is no more data to flush
     */
    public int writeFrame( JavPacket packet ) {
        return nWriteFrame( mPointer, packet == null ? 0L : packet.pointer() );
    }

    /**
     * Write a packet to an output media file ensuring correct interleaving.
     *
     * The packet must contain one audio or video frame.
     * If the packets are already correctly interleaved, the application should
     * call av_write_frame() instead as it is slightly faster. It is also important
     * to keep in mind that completely non-interleaved input will need huge amounts
     * of memory to interleave with this, so it is preferable to interleave at the
     * demuxer level.
     *
     * @param packet The packet containing the data to be written. Libavformat takes
     * ownership of the data and will free it when it sees fit using the packet's
     * This can be NULL (at any time, not just at the end), to flush the
     * interleaving queues.
     * @ref AVPacket.destruct "destruct" field. The caller must not access the data
     * after this function returns, as it may already be freed.
     * Packet's @ref AVPacket.stream_index "stream_index" field must be set to the
     * index of the corresponding stream in @ref AVFormatContext.streams
     * "s.streams".
     * It is very strongly recommended that timing information (@ref AVPacket.pts
     * "pts", @ref AVPacket.dts "dts" @ref AVPacket.duration "duration") is set to
     * correct values.
     *
     * @return 0 on success, a negative AVERROR on error.
     */
    public int writeInterleavedFrame( JavPacket packet ) {
        return nWriteInterleavedFrame( mPointer, packet == null ? 0L: packet.pointer() );
    }
    
    /**
     * Write the stream trailer to an output media file and free the
     * file private data.
     *
     * May only be called after a successful call to av_write_header.
     *
     * @return 0 if OK, AVERROR_xxx on error
     */
    public int writeTrailer() {
        return nWriteTrailer( mPointer );
    }
    
    /**
     * This method is called automatically when opening a 
     * JavFormatContext for output.
     * <p>
     */
    public int openOutputfile( File file ) {
        return nOpenOutputFile( mPointer, file.getAbsolutePath() );
    }

    /**
     * Try using {@code close()} instead.
     * <p>
     * This method closes open files. It should only
     * be called on JavFormatContexts that were opened
     * for output.
     */
    public void closeOutputFile() {
        nCloseOutputFile( mPointer );
    }
    
    /**
     * Try using {@code close()} instead.
     * <p>
     * This method should only be called on JavFormatContexts
     * that were opened for output. This method frees the
     * JavFormatContext memory, but does not close codecs
     * or files. 
     */
    public void freeContext() {
        long p = mPointer;
        mPointer = 0;
        if( p == 0 ) {
            return;
        }
        nFreeContext( p );
    }
    
    
    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.USER;
    }
    
    

    /**
     * Probably not what you think. I don't even know if filename is used 
     * for anything anymore.
     */
    public String filename() {
        return nFilename( mPointer );
    }
    
    /**
     * Probably not what you think. I don't even know if filename is used
     * for anything anymore.
     */
    public void filename( String filename ) {
        nFilename( mPointer, filename );
    }


    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    
    private static native long nAlloc();
    
    private static native int    nStreamCount( long pointer );
    private static native long   nStreamPointer( long pointer, int index );
    private static native String nFilename( long pointer );
    private static native void   nFilename( long pointer, String filename );
    private static native long   nMetadata( long pointer );
    private static native void   nMetadata( long pointer, long dictPointer );
    private static native long   nDuration( long pointer );
    private static native int    nDurationEstimationMethod( long pointer );
    private static native long   nAvio( long pointer );
    private static native int    nFindBestStream( long pointer, int type, int wantedNum, int relatedStream, int flags );

    private static native int  nOpenInput( long pointer, String path );
    private static native void nCloseInput( long pointer );
    private static native int  nReadFrame( long pointer, long packet );
    private static native int  nSeekFrame( long pointer, int stream, long timestamp, int flags );
    private static native int  nSeekFile( long pointer, int stream, long minTime, long time, long maxTime, int flags );
    
    private static native long nOutputFormat( long pointer );
    private static native void nOutputFormat( long pointer, long ofPointer );
    private static native long nNewStream( long pointer, long codecPointer );
    private static native int  nOpenOutput( long[] pointer, String path, long fmtPointer, String fmtName );
    private static native int  nOpenOutputFile( long pointer, String path );
    
    private static native int  nWriteHeader( long pointer );
    private static native int  nWriteFrame( long pointer, long packetPointer );
    private static native int  nWriteInterleavedFrame( long pointer, long packetPointer );
    private static native int  nWriteTrailer( long pointer );
    private static native int  nOutFormatFlags( long pointer );
    //private static native void nFlushOutput( long pointer );
    private static native void nCloseOutputFile( long pointer );
    private static native void nFreeContext( long pointer );

}
