/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

/**
 * @author decamp
 */
public class JavDict implements NativeObject {
    
    public static final int MATCH_CASE       =  1;
    public static final int IGNORE_SUFFIX    =  2;
    public static final int DONT_STRDUP_KEY  =  4; // HEY YOU! Don't use this flag unless you're goin to mess with native code.
                                                   // By default, the dictionary will duplicate keys and handle deallocing.
                                                   // // Take ownership of a key that's been 
                                                   // // allocated with av_malloc() and children.
    public static final int DONT_STRDUP_VAL  =  8; // HEY YOU! Don't use this flag unless you're going to mess with native code.
                                                   // By default, the dictionary will duplicate values and handle deallocing.
                                                   // // Take ownership of a value that's been
                                                   // // allocated with av_malloc() and children.
    public static final int DONT_OVERWRITE   = 16; // Don't overwrite existing entries.
    public static final int APPEND           = 32;

    
    private final long[] mOut = { 0L };
    private long mPointer;
    private ReleaseMethod mRelease = ReleaseMethod.SELF; 
    
    
    public JavDict() {
        mPointer = 0;
    }
    
    
    public JavDict( long pointer ) {
        mPointer = pointer;
    }
    
    
    /**
     * Get a dictionary entry with matching key. To find any entry:
     * {@code dict.get( "", null, JavDict.IGNORE_SUFFIX )}.
     *
     * @param key     Name of key to find. 
     * @param prev   Set to the previous matching element to find the next.
     *               If set to NULL the first matching element is returned.
     * @param flags  Allows case as well as suffix-insensitive comparisons.
     * @return Found entry or NULL, changing key or value leads to undefined behavior.
     */
    public Entry get( String key, Entry prev, int flags ) {
        long n = nGet( mPointer, key, prev == null ? 0L : prev.pointer(), flags );
        return n == 0L ? null : new Entry( n );
    }
    
    /**
     * @return number of entries in dictionary
     */
    public int count() {
        return nCount( mPointer );
    }
    
    /**
     * Set the given entry, overwriting an existing entry.
     *
     * @param key entry key to add to *pm (will be av_strduped depending on flags)
     * @param value entry value to add to *pm (will be av_strduped depending on flags).
     *        Passing a NULL value will cause an existing entry to be deleted.
     * @return >= 0 on success otherwise an error code <0
     */
    public int set( String key, String value, int flags ) {
        int ret = nSet( mPointer, key, value, flags, mOut );
        mPointer = mOut[0];
        return ret;
    }
    
    /**
     * Copy entries from one AVDictionary struct into another.
     * @param src pointer to source AVDictionary struct
     * @param flags flags to use when setting entries in *dst
     * NOTE: metadata is read using the AV_DICT_IGNORE_SUFFIX flag
     */
    public void copy( JavDict src, int flags ) {
        mPointer = nCopy( mPointer, src.pointer(), flags );
    }
    
    /**
     * Free all the memory allocated for an AVDictionary struct
     * and all keys and values.
     */
    public void free() {
        long p = mPointer;
        mPointer = 0;
        if( p == 0 || mRelease != ReleaseMethod.SELF ) {
            return;
        }
        nFree( p );
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
    
    
    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }
    
    
    public static final class Entry implements NativeObject {

        private final long mPointer;
        
        
        Entry( long pointer ) {
            mPointer = pointer;
        }
        
        
        public String key() {
            return nKey( mPointer );
        }
        
        public String value() {
            return nValue( mPointer );
        }
        
        public long pointer() {
            return mPointer;
        }
        
        public ReleaseMethod releaseMethod() {
            return ReleaseMethod.OWNER;
        }
        
    }
    
    
    private static native long nGet( long pointer, String key, long prevPointer, int flags );
    private static native int  nCount( long pointer );
    private static native int  nSet( long pointer, String key, String value, int flags, long[] outPointer );
    private static native long nCopy( long pointer, long srcPointer, int flags );
    private static native void nFree( long pointer );

    private static native String nKey( long pointer );
    private static native String nValue( long pointer );

}
