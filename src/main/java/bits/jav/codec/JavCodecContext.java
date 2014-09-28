/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.codec;

import bits.jav.*;
import bits.jav.util.*;


/**
 * main external API structure.
 *
 * Please use AVOptions (av_opt* / av_set/get*()) to access these fields from user
 * applications.
 *
 * sizeof(AVCodecContext) must not be used outside libav*.
 */
public final class JavCodecContext implements JavClass {

    // TODO: This should probably be hidden.
    public static final int FF_CODER_TYPE_VLC      = 0;
    public static final int FF_CODER_TYPE_AC       = 1;
    public static final int FF_CODER_TYPE_RAW      = 2;
    public static final int FF_CODER_TYPE_RLE      = 3;
    public static final int FF_CODER_TYPE_DEFLATE  = 4;


    /**
     * Allocate a JavCodecContext and set its fields to default values.  The
     * resulting struct can be deallocated by calling {@code close()} on it, optionally
     * followed by {@code release()}
     *
     * @return A JavCodecContext filled with default values, or NULL on failure.
     */
    public static JavCodecContext alloc() {
        return alloc( null );
    }
    
    /**
     * Allocate a JavCodecContext and set its fields to default values.  The
     * resulting struct can be deallocated by calling {@code close()} on it, optionally
     * followed by {@code release()}
     *
     * @param codec if non-NULL, allocate private data and initialize defaults
     *              for the given codec. It is illegal to then call open() with a 
     *              different codec. 
     *              If NULL, then the codec-specific defaults won't be initialized,
     *              which may result in suboptimal default settings (this is
     *              important mainly for encoders, e.g. libx264).
     *
     * @return An JavCodecContext filled with default values, or NULL on failure.
     */
    public static JavCodecContext alloc( JavCodec codec ) {
        long pointer = nAlloc( codec == null ? 0L : codec.pointer() );
        return new JavCodecContext( pointer );
    }
    
    /**
     * Wrap an existing native AVCodecContext with java JavCodecContext. 
     */
    public static JavCodecContext wrap( long pointer ) {
        return new JavCodecContext( pointer );
    }
    
    
    
    private long mPointer;
    private JavCodec mCodec = null;
    private ReleaseMethod mRelease = ReleaseMethod.SELF;
    
    
    JavCodecContext( long pointer ) {
        mPointer = pointer;
    }

    
    /**
     * Initialize the AVCodecContext to use the given AVCodec.
     * <p>
     * The functions {@code JavCodec.findDecoder()} and {@code JavCodec.findEncoder()}
     * provide a way for retrieving codecs.
     * <p>
     * Warning: This function is not thread safe!
     * <p>
     *
     * @param codec The codec to open this context for. If a non-NULL codec has been
     *              previously passed to JavCodecContext.alloc() for this context,
     *              then this parameter MUST be either NULL or equal to the previously 
     *              passed codec.
     *
     * @return zero on success, a negative value on error
     */
    public int open( JavCodec codec ) {
        if( mCodec != null ) {
            return Jav.AVERROR_CODEC_OPEN_FAILED;
        }
        
        // Tell codec to handle truncated bitstreams.
        if( ( codec.capabilities() & Jav.CODEC_CAP_TRUNCATED ) != 0 ) {
            flags( flags() | Jav.CODEC_FLAG_TRUNCATED );
        }
        
        int err = nOpen( mPointer, codec.pointer() );
        if( err == 0 ) {
            mCodec = codec;
        }
        
        return err;
    }
    
    /**
     * Close a given JavCodecContext and free all the data associated with it
     * (but not the JavCodecContext itself).
     * <p>
     * Calling this function on a JavCodecContext that hasn't been opened will free
     * the codec-specific data allocated in JavCodecContext.alloc() with a 
     * non-NULL codec. Subsequent calls will do nothing.
     */
    public int close() {
        if( mCodec == null ) {
            return 0;
        }
        mCodec = null;
        return nClose( mPointer );
    }
    
    /**
     * Frees memory allocated by this JavCodecContext. 
     * This method is called automatically when garbage collected.
     */
    public void release() {
        long p = mPointer;
        mPointer = 0;
        if( mRelease == ReleaseMethod.SELF ) {
            nFree( p );
        }
    }
    
    
    public boolean isOpen() {
        return mCodec != null;
    }
    
