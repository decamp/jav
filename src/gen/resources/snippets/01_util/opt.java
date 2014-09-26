//Option search flags
public static final int AV_OPT_SEARCH_CHILDREN   = 0x0001; // Search in possible children of the given object first.
public static final int AV_OPT_SEARCH_FAKE_OBJ   = 0x0002; // The obj passed to av_opt_find() is fake -- only a double pointer to AVClass
                                                           // instead of a required pointer to a struct containing AVClass. This is useful
                                                           // for searching for options without needing to alocate the corresponding object.