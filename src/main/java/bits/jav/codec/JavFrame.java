/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.codec;

import java.nio.*;

import bits.jav.JavException;
import bits.jav.util.*;
import bits.util.ref.*;


/**
 * This structure describes decoded (raw) audio or video data.
 *
 * <p>AVFrame must be allocated using one of the alloc() methods.
 * AVFrames are reference counted. See {@link bits.util.ref.Refable}.
 *
 * <p>AVFrame is typically allocated once and then reused multiple times to hold
 * different data (e.g. a single AVFrame to hold frames received from a
 * decoder). In such a case, av_frame_unref() will free any references held by
 * the frame and reset it to its original clean state before it
 * is reused again.
 */
public class JavFrame extends AbstractRefable implements NativeObject {
    /*
     * TODO: Fix reference counting to match original doc:
     * <p>The data described by an AVFrame is usually reference counted through the
     * AVBuffer API. The underlying buffer references are stored in AVFrame.buf /
     * AVFrame.extended_buf. An AVFrame is considered to be reference counted if at
     * least one reference is set, i.e. if AVFrame.buf[0] != NULL. In such a case,
     * every single data plane must be contained in one of the buffers in
     * AVFrame.buf or AVFrame.extended_buf.
     * There may be a single buffer for all the data, or one separate buffer for
     * each plane, or anything in between.
     */

    /** Number of data pointers in frame.data[] field. */
    @Deprecated public static final int AV_NUM_DATA_POINTERS = 8;
    
    
    public static JavFrame alloc() {
        return alloc( null );
    }

    
    public static JavFrame alloc( ObjectPool<? super JavFrame> optPool ) {
        long p = nAllocFrame();
        if( p == 0 ) {
            throw new OutOfMemoryError( "Allocation failed." );
        }
        return new JavFrame( p, optPool );
    }
    
    
    public static JavFrame allocVideo( int w,
                                       int h,
                                       int pixFormat,
                                       ObjectPool<? super JavFrame> optPool ) 
    {
        long pointer = nAllocFrame();
        if( pointer == 0 ) {
            throw new OutOfMemoryError();
        }

        int size = nComputeVideoBufferSize( w, h, pixFormat );
        ByteBuffer buf = ByteBuffer.allocateDirect( size );
        buf.order( ByteOrder.nativeOrder() );

        return allocVideo( w, h, pixFormat, buf, optPool );
    }


    public static JavFrame allocVideo( int w,
                                       int h,
                                       int pixFormat,
                                       ByteBuffer buf,
                                       ObjectPool<? super JavFrame> pool )
    {
        long pointer = nAllocFrame();
        if( pointer == 0 ) {
            throw new OutOfMemoryError();
        }
        JavFrame ret = new JavFrame( pointer, pool );
        try {
            ret.fillVideoFrame( w, h, pixFormat, buf );
        } catch( JavException ex ) {
            throw new RuntimeException( ex );
        }
        return ret;
    }
    
    
    public static JavFrame allocAudio( int channels,
                                       int samplesPerChannel,
                                       int sampleFormat,
                                       int align,
                                       ObjectPool<? super JavFrame> optPool )
    {
        int size = JavSampleFormat.getBufferSize( channels, samplesPerChannel, sampleFormat, align, null );
        ByteBuffer buf = ByteBuffer.allocateDirect( size );
        buf.order( ByteOrder.nativeOrder() );
        return allocAudio( channels, samplesPerChannel, sampleFormat, align, buf, optPool );
    }
    
    
    public static JavFrame allocAudio( int channels,
                                       int samplesPerChannel,
                                       int sampleFormat,
                                       int align,
                                       ByteBuffer buf,
                                       ObjectPool<? super JavFrame> pool )
    {
        long pointer = nAllocFrame();
        if( pointer == 0 ) {
            throw new OutOfMemoryError();
        }
        JavFrame ret = new JavFrame( pointer, pool );
        ret.fillAudioFrame( channels, samplesPerChannel, sampleFormat, buf, align );
        return ret;
    }
    
    
    public static int computeVideoBufferSize( int w, int h, int pixFmt ) {
        return nComputeVideoBufferSize( w, h, pixFmt );
    }


    private long mPointer;

    
    protected JavFrame( long pointer, ObjectPool<? super JavFrame> pool ) {
        super( pool );
        mPointer = pointer;
    }



    /**
     * Unreference all the buffers referenced by frame and reset the frame fields.
     */
    public void unrefData() {
        nUnref( mPointer );
    }