    /**
     * Decode the video frame of size {@code avpkt.size} from {@code avpkt.data} into picture.
     * Some decoders may support multiple frames in a single AVPacket, such
     * decoders would then just decode the first frame.
     * <p>
     * Warning: The input nativeBuffer must be {@code FF_INPUT_BUFFER_PADDING_SIZE} larger than
     * the actual read bytes because some optimized bitstream readers read 32 or 64
     * bits at once and could read over the end.
     * <p>
     * Warning: The end of the input nativeBuffer buf should be set to 0 to ensure that
     * no overreading happens for damaged MPEG streams.
     * <p>
     * Note: Codecs which have the CODEC_CAP_DELAY capability set have a delay
     * between input and output, these need to be fed with {@code avpkt.data = null}
     * {@code avpkt.size = 0} at the end to return the remaining frames.
     *
     * @param packet   [in] The input AVPacket containing the input nativeBuffer.
     *                 You can create such packet with av_init_packet() and by then setting
     *                 data and size, some decoders might in addition need other fields like
     *                 flags&AV_PKT_FLAG_KEY. All decoders are designed to use the least
     *                 fields possible.
     *
     * @param out      [out] The AVFrame in which the decoded video frame will be stored.
     *                 Use JavFrame.alloc() to get an AVFrame. The codec will
     *                 allocate memory for the actual bitmap by calling the
     *                 AVCodecContext.get_buffer2() callback.
     *                 When AVCodecContext.refcounted_frames is set to 1, the frame is
     *                 reference counted and the returned reference belongs to the
     *                 caller. The caller must release the frame using av_frame_unref()
     *                 when the frame is no longer needed. The caller may safely write
     *                 to the frame if av_frame_is_writable() returns 1.
     *                 When AVCodecContext.refcounted_frames is set to 0, the returned
     *                 reference belongs to the decoder and is valid only until the
     *                 next call to this function or until closing or flushing the
     *                 decoder. The caller may not write to it.
     *
     * @param gotFrame [in,out] if no frame could be decompressed, otherwise, it is nonzero.
     *
     * @return On error a negative value is returned, otherwise the number of bytes
     * used or zero if no frame could be decompressed.
     */
    public int decodeVideo( JavPacket packet, JavFrame out, int[] gotFrame ) {
        return nDecodeVideo( mPointer, packet.pointer(), out.pointer(), gotFrame );
    }
    
    /**
     * Encode a frame of video.
     * <p>
     * Takes input raw video data from frame and writes the next output packet, if
     * available, to avpkt. The output packet does not necessarily contain data for
     * the most recent frame, as encoders can delay and reorder input frames
     * internally as needed.
     * 
     * @param frame     [in] JavFrame containing the raw video data to be encoded.
     *                  May be NULL when flushing an encoder that has the
     *                  CODEC_CAP_DELAY capability set.
     *                  
     * @param out       [out] output JavPacket.
     *                  The user can supply an output nativeBuffer by setting
     *                  avpkt->data and avpkt->size prior to calling the
     *                  function, but if the size of the user-provided data is not
     *                  large enough, encoding will fail. All other AVPacket fields
     *                  will be reset by the encoder using av_init_packet(). If
     *                  avpkt->data is NULL, the encoder will allocate it.
     *                  The encoder will set avpkt->size to the size of the
     *                  output packet. The returned data (if any) belongs to the
     *                  caller, he is responsible for freeing it.
     *                  <p>
     *                  If this function fails or produces no output, avpkt will be
     *                  freed using av_free_packet() (i.e. avpkt->destruct will be
     *                  called to free the user supplied nativeBuffer).
     * 
     * @param gotPacket [out] got_packet_ptr This field is set to 1 by libavcodec if 
     *                  the output packet is non-empty, and to 0 if it is empty. 
     *                  If the function returns an error, the packet can be assumed 
     *                  to be invalid, and the value of gotPacket[0] is undefined 
     *                  and should not be used.
     * 
     * @return 0 on success, negative code on error.
     */
    public int encodeVideo( JavFrame frame, JavPacket out, int[] gotPacket ) {
        return nEncodeVideo( mPointer, 
                             frame == null ? 0L : frame.pointer(), 
                             out.pointer(), 
                             gotPacket );
    }
    
    /**
     * Decode the audio frame of size packet.size() from packet.dataPointer() into frame.
     * <p>
     * Some decoders may support multiple frames in a single AVPacket. Such
     * decoders would then just decode the first frame. In this case,
     * avcodec_decode_audio4 has to be called again with an AVPacket containing
     * the remaining data in order to decode the second frame, etc...
     * Even if no frames are returned, the packet needs to be fed to the decoder
     * with remaining data until it is completely consumed or an error occurs.
     * <p>
     * WARNING - The input nativeBuffer, avpkt->data must be FF_INPUT_BUFFER_PADDING_SIZE
     * larger than the actual read bytes because some optimized bitstream
     * readers read 32 or 64 bits at once and could read over the end.
     * <p>
     * NOTE - You might have to align the input nativeBuffer. The alignment requirements
     * depend on the CPU and the decoder.
     * 
     * @param packet     [in] The input JavPacket containing the input nativeBuffer.
     *                   At least packet->data and packet->size should be set. Some
     *                   decoders might also require additional fields to be set.
     * 
     * @param out        [out] The JavFrame in which to store decoded audio samples.
     *                   Decoders request a nativeBuffer of a particular size by setting
     *                   AVFrame.nb_samples prior to calling get_buffer(). The
     *                   decoder may, however, only utilize part of the nativeBuffer by
     *                   setting AVFrame.nb_samples to a smaller value in the
     *                   output frame.
     * 
     * @param gotFrame   [out] gotFrame[0] set to zero if no frame decoded, otherwise
     *                   non-zero.
     * 
     * @return A negative error code is returned if an error occurred during
     *         decoding, otherwise the number of bytes consumed from the input
     *         AVPacket is returned.
     */
    public int decodeAudio( JavPacket packet, JavFrame out, int[] gotFrame ) {
        return nDecodeAudio( mPointer, packet.pointer(), out.pointer(), gotFrame );
    }

