//package bits.jav.filter;
//
//import bits.jav.util.*;
//
//
///**
// * An instance of a filter
// **/
//public class JavFilterContext implements JavClass {
//
//    private long mPointer;
//    private ReleaseMethod mRelease;
//
//
//    JavFilterContext( long pointer, ReleaseMethod release ) {
//        mPointer = pointer;
//        mRelease = release;
//    }
//
//
//    /**
//     * @return the AVFilter of which this is an instance
//     */
//    public JavFilter filter() {
//        long p = nFilter( mPointer );
//        return p == 0 ? null : new JavFilter( p );
//    }
//
//    /**
//     * @return name of this filter instance
//     */
//    public String name() {
//        return nName( mPointer );
//    }
//
//
//    public int nbInputs() {
//        return nNbInputs( mPointer );
//    }
//
//
//    public JavFilterPad inputPad( int idx ) {
//        long p = nInputPad( mPointer, idx );
//        return p == 0 ? null : new JavFilterPad( p );
//    }
//
//
//    public JavFilterLink inputLink( int idx ) {
//        long p = nInputLink( mPointer, idx );
//        return p == 0 ? null : new JavFilterLink( p, ReleaseMethod.OWNER );
//    }
//
//
//    public int nbOutputs() {
//        return nNbOutputs( mPointer );
//    }
//
//
//    public JavFilterPad outputPad( int idx ) {
//        long p = nOutputPad( mPointer, idx );
//        return p == 0 ? null : new JavFilterPad( p );
//    }
//
//
//    public JavFilterLink outputLink( int idx ) {
//        long p = nOutputLink( mPointer, idx );
//        return p == 0 ? null : new JavFilterLink( p, ReleaseMethod.OWNER );
//    }
//
//
//    public long privData() {
//        return nPrivData( mPointer );
//    }
//
//
//    public JavFilterGraph graph() {
//        long p = nGraph( mPointer );
//        return p == 0 ? null : new JavFilterGraph( p );
//    }
//
//    /**
//     * Type of multithreading being allowed/used. A combination of
//     * AVFILTER_THREAD_* flags.
//     *
//     * May be set by the caller before initializing the filter to forbid some
//     * or all kinds of multithreading for this filter. The default is allowing
//     * everything.
//     *
//     * When the filter is initialized, this field is combined using bit AND with
//     * AVFilterGraph.thread_type to get the final mask used for determining
//     * allowed threading types. I.e. a threading type needs to be set in both
//     * to be allowed.
//     *
//     * After the filter is initialized, libavfilter sets this field to the
//     * threading type that is actually used (0 for no multithreading).
//     */
//    public int threadType() {
//        return nThreadType( mPointer );
//    }
//
//
//    public void threadType( int threadType ) {
//        nThreadType( mPointer, threadType );
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
//    public void releaseMethod( ReleaseMethod r ) {
//        mRelease = r;
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
//    //struct AVFilterCommand *command_queue;
//    //char *enable_str;               ///< enable expression string
//    //void *enable;                   ///< parsed expression (AVExpr*)
//    //double *var_values;             ///< variable values for the enable expression
//    //int is_disabled;                ///< the enabled state from the last expression evaluation
//
//
//    //** NATIVES **//
//
//    private static native void   nFree( long pointer );
//
//    private static native long   nFilter( long pointer );
//    private static native String nName( long pointer );
//    private static native int    nNbInputs( long pointer );
//    private static native long   nInputPad( long pointer, int index );
//    private static native long   nInputLink( long pointer, int index );
//    private static native int    nNbOutputs( long pointer );
//    private static native long   nOutputPad( long pointer, int index );
//    private static native long   nOutputLink( long pointer, int index );
//    private static native long   nPrivData( long pointer );
//    private static native long   nGraph( long pointer );
//    private static native int    nThreadType( long pointer );
//    private static native void   nThreadType( long pointer, int threadType );
//
//
//
//};
//
