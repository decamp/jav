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
 * @author decamp
 */
public class JavFrame extends AbstractRefable implements NativeObject {
    
    /** Number of data pointers in frame.data[] field. */
    @Deprecated public static final int AV_NUM_DATA_POINTERS = 8;
    
    
    public static JavFrame alloc() {
        return alloc( null );
    }

    
    public static JavFrame alloc( ObjectPool<? super JavFrame> pool ) {
        long p = nAllocFrame();
        if( p == 0 ) {
            throw new OutOfMemoryError( "Allocation failed." );
        }
        return new JavFrame( p, pool );
    }
    
    
    public static JavFrame allocVideo( int w,
                                       int h,
                                       int pixFormat,
                                       ObjectPool<? super JavFrame> optPool ) 
    {
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
        int size = nComputeAudioBufferSize( channels, samplesPerChannel, sampleFormat, align, null );
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
        ret.fillAudioFrame( channels, samplesPerChannel, sampleFormat, buf, 0, align );
        return ret;
    }
    
    
    public static int computeVideoBufferSize( int w, int h, int pixFmt ) {
        return nComputeVideoBufferSize( w, h, pixFmt );
    }
                                        
    
    public static int computeAudioBufferSize( int channelNum, 
                                              int sampleNum, 
                                              int sampleFmt, 
                                              int align, 
                                              int[] optLineSize ) 
    {
        return nComputeAudioBufferSize( channelNum, sampleNum, sampleFmt, align, optLineSize );
    }
    

    
    private long mPointer;
    //private final ObjectPool<? super JavFrame> mPool;
    private ByteBuffer mUserBuffer;
    
    
    protected JavFrame( long pointer, ObjectPool<? super JavFrame> pool ) {
        super( pool );
        mPointer = pointer;
        //mPool    = pool;
    }
    
    
    
    public void freeData() {
        mUserBuffer = null;
        nFreeData( mPointer );
    }
    
    
    public boolean hasDirectBuffer() {
        return mUserBuffer != null;
    }
    
    
    public int directBufferCapacity() {
        return mUserBuffer == null ? -1 : mUserBuffer.capacity();
    }
    
    
    public ByteBuffer directBuffer() {
        if( mUserBuffer == null ) {
            return null;
        }
        ByteBuffer ret = mUserBuffer.duplicate();
        ret.order( ByteOrder.nativeOrder() );
        return ret;
    }


    /**
     *
     *
     * Pointer to the picture/channel planes. <br>
     * <p>
     * This might be different from the first allocated byte
     * - encoding: Set by user
     * - decoding: set by AVCodecContext.get_buffer()
     *
     * @return A value of type: uint8_t *[AV_NUM_DATA_POINTERS].
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
    public long dataPointer( int layer ) {
        return nDataPointer( mPointer, layer );
    }

    /**
     * Sets data pointer for one layer.
     *
     * @param layer   Layer in data to set. May crash JVM if out-of-bounds.
     * @param pointer A value of type: uint8_t*
     */
    public void dataPointer( int layer, long pointer ) {
        nDataPointer( mPointer, layer, pointer );
    }

    /**
     * Gets max number of data pointers.
     *
     * @param out Array of length [Jav.AV_NUM_DATA_POINTERS].
     *            Will receive values of type uint8_t*.
     */
    public void dataPointers( long[] out ) {
        nDataPointers( mPointer, out );
    }

    /**
     * Pointer to array of data planes.
     * <p>
     * For video, extendedData() should equal data().
     * <p>
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel buffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the buffer for all channels.
     * <p>
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     *
     * @return A value of type: uint8_t**
     */
    public long extendedData() {
        return nExtendedData( mPointer );
    }

    /**
     * Sets extendedData pointer.
     *
     * @param dataPointer  A value of type: uint8_t**
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
     * linesize[0] contains the size of each channel buffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the buffer for all channels.
     * <p>
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     *
     * @return A value of type: uint8_t*
     */
    public long extendedDataPointer( int layer ) {
        return nExtendedDataPointer( mPointer, layer );
    }