    /**
     * Encode a frame of audio.
     * <p>
     * Takes input samples from frame and writes the next output packet, if
     * available, to avpkt. The output packet does not necessarily contain data for
     * the most recent frame, as encoders can delay, split, and combine input frames
     * internally as needed.
     * 
     * @param frame     [in] JavFrame containing the raw audio data to be encoded.
     *                  May be NULL when flushing an encoder that has the
     *                  CODEC_CAP_DELAY capability set. If CODEC_CAP_VARIABLE_FRAME_SIZE 
     *                  is set, then each frame can have any number of samples.
     *                  If it is not set, frame.nbSamples() must be equal to
     *                  this.frameSize() for all frames except the last. The
     *                  final frame may be smaller than this.frameSize().
     *                  
     * @param out       [out] The user can supply an output nativeBuffer by setting
     *                  out.dataPointer() and out.size() prior to calling the
     *                  function, but if the size of the user-provided data is not
     *                  large enough, encoding will fail. If out.dataPointer() and
     *                  out.size() are set, out.destruct() must also be set. All
     *                  other AVPacket fields will be reset by the encoder using
     *                  av_init_packet(). If packet.dataPointer() is NULL, the encoder will
     *                  allocate it. The encoder will set packet.size() to the size
     *                  of the output packet.
     *                  <p>
     *                  If this function fails or produces no output, out will be
     *                  freed using av_free_packet() (i.e. avpkt->destruct will be
     *                  called to free the user supplied nativeBuffer).
     *              
     * @param gotPacket [out] gotPacket[0] is set to 1 by libavcodec if the output
     *                  packet is non-empty, and to 0 if it is empty. If the function
     *                  returns an error, the packet can be assumed to be invalid,
     *                  and the value of got_packet_ptr is undefined and should
     *                  not be used.
     * @return 0 on success, negative error code on failure
     */
    public int encodeAudio( JavFrame frame, JavPacket out, int[] gotPacket ) {
        return nEncodeAudio( mPointer, 
                            frame == null ? 0L : frame.pointer(),
                            out.pointer(), 
                            gotPacket );
    }
    
    /**
     * Flush buffers, should be called when seeking or when switching to a different stream.
     */
    public void flushBuffers() {
        nFlush( mPointer );
    }
    
    
    public JavClass privData() {
        return new OpaqueJavClass( nPrivDataPointer( mPointer ) );
    }


    public long extraData() {
        return nExtraData( mPointer );
    }


    public int extraDataSize() {
        return nExtraDataSize( mPointer );
    }


    public void extraData( long bufPtr, int size ) {
        nExtraData( mPointer, bufPtr, size );
    }




    public int codecType() {
        return nCodecType( mPointer );
    }
    
    
    public void codecType( int type ) {
        nCodecType( mPointer, type );
    }
    
    
    public int codecId() {
        return nCodecId( mPointer );
    }


    public void codecId( int id ) {
        nCodecId( mPointer, id );
    }
    
    
    public String codecName() {
        return nCodecName( mPointer );
    }
    
    /**
     * the average bitrate
     * - encoding: Set by user; unused for constant quantizer encoding.
     * - decoding: Set by libavcodec. 0 or some bitrate if this info is available in the stream.
     */
    public int bitRate() {
        return nBitRate( mPointer );
    }
    

    public void bitRate( int bitRate ) {
        nBitRate( mPointer, bitRate );
    }
    
    /**
     * Number of samples per channel in an audio frame.
     *
     * - encoding: set by libavcodec in avcodec_open2(). Each submitted frame
     *   except the last must contain exactly frame_size samples per channel.
     *   May be 0 when the codec has CODEC_CAP_VARIABLE_FRAME_SIZE set, then the
     *   frame size is not restricted.
     * - decoding: may be set by some decoders to indicate constant frame size
     */
    public int frameSize() {
        return nFrameSize( mPointer );
    }
    
    
    public void frameSize( int frameSize ) {
        nFrameSize( mPointer, frameSize );
    }
    
    
    public Rational timeBase() {
        return Rational.fromNativeLong( nTimeBase( mPointer ) );
    }
    
    
    public void timeBase( Rational timeBase ) {
        nTimeBase( mPointer, timeBase.num(), timeBase.den() );
    }

    
    
    
    
