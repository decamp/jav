//package bits.jav.filter;
//
//import bits.jav.util.NativeObject;
//import bits.jav.util.ReleaseMethod;
//
//
///**
// * A linked-list of the inputs/outputs of the filter chain.
// *
// * This is mainly useful for avfilter_graph_parse() / avfilter_graph_parse2(),
// * where it is used to communicate open (unlinked) inputs and outputs from and
// * to the caller.
// * This struct specifies, per each not connected pad contained in the graph, the
// * filter context and the pad index required for establishing a link.
// */
//public class JavFilterInOut implements NativeObject {
//
//    /**
//     * Allocate a single AVFilterInOut entry.
//     * Should be freed with {@code release() }
//     * @return allocated AVFilterInOut on success, NULL on failure.
//     */
//    public JavFilterInOut alloc() {
//        long p = nAlloc();
//        return p == 0 ? null : new JavFilterInOut( p, ReleaseMethod.SELF );
//    }
//
//
//    private long mPointer;
//    private ReleaseMethod mRelease;
//
//
//    JavFilterInOut( long pointer, ReleaseMethod release ) {
//        mPointer = pointer;
//        mRelease = release;
//    }
//
//
//    /**
//     * @return unique name for this input/output in the list
//     */
//    public String name() {
//        return nName( mPointer );
//    }
//
//    /**
//     * @param name unique name for this input/output in the list
//     */
//    public void name( String name ) {
//        nName( mPointer, name );
//    }
//
//    /**
//     * @return filter context associated to this input/output
//     */
//    public JavFilterContext filterCtx() {
//        long p = nFilterCtx( mPointer );
//        return p == 0 ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * @param filter context associated to this input/output
//     */
//    public void filterCtx( JavFilterContext filter ) {
//        nFilterCtx( mPointer, filter == null ? 0 : filter.pointer() );
//    }
//
//    /**
//     * @return index of the filt_ctx pad to use for linking
//     **/
//    public int padIdx() {
//        return nPadIdx( mPointer );
//    }
//
//    /**
//     * @param index of the filt_ctx pad to use for linking
//     **/
//    public void padIdx( int index ) {
//        nPadIdx( mPointer, index );
//    }
//
//    /**
//     * @return next input/input in the list, NULL if this is the last
//     **/
//    public JavFilterInOut next() {
//        long p = nNext( mPointer );
//        return p == 0 ? null : new JavFilterInOut( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * @param next input/input in the list, NULL if this is the last
//     **/
//    public void next( JavFilterInOut next ) {
//        nNext( mPointer, next == null ? 0 : next.pointer() );
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
//        return ReleaseMethod.OWNER;
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
//    //** NATIVE METHODS **//
//
//    private static native long nAlloc();
//    private static native void nFree( long pointer );
//
//    private static native String nName( long pointer );
//    private static native void   nName( long pointer, String name );
//    private static native long   nFilterCtx( long pointer );
//    private static native void   nFilterCtx( long pointer, long filter );
//    private static native int    nPadIdx( long pointer );
//    private static native void   nPadIdx( long pointer, int pad );
//    private static native long   nNext( long pointer );
//    private static native void   nNext( long pointer, long next );
//
//}
//
