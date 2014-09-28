/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.util.*;

import static bits.jav.Jav.*;


/**
 * Used for getting and setting options on JavClasses.
 */
public final class JavOption implements NativeObject {
    
    /**
     * @param object  Instance of JavClass object.
     * @return List of all options for that object.
     */
    public static List<JavOption> find( JavClass object ) {
        final List<JavOption> ret = new ArrayList<JavOption>();
        long optionPointer = 0;
        while( ( optionPointer = nNextOption( object.pointer(), optionPointer ) ) != 0 ) {
            ret.add( new JavOption( optionPointer ) );
        }
        return ret;
    }
    
    /**
     * Finds option with given name.
     * 
     * @param object    Object to get option for.
     * @param name      Name of option.
     * @return Matching JavOption if found, otherwise {@code null}.
     */
    public static JavOption find( JavClass object, String name ) {
        return find( object, name, null, 0, 0 );
    }

    /**
     * Finds option that matches description.
     * 
     * @param object       Object to get option for.
     * @param name         Name of option
     * @param unit         Unit of option. {@code null} for any.
     * @param optFlags     Bitwise union of some set of JavConstants.AV_OPT_FLAG_*
     *                     Option {@code opt} may be returne-d if {@code opt.flags() & optFlags == optFlags}.
     * @param searchFlags  Bitwise union of some set of JavConstants.AV_OPT_SEARCH_*
     *                     Specifies how to search. 
     * @return Matching JavOption if found, otherwise {@code null}.
     */
    public static JavOption find( JavClass object, String name, String unit, int optFlags, int searchFlags) {
        long p = nFindOption( object.pointer(), name, unit, optFlags, searchFlags );
        return p == 0 ? null : new JavOption( p );
    }
    
    /**
     * Retrieves next option for an given object, or first option if {@code nullOrOption} is null.
     *
     * @param object       Object to get options for.
     * @param nullOrOption Either the option, or {@code null} to get first option.
     * @return JavOption if found, otherwise {@code null}
     *
     */
    public static JavOption next( JavClass object, JavOption nullOrOption ) {
        long p = nNextOption( object.pointer(), nullOrOption == null ? 0L : nullOrOption.pointer() );
        return p == 0L ? null : new JavOption( p );
    }


    public static String getString( JavClass object, String name, int searchFlags ) {
        return nGetOptionString( object.pointer(), name, searchFlags );
    }


    public static int setString( JavClass object, String name, String value, int searchFlags ) {
        return nSetOptionString( object.pointer(), name, value, searchFlags );
    }


    public static long getLong( JavClass object, String name, int searchFlags ) {
        return nGetOptionLong( object.pointer(), name, searchFlags );
    }


    public static int setLong( JavClass object, String name, long value, int searchFlags ) {
        return nSetOptionLong( object.pointer(), name, value, searchFlags );
    }


    public static double getDouble( JavClass object, String name, int searchFlags ) {
        return nGetOptionDouble( object.pointer(), name, searchFlags );
    }


    public static int setDouble( JavClass object, String name, double value, int searchFlags ) {
        return nSetOptionDouble( object.pointer(), name, value, searchFlags );
    }


    public static Rational getRational( JavClass object, String name, int searchFlags ) {
        return Rational.fromNativeLong( nGetOptionRational( object.pointer(), name, searchFlags ) );
    }


    public static int setRational( JavClass object, String name, int num, int den, int searchFlags ) {
        return nSetOptionRational( object.pointer(), name, num, den, searchFlags );
    }


    public static int[] getImageSize( JavClass object, String name, int searchFlags ) {
        long n = nGetOptionImageSize( object.pointer(), name, searchFlags );
        return new int[]{ (int)( n >>> 32 ), (int)n };
    }


    public static int setImageSize( JavClass object, String name, int w, int h, int searchFlags ) {
        return nSetOptionImageSize( object.pointer(), name, w, h, searchFlags );
    }


    public static int getPixelFormat( JavClass object, String name, int searchFlags ) {
        return nGetOptionPixelFormat( object.pointer(), name, searchFlags );
    }


    public static int setPixelFormat( JavClass object, String name, int value, int searchFlags ) {
        return nSetOptionPixelFormat( object.pointer(), name, value, searchFlags );
    }


    public static int getSampleFormat( JavClass object, String name, int searchFlags ) {
        return nGetOptionSampleFormat( object.pointer(), name, searchFlags );
    }


    public static int setSampleFormat( JavClass object, String name, int val, int searchFlags ) {
        return nSetOptionSampleFormat( object.pointer(), name, val, searchFlags );
    }


    public static Rational getVideoRate( JavClass object, String name, int searchFlags ) {
        return Rational.fromNativeLong( nGetOptionVideoRate( object.pointer(), name, searchFlags ) );
    }


    public static int setVideoRate( JavClass object, String name, int num, int den, int searchFlags ) {
        return nSetOptionVideoRate( object.pointer(), name, num, den, searchFlags );
    }


