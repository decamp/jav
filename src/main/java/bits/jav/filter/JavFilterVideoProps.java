//package bits.jav.filter;
//
//import bits.jav.util.*;
//
//
///**
// * Video specific properties in a reference to an AVFilterBuffer. Since
// * AVFilterBufferRef is common to different media formats, video specific
// * per reference properties must be separated out.
// */
//public class JavFilterVideoProps implements NativeObject {
//
//
//    public static JavFilterVideoProps alloc() {
//        long p = nAlloc();
//        return p == 0 ? null : new JavFilterVideoProps( p, ReleaseMethod.SELF );
//    }
//
//
//    private long mPointer;
//    private ReleaseMethod mRelease;
//
//
//    JavFilterVideoProps( long pointer, ReleaseMethod release ) {
//        mPointer = pointer;
//        mRelease = release;
//    }
//
//
//    /**
//     * @return image width
//     */
//    public int w() {
//        return nW( mPointer );
//    }
//
//    /**
//     * @param w image width
//     */
//    public void w( int w ) {
//        nW( mPointer, w );
//    }
//
//    /**
//     * @return image height
//     */
//    public int h() {
//        return nH( mPointer );
//    }
//
//    /**
//     * @param h image height
//     */
//    public void h( int h ) {
//        nH( mPointer, h );
//    }
//
//    /**
//     * @return image sample aspect ratio
//     */
//    public Rational sampleAspectRatio() {
//        return Rational.fromNativeLong( nSampleAspectRatio( mPointer ));
//    }
//
//    /**
//     * @param aspect image sample aspect ratio
//     */
//    public void sampleAspectRatio( Rational aspect ) {
//        nSampleAspectRatio( Rational.toNativeLong( aspect ) );
//    }
//
//    /**
//     * @return is frame interlaced
//     */
//    public int interlaced() {
//        return nInterlaced( mPointer );
//    }
//
//    /**
//     * @param interlaced is frame interlaced
//     */
//    public void interlaced( int interlaced ) {
//        nInterlaced( mPointer, interlaced );
//    }
//
//    /**
//     * @return field order
//     */
//    public int topFieldFirst() {
//        return nTopFieldFirst( mPointer );
//    }
//
//    /**
//     * @param topFieldFirst field order
//     */
//    public void topFieldFirst( int topFieldFirst ) {
//        nTopFieldFirst( mPointer, topFieldFirst );
//    }
//
//    /**
//     * @return picture type of frame
//     */
//    public int pictType() {
//        return nInterlaced( mPointer );
//    }
//
//    /**
//     * @param type picture type of frame
//     */
//    public void pictType( int type ) {
//        nPictType( mPointer, type );
//    }
//
//    /**
//     * @return 1 -> keyframe, 0-> not
//     */
//    public int keyFrame() {
//        return nKeyFrame( mPointer );
//    }
//
//    /**
//     * @param keyFrame 1 -> keyframe, 0-> not
//     */
//    public void keyFrame( int keyFrame ) {
//        nKeyFrame( mPointer, keyFrame );
//    }
//
//    /**
//     * @return qp_table stride
//     */
//    public int qpTableLinesize() {
//        return nQpTableLinesize( mPointer );
//    }
//
//    /**
//     * @param linesize qp_table stride
//     */
//    public void qpTableLinesize( int linesize ) {
//        nQpTableLinesize( mPointer, linesize );
//    }
//
//    /**
//     * @return qp_table ize
//     */
//    public int qpTableSize() {
//        return nQpTableSize( mPointer );
//    }
//
//    /**
//     * @param size qp_table stride
//     */
//    public void qpTableSize( int size ) {
//        nQpTableSize( mPointer, size );
//    }
//
//    /**
//     * @return qp_table ize
//     */
//    public long qpTable() {
//        return nQpTable( mPointer );
//    }
//
//    @Override
//    public long pointer() {
//        return 0;
//    }
//
//    @Override
//    public ReleaseMethod releaseMethod() {
//        return mRelease;
//    }
//
//
//    public void releaseMethod( ReleaseMethod release ) {
//        mRelease = release;
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
//    private static native long nAlloc();
//    private static native void nFree( long pointer );
//
//    private static native int  nW( long pointer );
//    private static native void nW( long pointer, int w );
//    private static native int  nH( long pointer );
//    private static native void nH( long pointer, int h );
//    private static native long nSampleAspectRatio( long pointer );
//    private static native void nSampleAspectRatio( long pointer, long s );
//    private static native int  nInterlaced( long pointer );
//    private static native void nInterlaced( long pointer, int interlaced );
//    private static native int  nTopFieldFirst( long pointer );
//    private static native void nTopFieldFirst( long pointer, int top );
//    private static native int  nPictType( long pointer );
//    private static native void nPictType( long pointer, int type );
//    private static native int  nKeyFrame( long pointer );
//    private static native void nKeyFrame( long pointer, int keyFrame );
//    private static native int  nQpTableLinesize( long pointer );
//    private static native void nQpTableLinesize( long pointer, int linesize );
//    private static native int  nQpTableSize( long pointer );
//    private static native void nQpTableSize( long pointer, int size );
//    private static native long nQpTable( long pointer );
//    private static native void nQpTable( long pointer, long table );
//
//}