/**
 * Call this function before using Jav.
 */
public static synchronized void init() {
    if( sInit ) {
        return;
    }
    sInit = true;
    System.loadLibrary( "jav" );
    nInit();
}

/**
 * Checks if return code {@code err} indicates an error, and if so,
 * throws that error as a JavException.
 */
public static void assertOkay( int err ) throws JavException {
    if( err < 0 ) {
        throw JavException.fromErr( err );
    }
}

/**
 * Use this method to allocate ByteBuffers.
 * It will set up appropriate byte ordering.
 */
public static ByteBuffer alloc( int size ) {
    return ByteBuffer.allocateDirect( size ).order( ByteOrder.nativeOrder() );
}

/**
 * Like alloc(), but ensures buffer has appropriate padding.
 */
public static ByteBuffer allocEncodingBuffer( int size ) {
    return alloc( encodingBufferSize( size ) );
}

/**
 * @param useableSize Minimum size needed for encoding buffer.
 * @return actual size of buffer including padding and min buffer size constraints.
 */
public static int encodingBufferSize( int useableSize ) {
    return Math.max( useableSize + FF_INPUT_BUFFER_PADDING_SIZE, FF_MIN_BUFFER_SIZE );
}
