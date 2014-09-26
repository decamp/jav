// Number of data pointers in frame.data[] field.
public static final int AV_NUM_DATA_POINTERS = 8;

//Buffer types - Determines de/allocation procedure.
public static final int FF_BUFFER_TYPE_INTERNAL = 1;
public static final int FF_BUFFER_TYPE_USER     = 2; ///< direct rendering buffers (image is (de)allocated by user)
public static final int FF_BUFFER_TYPE_SHARED   = 4; ///< Buffer from somewhere else; don't deallocate image (data/base), all other tables are not shared.
public static final int FF_BUFFER_TYPE_COPY     = 8;


