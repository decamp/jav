/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import java.io.IOException;


/**
 * @author decamp
 */
public class JavException extends IOException {


    public static JavException fromErr( int err ) {
        if( err == Jav.AVERROR_NONE ) {
            return null;
        }

        String str = errStr( err );
        if( str == null || str.length() == 0 ) {
            str = "Unknown";
        }
        str = String.format( "Err 0x%08X: %s", err, str );
        return new JavException( err, str, null );
    }
    
    
    private final int mErrorCode;
    
    
    public JavException( String msg ) {
        this( Jav.AVERROR_UNKNOWN, msg, null );
    }


    public JavException( int code ) {
        this( code, errStr( code ), null );
    }


    public JavException( int code, String msg ) {
        this( code, msg, null );
    }


    public JavException( int code, String msg, Throwable t ) {
        super( msg, t );
        mErrorCode = code;
    }



    public int errorCode() {
        return mErrorCode;
    }

    
    public static native String errStr( int err );



    @Deprecated public static void assertNoErr( int err ) throws JavException {
        if( err >= Jav.AVERROR_NONE ) {
            return;
        }

        throw fromErr( err );
    }



}