    /**
     * Pointer to the picture/channel planes.
     *
     * <p>This might be different from the first allocated byte
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     *
     * @return Pointer of type uint8_t *[AV_NUM_DATA_POINTERS].
     */
    public long data() {
        return nData( mPointer );
    }

    /**
     * pointer to the picture/channel planes.
     * This might be different from the first allocated byte
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     *
     * @param layer   Layer in data to get. May crash JVM if out-of-bounds.
     * @return A value of type: uint8_t*
     */
    public long dataElem( int layer ) {
        return nDataElem( mPointer, layer );
    }

    /**
     * Sets data pointer for one layer.
     *
     * @param layer   Layer in data to set. May crash JVM if out-of-bounds.
     * @param pointer A value of type: uint8_t*
     */
    public void dataElem( int layer, long pointer ) {
        nDataElem( mPointer, layer, pointer );
    }

    /**
     * Gets max number of data pointers.
     *
     * @param out Array of length [Jav.AV_NUM_DATA_POINTERS].
     *            Will receive values of type uint8_t*.
     */
    public void dataElem( long[] out ) {
        nDataElem( mPointer, out );
    }

    /**
     * Pointer to array of data planes.
     * <p>
     * For video, extendedData() should equal data().
     * <p>
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel nativeBuffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the nativeBuffer for all channels.
     * <p>
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     *
     * @return pointer of type uint8_t**
     */
    public long extendedData() {
        return nExtendedData( mPointer );
    }

    /**
     * Sets extendedData pointer.
     *
     * @param dataPointer Pointer of type uint8_t**
     * @see #extendedData()
     */
    public void extendedData( long dataPointer ) {
        nExtendedData( mPointer, dataPointer );
    }

    /**
     * Pointers to the data planes/channels.
     * <p>
     * For video, this should simply point to data[].
     * <p>
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel nativeBuffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the nativeBuffer for all channels.
     * <p>
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     *
     * @return pointer of type uint8_t*
     */
    public long extendedDataElem( int layer ) {
        return nExtendedDataElem( mPointer, layer );
    }

    /**
     * Pointers to the data planes/channels.
     *
     * For video, this should simply point to data[].
     *
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel nativeBuffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the nativeBuffer for all channels.
     *
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     */
    public void extendedDataElem( int layer, long planePointer ) {
        nExtendedDataElem( mPointer, layer, planePointer );
    }

    /**
     * Size, in bytes, of the data for each picture/channel plane.
     *
     * For audio, only linesize[0] may be set. For planar audio, each channel
     * plane must be the same size.
     *
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     *
     * @return pointer of type int[AV_NUM_DATA_POINTERS]
     */
    public long lineSize() {
        return nLineSize( mPointer );
    }

    /**
     * Size, in bytes, of the data for each picture/channel plane.
     *
     * For audio, only linesize[0] may be set. For planar audio, each channel
     * plane must be the same size.
     *
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     */
    public int lineSize( int layer ) {
        return nLineSize( mPointer, layer );
    }
    
    
    public void lineSize( int layer, int lineSize ) {
        nLineSize( mPointer, layer, lineSize );
    }
    
    
    public void lineSizes( int[] out8x1 ) {
        nLineSizes( mPointer, out8x1 );
    }
    
    
    public int width() {
        return nWidth( mPointer );
    }
    
    
    public void width( int w ) {
        nWidth( mPointer, w );
    }
    
    
    public int height() {
        return nHeight( mPointer );
    }
    
    
    public void height( int h ) {
        nHeight( mPointer, h );
    }
    
    /**
     * number of audio samples (per channel) described by this frame
     * - encoding: Set by user
     * - decoding: Set by libavcodec
     */
    public int nbSamples() {
        return nNbSamples( mPointer );
    }
    
    
    public void nbSamples( int numSamples ) {
        nNbSamples( mPointer, numSamples );
    }
    
    /**
     * format of the frame, -1 if unknown or unset
     * Values correspond to:
     * enum PixelFormat for video frames,
     * enum AVSampleFormat for audio frames
     * - encoding: unused
     * - decoding: Read by user.
     */
    public int format() {
        return nFormat( mPointer );
    }
    
    
    public void format( int format ) {
        nFormat( mPointer, format );
    }
    
    
    public boolean isKeyFrame() {
        return nKeyFrame( mPointer ) != 0;
    }
    
    
    public void isKeyFrame( boolean keyFrame ) {
        nKeyFrame( mPointer, keyFrame ? 1 : 0 );
    }
    
