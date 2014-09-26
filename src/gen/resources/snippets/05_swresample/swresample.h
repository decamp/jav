enum SwrDitherType {
    SWR_DITHER_NONE = 0,
    SWR_DITHER_RECTANGULAR,
    SWR_DITHER_TRIANGULAR,
    SWR_DITHER_TRIANGULAR_HIGHPASS,

    SWR_DITHER_NS = 64,         ///< not part of API/ABI
    SWR_DITHER_NS_LIPSHITZ,
    SWR_DITHER_NS_F_WEIGHTED,
    SWR_DITHER_NS_MODIFIED_E_WEIGHTED,
    SWR_DITHER_NS_IMPROVED_E_WEIGHTED,
    SWR_DITHER_NS_SHIBATA,
    SWR_DITHER_NS_LOW_SHIBATA,
    SWR_DITHER_NS_HIGH_SHIBATA,
    SWR_DITHER_NB,              ///< not part of API/ABI
};

/** Resampling Engines */
enum SwrEngine {
    SWR_ENGINE_SWR,             /**< SW Resampler */
    SWR_ENGINE_SOXR,            /**< SoX Resampler */
    SWR_ENGINE_NB,              ///< not part of API/ABI
};

/** Resampling Filter Types */
enum SwrFilterType {
    SWR_FILTER_TYPE_CUBIC,              /**< Cubic */
    SWR_FILTER_TYPE_BLACKMAN_NUTTALL,   /**< Blackman Nuttall Windowed Sinc */
    SWR_FILTER_TYPE_KAISER,             /**< Kaiser Windowed Sinc */
};

