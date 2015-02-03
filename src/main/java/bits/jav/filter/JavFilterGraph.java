//package bits.jav.filter;
//
//import bits.jav.util.JavClass;
//import bits.jav.util.ReleaseMethod;
//
//
///**
// * @author Philip DeCamp
// */
//public class JavFilterGraph implements JavClass {
//
//
//    public static JavFilterGraph alloc() {
//        long t = nAlloc();
//        return new JavFilterGraph( t );
//    }
//
//
//    private long mPointer;
//    private ReleaseMethod mRelease = ReleaseMethod.SELF;
//
//
//    JavFilterGraph( long pointer ) {
//        mPointer = pointer;
//    }
//
//
//    /**
//     * Free a graph and destroy its links.
//     */
//    public void release() {
//        long p = mPointer;
//        mPointer = 0;
//        if( mRelease == ReleaseMethod.SELF ) {
//            nFree( p );
//        }
//    }
//
//
//    public int nbFilters() {
//        return nNbFilters( mPointer );
//    }
//
//
//    public JavFilterContext filter( int index ) {
//        long p = nFilter( mPointer, index );
//        return p == 0 ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//
//    public String scaleSwsOpts() {
//        return nScaleSwsOpts( mPointer );
//    }
//
//
//    public String resampleLavrOpts() {
//        return nResampleLavrOpts( mPointer );
//    }
//
//    /**
//     * Type of multithreading allowed for filters in this graph. A combination
//     * of AVFILTER_THREAD_* flags.
//     *
//     * May be set by the caller at any point, the setting will apply to all
//     * filters initialized after that. The default is allowing everything.
//     *
//     * When a filter in this graph is initialized, this field is combined using
//     * bit AND with AVFilterContext.thread_type to get the final mask used for
//     * determining allowed threading types. I.e. a threading type needs to be
//     * set in both to be allowed.
//     */
//    public int threadType( int threadType ) {
//        return nThreadType( mPointer );
//    }
//
//    /**
//     * Maximum number of threads used by filters in this graph. May be set by
//     * the caller before adding any filters to the filtergraph. Zero (the
//     * default) means that the number of threads is determined automatically.
//     */
//    public int nbThreads() {
//        return nNbThreads( mPointer );
//    }
//
//    /**
//     * Opaque user data. May be set by the caller to an arbitrary value, e.g. to
//     * be used from callbacks like @ref AVFilterGraph.execute.
//     * Libavfilter will not touch this field in any way.
//     */
//    public long opaque() {
//        return nOpaque( mPointer );
//    }
//
//
//    public void opaque( long opaque ) {
//        nOpaque( mPointer, opaque );
//    }
//
//    /**
//     * Create a new filter instance in a filter graph.
//     *
//     * @param filter the filter to create an instance of
//     * @param name Name to give to the new instance (will be copied to
//     *             AVFilterContext.name). This may be used by the caller to identify
//     *             different filters, libavfilter itself assigns no semantics to
//     *             this parameter. May be NULL.
//     *
//     * @return the context of the newly created filter instance (note that it is
//     *         also retrievable directly through AVFilterGraph.filters or with
//     *         avfilter_graph_get_filter()) on success or NULL on failure.
//     */
//    public JavFilterContext allocFilter( JavFilter filter, String name ) {
//        long p = nAllocFilter( mPointer, filter.pointer(), name );
//        return p == 0 ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * Get a filter instance identified by instance name from graph.
//     *
//     * @param name filter instance name (should be unique in the graph).
//     * @return the pointer to the found filter instance or NULL if it
//     * cannot be found.
//     */
//    public JavFilterContext getFilter( String name ) {
//        long p = nGetFilter( mPointer, name );
//        return p == 0 ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * Enable or disable automatic format conversion inside the graph.
//     *
//     * Note that format conversion can still happen inside explicitly inserted
//     * scale and aresample filters.
//     *
//     * @param flags  any of the AVFILTER_AUTO_CONVERT_* constants
//     */
//    void setAutoConvert( int flags ) {
//        nSetAutoConvert( mPointer, flags );
//    }
//
//    /**
//     * Check validity and configure all the links and formats in the graph.
//     *
//     * @return >= 0 in case of success, a negative AVERROR code otherwise
//     */
//    public int config() {
//        return nConfig( mPointer, 0 );
//    }
//
//    /**
//     * Create and add a filter instance into an existing graph.
//     * The filter instance is created from the filter filt and inited
//     * with the parameters args and opaque.
//     *
//     * In case of success put in *filt_ctx the pointer to the created
//     * filter instance, otherwise set *filt_ctx to NULL.
//     *
//     * @param filter Type of filter to create.
//     * @param name the instance name to give to the created filter instance
//     * @param args filter init arguments
//     * @param opaque Whatever you want. Try 0L.
//     * @param outErr Will hold error code on return. May be null.
//     * @return newly created JavFilterContext.
//     */
//    public JavFilterContext createFilter( JavFilter filter,
//                                          String name,
//                                          String args,
//                                          long opaque,
//                                          int[] outErr )
//    {
//        long p = nCreateFilter( mPointer, filter.pointer(), name, args, opaque, outErr );
//        return p == 0 ? null : new JavFilterContext( p, ReleaseMethod.OWNER );
//    }
//
//    /**
//     * Add a graph described by a string to a graph.
//     *
//     * @param filters       string to be parsed
//     * @param outInsAndOuts [out] Length-2 array to recieve two linked-lists: <br>
//     *                      outInsAndOuts[0] will hold linked list of all free graph inputs; <br>
//     *                      outInsAndOuts[1] will hold linked list of al free graph outputs
//     *
//     *
//     * @return zero on success, a negative AVERROR code on error
//     *
//     * @note This function returns the inputs and outputs that are left
//     * unlinked after parsing the graph and the caller then deals with
//     * them.
//     * @note This function makes no reference whatsoever to already
//     * existing parts of the graph and the inputs parameter will on return
//     * contain inputs of the newly parsed part of the graph.  Analogously
//     * the outputs parameter will contain outputs of the newly created
//     * filters.
//     */
//    public int parse2( String filters, JavFilterInOut[] outInsAndOuts ) {
//        long[] ptr = { 0, 0 };
//        int ret = nParse2( mPointer, filters, ptr );
//        outInsAndOuts[0] = ptr[0] == 0 ? null : new JavFilterInOut( ptr[0], ReleaseMethod.SELF );
//        outInsAndOuts[0] = ptr[0] == 0 ? null : new JavFilterInOut( ptr[1], ReleaseMethod.SELF );
//        return ret;
//    }
//
//    /**
//     * Send a command to one or more filter instances.
//     *
//     * @param target the filter(s) to which the command should be sent
//     *               "all" sends to all filters
//     *               otherwise it can be a filter or filter instance name
//     *               which will send the command to all matching filters.
//     * @param cmd    the command to send, for handling simplicity all commands must be alphanumeric only
//     * @param arg    the arguments for the command
//     * @param res    a buffer with size res_size where the filter(s) can return a response.
//     *
//     * @returns >=0 on success otherwise an error code.
//     *              AVERROR(ENOSYS) on unsupported commands
//     */
//    public int sendCommand( String target, String cmd, String arg, String res, int resLen, int flags ) {
//        return nSendCommand( mPointer, target, cmd, arg, res, resLen, flags );
//    }
//
//    /**
//     * Queue a command for one or more filter instances.
//     *
//     * @param target the filter(s) to which the command should be sent
//     *               "all" sends to all filters
//     *               otherwise it can be a filter or filter instance name
//     *               which will send the command to all matching filters.
//     * @param cmd    the command to sent, for handling simplicity all commands must be alphanumeric only
//     * @param arg    the argument for the command
//     * @param ts     time at which the command should be sent to the filter
//     *
//     * @note As this executes commands after this function returns, no return code
//     *       from the filter is provided, also AVFILTER_CMD_FLAG_ONE is not supported.
//     */
//    public int queueCommand( String target, String cmd, String arg, int flags, double ts ) {
//        return nQueueCommand( mPointer, target, cmd, arg, flags, ts );
//    }
//
//
//    /**
//     * Dump a graph into a human-readable string representation.
//     *
//     * @param options  formatting options; currently ignored
//     * @return  a string, or NULL in case of memory allocation failure;
//     *          the string must be freed using av_free
//     */
//    public String dump( String options ) {
//        return nDump( mPointer, options );
//    }
//
//    /**
//     * Request a frame on the oldest sink link.
//     *
//     * If the request returns AVERROR_EOF, try the next.
//     *
//     * Note that this function is not meant to be the sole scheduling mechanism
//     * of a filtergraph, only a convenience function to help drain a filtergraph
//     * in a balanced way under normal circumstances.
//     *
//     * Also note that AVERROR_EOF does not mean that frames did not arrive on
//     * some of the sinks during the process.
//     * When there are multiple sink links, in case the requested link
//     * returns an EOF, this may cause a filter to flush pending frames
//     * which are sent to another sink link, although unrequested.
//     *
//     * @return  the return value of ff_request_frame(),
//     *          or AVERROR_EOF if all links returned AVERROR_EOF
//     */
//    public int requestOldest() {
//        return nRequestOldest( mPointer );
//    }
//
//
//
//
//
//
//
//    public long pointer() {
//        return mPointer;
//    }
//
//
//    public ReleaseMethod releaseMethod() {
//        return mRelease;
//    }
//
//
//    public void setReleaseMethod( ReleaseMethod method ) {
//        mRelease = method;
//    }
//
//
//    protected void finalize() throws Throwable {
//        release();
//        super.finalize();
//    }
//
//
//    //** NATIVES **//
//    private static native long   nAlloc();
//    private static native void   nFree( long pointer );
//
//    private static native int    nNbFilters( long pointer );
//    private static native long   nFilter( long pointer, int index );
//    private static native String nScaleSwsOpts( long pointer );
//    private static native String nResampleLavrOpts( long pointer );
//    private static native int    nThreadType( long pointer );
//    private static native int    nNbThreads( long pointer );
//    private static native long   nOpaque( long pointer );
//    private static native void   nOpaque( long pointer, long opaque );
//
//    private static native long   nAllocFilter( long pointer, long filter, String name );
//    private static native long   nGetFilter( long pointer, String name );
//    private static native void   nSetAutoConvert( long pointer, int flags );
//    private static native int    nConfig( long pointer, long logCtx );
//    private static native long   nCreateFilter( long pointer, long filter, String name, String args, long opaque, int[] outErr );
//
//    private static native int    nParse2( long pointer, String filters, long[] out );
//    private static native int    nSendCommand( long pointer, String target, String cmd, String arg, String res, int resLen, int flags );
//    private static native int    nQueueCommand( long pointer, String target, String cmd, String arg, int flags, double ts );
//    private static native String nDump( long pointer, String options );
//    private static native int    nRequestOldest( long pointer );
//
//}
//
//