    /**
     * CODEC_FLAG_*.
     * - encoding: Set by user.
     * - decoding: Set by user.
     */
    public int flags() {
        return nFlags( mPointer );
    }

    
    public void flags( int flags ) {
        nFlags( mPointer, flags );
    }

    /**
     * CODEC_FLAG2_*
     * - encoding: Set by user.
     * - decoding: Set by user.
     */
    public int flags2() {
        return nFlags2( mPointer );
    }

    
    public void flags2( int flags2 ) {
        nFlags2( mPointer, flags2 );
    }
    
    
    
    
    /** Video Fields **/

    /**
     * picture width / height.
     * - encoding: MUST be set by user.
     * - decoding: Set by libavcodec.
     * Note: For compatibility it is possible to set this instead of
     * coded_width/height before decoding.
     */
    public int width() {
        return nWidth( mPointer );
    }

    
    public void width( int width ) {
        nWidth( mPointer, width );
    }

    /**
     * picture width / height.
     * - encoding: MUST be set by user.
     * - decoding: Set by libavcodec.
     * Note: For compatibility it is possible to set this instead of
     * coded_width/height before decoding.
     */
    public int height() {
        return nHeight( mPointer );
    }

    
    public void height( int height ) {
        nHeight( mPointer, height );
    }
    
    /**
     * Pixel format, see PIX_FMT_xxx.
     * May be set by the demuxer if known from headers.
     * May be overridden by the decoder if it knows better.
     * - encoding: Set by user.
     * - decoding: Set by user if known, overridden by libavcodec if known
     */
    public int pixelFormat() {
        return nPixFmt( mPointer );
    }
    
    
    public void pixelFormat( int pixelFormat ) {
        nPixFmt( mPointer, pixelFormat );
    }


    public Rational sampleAspectRatio() {
        return Rational.fromNativeLong( nSampleAspectRatio( mPointer ) );
    }
    
    
    public void sampleAspectRatio( Rational sampleAspectRatio ) {
        nSampleAspectRatio( mPointer, sampleAspectRatio.num(), sampleAspectRatio.den() );
    }
    
    

    /** Audio Fields **/

    public int channels() {
        return nChannels( mPointer );
    }
    
    
    public void channels( int channels ) {
        nChannels( mPointer, channels );
    }


    public long channelLayout() {
        return nChannelLayout( mPointer );
    }

    
    public void channelLayout( long channelLayout ) {
        nChannelLayout( mPointer, channelLayout );
    }
    
    
    public int sampleRate() {
        return nSampleRate( mPointer );
    }
    
    
    public void sampleRate( int sampleRate ) {
        nSampleRate( mPointer, sampleRate );
    }


    public int sampleFormat() {
        return nSampleFmt( mPointer );
    }
    
    
    public void sampleFormat( int sampleFormat ) {
        nSampleFmt( mPointer, sampleFormat );
    }
    
    
    
    
    /** Encoding **/
    
    
    /**
     * the number of pictures in a group of pictures, or 0 for intra_only
     * - encoding: Set by user
     * - decoding: unused
     */
    public int gopSize() {
        return nGopSize( mPointer );
    }
    
    
    public void gopSize( int gopSize ) {
        nGopSize( mPointer, gopSize );
    }
    
    /**
     * maximum number of B-frames between non-B-frames
     * Note: The output will be delayed by max_b_frames+1 relative to the input.
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int maxBFrames() {
        return nMaxBFrames( mPointer );
    }
    
    
    public void maxBFrames( int maxBFrames ) {
        nMaxBFrames( mPointer, maxBFrames );
    }

    
    /**
     * minimum GOP size
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int keyintMin() {
        return nKeyintMin( mPointer );
    }
    
    
    public void keyintMin( int keyintMin ) {
        nKeyintMin( keyintMin );
    }
    
    /**
     * profile
     * - encoding: Set by user.
     * - decoding: Set by libavcodec.
     */
    public int profile() {
        return nProfile( mPointer );
    }
    
    
    public void profile( int profile ) {
        nProfile( mPointer, profile );
    }
    
    /**
     * level
     * - encoding: Set by user.
     * - decoding: Set by libavcodec.
     */
    public int level() {
        return nLevel( mPointer );
    }
    
    
    public void level( int level ) {
        nLevel( mPointer, level );
    }
    
    /**
     * number of bits the bitstream is allowed to diverge from the reference.
     *           the reference can be CBR (for CBR pass1) or VBR (for pass2)
     * - encoding: Set by user; unused for constant quantizer encoding.
     * - decoding: unused
     */
    public int bitRateTolerance() {
        return nBitRateTolerance( mPointer );
    }


    public void bitRateTolerance( int bitRateTol ) {
        nBitRateTolerance( mPointer, bitRateTol );
    }
    