    /**
     * Pointers to the data planes/channels.
     *
     * For video, this should simply point to data[].
     *
     * For planar audio, each channel has a separate data pointer, and
     * linesize[0] contains the size of each channel buffer.
     * For packed audio, there is just one data pointer, and linesize[0]
     * contains the total size of the buffer for all channels.
     *
     * Note: Both data and extended_data should always be set in a valid frame,
     * but for planar audio with more channels that can fit in data,
     * extended_data must be used in order to access all channels.
     */
    public void extendedDataPointer( int layer, long planePointer ) {
        nExtendedDataPointer( mPointer, layer, planePointer );
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
     * codec suggestion on buffer type if != 0
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
    
    
    
    
    /** Video **/
    
    /**
     * Setup the picture fields based on the specified image parameters and image data.
     * This method does not copy the data, but references the provided data buffer
     * directly. This method updates the following fields: <br/>
     * userBuffer <br/>
     * width <br/>
     * height <br/>
     * format <br/>
     * type <br/>
     * lineSizes <br/>
     * dataPointers <br/>
     * extendedDataPointers <br/>
     * <p>
     * If buf is NULL, the function will fill only the picture linesize
     * array and return the required size for the image buffer.
     *
     * @param width   width of the image in pixels
     * @param height  height of the image in pixels
     * @param pixFmt  pixel format of the image
     * @param optBuf  Directly allocated ByteBuffer containing picture data, or NULL.
     * 
     * @return the size in bytes required, or a negative error code in case of failure
     */
    public int fillVideoFrame( int width, int height, int pixFmt, ByteBuffer optBuf ) throws JavException {
        int bytes = nFillVideoFrame( mPointer, width, height, pixFmt, optBuf, optBuf == null ? 0 : optBuf.position() );
        if( bytes < 0 ) {
            throw new JavException( "Fill video frame failed: " + bytes );
        }
        if( optBuf == null ) {
            mUserBuffer = null;
        } else {
            mUserBuffer = optBuf.duplicate().order( ByteOrder.nativeOrder() );
        }
        return bytes;
    }
    
    /**
     * Setup contents of picture fields manually. This method does not copy the data, but references
     * the provided data buffer directly. This method updates the following fields: <br/>
     * userBuffer <br/>
     * width <br/>
     * height <br/>
     * format <br/>
     * type <br/>
     * lineSizes <br/>
     * dataPointers <br/>
     * extendedDataPointers <br/>
     * <p>
     * I don't believe this method will work for images with depth &gt; 8. 
     * 
     * @param w           Width of frame
     * @param h           Height of frame
     * @param pixFormat   Format of pixels.
     * @param depth       Number of components of frame.
     * @param optBuf      Directly allocated ByteBuffer containing picture data, or NULL.
     * @param bufOffsets  Array of length &geq; depth holding offsets into buf where data begins for each plane.
     * @param lineSizes   Array of length &geq; depth holding size of lines for each plane.   
     * 
     */
    public void fillVideoFrameManually( int w, 
                                        int h,
                                        int pixFormat,
                                        int depth,
                                        ByteBuffer optBuf,
                                        int[] bufOffsets,
                                        int[] lineSizes )
                                        throws JavException 
    {
        int err = nFillVideoFrameManually( mPointer, w, h, pixFormat, depth, optBuf, bufOffsets, lineSizes );
        if( err < 0 ) {
            throw new RuntimeException( "Unknown exception filling buffer: " + err );
        }
        if( optBuf == null ) {
            mUserBuffer = null;
        } else {
            mUserBuffer = optBuf.duplicate().order( ByteOrder.nativeOrder() );
        }
    }
    
    
    /** Audio **/


    /**
     * Sets contents of interleaved audio frame data to point to directly allocated buffer. (no copying).
     *
     * The buffer must be a preallocated buffer with a size big enough to contain
     * the specified samples amount. The filled AVFrame data pointers will point
     * to this buffer.
     *
     * AVFrame extended_data channel pointers are allocated if necessary for
     * planar audio.
     * 
     * @param channels           Number of channels.
     * @param samplesPerChannel  Number of samples for each channel.
     * @param sampleFormat       Format of audio samples.
     * @param buf                Directly allocated ByteBuffer containing audio data.
     * @param bufOffset          Offset into buffer where audio data begins.
     * @param align              Byte alignment for plane size. Normally 0.
     */
    public void fillAudioFrame( int channels,
                                int samplesPerChannel,
                                int sampleFormat,
                                ByteBuffer buf,
                                int bufOffset,
                                int align )
    {
        nbSamples( samplesPerChannel );
        int bytes = nFillAudioFrame( mPointer, 
                                     channels,
                                     sampleFormat,
                                     align,
                                     buf,
                                     bufOffset,
                                     buf.capacity() - bufOffset );
        
        if( bytes < 0 ) {
            throw new RuntimeException( "Unknown exception filling buffer: " + bytes );
        }
        mUserBuffer = buf.duplicate().order( ByteOrder.nativeOrder() );
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
    private static native void   nFreeData( long pointer );

    private static native long nData( long pointer );
    private static native long nDataPointer( long pointer, int layer );
    private static native void nDataPointer( long pointer, int layer, long dataPointer );
    private static native void nDataPointers( long pointer, long[] out8x1 );
    private static native int  nLineSize( long pointer, int layer );
    private static native void nLineSize( long pointer, int layer, int lineSize );
    private static native void nLineSizes( long pointer, int[] out8x1 );
    private static native long nExtendedData( long pointer );
    private static native void nExtendedData( long pointer, long dataPointer );
    private static native long nExtendedDataPointer( long pointer, int layer );
    private static native void nExtendedDataPointer( long pointer, int layer, long dataPointer );

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

    private static native long nBestEffortTimestamp( long pointer );
    private static native long nPktPos( long pointer );
    private static native long nPktDuration( long pointer );
    
    private static native long nMetadata( long pointer );
    private static native void nMetadata( long pointer, long dictPointer );
    
    private static native int  nDecodeErrorFlags( long pointer );
    private static native void nDecodeErrorFlags( long pointer, int flags );
    private static native int  nChannels( long pointer );
    private static native void nChannels( long pointer, int channels );

    private static native int  nFillVideoFrame( long pointer, int w, int h, int pixFmt, ByteBuffer buf, int off );
    private static native int  nFillVideoFrameManually( long pointer, int w, int h, int pixFmt, int d, ByteBuffer buf, int[] off, int[] lineSizes );
    private static native int  nFillAudioFrame( long pointer, int channels, int sampleFmt, int align, ByteBuffer buf, int pos, int remain );
    
    /**
     * Get the required buffer size for the given audio parameters.
     *
     * @param numChannels   the number of channels
     * @param numSamples    the number of samples in a single channel
     * @param sampleFmt     the sample format
     * @param align         buffer size alignment (0 = default, 1 = no alignment)
     * @param optLineSize   [out] calculated linesize, may be NULL
     * @return              required buffer size, or negative error code on failure
     */
    protected static native int nComputeAudioBufferSize( int numChannels, int numSamples, int sampleFmt, int align, int[] optLineSize );
    protected static native int nComputeVideoBufferSize( int w, int h, int pixelFormat );


    /**
     * Only call this if you know what you're doing. Will kill JVM if your
     * ByteBuffer is not correctly sized.
     * <p>
     * Copies data from one of the frame layers to a natively allocated ByteBuffer.
     *
     * @param srcLayer
     * @param dst       Byte buffer with position and limit set.
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


}
