//package bits.jav.filter;
//
//import bits.jav.codec.JavFrame;
//import bits.jav.util.NativeObject;
//import bits.jav.util.ReleaseMethod;
//
//
///**
// * A filter pad used for either input or output.
// *
// * See doc/filter_design.txt for details on how to implement the methods.
// *
// * @warning this struct might be removed from public API.
// * users should call avfilter_pad_get_name() and avfilter_pad_get_type()
// * to access the name and type fields; there should be no need to access
// * any other fields from outside of libavfilter.
// */
//public class JavFilterPad implements NativeObject {
//
//    /**
//     * Get the number of elements in a NULL-terminated array of AVFilterPads (e.g.
//     * AVFilter.inputs/outputs).
//     */
//    public static int padCount( JavFilterPad pads ) {
//        return pads == null ? 0 : nPadCount( pads.pointer() );
//    }
//
//
//    private long mPointer;
//
//    JavFilterPad( long pointer ) {
//        mPointer = pointer;
//    }
//
//
//    /**
//     * Pad name. The name is unique among inputs and among outputs, but an
//     * input may have the same name as an output. This may be NULL if this
//     * pad has no need to ever be referenced by name.
//     */
//    public String name() {
//        return nName( mPointer );
//    }
//
//    /**
//     * AVFilterPad type.
//     */
//    public int type() {
//        return nType( mPointer );
//    }
//
//    /**
//     * Callback function to get a video buffer. If NULL, the filter system will
//     * use ff_default_get_video_buffer().
//     * <p/>
//     * Input video pads only.
//     */
//    public JavFrame getVideoBuffer( JavFilterLink link, int w, int h ) {
//        //TODO: Implement factory pass ins.
//        return null;
//    }
//
//    /**
//     * Callback function to get an audio buffer. If NULL, the filter system will
//     * use ff_default_get_audio_buffer().
//     * <p/>
//     * Input audio pads only.
//     */
//    public JavFrame getAudioBuffer( JavFilterLink link, int nbSamples ) {
//        //TODO: Implement factory pass ins.
//        return null;
//    }
//
//    /**
//     * Filtering callback. This is where a filter receives a frame with
//     * audio/video data and should do its processing.
//     * <p/>
//     * Input pads only.
//     *
//     * @return >= 0 on success, a negative AVERROR on error. This function
//     * must ensure that frame is properly unreferenced on error if it
//     * hasn't been passed on to another filter.
//     */
//    public int filterFrame( JavFilterLink link, JavFrame frame ) {
//        return nFilterFrame( mPointer, link.pointer(), frame.pointer() );
//    }
//
//    /**
//     * Frame poll callback. This returns the number of immediately available
//     * samples. It should return a positive value if the next request_frame()
//     * is guaranteed to return one frame (with no delay).
//     * <p/>
//     * Defaults to just calling the source poll_frame() method.
//     * <p/>
//     * Output pads only.
//     */
//    public int pollFrame( JavFilterLink link ) {
//        return nPollFrame( mPointer, link.pointer() );
//    }
//
//    /**
//     * Frame request callback. A call to this should result in at least one
//     * frame being output over the given link. This should return zero on
//     * success, and another value on error.
//     * See ff_request_frame() for the error codes with a specific
//     * meaning.
//     * <p/>
//     * Output pads only.
//     */
//    public int requestFrame( JavFilterLink link ) {
//        return nRequestFrame( mPointer, link.pointer() );
//    }
//
//    /**
//     * Link configuration callback.
//     * <p/>
//     * For output pads, this should set the following link properties:
//     * video: width, height, sample_aspect_ratio, time_base
//     * audio: sample_rate.
//     * <p/>
//     * This should NOT set properties such as format, channel_layout, etc which
//     * are negotiated between filters by the filter system using the
//     * query_formats() callback before this function is called.
//     * <p/>
//     * For input pads, this should check the properties of the link, and update
//     * the filter's internal state as necessary.
//     * <p/>
//     * For both input and output pads, this should return zero on success,
//     * and another value on error.
//     */
//    public int configProps( JavFilterLink link ) {
//        return nConfigProps( mPointer, link.pointer() );
//    }
//
//    /**
//     * The filter expects a fifo to be inserted on its input link,
//     * typically because it has a delay.
//     * <p/>
//     * input pads only.
//     */
//    public int needsFifo() {
//        return nNeedsFifo( mPointer );
//    }
//
//    /**
//     * The filter expects writable frames from its input link,
//     * duplicating data buffers if needed.
//     * <p/>
//     * input pads only.
//     */
//    public int needsWritable() {
//        return nNeedsWritable( mPointer );
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
//    //** Natives **//
//
//    private static native String nName( long pointer );
//    private static native int    nType( long pointer );
//    private static native int    nFilterFrame( long pointer, long link, long frame );
//    private static native int    nPollFrame( long pointer, long link );
//    private static native int    nRequestFrame( long pointer, long link );
//    private static native int    nConfigProps( long pointr, long link );
//    private static native int    nNeedsFifo( long pointer );
//    private static native int    nNeedsWritable( long pointer );
//
//    private static native int    nPadCount( long pointer );
//
//
//}