    /**
     * Global quality for codecs which cannot change it per frame.
     * This should be proportional to MPEG-1/2/4 qscale.
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int globalQuality() {
        return nGlobalQuality( mPointer );
    }
    
    
    public void globalQuality( int gq ) {
        nGlobalQuality( mPointer, gq );
    }
    
    
    public static final int FF_COMPRESSION_DEFAULT = -1;
    
    /**
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int compressionLevel() {
        return nCompressionLevel( mPointer );
    }
    
    
    public void compressionLevel( int compLevel ) {
        nCompressionLevel( compLevel );
    }
    
    /**
     * If non-zero, the decoded audio and video frames returned from
     * avcodec_decode_video2() and avcodec_decode_audio4() are reference-counted
     * and are valid indefinitely. The caller must free them with
     * av_frame_unref() when they are not needed anymore.
     * Otherwise, the decoded frames must not be freed by the caller and are
     * only valid until the next decode call.
     *
     * - encoding: unused
     * - decoding: set by the caller before avcodec_open2().
     */
    public int refcountedFrames() {
        return nRefcountedFrames( mPointer );
    }
    
    public void refcountedFrames( int n ) {
        nRefcountedFrames( mPointer, n );
    }

    
    /**
     * @return Amount of qscale change between easy & hard scenes (0.0-1.0 )
     */
    public float qcompress() {
        return nQcompress( mPointer );
    }
    
    
    public void qcompress( float qcompress ) {
        nQcompress( mPointer, qcompress);
    }
    
    /**
     * @return amount of qscale smoothing over time (0.0-1.0)
     */
    public float qblur() {
        return nQblur( mPointer );
    }
    
    
    public void qblur( float qblur ) {
        nQblur( mPointer, qblur );
    }

    /**
     * minimum quantizer
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int qmin() {
        return nQmin( mPointer );
    }


    public void qmin( int qmin ) {
        nQmin( mPointer, qmin );
    }
    
    /**
     * maximum quantizer
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int qmax() {
        return nQmax( mPointer );
    }
    

    public void qmax( int qmax ) {
        nQmax( mPointer, qmax );
    }

    /**
     * maximum quantizer difference between frames
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int maxQdiff() {
        return nMaxQdiff( mPointer );
    }


    public void maxQdiff( int max_qdiff ) {
        nMaxQdiff( mPointer, max_qdiff );
    }

    /**
     * scene change detection threshold
     * 0 is default, larger means fewer detected scene changes.
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int scenechangeThreshold() {
        return nScenechangeThreshold( mPointer );
    }
    
    
    public void scenechangeThreshold( int thresh ) {
        nScenechangeThreshold( mPointer, thresh );
    }
    
    /**
     * Motion estimation algorithm used for video coding.
     * 1 (zero), 2 (full), 3 (log), 4 (phods), 5 (epzs), 6 (x1), 7 (hex),
     * 8 (umh), 9 (iter), 10 (tesa) [7, 8, 10 are x264 specific, 9 is snow specific]
     * - encoding: MUST be set by user.
     * - decoding: unused
     */
    public int meMethod() {
        return nMeMethod( mPointer );
    }
    
    
    public void meMethod( int meMethod ) {
        nMeMethod( mPointer, meMethod );
    }
    
