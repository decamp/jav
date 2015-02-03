/*

Probably unnecessary to ever implement.

*/


//package bits.jav.filter;
//
//import bits.jav.util.NativeObject;
//import bits.util.ref.AbstractRefable;
//import bits.util.ref.ObjectPool;
//
//
///**
// * A reference-counted buffer data type used by the filter system. Filters
// * should not store pointers to this structure directly, but instead use the
// * AVFilterBufferRef structure below.
// */
//public class JavFilterBuffer extends AbstractRefable implements NativeObject {
//
//    private long mPointer;
//
//
//    protected JavFilterBuffer( long pointer, ObjectPool<? super JavFilterBuffer> pool ) {
//        super( pool );
//        mPointer = pointer;
//    }
//
//
//
//    /**
//     * Unreference all the buffers referenced by frame and reset the frame fields.
//     */
//    public void unrefData() {
//        nUnref( mPointer );
//    }
//
//    /**
//     * Pointer to the picture/channel planes.
//     *
//     * @return Pointer of type uint8_t *[AV_NUM_DATA_POINTERS].
//     */
//    public long data() {
//        return nData( mPointer );
//    }
//
//    /**
//     * pointer to the picture/channel planes.
//     * This might be different from the first allocated byte
//     * - encoding: Set by user
//     * - decoding: set by AVCodecContext.get_buffer()
//     *
//     * @param layer   Layer in data to get. May crash JVM if out-of-bounds.
//     * @return A value of type: uint8_t*
//     */
//    public long dataElem( int layer ) {
//        return nDataElem( mPointer, layer );
//    }
//
//    /**
//     * Sets data pointer for one layer.
//     *
//     * @param layer   Layer in data to set. May crash JVM if out-of-bounds.
//     * @param pointer A value of type: uint8_t*
//     */
//    public void dataElem( int layer, long pointer ) {
//        nDataElem( mPointer, layer, pointer );
//    }
//
//    /**
//     * Gets max number of data pointers.
//     *
//     * @param out Array of length [Jav.AV_NUM_DATA_POINTERS].
//     *            Will receive values of type uint8_t*.
//     */
//    public void dataElem( long[] out ) {
//        nDataElem( mPointer, out );
//    }
//
//    /**
//     * Pointer to array of data planes.
//     * <p>
//     * For video, extendedData() should equal data().
//     * <p>
//     * For planar audio, each channel has a separate data pointer, and
//     * linesize[0] contains the size of each channel nativeBuffer.
//     * For packed audio, there is just one data pointer, and linesize[0]
//     * contains the total size of the nativeBuffer for all channels.
//     * <p>
//     * Note: Both data and extended_data should always be set in a valid frame,
//     * but for planar audio with more channels that can fit in data,
//     * extended_data must be used in order to access all channels.
//     *
//     * @return pointer of type uint8_t**
//     */
//    public long extendedData() {
//        return nExtendedData( mPointer );
//    }
//
//    /**
//     * Sets extendedData pointer.
//     *
//     * @param dataPointer Pointer of type uint8_t**
//     * @see #extendedData()
//     */
//    public void extendedData( long dataPointer ) {
//        nExtendedData( mPointer, dataPointer );
//    }
//
//    /**
//     * Pointers to the data planes/channels.
//     * <p>
//     * For video, this should simply point to data[].
//     * <p>
//     * For planar audio, each channel has a separate data pointer, and
//     * linesize[0] contains the size of each channel nativeBuffer.
//     * For packed audio, there is just one data pointer, and linesize[0]
//     * contains the total size of the nativeBuffer for all channels.
//     * <p>
//     * Note: Both data and extended_data should always be set in a valid frame,
//     * but for planar audio with more channels that can fit in data,
//     * extended_data must be used in order to access all channels.
//     *
//     * @return pointer of type uint8_t*
//     */
//    public long extendedDataElem( int layer ) {
//        return nExtendedDataElem( mPointer, layer );
//    }
//
//    /**
//     * Pointers to the data planes/channels.
//     *
//     * For video, this should simply point to data[].
//     *
//     * For planar audio, each channel has a separate data pointer, and
//     * linesize[0] contains the size of each channel nativeBuffer.
//     * For packed audio, there is just one data pointer, and linesize[0]
//     * contains the total size of the nativeBuffer for all channels.
//     *
//     * Note: Both data and extended_data should always be set in a valid frame,
//     * but for planar audio with more channels that can fit in data,
//     * extended_data must be used in order to access all channels.
//     */
//    public void extendedDataElem( int layer, long planePointer ) {
//        nExtendedDataElem( mPointer, layer, planePointer );
//    }
//
//    /**
//     * Size, in bytes, of the data for each picture/channel plane.
//     *
//     * For audio, only linesize[0] may be set. For planar audio, each channel
//     * plane must be the same size.
//     *
//     * - encoding: Set by user
//     * - decoding: set by AVCodecContext.get_buffer()
//     *
//     * @return pointer of type int[AV_NUM_DATA_POINTERS]
//     */
//    public long lineSize() {
//        return nLineSize( mPointer );
//    }
//
//    /**
//     * Size, in bytes, of the data for each picture/channel plane.
//     *
//     * For audio, only linesize[0] may be set. For planar audio, each channel
//     * plane must be the same size.
//     *
//     * - encoding: Set by user
//     * - decoding: set by AVCodecContext.get_buffer()
//     */
//    public int lineSize( int layer ) {
//        return nLineSize( mPointer, layer );
//    }
//
//
//    public void lineSize( int layer, int lineSize ) {
//        nLineSize( mPointer, layer, lineSize );
//    }
//
//
//    public void lineSizes( int[] out8x1 ) {
//        nLineSizes( mPointer, out8x1 );
//    }
//
//
//    public int width() {
//        return nWidth( mPointer );
//    }
//
//
//    public void width( int w ) {
//        nWidth( mPointer, w );
//    }
//
//
//    public int height() {
//        return nHeight( mPointer );
//    }
//
//
//    public void height( int h ) {
//        nHeight( mPointer, h );
//    }
//
//    /**
//     * format of the frame, -1 if unknown or unset
//     * Values correspond to:
//     * enum PixelFormat for video frames,
//     * enum AVSampleFormat for audio frames
//     * - encoding: unused
//     * - decoding: Read by user.
//     */
//    public int format() {
//        return nFormat( mPointer );
//    }
//
//
//    public void format( int format ) {
//        nFormat( mPointer, format );
//    }
//
//
//
//
//    protected static native long nAllocFrame();
//    private static native void   nFree( long pointer );
//    private static native void   nUnref( long pointer );
//
//    private static native long nData( long pointer );
//    private static native long nDataElem( long pointer, int layer );
//    private static native void nDataElem( long pointer, int layer, long dataPointer );
//    private static native void nDataElem( long pointer, long[] out8x1 );
//    private static native long nLineSize( long pointer );
//    private static native int  nLineSize( long pointer, int layer );
//    private static native void nLineSize( long pointer, int layer, int lineSize );
//    private static native void nLineSizes( long pointer, int[] out8x1 );
//    private static native long nExtendedData( long pointer );
//    private static native void nExtendedData( long pointer, long dataPointer );
//    private static native long nExtendedDataElem( long pointer, int layer );
//    private static native void nExtendedDataElem( long pointer, int layer, long dataPointer );
//
//    private static native int  nWidth( long pointer );
//    private static native void nWidth( long pointer, int width );
//    private static native int  nHeight( long pointer );
//    private static native void nHeight( long pointer, int width );
//    private static native int  nFormat( long pointer );
//    private static native void nFormat( long pointer, int format );
//}