    /**
     * Picture type of the frame, see ?_TYPE below.
     * - encoding: Set by libavcodec. for coded_picture (and set by user for input).
     * - decoding: Set by libavcodec.
     */
    public int pictureType() {
        return nPictType( mPointer );
    }
    
    
    public void pictureType( int type ) {
        nPictType( mPointer, type );
    }
    
    /**
     * sample aspect ratio for the video frame, 0/1 if unknown/unspecified
     * - encoding: unused
     * - decoding: Read by user.
     */
    public Rational sampleAspectRatio() {
        return Rational.fromNativeLong( nSampleAspectRatio( mPointer ) );
    }
    
    
    public void sampleAspectRatio( Rational sampleAspectRatio ) {
        nSampleAspectRatio( mPointer, sampleAspectRatio.num(), sampleAspectRatio.den() );
    }
    
    /**
     * presentation timestamp in time_base units (time when frame should be shown to user)
     * If AV_NOPTS_VALUE then frame_rate = 1/time_base will be assumed.
     * - encoding: MUST be set by user.
     * - decoding: Set by libavcodec.
     */
    public long pts() {
        return nPts(mPointer);
    }


    public void pts( long presentTime ) {
        nPts( mPointer, presentTime );
    }
    
    /**
     * reordered pts from the last AVPacket that has been input into the decoder
     * - encoding: unused
     * - decoding: Read by user.
     */
    public long packetPts() {
        return nPktPts(mPointer);
    }


    public void packetPts( long pts ) {
        nPktPts( mPointer, pts );
    }

    /**
     * dts from the last AVPacket that has been input into the decoder
     * - encoding: unused
     * - decoding: Read by user.
     */
    public long packetDts() {
        return nPktDts(mPointer);
    }


    public void packetDts( long pts ) {
        nPktDts( mPointer, pts );
    }
    
    /**
     * picture number in bitstream order
     * - encoding: set by
     * - decoding: Set by libavcodec.
     */
    public int codedPictureNumber() {
        return nCodedPictureNumber( mPointer );
    }
    
    
    public void codedPictureNumber( int num ) {
        nCodedPictureNumber( mPointer, num );
    }
    
    /**
     * picture number in display order
     * - encoding: set by
     * - decoding: Set by libavcodec.
     */
    public int displayPictureNumber() {
        return nDisplayPictureNumber( mPointer );
    }
    
    
    public void displayPictureNumber( int num ) {
        nDisplayPictureNumber( mPointer, num );
    }
    
    /**
     * quality (between 1 (good) and FF_LAMBDA_MAX (bad))
     * - encoding: Set by libavcodec. for coded_picture (and set by user for input).
     * - decoding: Set by libavcodec.
     */
    public int quality() {
        return nQuality( mPointer );
    }
    
    
    public void quality( int q ) {
        nQuality( mPointer, q );
    }
    
    /**
     * is this picture used as reference
     * The values for this are the same as the MpegEncContext.picture_structure
     * variable, that is 1->top field, 2->bottom field, 3->frame/both fields.
     * Set to 4 for delayed, non-reference frames.
     * - encoding: unused
     * - decoding: Set by libavcodec. (before get_buffer() call)).
     */
    public int reference() {
        return nReference( mPointer );
    }
    
    
    public void reference( int q ) {
        nReference( mPointer, q );
    }
        
    /**
     * uint64_t error[AV_NUM_DATA_POINTERS];
     * - encoding: Set by libavcodec. if flags&CODEC_FLAG_PSNR.
     * - decoding: unused
     */
    public long error( int index ) {
        return nError( mPointer, index );
    }
    
    
    public void error( int index, long err ) {
        nError( mPointer, index, err );
    }
    
    
    public void errors( long[] out8x1 ) {
        nErrors( mPointer, out8x1 );
    }
    
    
    public int bufferType() {
        return nType( mPointer );
    }
    
    
    public void bufferType( int type ) {
        nType( mPointer, type );
    }
    
    /**
     * When decoding, this signals how much the picture must be delayed.
     * extra_delay = repeat_pict / (2*fps)
     * - encoding: unused
     * - decoding: Set by libavcodec.
     */
    public int repeatPict() {
        return nRepeatPict( mPointer );
    }
    
    
    public void repeatPict( int n ) {
        nRepeatPict( mPointer, n );
    }

