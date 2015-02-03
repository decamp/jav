/**
 * Process multiple parts of the frame concurrently.
 */
#define AVFILTER_THREAD_SLICE (1 << 0)


enum {
    AVFILTER_AUTO_CONVERT_ALL  =  0, /**< all automatic conversions enabled */
    AVFILTER_AUTO_CONVERT_NONE = -1, /**< all automatic conversions disabled */
};