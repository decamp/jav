//package bits.jav.filter;
//
//import bits.jav.util.*;
//
//
///**
// * A link between two filters. This contains pointers to the source and
// * destination filters between which this link exists, and the indexes of
// * the pads involved. In addition, this link also contains the parameters
// * which have been negotiated and agreed upon between the filter, such as
// * image dimensions, format, etc.
// */
//public class JavFilterLink implements NativeObject {
//
//    public static int link( JavFilterContext src, int srcPad, JavFilterContext dst, int dstPad ) {
//        return nLink( src.pointer(), srcPad, dst.pointer(), dstPad );
//    }
//
//
//    private long mPointer;
//    private ReleaseMethod mRelease;
//
//    JavFilterLink( long pointer, ReleaseMethod release ) {
//        mPointer = pointer;
//        mRelease = release;
//    }
//
//
//    /**
//     * @return source filter
//     */
//    public JavFilterContext src() {
//        long p = nSrc( mPointer );
//        return p == 0L ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * @return output pad on the source filter
//     */
//    public JavFilterPad srcPad() {
//        long p = nSrcPad( mPointer );
//        return p == 0L ? null : new JavFilterPad( p );
//    }
//
//    /**
//     * @return dest filter
//     */
//    public JavFilterContext dst() {
//        long p = nDst( mPointer );
//        return p == 0L ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * @return input pad on the dest filter
//     */
//    public JavFilterPad dstPad() {
//        long p = nDstPad( mPointer );
//        return p == 0L ? null : new JavFilterPad( p );
//    }
//
//    /**
//     * @return filter media type from Jav.AVMEDIA_TYPE_*
//     */
//    public int type() {
//        return nType( mPointer );
//    }
//
//    /**
//     * @return agreed upon image width (video only)
//     */
//    public int w() {
//        return nW( mPointer );
//    }
//
//    /**
//     * @return agreed upon image height (video only)
//     */
//    public int h() {
//        return nH( mPointer );
//    }
//
//    /**
//     * @return agreed upon sample aspect ratio (video only)
//     */
//    public Rational sampleAspectRatio() {
//        return Rational.fromNativeLong( nSampleAspectRatio( mPointer ) );
//    }
//
//    /**
//     * @return channel layout of current buffer
//     * @see bits.jav.util.JavChannelLayout
//     */
//    public long channelLayout() {
//        return nChannelLayout( mPointer );
//    }
//
//    /**
//     * @return samples per second
//     */
//    public int sampleRate() {
//        return nSampleRate( mPointer );
//    }
//
//    /**
//     * @return media format
//     */
//    public int format() {
//        return nFormat( mPointer );
//    }
//
//    /**
//     * Define the time base used by the PTS of the frames/samples
//     * which will pass through this link.
//     * During the configuration stage, each filter is supposed to
//     * change only the output timebase, while the timebase of the
//     * input link is assumed to be an unchangeable property.
//     */
//    public Rational timeBase() {
//        return Rational.fromNativeLong( nTimeBase( mPointer ) );
//    }
//
//    /**
//     * Get the number of channels of a link.
//     */
//    public int getChannnels() {
//        return nGetChannels( mPointer );
//    }
//
//    /**
//     * Set the closed field of a link.
//     */
//    public void setClosed( int closed ) {
//        nSetClosed( mPointer, closed );
//    }
//
//
//    @Override
//    public long pointer() {
//        return mPointer;
//    }
//
//    @Override
//    public ReleaseMethod releaseMethod() {
//        return mRelease;
//    }
//
//
//    public void release() {
//        long p = mPointer;
//        mPointer = 0;
//        if( mRelease != ReleaseMethod.OWNER && p != 0L ) {
//            nFree( p );
//        }
//    }
//
//    @Override
//    protected void finalize() throws Throwable {
//        release();
//        super.finalize();
//    }
//
//
//
//    //** Native methods **//
//
//    public static native int  nLink( long srcPtr, int srcPad, long dstPtr, int dstPad );
//
//    public static native void nFree( long pointer );
//
//    public static native long nSrc( long pointer );
//    public static native long nSrcPad( long pointer );
//    public static native long nDst( long pointer );
//    public static native long nDstPad( long pointer );
//    public static native int  nType( long pointer );
//    public static native int  nW( long pointer );
//    public static native int  nH( long pointer );
//    public static native long nSampleAspectRatio( long pointer );
//    public static native long nChannelLayout( long pointer );
//    public static native int  nSampleRate( long pointer );
//    public static native int  nFormat( long pointer );
//    public static native long nTimeBase( long pointer );
//
//    public static native int  nGetChannels( long pointer );
//    public static native void nSetClosed( long pointer, int closed );
//
//}