    /**
     * The content of the picture is interlaced.
     * - encoding: Set by user.
     * - decoding: Set by libavcodec. (default 0)
     */
    public boolean interlacedFrame() {
        return nInterlacedFrame( mPointer ) != 0;
    }

    
    public void interlacedFrame( boolean interlaced ) {
        nInterlacedFrame( mPointer, interlaced ? 1 : 0 );
    }
    
    /**
     * If the content is interlaced, is top field displayed first.
     * - encoding: Set by user.
     * - decoding: Set by libavcodec.
     */
    public boolean topFieldFirst() {
        return nTopFieldFirst( mPointer ) != 0;
    }

    
    public void topFieldFirst( boolean topFirst ) {
        nTopFieldFirst( mPointer, topFirst ? 1 : 0 );
    }

    /**
     * Tell user application that palette has changed from previous frame.
     * - encoding: ??? (no palette-enabled encoder yet)
     * - decoding: Set by libavcodec. (default 0).
     */
    public int paletteHasChanged() {
        return nPaletteHasChanged( mPointer );
    }
    
    
    public void paletteHasChanged( int c ) {
        nPaletteHasChanged( mPointer, c );
    }
    
    /**
     * codec suggestion on nativeBuffer type if != 0
     * - encoding: unused
     * - decoding: Set by libavcodec. (before get_buffer() call)).
     */
    public int bufferHints() {
        return nBufferHints( mPointer );
    }
    
    
    public void bufferHints( int n ) {
        nBufferHints( mPointer, n );
    }

    /**
     * log2 of the size of the block which a single vector in motion_val represents:
     * (4->16x16, 3->8x8, 2-> 4x4, 1-> 2x2)
     * - encoding: unused
     * - decoding: Set by libavcodec.
     */
    public byte motionSubsampleLog2() {
        return nMotionSubsampleLog2( mPointer );
    }
    
    /**
     * Sample rate of the audio data.
     *
     * - encoding: unused
     * - decoding: read by user
     */
    public int sampleRate() {
        return nSampleRate( mPointer );
    }
        
    /**
     * Channel layout of the audio data.
     *
     * - encoding: unused
     * - decoding: read by user.
     */
    public long channelLayout() {
        return nChannelLayout( mPointer );
    }

    /**
     * @return direct pointer to buf field.
     */
    public long buf() {
        return nBuf( mPointer );
    }

    /**
     * AVBuffer references backing the data for this frame. If all elements of
     * this array are NULL, then this frame is not reference counted.
     *
     * <p>There may be at most one AVBuffer per data plane, so for video this array
     * always contains all the references. For planar audio with more than
     * AV_NUM_DATA_POINTERS channels, there may be more buffers than can fit in
     * this array. Then the extra AVBufferRef pointers are stored in the
     * extended_buf array.
     *
     * @return new reference to buffer (that SHOULD be released), or {@code null}.
     */
    public JavBufferRef bufElem( int idx ) {
        long n = nBufElem( mPointer, idx );
        return n == 0L ? null : JavBufferRef.wrapPointer( n );
    }

    /**
     * Like {@link #bufElem(int)}, but if the requested buf entry was allocated as
     * a Java Bytebuffer, that backing buffer will be retrieved directly and a duplicate
     * will be made. This is slightly more efficient than calling: <br>
     * {@code
     *   JavBufferRef ref = bufElem( idx );
     *   return ref == null ? null : ref.javaBuffer();
     * }
     *
     * @param idx Index into {@code buf} array.
     * @return backing ByteBuffer object, if exists.
     * @see #bufElem(int)
     */
    public ByteBuffer javaBufElem( int idx ) {
        return nJavaBufElem( mPointer, idx );
    }

    /**
     * @param idx Index into buf array.
     * @param ref Buffer to insert into array. May be null. Creates new reference if not null.
     */
    public void bufElem( int idx, JavBufferRef ref ) {
        nBufElem( mPointer, idx, ref == null ? 0L : ref.pointer() );
    }

    /**
     * @return direct pointer to extended_buf field.
     */
    public long extendedBuf() {
        return nExtendedBuf( mPointer );
    }

    /**
     * Sets extendedBuf pointer. Be careful not to cause memory leaks here.
     *
     * @param ptr Pointer of type AVBufferRef **.
     */
    public void extendedBuf( long ptr ) {
        nExtendedBuf( mPointer, ptr );
    }

