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
 * It will set up appropriate byte ordering and
 * necessary padding.
 */
public static ByteBuffer allocBuffer( int size ) {
    ByteBuffer ret = ByteBuffer.allocateDirect( size + FF_INPUT_BUFFER_PADDING_SIZE );
    return ret.order( ByteOrder.nativeOrder() );
}