    /**
     * maximum motion estimation search range in subpel units
     * If 0 then no limit.
     *
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int meRange() {
        return nMeRange( mPointer );
    }
    
    
    public void meRange( int meRange ) {
        nMeRange( mPointer, meRange );
    }
    
    /**
     * motion estimation comparison function
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int meCmp() {
        return nMeCmp( mPointer );
    }
    
    
    public void meCmp( int meCmp ) {
        nMeCmp( mPointer, meCmp );
    }

    /**
     * subpixel motion estimation comparison function
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int meSubCmp() {
        return nMeSubCmp( mPointer );
    }
    
    
    public void meSubCmp( int meCmp ) {
        nMeSubCmp( mPointer, meCmp );
    }


    /**
     * coder type
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int coderType() {
        return nCoderType( mPointer );
    }
    

    public void coderType( int coder_type ) {
        nCoderType( mPointer, coder_type );
    }

    /**
     * context model
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int contextModel() {
        return nContextModel( mPointer );
    }


    public void contextModel( int context_model ) {
        nContextModel( mPointer, context_model );
    }

    /**
     * minimum Lagrange multipler
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int lmin() {
        return nLmin( mPointer );
    }


    public void lmin( int lmin ) {
        nLmin( mPointer, lmin );
    }
    
    /**
     * maximum Lagrange multipler
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int lmax() {
        return nLmax( mPointer );
    }


    public void lmax( int lmax ) {
        nLmax( mPointer, lmax );
    }
    
    /**
     * frame skip threshold
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int frameSkipThreshold() {
        return nFrameSkipThreshold( mPointer );
    }


    public void frameSkipThreshold( int frame_skip_threshold ) {
        nFrameSkipThreshold( mPointer, frame_skip_threshold );
    }

    /**
     * frame skip factor
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int frameSkipFactor() {
        return nFrameSkipFactor( mPointer );
    }


    public void frameSkipFactor( int frame_skip_factor ) {
        nFrameSkipFactor( mPointer, frame_skip_factor );
    }

    /**
     * frame skip exponent
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int frameSkipExp() {
        return nFrameSkipExp( mPointer );
    }


    public void frameSkipExp( int frame_skip_exp ) {
        nFrameSkipExp( mPointer, frame_skip_exp );
    }

    /**
     * frame skip comparison function
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int frameSkipCmp() {
        return nFrameSkipCmp( mPointer );
    }


    public void frameSkipCmp( int frame_skip_cmp ) {
        nFrameSkipCmp( mPointer, frame_skip_cmp );
    }
    
    /**
     * trellis RD quantization
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int trellis() {
        return nTrellis( mPointer );
    }

    
    public void trellis( int trellis ) {
        nTrellis( mPointer, trellis );
    }
    
    /**
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int minPredictionOrder() {
        return nMinPredictionOrder( mPointer );
    }


    public void minPredictionOrder( int min_prediction_order ) {
        nMinPredictionOrder( mPointer, min_prediction_order );
    }

    /**
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int maxPredictionOrder() {
        return nMaxPredictionOrder( mPointer );
    }


    public void maxPredictionOrder( int max_prediction_order ) {
        nMaxPredictionOrder( mPointer, max_prediction_order );
    }
    
    /**
     * GOP timecode frame start number
     * - encoding: Set by user, in non drop frame format
     * - decoding: Set by libavcodec (timecode in the 25 bits format, -1 if unset)
     */
    public long timecodeFrameStart() {
        return nTimecodeFrameStart( mPointer );
    }


    public void timecodeFrameStart( long timecode_frame_start ) {
        nTimecodeFrameStart( mPointer, timecode_frame_start );
    }
    
    /**
     * ratecontrol qmin qmax limiting method
     * 0-> clipping, 1-> use a nice continuous function to limit qscale wthin qmin/qmax.
     * - encoding: Set by user.
     * - decoding: unused
     */
    public float rcQsquish() {
        return nRcQsquish( mPointer );
    }


    public void rcQsquish( float rc_qsquish ) {
        nRcQSquint( mPointer, rc_qsquish );
    }

    
    public float rcQmodAmp() {
        return nRcQmodAmp( mPointer );
    }


    public void rcQmodAmp( float rc_qmod_amp ) {
        nRcQmodAmp( mPointer, rc_qmod_amp );
    }


    public int rcQmodFreq() {
        return nRcQmodFreq( mPointer );
    }


    public void rcQmodFreq( int rc_qmod_freq ) {
        nRcQmodFreq( mPointer, rc_qmod_freq );
    }
    
    /**
     * decoder bitstream nativeBuffer size
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int rcBufferSize() {
        return nRcBufferSize( mPointer );
    }


    public void rcBufferSize( int rc_buffer_size ) {
        nRcBufferSize( mPointer, rc_buffer_size );
    }

    /**
     * ratecontrol override, see RcOverride
     * - encoding: Allocated/set/freed by user.
     * - decoding: unused
     */
    public int rcOverrideCount() {
        return nRcOverrideCount( mPointer );
    }


    public void rcOverrideCount( int rc_override_count ) {
        nRcOverrideCount( mPointer, rc_override_count );
    }

    /**
     * rate control equation
     * - encoding: Set by user
     * - decoding: unused
     */
    private String rcEq() {
        return nRcEq( mPointer );
    }
    
    
    private void rcEq( String rceq ) {
        nRcEq( mPointer, rceq );
    }

    /**
     * maximum bitrate
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int rcMaxRate() {
        return nRcMaxRate( mPointer );
    }


    public void rcMaxRate( int rc_max_rate ) {
        nRcMaxRate( mPointer, rc_max_rate );
    }

    /**
     * minimum bitrate
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int rcMinRate() {
        return nRcMinRate( mPointer );
    }


    public void rcMinRate( int rc_min_rate ) {
        nRcMinRate( mPointer, rc_min_rate );
    }

    
    public float rcBufferAggressivity() {
        return nRcBufferAggressivity( mPointer );
    }


    public void rcBufferAggressivity( float rc_buffer_aggressivity ) {
        nRcBufferAggressivity( mPointer, rc_buffer_aggressivity );
    }
    
    /**
     * initial complexity for pass1 ratecontrol
     * - encoding: Set by user.
     * - decoding: unused
     */
    public float rcInitialCplx() {
        return nRcInitialCplx( mPointer );
    }


    public void rcInitialCplx( float rc_initial_cplx ) {
        nRcInitialCplx( mPointer, rc_initial_cplx );
    }