    /**
     * For planar audio which requires more than AV_NUM_DATA_POINTERS
     * AVBufferRef pointers, this array will hold all the references which
     * cannot fit into AVFrame.buf.
     *
     * <p>Note that this is different from AVFrame.extended_data, which always
     * contains all the pointers. This array only contains the extra pointers,
     * which cannot fit into AVFrame.buf.
     *
     * <p>This array is always allocated using av_malloc() by whoever constructs
     * the frame. It is freed in av_frame_unref().
     *
     * @param idx Index of extended buffer to retrieve.
     * @return new reference to buffer (that SHOULD be released), or {@code null}.
     */
    public JavBufferRef extendedBufElem( int idx ) {
        long n = nExtendedBufElem( mPointer, idx );
        return n == 0L ? null : JavBufferRef.wrapPointer( n );
    }

    /**
     * Like {@link #extendedBufElem(int)}, but if the requested buf entry was allocated as
     * a Java Bytebuffer, that backing buffer will be retrieved directly and a duplicate
     * will be made. This is slightly more efficient than calling: <br>
     * {@code
     *   JavBufferRef ref = extendedBufElem( idx );
     *   return ref == null ? null : ref.javaBuffer();
     * }
     *
     * @param idx Index into {@code extended_buf} array.
     * @return backing ByteBuffer object, if exists.
     * @see #bufElem(int)
     */
    public ByteBuffer javaExtendedBufElem( int idx ) {
        return nJavaExtendedBufElem( mPointer, idx );
    }

    /**
     * @param idx Index into extended_buf array.
     * @param ref Buffer to insert into array. May be null. Creates new reference if not null.
     */
    public void extendedBufElem( int idx, JavBufferRef ref ) {

        nExtendedBufElem( mPointer, idx, ref == null ? 0L : ref.pointer() );
    }

    /**
     * Number of elements in extended_buf.
     * @see #extendedBuf()
     */
    public int nbExtendedBuf() {
        return nNbExtendedBuf( mPointer );
    }

    /**
     * Dangerous.
     *
     * Sets value of nb_extended_buf indicating length of extended_buf array.
     */
    public void nbExtendedBuf( int num ) {
        nNbExtendedBuf( mPointer, num );
    }


    /**
     * frame timestamp estimated using various heuristics, in stream time base
     * Code outside libavcodec should access this field using:
     * av_frame_get_best_effort_timestamp(frame)
     * - encoding: unused
     * - decoding: set by libavcodec, read by user.
     */
    public long bestEffortTimestamp() {
        return nBestEffortTimestamp( mPointer );
    }
    
    /**
     * reordered pos from the last AVPacket that has been input into the decoder
     * Code outside libavcodec should access this field using:
     * av_frame_get_pkt_pos(frame)
     * - encoding: unused
     * - decoding: Read by user.
     */
    public long packetPos() {
        return nPktPos( mPointer );
    }

    /**
     * duration of the corresponding packet, expressed in
     * AVStream->time_base units, 0 if unknown.
     * Code outside libavcodec should access this field using:
     * av_frame_get_pkt_duration(frame)
     * - encoding: unused
     * - decoding: Read by user.
     */
    public long packetDuration() {
        return nPktDuration( mPointer );
    }
    
    /**
     * metadata.
     * Code outside libavcodec should access this field using:
     * av_frame_get_metadata(frame)
     * - encoding: Set by user.
     * - decoding: Set by libavcodec.
     */
    public JavDict metadata() {
        long p = nMetadata( mPointer );
        return p == 0L ? null : new JavDict( p );
    }
    
    
    public void metadata( JavDict dict ) {
        nMetadata( mPointer, dict == null ? 0L : dict.pointer() );
    }
    
    /**
     * decode error flags of the frame, set to a combination of
     * FF_DECODE_ERROR_xxx flags if the decoder produced a frame, but there
     * were errors during the decoding.
     * Code outside libavcodec should access this field using:
     * av_frame_get_decode_error_flags(frame)
     * - encoding: unused
     * - decoding: set by libavcodec, read by user.
     */
    public int decodeErrorFlags() {
        return nDecodeErrorFlags( mPointer );
    }
    
    /**
     * number of audio channels, only used for audio.
     * 
     * - encoding: unused
     * - decoding: Read by user.
     */
    public int channels() {
        return nChannels( mPointer );
    }
    
    

    //** Video **/
    