    public static long getChannelLayout( JavClass object, String name, int searchFlags ) {
        return nGetOptionChannelLayout( object.pointer(), name, searchFlags );
    }


    public static int setChannelLayout( JavClass object, String name, long val, int searchFlags ) {
        return nSetOptionChannelLayout( object.pointer(), name, val, searchFlags );
    }



    private long mPointer = 0;

    private String mName    = null;
    private String mHelp    = null;
    private String mUnit    = null;
    
    
    private JavOption( long pointer ) {
        mPointer = pointer;
    }



    public String name() {
        if( mName != null )
            return mName;

        String s = nName( mPointer );
        mName = s == null ? "" : s;
        return mName;
    }


    public String help() {
        if( mHelp != null )
            return mHelp;

        String s = nHelp( mPointer );
        mHelp = s == null ? "" : s;
        return mHelp;
    }


    public int type() {
        return nType( mPointer );
    }


    public String typeName() {
        return typeToName( type() );
    }
    
    
    public String defaultString() {
        return nDefaultString( mPointer );
    }


    public long defaultLong() {
        return nDefaultLong( mPointer );
    }


    public double defaultDouble() {
        return nDefaultDouble( mPointer );
    }


    public Rational defaultRational() {
        return Rational.fromNativeLong( nDefaultRational( mPointer ) );
    }

    
    public double minValue() {
        return nMinValue( mPointer );
    }


    public double maxValue() {
        return nMaxValue( mPointer );
    }


    public int flags() {
        return nFlags( mPointer );
    }