    /**
     * Ratecontrol attempt to use, at maximum, <value> of what can be used without an underflow.
     * - encoding: Set by user.
     * - decoding: unused.
     */
    public float rcMaxAvailableVbvUse() {
        return nRcMaxAvailableVbvUse( mPointer );
    }


    public void rcMaxAvailableVbvUse( float rc_max_available_vbv_use ) {
        nRcMaxAvailableVbvUse( mPointer, rc_max_available_vbv_use );
    }
    
    /**
     * Ratecontrol attempt to use, at least, <value> times the amount needed to prevent a vbv overflow.
     * - encoding: Set by user.
     * - decoding: unused.
     */
    public float rcMinVbvOverflowUse() {
        return nRcMinVbvOverflowUse( mPointer );
    }


    public void rcMinVbvOverflowUse( float rc_min_vbv_overflow_use ) {
        nRcMinVbvOverflowUse( mPointer, rc_min_vbv_overflow_use );
    }

    /**
     * Number of bits which should be loaded into the rc nativeBuffer before decoding starts.
     * - encoding: Set by user.
     * - decoding: unused
     */
    public int rcInitialBufferOccupancy() {
        return nRcInitialBufferOccupancy( mPointer );
    }


    public void rcInitialBufferOccupancy( int rc_initial_buffer_occupancy ) {
        nRcInitialBufferOccupancy( mPointer, rc_initial_buffer_occupancy );
    }
    
    
    
    
    public long pointer() {
        return mPointer;
    }
 
    
    public ReleaseMethod releaseMethod() {
        return mRelease;
    }
    
    
    public void setReleaseMethod( ReleaseMethod method ) {
        mRelease = method;
    }
    
    
    protected void finalize() throws Throwable {
        release();
        super.finalize();
    }
    
    
    
    private static native long nAlloc( long codecPointer );
    private static native int  nOpen( long pointer, long codecPointer ) ;
    private static native int  nClose( long pointer );
    private static native void nFree( long pointer );
    
    private static native int  nDecodeVideo( long pointer, long packetPointer, long framePointer, int[] gotFrameOut );
    private static native int  nEncodeVideo( long pointer, long framePointer, long packetPointer, int[] gotFrameOut );
    private static native int  nDecodeAudio( long pointer, long packetPointer, long framePointer, int[] gotFrameOut );
    private static native int  nEncodeAudio( long pointer, long framePoniter, long packetPointer, int[] gotFrameOut );
    private static native void nFlush( long pointer );
    
    private static native long nTimeBase( long pointer );
    private static native void nTimeBase( long pointer, int num, int den );
    private static native long nSampleAspectRatio( long pointer );
    private static native void nSampleAspectRatio( long pointer, int num, int den );
    
    private static native long nPrivDataPointer( long pointer );
    private static native long nExtraData( long pointer );
    private static native int  nExtraDataSize( long pointer );
    private static native void nExtraData( long pointer, long bufPointer, int size );

    // Autogen start.
    private static native int  nCodecType( long pointer );
    private static native void nCodecType( long pointer, int type );
    private static native int  nCodecId( long pointer );
    private static native void nCodecId( long pointer, int type );
    private static native String nCodecName( long pointer );
    
    private static native int  nBitRate( long pointer );
    private static native void nBitRate( long pointer, int bitRate );
    private static native int  nFrameSize( long pointer );
    private static native void nFrameSize( long pointer, int frameSize );
    private static native int  nFlags( long pointer );
    private static native void nFlags( long pointer, int flags );
    private static native int  nFlags2( long pointer );
    private static native void nFlags2( long pointer, int flags2 );
    
    private static native int  nPixFmt( long pointer );
    private static native void nPixFmt( long pointer, int pixelFormat );
    private static native int  nSampleFmt( long pointer );
    private static native void nSampleFmt( long pointer, int sampleFormat );
    private static native int  nWidth( long pointer );
    private static native void nWidth( long pointer, int width );
    private static native int  nHeight( long pointer );
    private static native void nHeight( long pointer, int height );
    
    private static native int  nChannels( long pointer );
    private static native void nChannels( long pointer, int channels );
    private static native long nChannelLayout( long pointer );
    private static native void nChannelLayout( long pointer, long channelLayout );
    private static native int  nSampleRate( long pointer );
    private static native void nSampleRate( long pointer, int sampleRate );
    