    /**
     * Setup the picture fields based on the specified image parameters and image data.
     * This method does not copy the data, but references the provided data nativeBuffer
     * directly. This method updates the following fields: <br/>
     * userBuffer <br/>
     * width <br/>
     * height <br/>
     * format <br/>
     * type <br/>
     * lineSizes <br/>
     * dataElem <br/>
     * extendedDataPointers <br/>
     * <p>
     * If buf is NULL, the function will fill only the picture linesize
     * array and return the required size for the image nativeBuffer.
     *
     * @param width   width of the image in pixels
     * @param height  height of the image in pixels
     * @param pixFmt  pixel format of the image
     * @param optBuf  Directly allocated ByteBuffer containing picture data, or NULL.
     * 
     * @return the size in bytes required, or a negative error code in case of failure
     */
    public int fillVideoFrame( int width, int height, int pixFmt, ByteBuffer optBuf ) throws JavException {
        int bufPos = 0;
        int bufLen = 0;

        if( optBuf != null ) {
            optBuf = optBuf.duplicate().order( ByteOrder.nativeOrder() );
            bufPos = optBuf.position();
            bufLen = optBuf.remaining();
        }

        int bytes = nFillVideoFrame( mPointer, width, height, pixFmt, optBuf, bufPos, bufLen );
        if( bytes < 0 ) {
            throw new JavException( "Fill video frame failed: " + bytes );
        }

        return bytes;
    }

    
    //** Audio **/


