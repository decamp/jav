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
