

public static int leTag( int a, int b, int c, int d ) {
    return (   a & 0xFF         ) |
           ( ( b & 0xFF ) <<  8 ) |
           ( ( c & 0xFF ) << 16 ) |
           ( ( d & 0xFF ) << 24 );
}


public static int beTag( int a, int b, int c, int d ) {
    return (   d & 0xFF         ) |
           ( ( c & 0xFF ) <<  8 ) |
           ( ( b & 0xFF ) << 16 ) |
           ( ( a & 0xFF ) << 24 );
}


public static int errTag( int a, int b, int c, int d ) {
    return -leTag( a, b, c, d );
}



private Jav() {}

private static boolean sInit = false;

private static native void nInit();