    private static native int    nGopSize( long pointer );
    private static native void   nGopSize( long pointer, int gopSize );
    private static native int    nMaxBFrames( long pointer );
    private static native void   nMaxBFrames( long pointer, int maxBFrames );
    private static native int    nKeyintMin( long pointer );
    private static native void   nKeyintMin( long pointer, int keyintMin );
    private static native int    nProfile( long pointer );
    private static native void   nProfile( long pointer, int profile );
    private static native int    nLevel( long pointer );
    private static native void   nLevel( long pointer, int level );
    private static native int    nBitRateTolerance( long pointer );
    private static native void   nBitRateTolerance( long pointer, int bitRateTol );
    private static native int    nGlobalQuality( long pointer );
    private static native void   nGlobalQuality( long pointer, int globalQuality );
    private static native int    nCompressionLevel( long pointer );
    private static native void   nCompressionLevel( long pointer, int compLevel );
    private static native int    nRefcountedFrames( long pointer );
    private static native void   nRefcountedFrames( long pointer, int refcountedFrames );
    private static native float  nQcompress( long pointer );
    private static native void   nQcompress( long pointer, float qCompress );
    private static native float  nQblur( long pointer);
    private static native void   nQblur( long pointer, float qblur );
    private static native int    nQmin( long pointer);
    private static native void   nQmin( long pointer, int qmin );
    private static native int    nQmax( long pointer);
    private static native void   nQmax( long pointer, int qmax );
    private static native int    nMaxQdiff( long pointer);
    private static native void   nMaxQdiff( long pointer, int max_qdiff );
    private static native int    nScenechangeThreshold( long pointer );
    private static native void   nScenechangeThreshold( long pointer, int thresh );
    private static native int    nMeMethod( long pointer );
    private static native void   nMeMethod( long pointer, int meMethod );
    private static native int    nMeRange( long pointer );
    private static native void   nMeRange( long pointer, int meRange );
    private static native int    nMeCmp( long pointer );
    private static native void   nMeCmp( long pointer, int meCmp );
    private static native int    nMeSubCmp( long pointer );
    private static native void   nMeSubCmp( long pointer, int meSubCmp );
    
    private static native int    nCoderType( long pointer);
    private static native void   nCoderType( long pointer, int coder_type );
    private static native int    nContextModel( long pointer);
    private static native void   nContextModel( long pointer, int context_model );
    private static native int    nLmin( long pointer);
    private static native void   nLmin( long pointer, int lmin );
    private static native int    nLmax( long pointer);
    private static native void   nLmax( long pointer, int lmax );
    private static native int    nFrameSkipThreshold( long pointer);
    private static native void   nFrameSkipThreshold( long pointer, int frame_skip_threshold );
    private static native int    nFrameSkipFactor( long pointer);
    private static native void   nFrameSkipFactor( long pointer, int frame_skip_factor );
    private static native int    nFrameSkipExp( long pointer);
    private static native void   nFrameSkipExp( long pointer, int frame_skip_exp );
    private static native int    nFrameSkipCmp( long pointer);
    private static native void   nFrameSkipCmp( long pointer, int frame_skip_cmp );
    private static native int    nTrellis( long pointer);
    private static native void   nTrellis( long pointer, int trellis );
    private static native int    nMinPredictionOrder( long pointer);
    private static native void   nMinPredictionOrder( long pointer, int min_prediction_order );
    private static native int    nMaxPredictionOrder( long pointer);
    private static native void   nMaxPredictionOrder( long pointer, int max_prediction_order );
    private static native long   nTimecodeFrameStart( long pointer);
    private static native void   nTimecodeFrameStart( long pointer, long timecode_frame_start );
    
    private static native String nRcEq( long pointer );
    private static native void   nRcEq( long pointer, String eq );
    private static native float  nRcQsquish( long pointer);
    private static native void   nRcQSquint( long pointer, float rc_qsquish );
    private static native float  nRcQmodAmp( long pointer);
    private static native void   nRcQmodAmp( long pointer, float rc_qmod_amp );
    private static native int    nRcQmodFreq( long pointer);
    private static native void   nRcQmodFreq( long pointer, int rc_qmod_freq );
    private static native int    nRcBufferSize( long pointer);
    private static native void   nRcBufferSize( long pointer, int rc_buffer_size );
    private static native int    nRcOverrideCount( long pointer);
    private static native void   nRcOverrideCount( long pointer, int rc_override_count );    
    private static native int    nRcMaxRate( long pointer);
    private static native void   nRcMaxRate( long pointer, int rc_max_rate );
    private static native int    nRcMinRate( long pointer);
    private static native void   nRcMinRate( long pointer, int rc_min_rate );
    private static native float  nRcBufferAggressivity( long pointer);
    private static native void   nRcBufferAggressivity( long pointer, float rc_buffer_aggressivity );
    private static native float  nRcInitialCplx( long pointer);
    private static native void   nRcInitialCplx( long pointer, float rc_initial_cplx );
    private static native float  nRcMaxAvailableVbvUse( long pointer);
    private static native void   nRcMaxAvailableVbvUse( long pointer, float rc_max_available_vbv_use );
    private static native float  nRcMinVbvOverflowUse( long pointer);
    private static native void   nRcMinVbvOverflowUse( long pointer, float rc_min_vbv_overflow_use );
    private static native int    nRcInitialBufferOccupancy( long pointer);
    private static native void   nRcInitialBufferOccupancy( long pointer, int rc_initial_buffer_occupancy );

}