    public String unit() {
        if( mUnit != null )
            return mUnit;

        String s = nUnit( mPointer );
        mUnit = s == null ? "" : s;
        return mUnit;
    }
    
    
    public long pointer() {
        return mPointer;
    }
    
    
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }
    
    
    //public void setReleaseMethod( ReleaseMethod rel ) {}
    
    
    public String toString() {
        return String.format( "Option: %s [%s]", name(), typeName() );
    }


    public String toDescription() {
        int type        = type();
        StringBuilder s = new StringBuilder();
        s.append( String.format( "Option: %s\n%s\n  type: %s\n  def val: ",
                                 name(),
                                 help(),
                                 typeToName( type ) ) );
                               
        String seg = "";
        
        switch( type ) {
        case AV_OPT_TYPE_FLAGS:
        case AV_OPT_TYPE_INT:
            seg = String.format( "0x%08X", defaultLong() );
            break;
        case AV_OPT_TYPE_INT64:
            seg = String.format( "0x%016X", defaultLong() );
            break;
        case AV_OPT_TYPE_DOUBLE:
        case AV_OPT_TYPE_FLOAT:
            seg = String.format( "%f", defaultDouble() );
            break;
        case AV_OPT_TYPE_STRING:
            seg = defaultString();
            break;
        case AV_OPT_TYPE_RATIONAL:
            seg = defaultRational().toString();
            break;
        default:
            seg = "?";
        }
        
        s.append( seg ).append( "\n" );
        s.append( String.format( "  min val: %f\n  max val: %f\n  flags: 0x%08X\n  unit: %s",
                                 minValue(),
                                 maxValue(),
                                 flags(),
                                 unit() ) );
        
        return s.toString();
    }



    private static String typeToName( int type ) {
        switch( type ) {
        case AV_OPT_TYPE_FLAGS:
            return "FLAGS";
        case AV_OPT_TYPE_INT:
            return "INT";
        case AV_OPT_TYPE_INT64:
            return "INT64";
        case AV_OPT_TYPE_DOUBLE:
            return "DOUBLE";
        case AV_OPT_TYPE_FLOAT:
            return "FLOAT";
        case AV_OPT_TYPE_STRING:
            return "STRING";
        case AV_OPT_TYPE_RATIONAL:
            return "RATIONAL";
        case AV_OPT_TYPE_BINARY:
            return "BINARY";
        case AV_OPT_TYPE_CONST:
            return "CONST";
        case AV_OPT_TYPE_IMAGE_SIZE:
            return "IMAGE_SIZE";
        case AV_OPT_TYPE_PIXEL_FMT:
            return "PIXEL_FMT";
        case AV_OPT_TYPE_CHANNEL_LAYOUT:
            return "CHANNEL_LAYOUT";
        case AV_OPT_TYPE_SAMPLE_FMT:
            return "SAMPLE FORMAT";
        default:
            return "invalid";
        }
    }



    private static native long   nFindOption(      long objectPointer, String name, String unit, int mask, int flags );
    private static native long   nNextOption(      long objectPointer, long optionPointer );
    private static native String nName(            long pointer );
    private static native String nHelp(            long pointer );
    private static native int    nType(            long pointer );
    private static native double nMinValue(        long pointer );
    private static native double nMaxValue(        long pointer );
    private static native String nDefaultString(   long pointer );
    private static native long   nDefaultLong(     long pointer );
    private static native double nDefaultDouble(   long pointer );
    private static native long   nDefaultRational( long pointer );
    private static native int    nFlags(           long pointer );
    private static native String nUnit(            long pointer );

    private static native String nGetOptionString        ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionString        ( long objectPointer, String name, String val, int searchFlags );
    private static native double nGetOptionDouble        ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionDouble        ( long objectPointer, String name, double val, int searchFlags );
    private static native long   nGetOptionLong          ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionLong          ( long objectPointer, String name, long val, int searchFlags );
    private static native long   nGetOptionRational      ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionRational      ( long objectPointer, String name, int num, int den, int searchFlags );
    private static native int    nGetOptionSampleFormat  ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionSampleFormat  ( long objectPointer, String name, int val, int searchFlags );
    private static native long   nGetOptionChannelLayout ( long objectPointer, String name, int searchFlags );
    private static native int    nSetOptionChannelLayout ( long objectPointer, String name, long val, int searchFlags );
    private static native int    nGetOptionPixelFormat   ( long objectPointer, String name, int flags );
    private static native int    nSetOptionPixelFormat   ( long objectPointer, String name, int value, int flags );
    private static native long   nGetOptionImageSize     ( long objectPointer, String name, int flags );
    private static native int    nSetOptionImageSize     ( long objectPointer, String name, int w, int h, int flags );
    private static native long   nGetOptionVideoRate     ( long objectPointer, String name, int flags );
    private static native int    nSetOptionVideoRate     ( long objectPointer, String name, int num, int den, int flags );


    /**
     * @param object  Instance of JavClass object.
     * @return List of all options for that object.
     */
    @Deprecated public static List<JavOption> findOptions( JavClass object ) {
        final List<JavOption> ret = new ArrayList<JavOption>();
        long optionPointer = 0;

        while( ( optionPointer = nNextOption( object.pointer(), optionPointer ) ) != 0 ) {
            ret.add( new JavOption( optionPointer ) );
        }

        return ret;
    }

    /**
     * Finds option with given name.
     *
     * @param object    Object to get option for.
     * @param name      Name of option.
     * @return Matching JavOption if found, otherwise {@code null}.
     */
    @Deprecated public static JavOption findOption( JavClass object, String name ) {
        return findOption( object, name, null, 0, 0 );
    }

    /**
     * Finds option that matches description.
     *
     * @param object       Object to get option for.
     * @param name         Name of option
     * @param unit         Unit of option. {@code null} for any.
     * @param optFlags     Bitwise union of some set of JavConstants.AV_OPT_FLAG_*
     *                     Option {@code opt} may be returned if {@code opt.flags() & optFlags == optFlags}.
     * @param searchFlags  Bitwise union of some set of JavConstants.AV_OPT_SEARCH_*
     *                     Specifies how to search.
     * @return Matching JavOption if found, otherwise {@code null}.
     */
    @Deprecated public static JavOption findOption( JavClass object, String name, String unit, int optFlags, int searchFlags) {
        long p = nFindOption( object.pointer(), name, unit, optFlags, searchFlags );

        if( p == 0 )
            return null;

        return new JavOption( p );
    }

    /**
     *
     */
    @Deprecated public static JavOption nextOption( JavClass object, JavOption option ) {
        long p = nNextOption( object.pointer(), option == null ? 0L : option.pointer() );
        return p == 0L ? null : new JavOption( p );
    }


    @Deprecated public static boolean setOptionString( JavClass object, String name, String value, int searchFlags ) {
        return nSetOptionString( object.pointer(), name, value, searchFlags ) == 0;
    }


    @Deprecated public static boolean setOptionLong( JavClass object, String name, long value, int searchFlags ) {
        return nSetOptionLong( object.pointer(), name, value, searchFlags ) == 0;
    }


    @Deprecated public static boolean setOptionDouble( JavClass object, String name, double value, int searchFlags ) {
        return nSetOptionDouble( object.pointer(), name, value, searchFlags ) == 0;
    }


    @Deprecated public static boolean setOptionRational( JavClass object, String name, int num, int den, int searchFlags ) {
        return nSetOptionRational( object.pointer(), name, num, den, searchFlags ) == 0;
    }


    @Deprecated public static String getOptionString( JavClass object, String name, int searchFlags ) {
        return nGetOptionString( object.pointer(), name, searchFlags );
    }


    @Deprecated public static long getOptionLong( JavClass object, String name, int searchFlags ) {
        return nGetOptionLong( object.pointer(), name, searchFlags );
    }


    @Deprecated public static double getOptionDouble( JavClass object, String name, int searchFlags ) {
        return nGetOptionDouble( object.pointer(), name, searchFlags );
    }


    @Deprecated public static Rational getOptionRational( JavClass object, String name, int searchFlags ) {
        return Rational.fromNativeLong( nGetOptionRational( object.pointer(), name, searchFlags ) );
    }


}
