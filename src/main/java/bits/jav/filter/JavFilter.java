//package bits.jav.filter;
//
//import bits.jav.util.*;
//
//
///**
// * Filter definition. This defines the pads a filter contains, and all the
// * callback functions used to interact with the filter.
// */
//public class JavFilter implements NativeObject {
//
//
//    /**
//     * Initialize the filter system. Register all builtin filters.
//     **/
//    public static void registerAll() {
//        nRegisterAll();
//    }
//
//    /**
//     * Get a filter definition matching the given name.
//     *
//     * @param name the filter name to find
//     * @return     the filter definition, if any matching one is registered.
//     *             NULL if none found.
//     */
//    public static JavFilter getByName( String name ) {
//        long ptr = nGetByName( name );
//        return ptr == 0 ? null : new JavFilter( ptr );
//    }
//
//    /**
//     * Iterate over all registered filters.
//     * @return If prev is non-NULL, next registered filter after prev or NULL if
//     * prev is the last filter. If prev is NULL, return the first registered filter.
//     */
//    public static JavFilter next( JavFilter prev ) {
//        long ptr = nNext( prev == null ? 0L : prev.pointer() );
//        return ptr == 0 ? null : new JavFilter( ptr );
//    }
//
//
//
//    private long mPointer;
//
//    JavFilter( long pointer ) {
//        mPointer = pointer;
//    }
//
//
//    /**
//     * Filter name. Must be non-NULL and unique among filters.
//     */
//    public String name() {
//        return nName( mPointer );
//    }
//
//    /**
//     * A description of the filter. May be NULL.
//     *
//     * You should use the NULL_IF_CONFIG_SMALL() macro to define it.
//     */
//    public String description() {
//        return nDescription( mPointer );
//    }
//
//    /**
//     * List of inputs, terminated by a zeroed element.
//     *
//     * NULL if there are no (static) inputs. Instances of filters with
//     * AVFILTER_FLAG_DYNAMIC_INPUTS set may have more inputs than present in
//     * this list.
//     */
//    public JavFilterPad inputs() {
//        return new JavFilterPad( nInputs( mPointer ) );
//    }
//
//    /**
//     * List of outputs, terminated by a zeroed element.
//     *
//     * NULL if there are no (static) outputs. Instances of filters with
//     * AVFILTER_FLAG_DYNAMIC_OUTPUTS set may have more outputs than present in
//     * this list.
//     */
//    public JavFilterPad outputs() {
//        return new JavFilterPad( nOutputs( mPointer ) );
//    }
//
//    /**
//     * A class for the private data, used to declare filter private AVOptions.
//     * This field is NULL for filters that do not declare any options.
//     *
//     * If this field is non-NULL, the first member of the filter private data
//     * must be a pointer to AVClass, which will be set by libavfilter generic
//     * code to this class.
//     */
//
//    public JavClass privClass() {
//        return new OpaqueJavClass( nPrivClass( mPointer ) );
//    }
//
//    /**
//     * A combination of AVFILTER_FLAG_*
//     */
//    public int flags() {
//        return nFlags( mPointer );
//    }
//
//
//
//    public long pointer() {
//        return mPointer;
//    }
//
//
//    public ReleaseMethod releaseMethod() {
//        return ReleaseMethod.OWNER;
//    }
//
//
//
//    //** Natives **//
//
//    private static native void   nRegisterAll();
//    private static native long   nGetByName( String name );
//    private static native long   nNext( long pointer );
//
//    private static native String nName( long pointer );
//    private static native String nDescription( long pointer );
//    private static native long   nInputs( long pointer );
//    private static native long   nOutputs( long pointer );
//    private static native long   nPrivClass( long pointer );
//    private static native int    nFlags( long pointer );
//
//}
