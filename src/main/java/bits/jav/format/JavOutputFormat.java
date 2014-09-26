/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import bits.jav.util.*;

/**
 * @author decamp
 */
public class JavOutputFormat implements NativeObject {
    
    
    public static JavOutputFormat nextOutputFormat( JavOutputFormat f ) {
        long p = nNextOutputFormat( f == null ? 0 : f.pointer() );
        return p == 0L ? null : new JavOutputFormat( p );
    }
    
    
    /**
     * Return the output format in the list of registered output formats
     * which best matches the provided parameters, or return NULL if
     * there is no match.
     *
     * @param shortName if non-NULL, checks if short_name matches with the
     * names of the registered formats
     * @param fileName if non-NULL, checks if filename terminates with the
     * extensions of the registered formats
     * @param mimeType if non-NULL, checks if mime_type matches with the
     * MIME type of the registered formats
     */
    public static JavOutputFormat guessFormat( String shortName,
                                               String fileName,
                                               String mimeType )
    {
        long p = nGuessFormat( shortName, fileName, mimeType );
        return p == 0L ? null : new JavOutputFormat( p );
    }
    
    
    
    private final Long mPointer;
    
    
    JavOutputFormat( long pointer ) {
        mPointer = pointer;
    }

    
    
    public String name() {
        return nName( mPointer );
    }
    
    
    public String longName() {
        return nLongName( mPointer );
    }
    
    
    public String mimeType() {
        return nMimeType( mPointer );
    }
    
    
    public String extensions() {
        return nExtensions( mPointer );
    }
    
    
    public int audioCodec() {
        return nAudioCodec( mPointer );
    }
    
    
    public int videoCodec() {
        return nVideoCodec( mPointer );
    }
    
    
    public int subtitleCodec() {
        return nSubtitleCodec( mPointer );
    }
        
    
    /**
     * can use flags: AVFMT_NOFILE, AVFMT_NEEDNUMBER, AVFMT_RAWPICTURE,
     * AVFMT_GLOBALHEADER, AVFMT_NOTIMESTAMPS, AVFMT_VARIABLE_FPS,
     * AVFMT_NODIMENSIONS, AVFMT_NOSTREAMS, AVFMT_ALLOW_FLUSH,
     * AVFMT_TS_NONSTRICT
     */
    public int flags() {
        return nFlags( mPointer );
    }
    
    
    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }
    
    
    private static native long nNextOutputFormat( long pointer );
    private static native long nGuessFormat( String shortName, String fileName, String mimeType );
   
    private static native String nName( long pointer );
    private static native String nLongName( long pointer );
    private static native String nMimeType( long pointer );
    private static native String nExtensions( long pointer );
    private static native int    nAudioCodec( long pointer );
    private static native int    nVideoCodec( long poniter );
    private static native int    nSubtitleCodec( long pointer );
    private static native int    nFlags( long pointer );
    
}