    /**
     * Sets contents of interleaved audio frame data to point to directly allocated nativeBuffer. (no copying).
     *
     * The nativeBuffer must be a preallocated nativeBuffer with a size big enough to contain
     * the specified samples amount. The filled AVFrame data pointers will point
     * to this nativeBuffer.
     *
     * AVFrame extended_data channel pointers are allocated if necessary for
     * planar audio.
     * 
     * @param channels           Number of channels.
     * @param samplesPerChannel  Number of samples for each channel.
     * @param sampleFormat       Format of audio samples.
     * @param buf                Directly allocated ByteBuffer containing audio data. Position and limit are used.
     * @param align              Byte alignment for plane size. Normally 0.
     */
    public void fillAudioFrame( int channels,
                                int samplesPerChannel,
                                int sampleFormat,
                                ByteBuffer buf,
                                int align )
    {
        int bytes = nFillAudioFrame( mPointer,
                                     channels,
                                     samplesPerChannel,
                                     sampleFormat,
                                     align,
                                     buf,
                                     buf.position(),
                                     buf.remaining() );
        
        if( bytes < 0 ) {
            throw new RuntimeException( "Unknown exception filling nativeBuffer: " + bytes );
        }
    }
    
    
    
    
    /**
     * Native 
     */
    
    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.SELF;
    }
    
    
    
    
    @Override
    protected void finalize() throws Throwable {
        freeObject();
        super.finalize();
    }
    
    
    protected void freeObject() {
        long p;
        synchronized( this ) {
            p = mPointer;
            mPointer = 0;
        }
        if( p == 0L ) {
            return;
        }
        nFree( p );
    }
    
    
    protected static native long nAllocFrame();
    private static native void   nFree( long pointer );
    private static native void   nUnref( long pointer );

    private static native long nData( long pointer );
    private static native long nDataElem( long pointer, int layer );
    private static native void nDataElem( long pointer, int layer, long dataPointer );
    private static native void nDataElem( long pointer, long[] out8x1 );
    private static native long nLineSize( long pointer );
    private static native int  nLineSize( long pointer, int layer );
    private static native void nLineSize( long pointer, int layer, int lineSize );
    private static native void nLineSizes( long pointer, int[] out8x1 );
    private static native long nExtendedData( long pointer );
    private static native void nExtendedData( long pointer, long dataPointer );
    private static native long nExtendedDataElem( long pointer, int layer );
    private static native void nExtendedDataElem( long pointer, int layer, long dataPointer );

    private static native int  nWidth( long pointer );
    private static native void nWidth( long pointer, int width );
    private static native int  nHeight( long pointer );
    private static native void nHeight( long pointer, int width );
    private static native int  nNbSamples( long pointer );
    private static native void nNbSamples( long pointer, int numSamples );    
    private static native int  nFormat( long pointer );
    private static native void nFormat( long pointer, int format );
    private static native int  nKeyFrame( long pointer );
    private static native void nKeyFrame( long pointer, int keyframe );
    private static native int  nPictType( long pointer );
    private static native void nPictType( long pointer, int pictType );
    private static native long nSampleAspectRatio( long pointer );
    private static native void nSampleAspectRatio( long pointer, int num, int den );
    private static native long nPts( long pointer );
    private static native void nPts( long pointer, long presentTime );
    private static native long nPktPts( long pointer );
    private static native void nPktPts( long pointer, long presentTime );
    private static native long nPktDts( long pointer );
    private static native void nPktDts( long pointer, long presentTime );
    private static native int  nCodedPictureNumber( long pointer );
    private static native void nCodedPictureNumber( long pointer, int num );
    private static native int  nDisplayPictureNumber( long pointer );
    private static native void nDisplayPictureNumber( long pointer, int num );
    private static native int  nQuality( long pointer );
    private static native void nQuality( long pointer, int q );
    private static native int  nReference( long pointer );
    private static native void nReference( long pointer, int ref );

    private static native long nMacroBlockTypePointer( long pointer );
    private static native long nMotionVectorPointer( long pointer, int dir );
    
    private static native long nError( long pointer, int index );
    private static native void nError( long pointer, int index, long error );
    private static native void nErrors( long pointer, long[] out4x1 );
    private static native int  nType( long pointer );
    private static native void nType( long pointer, int type );
    private static native int  nRepeatPict( long pointer );
    private static native void nRepeatPict( long pointer, int n );
    private static native int  nInterlacedFrame( long pointer );
    private static native void nInterlacedFrame( long pointer, int n );
    private static native int  nTopFieldFirst( long pointer );
    private static native void nTopFieldFirst( long pointer, int n );
    private static native int  nPaletteHasChanged( long pointer );
    private static native void nPaletteHasChanged( long pointer, int n );
    private static native int  nBufferHints( long pointer );
    private static native void nBufferHints( long pointer, int n );
    private static native byte nMotionSubsampleLog2( long pointer );
    private static native void nMotionSubsampleLog2( long pointer, byte val );
    private static native int  nSampleRate( long pointer );
    private static native void nSampleRate( long pointer, int sampleRate );
    private static native long nChannelLayout( long pointer );
    private static native void nChannelLayout( long pointer, long channelLayout );

    private static native long nBuf( long pointer );
    private static native long nBufElem( long pointer, int idx );
    private static native void nBufElem( long pointer, int idx, long ref );
    private static native ByteBuffer nJavaBufElem( long pointer, int idx );
    private static native long nExtendedBuf( long pointer );
    private static native void nExtendedBuf( long pointer, long bufPointer );
    private static native long nExtendedBufElem( long pointer, int idx );
    private static native void nExtendedBufElem( long pointer, int idx, long ref );
    private static native ByteBuffer nJavaExtendedBufElem( long pointer, int idx );
    private static native int  nNbExtendedBuf( long pointer );
    private static native void nNbExtendedBuf( long pointer, int nb );

    private static native long nBestEffortTimestamp( long pointer );
    private static native long nPktPos( long pointer );
    private static native long nPktDuration( long pointer );
    
    private static native long nMetadata( long pointer );
    private static native void nMetadata( long pointer, long dictPointer );
    
    private static native int  nDecodeErrorFlags( long pointer );
    private static native void nDecodeErrorFlags( long pointer, int flags );
    private static native int  nChannels( long pointer );
    private static native void nChannels( long pointer, int channels );

    private static native int  nFillVideoFrame( long pointer, int w, int h, int pixFmt, ByteBuffer buf, int bufOff, int bufLen );
    private static native int  nFillAudioFrame( long pointer,
                                                int channelNum,
                                                int sampleNum,
                                                int sampleFmt,
                                                int align,
                                                ByteBuffer buf,
                                                int bufOff,
                                                int bufLen );

    protected static native int nComputeVideoBufferSize( int w, int h, int pixelFormat );


    /**
     * Only call this if you know what you're doing. Will kill JVM if your
     * ByteBuffer is not correctly sized.
     * <p>
     * Copies data from one of the frame layers to a natively allocated ByteBuffer.
     *
     * @param dst       Byte nativeBuffer with position and limit set.
     */
    @Deprecated public void readData( int srcLayer, ByteBuffer dst ) {
        JavMem.copy( dataPointer( srcLayer ), dst );
    }

    /**
     * Only call this if you know what you're doing. Will kill JVM if your
     * ByteBuffer is not correctly sized.
     * <p>
     * Copies data from one of the frame layers to a natively allocated ByteBuffer.
     *
     * @param src      ByteBuffer with position and limit set.
     * @param dstLayer Layer to write data to.
     */
    @Deprecated public void writeData( ByteBuffer src, int dstLayer ) {
        JavMem.copy( src, dataPointer( dstLayer ) );
    }



    /**
     * pointer to the picture/channel planes.
     * This might be different from the first allocated byte
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     *
     * @param layer   Layer in data to get. May crash JVM if out-of-bounds.
     * @return A value of type: uint8_t*
     */
    public long dataPointer( int layer ) {
        return nDataElem( mPointer, layer );
    }

    /**
     * Sets data pointer for one layer.
     *
     * @param layer   Layer in data to set. May crash JVM if out-of-bounds.
     * @param pointer A value of type: uint8_t*
     */
    public void dataPointer( int layer, long pointer ) {
        nDataElem( mPointer, layer, pointer );
    }

    /**
     * Gets max number of data pointers.
     *
     * @param out Array of length [Jav.AV_NUM_DATA_POINTERS].
     *            Will receive values of type uint8_t*.
     */
    public void dataPointers( long[] out ) {
        nDataElem( mPointer, out );
    }

    /**
     * Pointers to the data planes/channels.
     * <p>
     * For video, this should simply point to data[].
     * <p>
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel nativeBuffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the nativeBuffer for all channels.
     * <p>
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     *
     * @return pointer of type uint8_t*
     */
    public long extendedDataPointer( int layer ) {
        return nExtendedDataElem( mPointer, layer );
    }

    /**
     * Pointers to the data planes/channels.
     *
     * For video, this should simply point to data[].
     *
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel nativeBuffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the nativeBuffer for all channels.
     *
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     */
    public void extendedDataPointer( int layer, long planePointer ) {
        nExtendedDataElem( mPointer, layer, planePointer );
    }


    /**
     * @deprecated Use JavSampleFormat.getBufferSize
     */
    @Deprecated public static int computeAudioBufferSize( int channelNum,
                                                          int sampleNum,
                                                          int sampleFmt,
                                                          int align,
                                                          int[] optLineSize )
    {
        return JavSampleFormat.getBufferSize( channelNum, sampleNum, sampleFmt, align, optLineSize );
    }


    @Deprecated public void freeData() {
        nUnref( mPointer );
    }


    @Deprecated public boolean hasDirectBuffer() {
        return nJavaBufElem( mPointer, 0 ) != null;
    }


    @Deprecated public int directBufferCapacity() {
        ByteBuffer b = nJavaBufElem( mPointer, 0 );
        return b == null ? -1 : b.capacity();
    }


    @Deprecated public ByteBuffer directBuffer() {
        return javaBufElem( 0 );
    }


    //    /**
//     * Setup contents of picture fields manually. This method does not copy the data, but references
//     * the provided data nativeBuffer directly. This method updates the following fields: <br/>
//     * userBuffer <br/>
//     * width <br/>
//     * height <br/>
//     * format <br/>
//     * type <br/>
//     * lineSizes <br/>
//     * dataElem <br/>
//     * extendedDataPointers <br/>
//     * <p>
//     * I don't believe this method will work for images with depth &gt; 8.
//     *
//     * @param w           Width of frame
//     * @param h           Height of frame
//     * @param pixFormat   Format of pixels.
//     * @param depth       Number of components of frame.
//     * @param optBuf      Directly allocated ByteBuffer containing picture data, or NULL.
//     * @param bufOffsets  Array of length &geq; depth holding offsets into buf where data begins for each plane.
//     * @param lineSizes   Array of length &geq; depth holding size of lines for each plane.
//     *
//     */
//    public void fillVideoFrameManually( int w,
//                                        int h,
//                                        int pixFormat,
//                                        int depth,
//                                        ByteBuffer optBuf,
//                                        int[] bufOffsets,
//                                        int[] lineSizes )
//                                        throws JavException
//    {
//        int err = nFillVideoFrameManually( mPointer, w, h, pixFormat, depth, optBuf, bufOffsets, lineSizes );
//        if( err < 0 ) {
//            throw new RuntimeException( "Unknown exception filling nativeBuffer: " + err );
//        }
//        if( optBuf == null ) {
//            mUserBuffer = null;
//        } else {
//            mUserBuffer = optBuf.duplicate().order( ByteOrder.nativeOrder() );
//        }
//    }

    //private static native int  nFillVideoFrameManually( long pointer, int w, int h, int pixFmt, int d, ByteBuffer buf, int[] off, int[] lineSizes );
}
