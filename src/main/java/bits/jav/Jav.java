/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

/* DO NOT MODIFY. This file was generated automatically by bits.jav.gen.GenConsts */
package bits.jav;

import bits.jav.util.Rational;

/**
 * JAV library initialization and constants.
 */
public class Jav { 

    //==============================================================
    // 00_jav
    //==============================================================

    /**
     * Call this function before using Jav.
     */
    public static synchronized void init() {
        if( sInit ) {
            return;
        }
        sInit = true;
        System.loadLibrary( "jav" );
        nInit();
    }
    
    
    /**
     * Checks if return code {@code err} indicates an error, and if so,
     * throws that error as a JavException.
     */
    public static void assertOkay( int err ) throws JavException {
        if( err < 0 ) {
            throw JavException.fromErr( err );
        }
    }




    //==============================================================
    // 01_util
    //==============================================================

    
    public static final int AVMEDIA_TYPE_UNKNOWN = 0xFFFFFFFF; ///< Usually treated as AVMEDIA_TYPE_DATA
    public static final int AVMEDIA_TYPE_VIDEO = 0x00000000; 
    public static final int AVMEDIA_TYPE_AUDIO = 0x00000001; 
    public static final int AVMEDIA_TYPE_DATA = 0x00000002; ///< Opaque data information usually continuous
    public static final int AVMEDIA_TYPE_SUBTITLE = 0x00000003; 
    public static final int AVMEDIA_TYPE_ATTACHMENT = 0x00000004; ///< Opaque data information usually sparse
    public static final int AVMEDIA_TYPE_NB = 0x00000005; 
    
    
    
    
    public static final int AV_PICTURE_TYPE_NONE = 0x00000000; ///< Undefined
    public static final int AV_PICTURE_TYPE_I = 0x00000001; ///< Intra
    public static final int AV_PICTURE_TYPE_P = 0x00000002; ///< Predicted
    public static final int AV_PICTURE_TYPE_B = 0x00000003; ///< Bi-dir predicted
    public static final int AV_PICTURE_TYPE_S = 0x00000004; ///< S(GMC)-VOP MPEG4
    public static final int AV_PICTURE_TYPE_SI = 0x00000005; ///< Switching Intra
    public static final int AV_PICTURE_TYPE_SP = 0x00000006; ///< Switching Predicted
    public static final int AV_PICTURE_TYPE_BI = 0x00000007; ///< BI type


    //Time stamp values.
    public static final long     AV_NOPTS_VALUE = 0x8000000000000000L;
    public static final int      AV_TIME_BASE   = 1000000;
    public static final Rational AV_TIME_BASE_Q = new Rational(1, AV_TIME_BASE);


    /**
     * Always treat the nativeBuffer as read-only, even when it has only one
     * reference.
     */
    public static final int AV_BUFFER_FLAG_READONLY = (1 << 0); 


    public static final int AV_CH_FRONT_LEFT             = 0x00000001; 
    public static final int AV_CH_FRONT_RIGHT            = 0x00000002; 
    public static final int AV_CH_FRONT_CENTER           = 0x00000004; 
    public static final int AV_CH_LOW_FREQUENCY          = 0x00000008; 
    public static final int AV_CH_BACK_LEFT              = 0x00000010; 
    public static final int AV_CH_BACK_RIGHT             = 0x00000020; 
    public static final int AV_CH_FRONT_LEFT_OF_CENTER   = 0x00000040; 
    public static final int AV_CH_FRONT_RIGHT_OF_CENTER  = 0x00000080; 
    public static final int AV_CH_BACK_CENTER            = 0x00000100; 
    public static final int AV_CH_SIDE_LEFT              = 0x00000200; 
    public static final int AV_CH_SIDE_RIGHT             = 0x00000400; 
    public static final int AV_CH_TOP_CENTER             = 0x00000800; 
    public static final int AV_CH_TOP_FRONT_LEFT         = 0x00001000; 
    public static final int AV_CH_TOP_FRONT_CENTER       = 0x00002000; 
    public static final int AV_CH_TOP_FRONT_RIGHT        = 0x00004000; 
    public static final int AV_CH_TOP_BACK_LEFT          = 0x00008000; 
    public static final int AV_CH_TOP_BACK_CENTER        = 0x00010000; 
    public static final int AV_CH_TOP_BACK_RIGHT         = 0x00020000; 
    public static final int AV_CH_STEREO_LEFT            = 0x20000000; ///< Stereo downmix.
    public static final int AV_CH_STEREO_RIGHT           = 0x40000000; ///< See AV_CH_STEREO_LEFT.
    public static final long AV_CH_WIDE_LEFT              = 0x0000000080000000L; 
    public static final long AV_CH_WIDE_RIGHT             = 0x0000000100000000L; 
    public static final long AV_CH_SURROUND_DIRECT_LEFT   = 0x0000000200000000L; 
    public static final long AV_CH_SURROUND_DIRECT_RIGHT  = 0x0000000400000000L; 
    public static final long AV_CH_LOW_FREQUENCY_2        = 0x0000000800000000L; 
    
    /** Channel mask value used for AVCodecContext.request_channel_layout
        to indicate that the user requests the channel order of the decoder output
        to be the native codec channel order. */
    public static final long AV_CH_LAYOUT_NATIVE          = 0x8000000000000000L; 
    
    
    public static final int AV_CH_LAYOUT_MONO              = (AV_CH_FRONT_CENTER); 
    public static final int AV_CH_LAYOUT_STEREO            = (AV_CH_FRONT_LEFT|AV_CH_FRONT_RIGHT); 
    public static final int AV_CH_LAYOUT_2POINT1           = (AV_CH_LAYOUT_STEREO|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_2_1               = (AV_CH_LAYOUT_STEREO|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_SURROUND          = (AV_CH_LAYOUT_STEREO|AV_CH_FRONT_CENTER); 
    public static final int AV_CH_LAYOUT_3POINT1           = (AV_CH_LAYOUT_SURROUND|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_4POINT0           = (AV_CH_LAYOUT_SURROUND|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_4POINT1           = (AV_CH_LAYOUT_4POINT0|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_2_2               = (AV_CH_LAYOUT_STEREO|AV_CH_SIDE_LEFT|AV_CH_SIDE_RIGHT); 
    public static final int AV_CH_LAYOUT_QUAD              = (AV_CH_LAYOUT_STEREO|AV_CH_BACK_LEFT|AV_CH_BACK_RIGHT); 
    public static final int AV_CH_LAYOUT_5POINT0           = (AV_CH_LAYOUT_SURROUND|AV_CH_SIDE_LEFT|AV_CH_SIDE_RIGHT); 
    public static final int AV_CH_LAYOUT_5POINT1           = (AV_CH_LAYOUT_5POINT0|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_5POINT0_BACK      = (AV_CH_LAYOUT_SURROUND|AV_CH_BACK_LEFT|AV_CH_BACK_RIGHT); 
    public static final int AV_CH_LAYOUT_5POINT1_BACK      = (AV_CH_LAYOUT_5POINT0_BACK|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_6POINT0           = (AV_CH_LAYOUT_5POINT0|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_6POINT0_FRONT     = (AV_CH_LAYOUT_2_2|AV_CH_FRONT_LEFT_OF_CENTER|AV_CH_FRONT_RIGHT_OF_CENTER); 
    public static final int AV_CH_LAYOUT_HEXAGONAL         = (AV_CH_LAYOUT_5POINT0_BACK|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_6POINT1           = (AV_CH_LAYOUT_5POINT1|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_6POINT1_BACK      = (AV_CH_LAYOUT_5POINT1_BACK|AV_CH_BACK_CENTER); 
    public static final int AV_CH_LAYOUT_6POINT1_FRONT     = (AV_CH_LAYOUT_6POINT0_FRONT|AV_CH_LOW_FREQUENCY); 
    public static final int AV_CH_LAYOUT_7POINT0           = (AV_CH_LAYOUT_5POINT0|AV_CH_BACK_LEFT|AV_CH_BACK_RIGHT); 
    public static final int AV_CH_LAYOUT_7POINT0_FRONT     = (AV_CH_LAYOUT_5POINT0|AV_CH_FRONT_LEFT_OF_CENTER|AV_CH_FRONT_RIGHT_OF_CENTER); 
    public static final int AV_CH_LAYOUT_7POINT1           = (AV_CH_LAYOUT_5POINT1|AV_CH_BACK_LEFT|AV_CH_BACK_RIGHT); 
    public static final int AV_CH_LAYOUT_7POINT1_WIDE      = (AV_CH_LAYOUT_5POINT1|AV_CH_FRONT_LEFT_OF_CENTER|AV_CH_FRONT_RIGHT_OF_CENTER); 
    public static final int AV_CH_LAYOUT_7POINT1_WIDE_BACK = (AV_CH_LAYOUT_5POINT1_BACK|AV_CH_FRONT_LEFT_OF_CENTER|AV_CH_FRONT_RIGHT_OF_CENTER); 
    public static final int AV_CH_LAYOUT_OCTAGONAL         = (AV_CH_LAYOUT_5POINT0|AV_CH_BACK_LEFT|AV_CH_BACK_CENTER|AV_CH_BACK_RIGHT); 
    public static final int AV_CH_LAYOUT_STEREO_DOWNMIX    = (AV_CH_STEREO_LEFT|AV_CH_STEREO_RIGHT); 


    public static final int AVERROR_BSF_NOT_FOUND      = 0xB9ACBD08; ///< Bitstream filter not found
    public static final int AVERROR_BUG                = 0xDEB8AABE; ///< Internal bug, also see AVERROR_BUG2
    public static final int AVERROR_BUFFER_TOO_SMALL   = 0xACB9AABE; ///< Buffer too small
    public static final int AVERROR_DECODER_NOT_FOUND  = 0xBCBABB08; ///< Decoder not found
    public static final int AVERROR_DEMUXER_NOT_FOUND  = 0xB2BABB08; ///< Demuxer not found
    public static final int AVERROR_ENCODER_NOT_FOUND  = 0xBCB1BA08; ///< Encoder not found
    public static final int AVERROR_EOF                = 0xDFB9B0BB; ///< End of file
    public static final int AVERROR_EXIT               = 0xABB6A7BB; ///< Immediate exit was requested; the called function should not be restarted
    public static final int AVERROR_EXTERNAL           = 0xDFABA7BB; ///< Generic error in an external library
    public static final int AVERROR_FILTER_NOT_FOUND   = 0xB3B6B908; ///< Filter not found
    public static final int AVERROR_INVALIDDATA        = 0xBEBBB1B7; ///< Invalid data found when processing input
    public static final int AVERROR_MUXER_NOT_FOUND    = 0xA7AAB208; ///< Muxer not found
    public static final int AVERROR_OPTION_NOT_FOUND   = 0xABAFB008; ///< Option not found
    public static final int AVERROR_PATCHWELCOME       = 0xBAA8BEB0; ///< Not yet implemented in FFmpeg, patches welcome
    public static final int AVERROR_PROTOCOL_NOT_FOUND = 0xB0ADAF08; ///< Protocol not found
    
    public static final int AVERROR_STREAM_NOT_FOUND   = 0xADABAC08; ///< Stream not found
    /**
     * This is semantically identical to AVERROR_BUG
     * it has been introduced in Libav after our AVERROR_BUG and with a modified value.
     */
    public static final int AVERROR_BUG2               = 0xDFB8AABE; 
    public static final int AVERROR_UNKNOWN            = 0xB1B4B1AB; ///< Unknown error, typically from an external library
    public static final int AVERROR_EXPERIMENTAL       = (-0x2bb2afa8) ; ///< Requested feature is flagged experimental. Set strict_std_compliance if you really want to use it.


    // Additional errors for JAV wrapper.
    public static final int AVERROR_NONE                    = 0x00000000;
    public static final int AVERROR_ALLOC_FAILED            = 0xB9B3BE02; //makeErrTag( 0xFE, 'A', 'L', 'F' )
    public static final int AVERROR_CODEC_OPEN_FAILED       = 0xB9B0BC02; //makeErrTag( 0xFE, 'C', 'O', 'F' )
    public static final int AVERROR_CODEC_CLOSED_FAILED     = 0xB9BCBC02; //makeErrTag( 0xFE, 'C', 'C', 'F' )
    public static final int AVERROR_DECODE_FAILED           = 0xB9BABB02; //makeErrTag( 0xFE, 'D', 'E', 'F' )
    public static final int AVERROR_STREAM_FAILED           = 0xB9ABAC02; //makeErrTag( 0xFE, 'S', 'T', 'F' )
    public static final int AVERROR_UNSUPPORTED_COLOR_SPACE = 0xACBCAA02; //makeErrTag( 0xFE, 'U', 'C', 'S' )


    
    public static final int AV_OPT_TYPE_FLAGS = 0x00000000; 
    public static final int AV_OPT_TYPE_INT = 0x00000001; 
    public static final int AV_OPT_TYPE_INT64 = 0x00000002; 
    public static final int AV_OPT_TYPE_DOUBLE = 0x00000003; 
    public static final int AV_OPT_TYPE_FLOAT = 0x00000004; 
    public static final int AV_OPT_TYPE_STRING = 0x00000005; 
    public static final int AV_OPT_TYPE_RATIONAL = 0x00000006; 
    public static final int AV_OPT_TYPE_BINARY = 0x00000007; ///< offset must point to a pointer immediately followed by an int for the length
    public static final int AV_OPT_TYPE_CONST = 0x00000080; 
    public static final int AV_OPT_TYPE_IMAGE_SIZE = 0x53495A45; ///< offset must point to two consecutive integers
    public static final int AV_OPT_TYPE_PIXEL_FMT  = 0x50464D54; 
    public static final int AV_OPT_TYPE_SAMPLE_FMT = 0x53464D54; 
    public static final int AV_OPT_TYPE_VIDEO_RATE = 0x56524154; ///< offset must point to AVRational
    public static final int AV_OPT_TYPE_DURATION   = 0x44555220; 
    public static final int AV_OPT_TYPE_COLOR      = 0x434F4C52; 
    public static final int AV_OPT_TYPE_CHANNEL_LAYOUT = 0x43484C41; 
    
    
    
    public static final int AV_OPT_FLAG_ENCODING_PARAM  = 0x00000001; ///< a generic parameter which can be set by the user for muxing or encoding
    public static final int AV_OPT_FLAG_DECODING_PARAM  = 0x00000002; ///< a generic parameter which can be set by the user for demuxing or decoding
    public static final int AV_OPT_FLAG_METADATA        = 0x00000004; ///< some data extracted or inserted into the file like title, comment, ...
    public static final int AV_OPT_FLAG_AUDIO_PARAM     = 0x00000008; 
    public static final int AV_OPT_FLAG_VIDEO_PARAM     = 0x00000010; 
    public static final int AV_OPT_FLAG_SUBTITLE_PARAM  = 0x00000020; 
    public static final int AV_OPT_FLAG_FILTERING_PARAM = (1<<16) ; ///< a generic parameter which can be set by the user for filtering


    //Option search flags
    public static final int AV_OPT_SEARCH_CHILDREN   = 0x0001; // Search in possible children of the given object first.
    public static final int AV_OPT_SEARCH_FAKE_OBJ   = 0x0002; // The obj passed to av_opt_find() is fake -- only a double pointer to AVClass
                                                               // instead of a required pointer to a struct containing AVClass. This is useful
                                                               // for searching for options without needing to alocate the corresponding object.


    public static final int AVPALETTE_SIZE = 0x00000400; 
    public static final int AVPALETTE_COUNT = 0x00000100; 
    
    
    
    public static final int AV_PIX_FMT_NONE = 0xFFFFFFFF; 
    public static final int AV_PIX_FMT_YUV420P = 0x00000000; ///< planar YUV 4:2:0, 12bpp, (1 Cr & Cb sample per 2x2 Y samples)
    public static final int AV_PIX_FMT_YUYV422 = 0x00000001; ///< packed YUV 4:2:2, 16bpp, Y0 Cb Y1 Cr
    public static final int AV_PIX_FMT_RGB24 = 0x00000002; ///< packed RGB 8:8:8, 24bpp, RGBRGB...
    public static final int AV_PIX_FMT_BGR24 = 0x00000003; ///< packed RGB 8:8:8, 24bpp, BGRBGR...
    public static final int AV_PIX_FMT_YUV422P = 0x00000004; ///< planar YUV 4:2:2, 16bpp, (1 Cr & Cb sample per 2x1 Y samples)
    public static final int AV_PIX_FMT_YUV444P = 0x00000005; ///< planar YUV 4:4:4, 24bpp, (1 Cr & Cb sample per 1x1 Y samples)
    public static final int AV_PIX_FMT_YUV410P = 0x00000006; ///< planar YUV 4:1:0,  9bpp, (1 Cr & Cb sample per 4x4 Y samples)
    public static final int AV_PIX_FMT_YUV411P = 0x00000007; ///< planar YUV 4:1:1, 12bpp, (1 Cr & Cb sample per 4x1 Y samples)
    public static final int AV_PIX_FMT_GRAY8 = 0x00000008; ///<        Y        ,  8bpp
    public static final int AV_PIX_FMT_MONOWHITE = 0x00000009; ///<        Y        ,  1bpp, 0 is white, 1 is black, in each byte pixels are ordered from the msb to the lsb
    public static final int AV_PIX_FMT_MONOBLACK = 0x0000000A; ///<        Y        ,  1bpp, 0 is black, 1 is white, in each byte pixels are ordered from the msb to the lsb
    public static final int AV_PIX_FMT_PAL8 = 0x0000000B; ///< 8 bit with PIX_FMT_RGB32 palette
    public static final int AV_PIX_FMT_YUVJ420P = 0x0000000C; ///< planar YUV 4:2:0, 12bpp, full scale (JPEG), deprecated in favor of PIX_FMT_YUV420P and setting color_range
    public static final int AV_PIX_FMT_YUVJ422P = 0x0000000D; ///< planar YUV 4:2:2, 16bpp, full scale (JPEG), deprecated in favor of PIX_FMT_YUV422P and setting color_range
    public static final int AV_PIX_FMT_YUVJ444P = 0x0000000E; ///< planar YUV 4:4:4, 24bpp, full scale (JPEG), deprecated in favor of PIX_FMT_YUV444P and setting color_range
    public static final int AV_PIX_FMT_XVMC_MPEG2_MC = 0x0000000F; ///< XVideo Motion Acceleration via common packet passing
    public static final int AV_PIX_FMT_XVMC_MPEG2_IDCT = 0x00000010; 
    public static final int AV_PIX_FMT_UYVY422 = 0x00000011; ///< packed YUV 4:2:2, 16bpp, Cb Y0 Cr Y1
    public static final int AV_PIX_FMT_UYYVYY411 = 0x00000012; ///< packed YUV 4:1:1, 12bpp, Cb Y0 Y1 Cr Y2 Y3
    public static final int AV_PIX_FMT_BGR8 = 0x00000013; ///< packed RGB 3:3:2,  8bpp, (msb)2B 3G 3R(lsb)
    public static final int AV_PIX_FMT_BGR4 = 0x00000014; ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1B 2G 1R(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
    public static final int AV_PIX_FMT_BGR4_BYTE = 0x00000015; ///< packed RGB 1:2:1,  8bpp, (msb)1B 2G 1R(lsb)
    public static final int AV_PIX_FMT_RGB8 = 0x00000016; ///< packed RGB 3:3:2,  8bpp, (msb)2R 3G 3B(lsb)
    public static final int AV_PIX_FMT_RGB4 = 0x00000017; ///< packed RGB 1:2:1 bitstream,  4bpp, (msb)1R 2G 1B(lsb), a byte contains two pixels, the first pixel in the byte is the one composed by the 4 msb bits
    public static final int AV_PIX_FMT_RGB4_BYTE = 0x00000018; ///< packed RGB 1:2:1,  8bpp, (msb)1R 2G 1B(lsb)
    public static final int AV_PIX_FMT_NV12 = 0x00000019; ///< planar YUV 4:2:0, 12bpp, 1 plane for Y and 1 plane for the UV components, which are interleaved (first byte U and the following byte V)
    public static final int AV_PIX_FMT_NV21 = 0x0000001A; ///< as above, but U and V bytes are swapped
    
    public static final int AV_PIX_FMT_ARGB = 0x0000001B; ///< packed ARGB 8:8:8:8, 32bpp, ARGBARGB...
    public static final int AV_PIX_FMT_RGBA = 0x0000001C; ///< packed RGBA 8:8:8:8, 32bpp, RGBARGBA...
    public static final int AV_PIX_FMT_ABGR = 0x0000001D; ///< packed ABGR 8:8:8:8, 32bpp, ABGRABGR...
    public static final int AV_PIX_FMT_BGRA = 0x0000001E; ///< packed BGRA 8:8:8:8, 32bpp, BGRABGRA...
    
    public static final int AV_PIX_FMT_GRAY16BE = 0x0000001F; ///<        Y        , 16bpp, big-endian
    public static final int AV_PIX_FMT_GRAY16LE = 0x00000020; ///<        Y        , 16bpp, little-endian
    public static final int AV_PIX_FMT_YUV440P = 0x00000021; ///< planar YUV 4:4:0 (1 Cr & Cb sample per 1x2 Y samples)
    public static final int AV_PIX_FMT_YUVJ440P = 0x00000022; ///< planar YUV 4:4:0 full scale (JPEG), deprecated in favor of PIX_FMT_YUV440P and setting color_range
    public static final int AV_PIX_FMT_YUVA420P = 0x00000023; ///< planar YUV 4:2:0, 20bpp, (1 Cr & Cb sample per 2x2 Y & A samples)
    public static final int AV_PIX_FMT_VDPAU_H264 = 0x00000024; ///< H.264 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_VDPAU_MPEG1 = 0x00000025; ///< MPEG-1 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_VDPAU_MPEG2 = 0x00000026; ///< MPEG-2 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_VDPAU_WMV3 = 0x00000027; ///< WMV3 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_VDPAU_VC1 = 0x00000028; ///< VC-1 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_RGB48BE = 0x00000029; ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as big-endian
    public static final int AV_PIX_FMT_RGB48LE = 0x0000002A; ///< packed RGB 16:16:16, 48bpp, 16R, 16G, 16B, the 2-byte value for each R/G/B component is stored as little-endian
    
    public static final int AV_PIX_FMT_RGB565BE = 0x0000002B; ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), big-endian
    public static final int AV_PIX_FMT_RGB565LE = 0x0000002C; ///< packed RGB 5:6:5, 16bpp, (msb)   5R 6G 5B(lsb), little-endian
    public static final int AV_PIX_FMT_RGB555BE = 0x0000002D; ///< packed RGB 5:5:5, 16bpp, (msb)1A 5R 5G 5B(lsb), big-endian, most significant bit to 0
    public static final int AV_PIX_FMT_RGB555LE = 0x0000002E; ///< packed RGB 5:5:5, 16bpp, (msb)1A 5R 5G 5B(lsb), little-endian, most significant bit to 0
    
    public static final int AV_PIX_FMT_BGR565BE = 0x0000002F; ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), big-endian
    public static final int AV_PIX_FMT_BGR565LE = 0x00000030; ///< packed BGR 5:6:5, 16bpp, (msb)   5B 6G 5R(lsb), little-endian
    public static final int AV_PIX_FMT_BGR555BE = 0x00000031; ///< packed BGR 5:5:5, 16bpp, (msb)1A 5B 5G 5R(lsb), big-endian, most significant bit to 1
    public static final int AV_PIX_FMT_BGR555LE = 0x00000032; ///< packed BGR 5:5:5, 16bpp, (msb)1A 5B 5G 5R(lsb), little-endian, most significant bit to 1
    
    public static final int AV_PIX_FMT_VAAPI_MOCO = 0x00000033; ///< HW acceleration through VA API at motion compensation entry-point, Picture.data[3] contains a vaapi_render_state struct which contains macroblocks as well as various fields extracted from headers
    public static final int AV_PIX_FMT_VAAPI_IDCT = 0x00000034; ///< HW acceleration through VA API at IDCT entry-point, Picture.data[3] contains a vaapi_render_state struct which contains fields extracted from headers
    public static final int AV_PIX_FMT_VAAPI_VLD = 0x00000035; ///< HW decoding through VA API, Picture.data[3] contains a vaapi_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    
    public static final int AV_PIX_FMT_YUV420P16LE = 0x00000036; ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV420P16BE = 0x00000037; ///< planar YUV 4:2:0, 24bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV422P16LE = 0x00000038; ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV422P16BE = 0x00000039; ///< planar YUV 4:2:2, 32bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV444P16LE = 0x0000003A; ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV444P16BE = 0x0000003B; ///< planar YUV 4:4:4, 48bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    public static final int AV_PIX_FMT_VDPAU_MPEG4 = 0x0000003C; ///< MPEG4 HW decoding with VDPAU, data[0] contains a vdpau_render_state struct which contains the bitstream of the slices as well as various fields extracted from headers
    public static final int AV_PIX_FMT_DXVA2_VLD = 0x0000003D; ///< HW decoding through DXVA2, Picture.data[3] contains a LPDIRECT3DSURFACE9 pointer
    
    public static final int AV_PIX_FMT_RGB444LE = 0x0000003E; ///< packed RGB 4:4:4, 16bpp, (msb)4A 4R 4G 4B(lsb), little-endian, most significant bits to 0
    public static final int AV_PIX_FMT_RGB444BE = 0x0000003F; ///< packed RGB 4:4:4, 16bpp, (msb)4A 4R 4G 4B(lsb), big-endian, most significant bits to 0
    public static final int AV_PIX_FMT_BGR444LE = 0x00000040; ///< packed BGR 4:4:4, 16bpp, (msb)4A 4B 4G 4R(lsb), little-endian, most significant bits to 1
    public static final int AV_PIX_FMT_BGR444BE = 0x00000041; ///< packed BGR 4:4:4, 16bpp, (msb)4A 4B 4G 4R(lsb), big-endian, most significant bits to 1
    public static final int AV_PIX_FMT_GRAY8A = 0x00000042; ///< 8bit gray, 8bit alpha
    public static final int AV_PIX_FMT_BGR48BE = 0x00000043; ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as big-endian
    public static final int AV_PIX_FMT_BGR48LE = 0x00000044; ///< packed RGB 16:16:16, 48bpp, 16B, 16G, 16R, the 2-byte value for each R/G/B component is stored as little-endian
    
    /**
         * The following 12 formats have the disadvantage of needing 1 format for each bit depth.
         * Notice that each 9/10 bits sample is stored in 16 bits with extra padding.
         * If you want to support multiple bit depths, then using AV_PIX_FMT_YUV420P16* with the bpp stored separately is better.
         */
    public static final int AV_PIX_FMT_YUV420P9BE = 0x00000045; ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV420P9LE = 0x00000046; ///< planar YUV 4:2:0, 13.5bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV420P10BE = 0x00000047; ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV420P10LE = 0x00000048; ///< planar YUV 4:2:0, 15bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV422P10BE = 0x00000049; ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV422P10LE = 0x0000004A; ///< planar YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV444P9BE = 0x0000004B; ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV444P9LE = 0x0000004C; ///< planar YUV 4:4:4, 27bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV444P10BE = 0x0000004D; ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV444P10LE = 0x0000004E; ///< planar YUV 4:4:4, 30bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV422P9BE = 0x0000004F; ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV422P9LE = 0x00000050; ///< planar YUV 4:2:2, 18bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_VDA_VLD = 0x00000051; ///< hardware decoding through VDA
    
    public static final int AV_PIX_FMT_RGBA64BE = 0x00000052; ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
    public static final int AV_PIX_FMT_RGBA64LE = 0x00000053; ///< packed RGBA 16:16:16:16, 64bpp, 16R, 16G, 16B, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
    public static final int AV_PIX_FMT_BGRA64BE = 0x00000054; ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as big-endian
    public static final int AV_PIX_FMT_BGRA64LE = 0x00000055; ///< packed RGBA 16:16:16:16, 64bpp, 16B, 16G, 16R, 16A, the 2-byte value for each R/G/B/A component is stored as little-endian
    public static final int AV_PIX_FMT_GBRP = 0x00000056; ///< planar GBR 4:4:4 24bpp
    public static final int AV_PIX_FMT_GBRP9BE = 0x00000057; ///< planar GBR 4:4:4 27bpp, big-endian
    public static final int AV_PIX_FMT_GBRP9LE = 0x00000058; ///< planar GBR 4:4:4 27bpp, little-endian
    public static final int AV_PIX_FMT_GBRP10BE = 0x00000059; ///< planar GBR 4:4:4 30bpp, big-endian
    public static final int AV_PIX_FMT_GBRP10LE = 0x0000005A; ///< planar GBR 4:4:4 30bpp, little-endian
    public static final int AV_PIX_FMT_GBRP16BE = 0x0000005B; ///< planar GBR 4:4:4 48bpp, big-endian
    public static final int AV_PIX_FMT_GBRP16LE = 0x0000005C; ///< planar GBR 4:4:4 48bpp, little-endian
    
    /**
         * duplicated pixel formats for compatibility with libav.
         * FFmpeg supports these formats since May 8 2012 and Jan 28 2012 (commits f9ca1ac7 and 143a5c55)
         * Libav added them Oct 12 2012 with incompatible values (commit 6d5600e85)
         */
    public static final int AV_PIX_FMT_YUVA422P_LIBAV = 0x0000005D; ///< planar YUV 4:2:2 24bpp, (1 Cr & Cb sample per 2x1 Y & A samples)
    public static final int AV_PIX_FMT_YUVA444P_LIBAV = 0x0000005E; ///< planar YUV 4:4:4 32bpp, (1 Cr & Cb sample per 1x1 Y & A samples)
    
    public static final int AV_PIX_FMT_YUVA420P9BE = 0x0000005F; ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), big-endian
    public static final int AV_PIX_FMT_YUVA420P9LE = 0x00000060; ///< planar YUV 4:2:0 22.5bpp, (1 Cr & Cb sample per 2x2 Y & A samples), little-endian
    public static final int AV_PIX_FMT_YUVA422P9BE = 0x00000061; ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), big-endian
    public static final int AV_PIX_FMT_YUVA422P9LE = 0x00000062; ///< planar YUV 4:2:2 27bpp, (1 Cr & Cb sample per 2x1 Y & A samples), little-endian
    public static final int AV_PIX_FMT_YUVA444P9BE = 0x00000063; ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), big-endian
    public static final int AV_PIX_FMT_YUVA444P9LE = 0x00000064; ///< planar YUV 4:4:4 36bpp, (1 Cr & Cb sample per 1x1 Y & A samples), little-endian
    public static final int AV_PIX_FMT_YUVA420P10BE = 0x00000065; ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA420P10LE = 0x00000066; ///< planar YUV 4:2:0 25bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
    public static final int AV_PIX_FMT_YUVA422P10BE = 0x00000067; ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA422P10LE = 0x00000068; ///< planar YUV 4:2:2 30bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
    public static final int AV_PIX_FMT_YUVA444P10BE = 0x00000069; ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA444P10LE = 0x0000006A; ///< planar YUV 4:4:4 40bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)
    public static final int AV_PIX_FMT_YUVA420P16BE = 0x0000006B; ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA420P16LE = 0x0000006C; ///< planar YUV 4:2:0 40bpp, (1 Cr & Cb sample per 2x2 Y & A samples, little-endian)
    public static final int AV_PIX_FMT_YUVA422P16BE = 0x0000006D; ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA422P16LE = 0x0000006E; ///< planar YUV 4:2:2 48bpp, (1 Cr & Cb sample per 2x1 Y & A samples, little-endian)
    public static final int AV_PIX_FMT_YUVA444P16BE = 0x0000006F; ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, big-endian)
    public static final int AV_PIX_FMT_YUVA444P16LE = 0x00000070; ///< planar YUV 4:4:4 64bpp, (1 Cr & Cb sample per 1x1 Y & A samples, little-endian)
    
    public static final int AV_PIX_FMT_VDPAU = 0x00000071; ///< HW acceleration through VDPAU, Picture.data[3] contains a VdpVideoSurface
    
    public static final int AV_PIX_FMT_XYZ12LE = 0x00000072; ///< packed XYZ 4:4:4, 36 bpp, (msb) 12X, 12Y, 12Z (lsb), the 2-byte value for each X/Y/Z is stored as little-endian, the 4 lower bits are set to 0
    public static final int AV_PIX_FMT_XYZ12BE = 0x00000073; ///< packed XYZ 4:4:4, 36 bpp, (msb) 12X, 12Y, 12Z (lsb), the 2-byte value for each X/Y/Z is stored as big-endian, the 4 lower bits are set to 0
    public static final int AV_PIX_FMT_NV16 = 0x00000074; ///< interleaved chroma YUV 4:2:2, 16bpp, (1 Cr & Cb sample per 2x1 Y samples)
    public static final int AV_PIX_FMT_NV20LE = 0x00000075; ///< interleaved chroma YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_NV20BE = 0x00000076; ///< interleaved chroma YUV 4:2:2, 20bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    
    public static final int AV_PIX_FMT_0RGB = 0x00000123; ///< packed RGB 8:8:8, 32bpp, 0RGB0RGB...
    public static final int AV_PIX_FMT_RGB0 = 0x00000124; ///< packed RGB 8:8:8, 32bpp, RGB0RGB0...
    public static final int AV_PIX_FMT_0BGR = 0x00000125; ///< packed BGR 8:8:8, 32bpp, 0BGR0BGR...
    public static final int AV_PIX_FMT_BGR0 = 0x00000126; ///< packed BGR 8:8:8, 32bpp, BGR0BGR0...
    public static final int AV_PIX_FMT_YUVA444P = 0x00000127; ///< planar YUV 4:4:4 32bpp, (1 Cr & Cb sample per 1x1 Y & A samples)
    public static final int AV_PIX_FMT_YUVA422P = 0x00000128; ///< planar YUV 4:2:2 24bpp, (1 Cr & Cb sample per 2x1 Y & A samples)
    
    public static final int AV_PIX_FMT_YUV420P12BE = 0x00000129; ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV420P12LE = 0x0000012A; ///< planar YUV 4:2:0,18bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV420P14BE = 0x0000012B; ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV420P14LE = 0x0000012C; ///< planar YUV 4:2:0,21bpp, (1 Cr & Cb sample per 2x2 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV422P12BE = 0x0000012D; ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV422P12LE = 0x0000012E; ///< planar YUV 4:2:2,24bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV422P14BE = 0x0000012F; ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV422P14LE = 0x00000130; ///< planar YUV 4:2:2,28bpp, (1 Cr & Cb sample per 2x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV444P12BE = 0x00000131; ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV444P12LE = 0x00000132; ///< planar YUV 4:4:4,36bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    public static final int AV_PIX_FMT_YUV444P14BE = 0x00000133; ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), big-endian
    public static final int AV_PIX_FMT_YUV444P14LE = 0x00000134; ///< planar YUV 4:4:4,42bpp, (1 Cr & Cb sample per 1x1 Y samples), little-endian
    public static final int AV_PIX_FMT_GBRP12BE = 0x00000135; ///< planar GBR 4:4:4 36bpp, big-endian
    public static final int AV_PIX_FMT_GBRP12LE = 0x00000136; ///< planar GBR 4:4:4 36bpp, little-endian
    public static final int AV_PIX_FMT_GBRP14BE = 0x00000137; ///< planar GBR 4:4:4 42bpp, big-endian
    public static final int AV_PIX_FMT_GBRP14LE = 0x00000138; ///< planar GBR 4:4:4 42bpp, little-endian
    public static final int AV_PIX_FMT_GBRAP = 0x00000139; ///< planar GBRA 4:4:4:4 32bpp
    public static final int AV_PIX_FMT_GBRAP16BE = 0x0000013A; ///< planar GBRA 4:4:4:4 64bpp, big-endian
    public static final int AV_PIX_FMT_GBRAP16LE = 0x0000013B; ///< planar GBRA 4:4:4:4 64bpp, little-endian
    public static final int AV_PIX_FMT_YUVJ411P = 0x0000013C; ///< planar YUV 4:1:1, 12bpp, (1 Cr & Cb sample per 4x1 Y samples) full scale (JPEG), deprecated in favor of PIX_FMT_YUV411P and setting color_range
    
    public static final int AV_PIX_FMT_BAYER_BGGR8 = 0x0000013D; ///< bayer, BGBG..(odd line), GRGR..(even line), 8-bit samples */
    public static final int AV_PIX_FMT_BAYER_RGGB8 = 0x0000013E; ///< bayer, RGRG..(odd line), GBGB..(even line), 8-bit samples */
    public static final int AV_PIX_FMT_BAYER_GBRG8 = 0x0000013F; ///< bayer, GBGB..(odd line), RGRG..(even line), 8-bit samples */
    public static final int AV_PIX_FMT_BAYER_GRBG8 = 0x00000140; ///< bayer, GRGR..(odd line), BGBG..(even line), 8-bit samples */
    public static final int AV_PIX_FMT_BAYER_BGGR16LE = 0x00000141; ///< bayer, BGBG..(odd line), GRGR..(even line), 16-bit samples, little-endian */
    public static final int AV_PIX_FMT_BAYER_BGGR16BE = 0x00000142; ///< bayer, BGBG..(odd line), GRGR..(even line), 16-bit samples, big-endian */
    public static final int AV_PIX_FMT_BAYER_RGGB16LE = 0x00000143; ///< bayer, RGRG..(odd line), GBGB..(even line), 16-bit samples, little-endian */
    public static final int AV_PIX_FMT_BAYER_RGGB16BE = 0x00000144; ///< bayer, RGRG..(odd line), GBGB..(even line), 16-bit samples, big-endian */
    public static final int AV_PIX_FMT_BAYER_GBRG16LE = 0x00000145; ///< bayer, GBGB..(odd line), RGRG..(even line), 16-bit samples, little-endian */
    public static final int AV_PIX_FMT_BAYER_GBRG16BE = 0x00000146; ///< bayer, GBGB..(odd line), RGRG..(even line), 16-bit samples, big-endian */
    public static final int AV_PIX_FMT_BAYER_GRBG16LE = 0x00000147; ///< bayer, GRGR..(odd line), BGBG..(even line), 16-bit samples, little-endian */
    public static final int AV_PIX_FMT_BAYER_GRBG16BE = 0x00000148; ///< bayer, GRGR..(odd line), BGBG..(even line), 16-bit samples, big-endian */
    
    public static final int AV_PIX_FMT_NB = 0x00000149; ///< number of pixel formats, DO NOT USE THIS if you want to link with shared libav* because the number of formats might differ between versions
    
    
    
    public static final int AV_PIX_FMT_Y400A = AV_PIX_FMT_GRAY8A; 
    public static final int AV_PIX_FMT_GBR24P = AV_PIX_FMT_GBRP; 
    
    //#   define AV_PIX_FMT_NE(be, le) AV_PIX_FMT_##be
    //#   define AV_PIX_FMT_NE(be, le) AV_PIX_FMT_##le
    
    public static final int AV_PIX_FMT_RGB32   = AV_PIX_FMT_ARGB; 
    public static final int AV_PIX_FMT_RGB32_1 = AV_PIX_FMT_RGBA; 
    public static final int AV_PIX_FMT_BGR32   = AV_PIX_FMT_ABGR; 
    public static final int AV_PIX_FMT_BGR32_1 = AV_PIX_FMT_BGRA; 
    public static final int AV_PIX_FMT_0RGB32  = AV_PIX_FMT_0RGB; 
    public static final int AV_PIX_FMT_0BGR32  = AV_PIX_FMT_0BGR; 
    
    public static final int AV_PIX_FMT_GRAY16 = AV_PIX_FMT_GRAY16BE; 
    public static final int AV_PIX_FMT_RGB48  = AV_PIX_FMT_RGB48BE; 
    public static final int AV_PIX_FMT_RGB565 = AV_PIX_FMT_RGB565BE; 
    public static final int AV_PIX_FMT_RGB555 = AV_PIX_FMT_RGB555BE; 
    public static final int AV_PIX_FMT_RGB444 = AV_PIX_FMT_RGB444BE; 
    public static final int AV_PIX_FMT_BGR48  = AV_PIX_FMT_BGR48BE; 
    public static final int AV_PIX_FMT_BGR565 = AV_PIX_FMT_BGR565BE; 
    public static final int AV_PIX_FMT_BGR555 = AV_PIX_FMT_BGR555BE; 
    public static final int AV_PIX_FMT_BGR444 = AV_PIX_FMT_BGR444BE; 
    
    public static final int AV_PIX_FMT_YUV420P9  = AV_PIX_FMT_YUV420P9BE; 
    public static final int AV_PIX_FMT_YUV422P9  = AV_PIX_FMT_YUV422P9BE; 
    public static final int AV_PIX_FMT_YUV444P9  = AV_PIX_FMT_YUV444P9BE; 
    public static final int AV_PIX_FMT_YUV420P10 = AV_PIX_FMT_YUV420P10BE; 
    public static final int AV_PIX_FMT_YUV422P10 = AV_PIX_FMT_YUV422P10BE; 
    public static final int AV_PIX_FMT_YUV444P10 = AV_PIX_FMT_YUV444P10BE; 
    public static final int AV_PIX_FMT_YUV420P12 = AV_PIX_FMT_YUV420P12BE; 
    public static final int AV_PIX_FMT_YUV422P12 = AV_PIX_FMT_YUV422P12BE; 
    public static final int AV_PIX_FMT_YUV444P12 = AV_PIX_FMT_YUV444P12BE; 
    public static final int AV_PIX_FMT_YUV420P14 = AV_PIX_FMT_YUV420P14BE; 
    public static final int AV_PIX_FMT_YUV422P14 = AV_PIX_FMT_YUV422P14BE; 
    public static final int AV_PIX_FMT_YUV444P14 = AV_PIX_FMT_YUV444P14BE; 
    public static final int AV_PIX_FMT_YUV420P16 = AV_PIX_FMT_YUV420P16BE; 
    public static final int AV_PIX_FMT_YUV422P16 = AV_PIX_FMT_YUV422P16BE; 
    public static final int AV_PIX_FMT_YUV444P16 = AV_PIX_FMT_YUV444P16BE; 
    
    public static final int AV_PIX_FMT_RGBA64 = AV_PIX_FMT_RGBA64BE; 
    public static final int AV_PIX_FMT_BGRA64 = AV_PIX_FMT_BGRA64BE; 
    public static final int AV_PIX_FMT_GBRP9     = AV_PIX_FMT_GBRP9BE; 
    public static final int AV_PIX_FMT_GBRP10    = AV_PIX_FMT_GBRP10BE; 
    public static final int AV_PIX_FMT_GBRP12    = AV_PIX_FMT_GBRP12BE; 
    public static final int AV_PIX_FMT_GBRP14    = AV_PIX_FMT_GBRP14BE; 
    public static final int AV_PIX_FMT_GBRP16    = AV_PIX_FMT_GBRP16BE; 
    public static final int AV_PIX_FMT_GBRAP16   = AV_PIX_FMT_GBRAP16BE; 
    
    public static final int AV_PIX_FMT_BAYER_BGGR16 = AV_PIX_FMT_BAYER_BGGR16BE; 
    public static final int AV_PIX_FMT_BAYER_RGGB16 = AV_PIX_FMT_BAYER_RGGB16BE; 
    public static final int AV_PIX_FMT_BAYER_GBRG16 = AV_PIX_FMT_BAYER_GBRG16BE; 
    public static final int AV_PIX_FMT_BAYER_GRBG16 = AV_PIX_FMT_BAYER_GRBG16BE; 
    
    public static final int AV_PIX_FMT_YUVA420P9  = AV_PIX_FMT_YUVA420P9BE; 
    public static final int AV_PIX_FMT_YUVA422P9  = AV_PIX_FMT_YUVA422P9BE; 
    public static final int AV_PIX_FMT_YUVA444P9  = AV_PIX_FMT_YUVA444P9BE; 
    public static final int AV_PIX_FMT_YUVA420P10 = AV_PIX_FMT_YUVA420P10BE; 
    public static final int AV_PIX_FMT_YUVA422P10 = AV_PIX_FMT_YUVA422P10BE; 
    public static final int AV_PIX_FMT_YUVA444P10 = AV_PIX_FMT_YUVA444P10BE; 
    public static final int AV_PIX_FMT_YUVA420P16 = AV_PIX_FMT_YUVA420P16BE; 
    public static final int AV_PIX_FMT_YUVA422P16 = AV_PIX_FMT_YUVA422P16BE; 
    public static final int AV_PIX_FMT_YUVA444P16 = AV_PIX_FMT_YUVA444P16BE; 
    
    public static final int AV_PIX_FMT_XYZ12      = AV_PIX_FMT_XYZ12BE; 
    public static final int AV_PIX_FMT_NV20       = AV_PIX_FMT_NV20BE; 


    /**
     * Audio Sample Formats
     *
     * @par
     * The data described by the sample format is always in native-endian order.
     * Sample values can be expressed by native C types, hence the lack of a signed
     * 24-bit sample format even though it is a common raw audio data format.
     *
     * @par
     * The floating-point formats are based on full volume being in the range
     * [-1.0, 1.0]. Any values outside this range are beyond full volume level.
     *
     * @par
     * The data layout as used in av_samples_fill_arrays() and elsewhere in FFmpeg
     * (such as AVFrame in libavcodec) is as follows:
     *
     * For planar sample formats, each audio channel is in a separate data plane,
     * and linesize is the nativeBuffer size, in bytes, for a single plane. All data
     * planes must be the same size. For packed sample formats, only the first data
     * plane is used, and samples for each channel are interleaved. In this case,
     * linesize is the nativeBuffer size, in bytes, for the 1 plane.
     */
    
    public static final int AV_SAMPLE_FMT_NONE = 0xFFFFFFFF; 
    public static final int AV_SAMPLE_FMT_U8 = 0x00000000; ///< unsigned 8 bits
    public static final int AV_SAMPLE_FMT_S16 = 0x00000001; ///< signed 16 bits
    public static final int AV_SAMPLE_FMT_S32 = 0x00000002; ///< signed 32 bits
    public static final int AV_SAMPLE_FMT_FLT = 0x00000003; ///< float
    public static final int AV_SAMPLE_FMT_DBL = 0x00000004; ///< double
    
    public static final int AV_SAMPLE_FMT_U8P = 0x00000005; ///< unsigned 8 bits, planar
    public static final int AV_SAMPLE_FMT_S16P = 0x00000006; ///< signed 16 bits, planar
    public static final int AV_SAMPLE_FMT_S32P = 0x00000007; ///< signed 32 bits, planar
    public static final int AV_SAMPLE_FMT_FLTP = 0x00000008; ///< float, planar
    public static final int AV_SAMPLE_FMT_DBLP = 0x00000009; ///< double, planar
    
    public static final int AV_SAMPLE_FMT_NB           = 0x0000000A; ///< Number of sample formats. DO NOT USE if linking dynamically




    //==============================================================
    // 02_codec
    //==============================================================

    public static final int AV_PKT_FLAG_KEY     = 0x00000001; ///< The packet contains a keyframe
    public static final int AV_PKT_FLAG_CORRUPT = 0x00000002; ///< The packet content is corrupted
    
    /**
     * Identify the syntax and semantics of the bitstream.
     * The principle is roughly:
     * Two decoders with the same ID can decode the same streams.
     * Two encoders with the same ID can encode compatible streams.
     * There may be slight deviations from the principle due to implementation
     * details.
     *
     * If you add a codec ID to this list, add it so that
     * 1. no value of a existing codec ID changes (that would break ABI),
     * 2. Give it a value which when taken as ASCII is recognized uniquely by a human as this specific codec.
     *    This ensures that 2 forks can independently add AVCodecIDs without producing conflicts.
     *
     * After adding new codec IDs, do not forget to add an entry to the codec
     * descriptor list and bump libavcodec minor version.
     */
    
    public static final int AV_CODEC_ID_NONE = 0x00000000; 
    
    /* video codecs */
    public static final int AV_CODEC_ID_MPEG1VIDEO = 0x00000001; 
    public static final int AV_CODEC_ID_MPEG2VIDEO = 0x00000002; ///< preferred ID for MPEG-1/2 video decoding
    public static final int AV_CODEC_ID_MPEG2VIDEO_XVMC = 0x00000003; 
    public static final int AV_CODEC_ID_H261 = 0x00000004; 
    public static final int AV_CODEC_ID_H263 = 0x00000005; 
    public static final int AV_CODEC_ID_RV10 = 0x00000006; 
    public static final int AV_CODEC_ID_RV20 = 0x00000007; 
    public static final int AV_CODEC_ID_MJPEG = 0x00000008; 
    public static final int AV_CODEC_ID_MJPEGB = 0x00000009; 
    public static final int AV_CODEC_ID_LJPEG = 0x0000000A; 
    public static final int AV_CODEC_ID_SP5X = 0x0000000B; 
    public static final int AV_CODEC_ID_JPEGLS = 0x0000000C; 
    public static final int AV_CODEC_ID_MPEG4 = 0x0000000D; 
    public static final int AV_CODEC_ID_RAWVIDEO = 0x0000000E; 
    public static final int AV_CODEC_ID_MSMPEG4V1 = 0x0000000F; 
    public static final int AV_CODEC_ID_MSMPEG4V2 = 0x00000010; 
    public static final int AV_CODEC_ID_MSMPEG4V3 = 0x00000011; 
    public static final int AV_CODEC_ID_WMV1 = 0x00000012; 
    public static final int AV_CODEC_ID_WMV2 = 0x00000013; 
    public static final int AV_CODEC_ID_H263P = 0x00000014; 
    public static final int AV_CODEC_ID_H263I = 0x00000015; 
    public static final int AV_CODEC_ID_FLV1 = 0x00000016; 
    public static final int AV_CODEC_ID_SVQ1 = 0x00000017; 
    public static final int AV_CODEC_ID_SVQ3 = 0x00000018; 
    public static final int AV_CODEC_ID_DVVIDEO = 0x00000019; 
    public static final int AV_CODEC_ID_HUFFYUV = 0x0000001A; 
    public static final int AV_CODEC_ID_CYUV = 0x0000001B; 
    public static final int AV_CODEC_ID_H264 = 0x0000001C; 
    public static final int AV_CODEC_ID_INDEO3 = 0x0000001D; 
    public static final int AV_CODEC_ID_VP3 = 0x0000001E; 
    public static final int AV_CODEC_ID_THEORA = 0x0000001F; 
    public static final int AV_CODEC_ID_ASV1 = 0x00000020; 
    public static final int AV_CODEC_ID_ASV2 = 0x00000021; 
    public static final int AV_CODEC_ID_FFV1 = 0x00000022; 
    public static final int AV_CODEC_ID_4XM = 0x00000023; 
    public static final int AV_CODEC_ID_VCR1 = 0x00000024; 
    public static final int AV_CODEC_ID_CLJR = 0x00000025; 
    public static final int AV_CODEC_ID_MDEC = 0x00000026; 
    public static final int AV_CODEC_ID_ROQ = 0x00000027; 
    public static final int AV_CODEC_ID_INTERPLAY_VIDEO = 0x00000028; 
    public static final int AV_CODEC_ID_XAN_WC3 = 0x00000029; 
    public static final int AV_CODEC_ID_XAN_WC4 = 0x0000002A; 
    public static final int AV_CODEC_ID_RPZA = 0x0000002B; 
    public static final int AV_CODEC_ID_CINEPAK = 0x0000002C; 
    public static final int AV_CODEC_ID_WS_VQA = 0x0000002D; 
    public static final int AV_CODEC_ID_MSRLE = 0x0000002E; 
    public static final int AV_CODEC_ID_MSVIDEO1 = 0x0000002F; 
    public static final int AV_CODEC_ID_IDCIN = 0x00000030; 
    public static final int AV_CODEC_ID_8BPS = 0x00000031; 
    public static final int AV_CODEC_ID_SMC = 0x00000032; 
    public static final int AV_CODEC_ID_FLIC = 0x00000033; 
    public static final int AV_CODEC_ID_TRUEMOTION1 = 0x00000034; 
    public static final int AV_CODEC_ID_VMDVIDEO = 0x00000035; 
    public static final int AV_CODEC_ID_MSZH = 0x00000036; 
    public static final int AV_CODEC_ID_ZLIB = 0x00000037; 
    public static final int AV_CODEC_ID_QTRLE = 0x00000038; 
    public static final int AV_CODEC_ID_TSCC = 0x00000039; 
    public static final int AV_CODEC_ID_ULTI = 0x0000003A; 
    public static final int AV_CODEC_ID_QDRAW = 0x0000003B; 
    public static final int AV_CODEC_ID_VIXL = 0x0000003C; 
    public static final int AV_CODEC_ID_QPEG = 0x0000003D; 
    public static final int AV_CODEC_ID_PNG = 0x0000003E; 
    public static final int AV_CODEC_ID_PPM = 0x0000003F; 
    public static final int AV_CODEC_ID_PBM = 0x00000040; 
    public static final int AV_CODEC_ID_PGM = 0x00000041; 
    public static final int AV_CODEC_ID_PGMYUV = 0x00000042; 
    public static final int AV_CODEC_ID_PAM = 0x00000043; 
    public static final int AV_CODEC_ID_FFVHUFF = 0x00000044; 
    public static final int AV_CODEC_ID_RV30 = 0x00000045; 
    public static final int AV_CODEC_ID_RV40 = 0x00000046; 
    public static final int AV_CODEC_ID_VC1 = 0x00000047; 
    public static final int AV_CODEC_ID_WMV3 = 0x00000048; 
    public static final int AV_CODEC_ID_LOCO = 0x00000049; 
    public static final int AV_CODEC_ID_WNV1 = 0x0000004A; 
    public static final int AV_CODEC_ID_AASC = 0x0000004B; 
    public static final int AV_CODEC_ID_INDEO2 = 0x0000004C; 
    public static final int AV_CODEC_ID_FRAPS = 0x0000004D; 
    public static final int AV_CODEC_ID_TRUEMOTION2 = 0x0000004E; 
    public static final int AV_CODEC_ID_BMP = 0x0000004F; 
    public static final int AV_CODEC_ID_CSCD = 0x00000050; 
    public static final int AV_CODEC_ID_MMVIDEO = 0x00000051; 
    public static final int AV_CODEC_ID_ZMBV = 0x00000052; 
    public static final int AV_CODEC_ID_AVS = 0x00000053; 
    public static final int AV_CODEC_ID_SMACKVIDEO = 0x00000054; 
    public static final int AV_CODEC_ID_NUV = 0x00000055; 
    public static final int AV_CODEC_ID_KMVC = 0x00000056; 
    public static final int AV_CODEC_ID_FLASHSV = 0x00000057; 
    public static final int AV_CODEC_ID_CAVS = 0x00000058; 
    public static final int AV_CODEC_ID_JPEG2000 = 0x00000059; 
    public static final int AV_CODEC_ID_VMNC = 0x0000005A; 
    public static final int AV_CODEC_ID_VP5 = 0x0000005B; 
    public static final int AV_CODEC_ID_VP6 = 0x0000005C; 
    public static final int AV_CODEC_ID_VP6F = 0x0000005D; 
    public static final int AV_CODEC_ID_TARGA = 0x0000005E; 
    public static final int AV_CODEC_ID_DSICINVIDEO = 0x0000005F; 
    public static final int AV_CODEC_ID_TIERTEXSEQVIDEO = 0x00000060; 
    public static final int AV_CODEC_ID_TIFF = 0x00000061; 
    public static final int AV_CODEC_ID_GIF = 0x00000062; 
    public static final int AV_CODEC_ID_DXA = 0x00000063; 
    public static final int AV_CODEC_ID_DNXHD = 0x00000064; 
    public static final int AV_CODEC_ID_THP = 0x00000065; 
    public static final int AV_CODEC_ID_SGI = 0x00000066; 
    public static final int AV_CODEC_ID_C93 = 0x00000067; 
    public static final int AV_CODEC_ID_BETHSOFTVID = 0x00000068; 
    public static final int AV_CODEC_ID_PTX = 0x00000069; 
    public static final int AV_CODEC_ID_TXD = 0x0000006A; 
    public static final int AV_CODEC_ID_VP6A = 0x0000006B; 
    public static final int AV_CODEC_ID_AMV = 0x0000006C; 
    public static final int AV_CODEC_ID_VB = 0x0000006D; 
    public static final int AV_CODEC_ID_PCX = 0x0000006E; 
    public static final int AV_CODEC_ID_SUNRAST = 0x0000006F; 
    public static final int AV_CODEC_ID_INDEO4 = 0x00000070; 
    public static final int AV_CODEC_ID_INDEO5 = 0x00000071; 
    public static final int AV_CODEC_ID_MIMIC = 0x00000072; 
    public static final int AV_CODEC_ID_RL2 = 0x00000073; 
    public static final int AV_CODEC_ID_ESCAPE124 = 0x00000074; 
    public static final int AV_CODEC_ID_DIRAC = 0x00000075; 
    public static final int AV_CODEC_ID_BFI = 0x00000076; 
    public static final int AV_CODEC_ID_CMV = 0x00000077; 
    public static final int AV_CODEC_ID_MOTIONPIXELS = 0x00000078; 
    public static final int AV_CODEC_ID_TGV = 0x00000079; 
    public static final int AV_CODEC_ID_TGQ = 0x0000007A; 
    public static final int AV_CODEC_ID_TQI = 0x0000007B; 
    public static final int AV_CODEC_ID_AURA = 0x0000007C; 
    public static final int AV_CODEC_ID_AURA2 = 0x0000007D; 
    public static final int AV_CODEC_ID_V210X = 0x0000007E; 
    public static final int AV_CODEC_ID_TMV = 0x0000007F; 
    public static final int AV_CODEC_ID_V210 = 0x00000080; 
    public static final int AV_CODEC_ID_DPX = 0x00000081; 
    public static final int AV_CODEC_ID_MAD = 0x00000082; 
    public static final int AV_CODEC_ID_FRWU = 0x00000083; 
    public static final int AV_CODEC_ID_FLASHSV2 = 0x00000084; 
    public static final int AV_CODEC_ID_CDGRAPHICS = 0x00000085; 
    public static final int AV_CODEC_ID_R210 = 0x00000086; 
    public static final int AV_CODEC_ID_ANM = 0x00000087; 
    public static final int AV_CODEC_ID_BINKVIDEO = 0x00000088; 
    public static final int AV_CODEC_ID_IFF_ILBM = 0x00000089; 
    public static final int AV_CODEC_ID_IFF_BYTERUN1 = 0x0000008A; 
    public static final int AV_CODEC_ID_KGV1 = 0x0000008B; 
    public static final int AV_CODEC_ID_YOP = 0x0000008C; 
    public static final int AV_CODEC_ID_VP8 = 0x0000008D; 
    public static final int AV_CODEC_ID_PICTOR = 0x0000008E; 
    public static final int AV_CODEC_ID_ANSI = 0x0000008F; 
    public static final int AV_CODEC_ID_A64_MULTI = 0x00000090; 
    public static final int AV_CODEC_ID_A64_MULTI5 = 0x00000091; 
    public static final int AV_CODEC_ID_R10K = 0x00000092; 
    public static final int AV_CODEC_ID_MXPEG = 0x00000093; 
    public static final int AV_CODEC_ID_LAGARITH = 0x00000094; 
    public static final int AV_CODEC_ID_PRORES = 0x00000095; 
    public static final int AV_CODEC_ID_JV = 0x00000096; 
    public static final int AV_CODEC_ID_DFA = 0x00000097; 
    public static final int AV_CODEC_ID_WMV3IMAGE = 0x00000098; 
    public static final int AV_CODEC_ID_VC1IMAGE = 0x00000099; 
    public static final int AV_CODEC_ID_UTVIDEO = 0x0000009A; 
    public static final int AV_CODEC_ID_BMV_VIDEO = 0x0000009B; 
    public static final int AV_CODEC_ID_VBLE = 0x0000009C; 
    public static final int AV_CODEC_ID_DXTORY = 0x0000009D; 
    public static final int AV_CODEC_ID_V410 = 0x0000009E; 
    public static final int AV_CODEC_ID_XWD = 0x0000009F; 
    public static final int AV_CODEC_ID_CDXL = 0x000000A0; 
    public static final int AV_CODEC_ID_XBM = 0x000000A1; 
    public static final int AV_CODEC_ID_ZEROCODEC = 0x000000A2; 
    public static final int AV_CODEC_ID_MSS1 = 0x000000A3; 
    public static final int AV_CODEC_ID_MSA1 = 0x000000A4; 
    public static final int AV_CODEC_ID_TSCC2 = 0x000000A5; 
    public static final int AV_CODEC_ID_MTS2 = 0x000000A6; 
    public static final int AV_CODEC_ID_CLLC = 0x000000A7; 
    public static final int AV_CODEC_ID_MSS2 = 0x000000A8; 
    public static final int AV_CODEC_ID_VP9 = 0x000000A9; 
    public static final int AV_CODEC_ID_AIC = 0x000000AA; 
    public static final int AV_CODEC_ID_ESCAPE130_DEPRECATED = 0x000000AB; 
    public static final int AV_CODEC_ID_G2M_DEPRECATED = 0x000000AC; 
    public static final int AV_CODEC_ID_WEBP_DEPRECATED = 0x000000AD; 
    
    public static final int AV_CODEC_ID_BRENDER_PIX = 0x42504958; 
    public static final int AV_CODEC_ID_Y41P       = 0x59343150; 
    public static final int AV_CODEC_ID_ESCAPE130  = 0x45313330; 
    public static final int AV_CODEC_ID_EXR        = 0x30455852; 
    public static final int AV_CODEC_ID_AVRP       = 0x41565250; 
    
    public static final int AV_CODEC_ID_012V       = 0x30313256; 
    public static final int AV_CODEC_ID_G2M        = 0x0047324D; 
    public static final int AV_CODEC_ID_AVUI       = 0x41565549; 
    public static final int AV_CODEC_ID_AYUV       = 0x41595556; 
    public static final int AV_CODEC_ID_TARGA_Y216 = 0x54323136; 
    public static final int AV_CODEC_ID_V308       = 0x56333038; 
    public static final int AV_CODEC_ID_V408       = 0x56343038; 
    public static final int AV_CODEC_ID_YUV4       = 0x59555634; 
    public static final int AV_CODEC_ID_SANM       = 0x53414E4D; 
    public static final int AV_CODEC_ID_PAF_VIDEO  = 0x50414656; 
    public static final int AV_CODEC_ID_AVRN       = 0x4156526E; 
    public static final int AV_CODEC_ID_CPIA       = 0x43504941; 
    public static final int AV_CODEC_ID_XFACE      = 0x58464143; 
    public static final int AV_CODEC_ID_SGIRLE     = 0x53474952; 
    public static final int AV_CODEC_ID_MVC1       = 0x4D564331; 
    public static final int AV_CODEC_ID_MVC2       = 0x4D564332; 
    public static final int AV_CODEC_ID_SNOW       = 0x534E4F57; 
    public static final int AV_CODEC_ID_WEBP       = 0x57454250; 
    public static final int AV_CODEC_ID_SMVJPEG    = 0x534D564A; 
    public static final int AV_CODEC_ID_HEVC       = 0x48323635; 
    public static final int AV_CODEC_ID_H265 = AV_CODEC_ID_HEVC; 
    
    /* various PCM "codecs" */
    public static final int AV_CODEC_ID_FIRST_AUDIO = 0x00010000; ///< A dummy id pointing at the start of audio codecs
    public static final int AV_CODEC_ID_PCM_S16LE = 0x00010000; 
    public static final int AV_CODEC_ID_PCM_S16BE = 0x00010001; 
    public static final int AV_CODEC_ID_PCM_U16LE = 0x00010002; 
    public static final int AV_CODEC_ID_PCM_U16BE = 0x00010003; 
    public static final int AV_CODEC_ID_PCM_S8 = 0x00010004; 
    public static final int AV_CODEC_ID_PCM_U8 = 0x00010005; 
    public static final int AV_CODEC_ID_PCM_MULAW = 0x00010006; 
    public static final int AV_CODEC_ID_PCM_ALAW = 0x00010007; 
    public static final int AV_CODEC_ID_PCM_S32LE = 0x00010008; 
    public static final int AV_CODEC_ID_PCM_S32BE = 0x00010009; 
    public static final int AV_CODEC_ID_PCM_U32LE = 0x0001000A; 
    public static final int AV_CODEC_ID_PCM_U32BE = 0x0001000B; 
    public static final int AV_CODEC_ID_PCM_S24LE = 0x0001000C; 
    public static final int AV_CODEC_ID_PCM_S24BE = 0x0001000D; 
    public static final int AV_CODEC_ID_PCM_U24LE = 0x0001000E; 
    public static final int AV_CODEC_ID_PCM_U24BE = 0x0001000F; 
    public static final int AV_CODEC_ID_PCM_S24DAUD = 0x00010010; 
    public static final int AV_CODEC_ID_PCM_ZORK = 0x00010011; 
    public static final int AV_CODEC_ID_PCM_S16LE_PLANAR = 0x00010012; 
    public static final int AV_CODEC_ID_PCM_DVD = 0x00010013; 
    public static final int AV_CODEC_ID_PCM_F32BE = 0x00010014; 
    public static final int AV_CODEC_ID_PCM_F32LE = 0x00010015; 
    public static final int AV_CODEC_ID_PCM_F64BE = 0x00010016; 
    public static final int AV_CODEC_ID_PCM_F64LE = 0x00010017; 
    public static final int AV_CODEC_ID_PCM_BLURAY = 0x00010018; 
    public static final int AV_CODEC_ID_PCM_LXF = 0x00010019; 
    public static final int AV_CODEC_ID_S302M = 0x0001001A; 
    public static final int AV_CODEC_ID_PCM_S8_PLANAR = 0x0001001B; 
    public static final int AV_CODEC_ID_PCM_S24LE_PLANAR_DEPRECATED = 0x0001001C; 
    public static final int AV_CODEC_ID_PCM_S32LE_PLANAR_DEPRECATED = 0x0001001D; 
    public static final int AV_CODEC_ID_PCM_S24LE_PLANAR = 0x18505350; 
    public static final int AV_CODEC_ID_PCM_S32LE_PLANAR = 0x20505350; 
    public static final int AV_CODEC_ID_PCM_S16BE_PLANAR = 0x50535010; 
    
    /* various ADPCM codecs */
    public static final int AV_CODEC_ID_ADPCM_IMA_QT = 0x00011000; 
    public static final int AV_CODEC_ID_ADPCM_IMA_WAV = 0x00011001; 
    public static final int AV_CODEC_ID_ADPCM_IMA_DK3 = 0x00011002; 
    public static final int AV_CODEC_ID_ADPCM_IMA_DK4 = 0x00011003; 
    public static final int AV_CODEC_ID_ADPCM_IMA_WS = 0x00011004; 
    public static final int AV_CODEC_ID_ADPCM_IMA_SMJPEG = 0x00011005; 
    public static final int AV_CODEC_ID_ADPCM_MS = 0x00011006; 
    public static final int AV_CODEC_ID_ADPCM_4XM = 0x00011007; 
    public static final int AV_CODEC_ID_ADPCM_XA = 0x00011008; 
    public static final int AV_CODEC_ID_ADPCM_ADX = 0x00011009; 
    public static final int AV_CODEC_ID_ADPCM_EA = 0x0001100A; 
    public static final int AV_CODEC_ID_ADPCM_G726 = 0x0001100B; 
    public static final int AV_CODEC_ID_ADPCM_CT = 0x0001100C; 
    public static final int AV_CODEC_ID_ADPCM_SWF = 0x0001100D; 
    public static final int AV_CODEC_ID_ADPCM_YAMAHA = 0x0001100E; 
    public static final int AV_CODEC_ID_ADPCM_SBPRO_4 = 0x0001100F; 
    public static final int AV_CODEC_ID_ADPCM_SBPRO_3 = 0x00011010; 
    public static final int AV_CODEC_ID_ADPCM_SBPRO_2 = 0x00011011; 
    public static final int AV_CODEC_ID_ADPCM_THP = 0x00011012; 
    public static final int AV_CODEC_ID_ADPCM_IMA_AMV = 0x00011013; 
    public static final int AV_CODEC_ID_ADPCM_EA_R1 = 0x00011014; 
    public static final int AV_CODEC_ID_ADPCM_EA_R3 = 0x00011015; 
    public static final int AV_CODEC_ID_ADPCM_EA_R2 = 0x00011016; 
    public static final int AV_CODEC_ID_ADPCM_IMA_EA_SEAD = 0x00011017; 
    public static final int AV_CODEC_ID_ADPCM_IMA_EA_EACS = 0x00011018; 
    public static final int AV_CODEC_ID_ADPCM_EA_XAS = 0x00011019; 
    public static final int AV_CODEC_ID_ADPCM_EA_MAXIS_XA = 0x0001101A; 
    public static final int AV_CODEC_ID_ADPCM_IMA_ISS = 0x0001101B; 
    public static final int AV_CODEC_ID_ADPCM_G722 = 0x0001101C; 
    public static final int AV_CODEC_ID_ADPCM_IMA_APC = 0x0001101D; 
    public static final int AV_CODEC_ID_VIMA       = 0x56494D41; 
    public static final int AV_CODEC_ID_ADPCM_AFC  = 0x41464320; 
    public static final int AV_CODEC_ID_ADPCM_IMA_OKI = 0x4F4B4920; 
    public static final int AV_CODEC_ID_ADPCM_DTK  = 0x44544B20; 
    public static final int AV_CODEC_ID_ADPCM_IMA_RAD = 0x52414420; 
    public static final int AV_CODEC_ID_ADPCM_G726LE = 0x36323747; 
    
    /* AMR */
    public static final int AV_CODEC_ID_AMR_NB = 0x00012000; 
    public static final int AV_CODEC_ID_AMR_WB = 0x00012001; 
    
    /* RealAudio codecs*/
    public static final int AV_CODEC_ID_RA_144 = 0x00013000; 
    public static final int AV_CODEC_ID_RA_288 = 0x00013001; 
    
    /* various DPCM codecs */
    public static final int AV_CODEC_ID_ROQ_DPCM = 0x00014000; 
    public static final int AV_CODEC_ID_INTERPLAY_DPCM = 0x00014001; 
    public static final int AV_CODEC_ID_XAN_DPCM = 0x00014002; 
    public static final int AV_CODEC_ID_SOL_DPCM = 0x00014003; 
    
    /* audio codecs */
    public static final int AV_CODEC_ID_MP2 = 0x00015000; 
    public static final int AV_CODEC_ID_MP3 = 0x00015001; ///< preferred ID for decoding MPEG audio layer 1, 2 or 3
    public static final int AV_CODEC_ID_AAC = 0x00015002; 
    public static final int AV_CODEC_ID_AC3 = 0x00015003; 
    public static final int AV_CODEC_ID_DTS = 0x00015004; 
    public static final int AV_CODEC_ID_VORBIS = 0x00015005; 
    public static final int AV_CODEC_ID_DVAUDIO = 0x00015006; 
    public static final int AV_CODEC_ID_WMAV1 = 0x00015007; 
    public static final int AV_CODEC_ID_WMAV2 = 0x00015008; 
    public static final int AV_CODEC_ID_MACE3 = 0x00015009; 
    public static final int AV_CODEC_ID_MACE6 = 0x0001500A; 
    public static final int AV_CODEC_ID_VMDAUDIO = 0x0001500B; 
    public static final int AV_CODEC_ID_FLAC = 0x0001500C; 
    public static final int AV_CODEC_ID_MP3ADU = 0x0001500D; 
    public static final int AV_CODEC_ID_MP3ON4 = 0x0001500E; 
    public static final int AV_CODEC_ID_SHORTEN = 0x0001500F; 
    public static final int AV_CODEC_ID_ALAC = 0x00015010; 
    public static final int AV_CODEC_ID_WESTWOOD_SND1 = 0x00015011; 
    public static final int AV_CODEC_ID_GSM = 0x00015012; ///< as in Berlin toast format
    public static final int AV_CODEC_ID_QDM2 = 0x00015013; 
    public static final int AV_CODEC_ID_COOK = 0x00015014; 
    public static final int AV_CODEC_ID_TRUESPEECH = 0x00015015; 
    public static final int AV_CODEC_ID_TTA = 0x00015016; 
    public static final int AV_CODEC_ID_SMACKAUDIO = 0x00015017; 
    public static final int AV_CODEC_ID_QCELP = 0x00015018; 
    public static final int AV_CODEC_ID_WAVPACK = 0x00015019; 
    public static final int AV_CODEC_ID_DSICINAUDIO = 0x0001501A; 
    public static final int AV_CODEC_ID_IMC = 0x0001501B; 
    public static final int AV_CODEC_ID_MUSEPACK7 = 0x0001501C; 
    public static final int AV_CODEC_ID_MLP = 0x0001501D; 
    public static final int AV_CODEC_ID_GSM_MS = 0x0001501E; /* as found in WAV */
    public static final int AV_CODEC_ID_ATRAC3 = 0x0001501F; 
    public static final int AV_CODEC_ID_VOXWARE = 0x00015020; 
    public static final int AV_CODEC_ID_APE = 0x00015021; 
    public static final int AV_CODEC_ID_NELLYMOSER = 0x00015022; 
    public static final int AV_CODEC_ID_MUSEPACK8 = 0x00015023; 
    public static final int AV_CODEC_ID_SPEEX = 0x00015024; 
    public static final int AV_CODEC_ID_WMAVOICE = 0x00015025; 
    public static final int AV_CODEC_ID_WMAPRO = 0x00015026; 
    public static final int AV_CODEC_ID_WMALOSSLESS = 0x00015027; 
    public static final int AV_CODEC_ID_ATRAC3P = 0x00015028; 
    public static final int AV_CODEC_ID_EAC3 = 0x00015029; 
    public static final int AV_CODEC_ID_SIPR = 0x0001502A; 
    public static final int AV_CODEC_ID_MP1 = 0x0001502B; 
    public static final int AV_CODEC_ID_TWINVQ = 0x0001502C; 
    public static final int AV_CODEC_ID_TRUEHD = 0x0001502D; 
    public static final int AV_CODEC_ID_MP4ALS = 0x0001502E; 
    public static final int AV_CODEC_ID_ATRAC1 = 0x0001502F; 
    public static final int AV_CODEC_ID_BINKAUDIO_RDFT = 0x00015030; 
    public static final int AV_CODEC_ID_BINKAUDIO_DCT = 0x00015031; 
    public static final int AV_CODEC_ID_AAC_LATM = 0x00015032; 
    public static final int AV_CODEC_ID_QDMC = 0x00015033; 
    public static final int AV_CODEC_ID_CELT = 0x00015034; 
    public static final int AV_CODEC_ID_G723_1 = 0x00015035; 
    public static final int AV_CODEC_ID_G729 = 0x00015036; 
    public static final int AV_CODEC_ID_8SVX_EXP = 0x00015037; 
    public static final int AV_CODEC_ID_8SVX_FIB = 0x00015038; 
    public static final int AV_CODEC_ID_BMV_AUDIO = 0x00015039; 
    public static final int AV_CODEC_ID_RALF = 0x0001503A; 
    public static final int AV_CODEC_ID_IAC = 0x0001503B; 
    public static final int AV_CODEC_ID_ILBC = 0x0001503C; 
    public static final int AV_CODEC_ID_OPUS_DEPRECATED = 0x0001503D; 
    public static final int AV_CODEC_ID_COMFORT_NOISE = 0x0001503E; 
    public static final int AV_CODEC_ID_TAK_DEPRECATED = 0x0001503F; 
    public static final int AV_CODEC_ID_METASOUND = 0x00015040; 
    public static final int AV_CODEC_ID_FFWAVESYNTH = 0x46465753; 
    public static final int AV_CODEC_ID_SONIC       = 0x534F4E43; 
    public static final int AV_CODEC_ID_SONIC_LS    = 0x534F4E4C; 
    public static final int AV_CODEC_ID_PAF_AUDIO   = 0x50414641; 
    public static final int AV_CODEC_ID_OPUS        = 0x4F505553; 
    public static final int AV_CODEC_ID_TAK         = 0x7442614B; 
    public static final int AV_CODEC_ID_EVRC        = 0x73657663; 
    public static final int AV_CODEC_ID_SMV         = 0x73736D76; 
    
    /* subtitle codecs */
    public static final int AV_CODEC_ID_FIRST_SUBTITLE = 0x00017000; ///< A dummy ID pointing at the start of subtitle codecs.
    public static final int AV_CODEC_ID_DVD_SUBTITLE = 0x00017000; 
    public static final int AV_CODEC_ID_DVB_SUBTITLE = 0x00017001; 
    public static final int AV_CODEC_ID_TEXT = 0x00017002; ///< raw UTF-8 text
    public static final int AV_CODEC_ID_XSUB = 0x00017003; 
    public static final int AV_CODEC_ID_SSA = 0x00017004; 
    public static final int AV_CODEC_ID_MOV_TEXT = 0x00017005; 
    public static final int AV_CODEC_ID_HDMV_PGS_SUBTITLE = 0x00017006; 
    public static final int AV_CODEC_ID_DVB_TELETEXT = 0x00017007; 
    public static final int AV_CODEC_ID_SRT = 0x00017008; 
    public static final int AV_CODEC_ID_MICRODVD   = 0x6D445644; 
    public static final int AV_CODEC_ID_EIA_608    = 0x63363038; 
    public static final int AV_CODEC_ID_JACOSUB    = 0x4A535542; 
    public static final int AV_CODEC_ID_SAMI       = 0x53414D49; 
    public static final int AV_CODEC_ID_REALTEXT   = 0x52545854; 
    public static final int AV_CODEC_ID_SUBVIEWER1 = 0x53625631; 
    public static final int AV_CODEC_ID_SUBVIEWER  = 0x53756256; 
    public static final int AV_CODEC_ID_SUBRIP     = 0x53526970; 
    public static final int AV_CODEC_ID_WEBVTT     = 0x57565454; 
    public static final int AV_CODEC_ID_MPL2       = 0x4D504C32; 
    public static final int AV_CODEC_ID_VPLAYER    = 0x56506C72; 
    public static final int AV_CODEC_ID_PJS        = 0x50684A53; 
    public static final int AV_CODEC_ID_ASS        = 0x41535320; ///< ASS as defined in Matroska
    
    /* other specific kind of codecs (generally used for attachments) */
    public static final int AV_CODEC_ID_FIRST_UNKNOWN = 0x00018000; ///< A dummy ID pointing at the start of various fake codecs.
    public static final int AV_CODEC_ID_TTF = 0x00018000; 
    public static final int AV_CODEC_ID_BINTEXT    = 0x42545854; 
    public static final int AV_CODEC_ID_XBIN       = 0x5842494E; 
    public static final int AV_CODEC_ID_IDF        = 0x00494446; 
    public static final int AV_CODEC_ID_OTF        = 0x004F5446; 
    public static final int AV_CODEC_ID_SMPTE_KLV  = 0x4B4C5641; 
    public static final int AV_CODEC_ID_DVD_NAV    = 0x444E4156; 
    
    
    public static final int AV_CODEC_ID_PROBE = 0x00019000; ///< codec_id is not known (like AV_CODEC_ID_NONE) but lavf should attempt to identify it
    
    public static final int AV_CODEC_ID_MPEG2TS = 0x00020000; /**< _FAKE_ codec to indicate a raw MPEG-2 TS
                                    * stream (only used by libavformat) */
    public static final int AV_CODEC_ID_MPEG4SYSTEMS = 0x00020001; /**< _FAKE_ codec to indicate a MPEG-4 Systems
                                    * stream (only used by libavformat) */
    public static final int AV_CODEC_ID_FFMETADATA = 0x00021000; ///< Dummy codec for streams containing only metadata information.
    
    
    
    
    /**
     * Codec uses only intra compression.
     * Video codecs only.
     */
    public static final int AV_CODEC_PROP_INTRA_ONLY    = (1 << 0); 
    /**
     * Codec supports lossy compression. Audio and video codecs only.
     * @note a codec may support both lossy and lossless
     * compression modes
     */
    public static final int AV_CODEC_PROP_LOSSY         = (1 << 1); 
    /**
     * Codec supports lossless compression. Audio and video codecs only.
     */
    public static final int AV_CODEC_PROP_LOSSLESS      = (1 << 2); 
    /**
     * Subtitle codec is bitmap based
     * Decoded AVSubtitle data can be read from the AVSubtitleRect->pict field.
     */
    public static final int AV_CODEC_PROP_BITMAP_SUB    = (1 << 16); 
    /**
     * Subtitle codec is text based.
     * Decoded AVSubtitle data can be read from the AVSubtitleRect->ass field.
     */
    public static final int AV_CODEC_PROP_TEXT_SUB      = (1 << 17); 
    
    /**
     * @ingroup lavc_decoding
     * Required number of additionally allocated bytes at the end of the input bitstream for decoding.
     * This is mainly needed because some optimized bitstream readers read
     * 32 or 64 bit at once and could read over the end.<br>
     * Note: If the first 23 bits of the additional bytes are not 0, then damaged
     * MPEG bitstreams could cause overread and segfault.
     */
    public static final int FF_INPUT_BUFFER_PADDING_SIZE = 0x00000010; 
    
    /**
     * @ingroup lavc_encoding
     * minimum encoding nativeBuffer size
     * Used to avoid some checks during header writing.
     */
    public static final int FF_MIN_BUFFER_SIZE = 0x00004000; 
    
    /**
     * @ingroup lavc_encoding
     * motion estimation type.
     */
    
    public static final int ME_ZERO = 0x00000001; ///< no search, that is use 0,0 vector whenever one is needed
    public static final int ME_FULL = 0x00000002; 
    public static final int ME_LOG = 0x00000003; 
    public static final int ME_PHODS = 0x00000004; 
    public static final int ME_EPZS = 0x00000005; ///< enhanced predictive zonal search
    public static final int ME_X1 = 0x00000006; ///< reserved for experiments
    public static final int ME_HEX = 0x00000007; ///< hexagon based search
    public static final int ME_UMH = 0x00000008; ///< uneven multi-hexagon search
    public static final int ME_TESA = 0x00000009; ///< transformed exhaustive search algorithm
    public static final int ME_ITER = 0x00000032; ///< iterative search
    
    
    /**
     * @ingroup lavc_decoding
     */
    
    /* We leave some space between them for extensions (drop some
         * keyframes for intra-only or drop just some bidir frames). */
    public static final int AVDISCARD_NONE    = 0xFFFFFFF0; ///< discard nothing
    public static final int AVDISCARD_DEFAULT = 0x00000000; ///< discard useless packets like 0 size packets in avi
    public static final int AVDISCARD_NONREF  = 0x00000008; ///< discard all non reference
    public static final int AVDISCARD_BIDIR   = 0x00000010; ///< discard all bidirectional frames
    public static final int AVDISCARD_NONKEY  = 0x00000020; ///< discard all frames except keyframes
    public static final int AVDISCARD_ALL     = 0x00000030; ///< discard all
    
    
    
    public static final int AVCOL_PRI_BT709       = 0x00000001; ///< also ITU-R BT1361 / IEC 61966-2-4 / SMPTE RP177 Annex B
    public static final int AVCOL_PRI_UNSPECIFIED = 0x00000002; 
    public static final int AVCOL_PRI_BT470M      = 0x00000004; 
    public static final int AVCOL_PRI_BT470BG     = 0x00000005; ///< also ITU-R BT601-6 625 / ITU-R BT1358 625 / ITU-R BT1700 625 PAL & SECAM
    public static final int AVCOL_PRI_SMPTE170M   = 0x00000006; ///< also ITU-R BT601-6 525 / ITU-R BT1358 525 / ITU-R BT1700 NTSC
    public static final int AVCOL_PRI_SMPTE240M   = 0x00000007; ///< functionally identical to above
    public static final int AVCOL_PRI_FILM        = 0x00000008; 
    public static final int AVCOL_PRI_NB             = 0x00000009; ///< Not part of ABI
    
    
    
    public static final int AVCOL_TRC_BT709       = 0x00000001; ///< also ITU-R BT1361
    public static final int AVCOL_TRC_UNSPECIFIED = 0x00000002; 
    public static final int AVCOL_TRC_GAMMA22     = 0x00000004; ///< also ITU-R BT470M / ITU-R BT1700 625 PAL & SECAM
    public static final int AVCOL_TRC_GAMMA28     = 0x00000005; ///< also ITU-R BT470BG
    public static final int AVCOL_TRC_SMPTE240M   = 0x00000007; 
    public static final int AVCOL_TRC_NB             = 0x00000008; ///< Not part of ABI
    
    
    /**
     *  X   X      3 4 X      X are luma samples,
     *             1 2        1-6 are possible chroma positions
     *  X   X      5 6 X      0 is undefined/unknown position
     */
    
    public static final int AVCHROMA_LOC_UNSPECIFIED = 0x00000000; 
    public static final int AVCHROMA_LOC_LEFT        = 0x00000001; ///< mpeg2/4, h264 default
    public static final int AVCHROMA_LOC_CENTER      = 0x00000002; ///< mpeg1, jpeg, h263
    public static final int AVCHROMA_LOC_TOPLEFT     = 0x00000003; ///< DV
    public static final int AVCHROMA_LOC_TOP         = 0x00000004; 
    public static final int AVCHROMA_LOC_BOTTOMLEFT  = 0x00000005; 
    public static final int AVCHROMA_LOC_BOTTOM      = 0x00000006; 
    public static final int AVCHROMA_LOC_NB             = 0x00000007; ///< Not part of ABI
    
    
    
    public static final int AV_AUDIO_SERVICE_TYPE_MAIN              = 0x00000000; 
    public static final int AV_AUDIO_SERVICE_TYPE_EFFECTS           = 0x00000001; 
    public static final int AV_AUDIO_SERVICE_TYPE_VISUALLY_IMPAIRED = 0x00000002; 
    public static final int AV_AUDIO_SERVICE_TYPE_HEARING_IMPAIRED  = 0x00000003; 
    public static final int AV_AUDIO_SERVICE_TYPE_DIALOGUE          = 0x00000004; 
    public static final int AV_AUDIO_SERVICE_TYPE_COMMENTARY        = 0x00000005; 
    public static final int AV_AUDIO_SERVICE_TYPE_EMERGENCY         = 0x00000006; 
    public static final int AV_AUDIO_SERVICE_TYPE_VOICE_OVER        = 0x00000007; 
    public static final int AV_AUDIO_SERVICE_TYPE_KARAOKE           = 0x00000008; 
    public static final int AV_AUDIO_SERVICE_TYPE_NB                   = 0x00000009; ///< Not part of ABI
    
    
    /**
     * Allow decoders to produce frames with data planes that are not aligned
     * to CPU requirements (e.g. due to cropping).
     */
    public static final int CODEC_FLAG_UNALIGNED = 0x00000001; 
    public static final int CODEC_FLAG_QSCALE = 0x00000002; ///< Use fixed qscale.
    public static final int CODEC_FLAG_4MV    = 0x00000004; ///< 4 MV per MB allowed / advanced prediction for H.263.
    public static final int CODEC_FLAG_QPEL   = 0x00000010; ///< Use qpel MC.
    public static final int CODEC_FLAG_GMC    = 0x00000020; ///< Use GMC.
    public static final int CODEC_FLAG_MV0    = 0x00000040; ///< Always try a MB with MV=<0,0>.
    /**
     * The parent program guarantees that the input for B-frames containing
     * streams is not written to for at least s->max_b_frames+1 frames, if
     * this is not set the input will be copied.
     */
    public static final int CODEC_FLAG_INPUT_PRESERVED = 0x00000100; 
    public static final int CODEC_FLAG_PASS1           = 0x00000200; ///< Use internal 2pass ratecontrol in first pass mode.
    public static final int CODEC_FLAG_PASS2           = 0x00000400; ///< Use internal 2pass ratecontrol in second pass mode.
    public static final int CODEC_FLAG_GRAY            = 0x00002000; ///< Only decode/encode grayscale.
    public static final int CODEC_FLAG_EMU_EDGE        = 0x00004000; ///< Don't draw edges.
    public static final int CODEC_FLAG_PSNR            = 0x00008000; ///< error[?] variables will be set during encoding.
    public static final int CODEC_FLAG_TRUNCATED       = 0x00010000; /** Input bitstream might be truncated at a random
                                                      location instead of only at frame boundaries. */
    public static final int CODEC_FLAG_NORMALIZE_AQP  = 0x00020000; ///< Normalize adaptive quantization.
    public static final int CODEC_FLAG_INTERLACED_DCT = 0x00040000; ///< Use interlaced DCT.
    public static final int CODEC_FLAG_LOW_DELAY      = 0x00080000; ///< Force low delay.
    public static final int CODEC_FLAG_GLOBAL_HEADER  = 0x00400000; ///< Place global headers in extradata instead of every keyframe.
    public static final int CODEC_FLAG_BITEXACT       = 0x00800000; ///< Use only bitexact stuff (except (I)DCT).
    /* Fx : Flag for h263+ extra options */
    public static final int CODEC_FLAG_AC_PRED        = 0x01000000; ///< H.263 advanced intra coding / MPEG-4 AC prediction
    public static final int CODEC_FLAG_LOOP_FILTER    = 0x00000800; ///< loop filter
    public static final int CODEC_FLAG_INTERLACED_ME  = 0x20000000; ///< interlaced motion estimation
    public static final int CODEC_FLAG_CLOSED_GOP     = 0x80000000; 
    public static final int CODEC_FLAG2_FAST          = 0x00000001; ///< Allow non spec compliant speedup tricks.
    public static final int CODEC_FLAG2_NO_OUTPUT     = 0x00000004; ///< Skip bitstream encoding.
    public static final int CODEC_FLAG2_LOCAL_HEADER  = 0x00000008; ///< Place global headers at every keyframe instead of in extradata.
    public static final int CODEC_FLAG2_DROP_FRAME_TIMECODE = 0x00002000; ///< timecode is in drop frame format. DEPRECATED!!!!
    public static final int CODEC_FLAG2_IGNORE_CROP   = 0x00010000; ///< Discard cropping information from SPS.
    
    public static final int CODEC_FLAG2_CHUNKS        = 0x00008000; ///< Input bitstream might be truncated at a packet boundaries instead of only at frame boundaries.
    public static final int CODEC_FLAG2_SHOW_ALL      = 0x00400000; ///< Show all frames before the first keyframe
    
    /* Unsupported options :
     *              Syntax Arithmetic coding (SAC)
     *              Reference Picture Selection
     *              Independent Segment Decoding */
    /* /Fx */
    /* codec capabilities */
    
    public static final int CODEC_CAP_DRAW_HORIZ_BAND = 0x00000001; ///< Decoder can use draw_horiz_band callback.
    /**
     * Codec uses get_buffer() for allocating buffers and supports custom allocators.
     * If not set, it might not use get_buffer() at all or use operations that
     * assume the nativeBuffer was allocated by avcodec_default_get_buffer.
     */
    public static final int CODEC_CAP_DR1             = 0x00000002; 
    public static final int CODEC_CAP_TRUNCATED       = 0x00000008; 
    /* Codec can export data for HW decoding (XvMC). */
    public static final int CODEC_CAP_HWACCEL         = 0x00000010; 
    /**
     * Encoder or decoder requires flushing with NULL input at the end in order to
     * give the complete and correct output.
     *
     * NOTE: If this flag is not set, the codec is guaranteed to never be fed with
     *       with NULL data. The user can still send NULL data to the public encode
     *       or decode function, but libavcodec will not pass it along to the codec
     *       unless this flag is set.
     *
     * Decoders:
     * The decoder has a non-zero delay and needs to be fed with avpkt->data=NULL,
     * avpkt->size=0 at the end to get the delayed data until the decoder no longer
     * returns frames.
     *
     * Encoders:
     * The encoder needs to be fed with NULL data at the end of encoding until the
     * encoder no longer returns data.
     *
     * NOTE: For encoders implementing the AVCodec.encode2() function, setting this
     *       flag also means that the encoder must set the pts and duration for
     *       each output packet. If this flag is not set, the pts and duration will
     *       be determined by libavcodec from the input frame.
     */
    public static final int CODEC_CAP_DELAY           = 0x00000020; 
    /**
     * Codec can be fed a final frame with a smaller size.
     * This can be used to prevent truncation of the last audio samples.
     */
    public static final int CODEC_CAP_SMALL_LAST_FRAME = 0x00000040; 
    /**
     * Codec can export data for HW decoding (VDPAU).
     */
    public static final int CODEC_CAP_HWACCEL_VDPAU    = 0x00000080; 
    /**
     * Codec can output multiple frames per AVPacket
     * Normally demuxers return one frame at a time, demuxers which do not do
     * are connected to a parser to split what they return into proper frames.
     * This flag is reserved to the very rare category of codecs which have a
     * bitstream that cannot be split into frames without timeconsuming
     * operations like full decoding. Demuxers carring such bitstreams thus
     * may return multiple frames in a packet. This has many disadvantages like
     * prohibiting stream copy in many cases thus it should only be considered
     * as a last resort.
     */
    public static final int CODEC_CAP_SUBFRAMES        = 0x00000100; 
    /**
     * Codec is experimental and is thus avoided in favor of non experimental
     * encoders
     */
    public static final int CODEC_CAP_EXPERIMENTAL     = 0x00000200; 
    /**
     * Codec should fill in channel configuration and samplerate instead of container
     */
    public static final int CODEC_CAP_CHANNEL_CONF     = 0x00000400; 
    
    /**
     * Codec is able to deal with negative linesizes
     */
    public static final int CODEC_CAP_NEG_LINESIZES    = 0x00000800; 
    
    /**
     * Codec supports frame-level multithreading.
     */
    public static final int CODEC_CAP_FRAME_THREADS    = 0x00001000; 
    /**
     * Codec supports slice-based (or partition-based) multithreading.
     */
    public static final int CODEC_CAP_SLICE_THREADS    = 0x00002000; 
    /**
     * Codec supports changed parameters at any point.
     */
    public static final int CODEC_CAP_PARAM_CHANGE     = 0x00004000; 
    /**
     * Codec supports avctx->thread_count == 0 (auto).
     */
    public static final int CODEC_CAP_AUTO_THREADS     = 0x00008000; 
    /**
     * Audio encoder supports receiving a different number of samples in each call.
     */
    public static final int CODEC_CAP_VARIABLE_FRAME_SIZE = 0x00010000; 
    /**
     * Codec is intra only.
     */
    public static final int CODEC_CAP_INTRA_ONLY       = 0x40000000; 
    /**
     * Codec is lossless.
     */
    public static final int CODEC_CAP_LOSSLESS         = 0x80000000; 
    
    
    
    
    public static final int AV_FIELD_UNKNOWN = 0x00000000; 
    public static final int AV_FIELD_PROGRESSIVE = 0x00000001; 
    public static final int AV_FIELD_TT = 0x00000002; //< Top coded_first, top displayed first
    public static final int AV_FIELD_BB = 0x00000003; //< Bottom coded first, bottom displayed first
    public static final int AV_FIELD_TB = 0x00000004; //< Top coded first, bottom displayed first
    public static final int AV_FIELD_BT = 0x00000005; //< Bottom coded first, top displayed first


    // Number of data pointers in frame.data[] field.
    public static final int AV_NUM_DATA_POINTERS = 8;
    
    //Buffer types - Determines de/allocation procedure.
    public static final int FF_BUFFER_TYPE_INTERNAL = 1;
    public static final int FF_BUFFER_TYPE_USER     = 2; ///< direct rendering buffers (image is (de)allocated by user)
    public static final int FF_BUFFER_TYPE_SHARED   = 4; ///< Buffer from somewhere else; don't deallocate image (data/base), all other tables are not shared.
    public static final int FF_BUFFER_TYPE_COPY     = 8;




    //==============================================================
    // 03_format
    //==============================================================

    public static final int AVSEEK_FLAG_BACKWARD = 0x00000001; ///< seek backward
    public static final int AVSEEK_FLAG_BYTE     = 0x00000002; ///< seeking based on position in bytes
    public static final int AVSEEK_FLAG_ANY      = 0x00000004; ///< seek to any frame, even non-keyframes
    public static final int AVSEEK_FLAG_FRAME    = 0x00000008; ///< seeking based on frame number
    
    
    public static final int AVFMT_NOFILE        = 0x00000001; 
    public static final int AVFMT_NEEDNUMBER    = 0x00000002; /**< Needs '%d' in filename. */
    public static final int AVFMT_SHOW_IDS      = 0x00000008; /**< Show format stream IDs numbers. */
    public static final int AVFMT_RAWPICTURE    = 0x00000020; /**< Format wants AVPicture structure for
                                          raw picture data. */
    public static final int AVFMT_GLOBALHEADER  = 0x00000040; /**< Format wants global header. */
    public static final int AVFMT_NOTIMESTAMPS  = 0x00000080; /**< Format does not need / have any timestamps. */
    public static final int AVFMT_GENERIC_INDEX = 0x00000100; /**< Use generic index building code. */
    public static final int AVFMT_TS_DISCONT    = 0x00000200; /**< Format allows timestamp discontinuities. Note, muxers always require valid (monotone) timestamps */
    public static final int AVFMT_VARIABLE_FPS  = 0x00000400; /**< Format allows variable fps. */
    public static final int AVFMT_NODIMENSIONS  = 0x00000800; /**< Format does not need width/height */
    public static final int AVFMT_NOSTREAMS     = 0x00001000; /**< Format does not require any streams */
    public static final int AVFMT_NOBINSEARCH   = 0x00002000; /**< Format does not allow to fall back on binary search via read_timestamp */
    public static final int AVFMT_NOGENSEARCH   = 0x00004000; /**< Format does not allow to fall back on generic search */
    public static final int AVFMT_NO_BYTE_SEEK  = 0x00008000; /**< Format does not allow seeking by bytes */
    public static final int AVFMT_ALLOW_FLUSH  = 0x00010000; /**< Format allows flushing. If not set, the muxer will not receive a NULL packet in the write_packet function. */
    public static final int AVFMT_TS_NONSTRICT = 0x00020000; /**< Format does not require strictly
                                            increasing timestamps, but they must
                                            still be monotonic */
    public static final int AVFMT_TS_NEGATIVE  = 0x00040000; /**< Format allows muxing negative
                                            timestamps. If not set the timestamp
                                            will be shifted in av_write_frame and
                                            av_interleaved_write_frame so they
                                            start from 0.
                                            The user or muxer can override this through
                                            AVFormatContext.avoid_negative_ts
                                            */
    
    public static final int AVFMT_SEEK_TO_PTS   = 0x04000000; /**< Seeking is based on PTS */
    
    
    /**
     * The duration of a video can be estimated through various ways, and this enum can be used
     * to know how the duration was estimated.
     */
    
    public static final int AVFMT_DURATION_FROM_PTS = 0x00000000; ///< Duration accurately estimated from PTSes
    public static final int AVFMT_DURATION_FROM_STREAM = 0x00000001; ///< Duration estimated from a stream with a known duration
    public static final int AVFMT_DURATION_FROM_BITRATE = 0x00000002; ///< Duration estimated from bitrate (less accurate)




    //==============================================================
    // 04_swscale
    //==============================================================

    public static final int SWS_FAST_BILINEAR     = 0x00000001; 
    public static final int SWS_BILINEAR          = 0x00000002; 
    public static final int SWS_BICUBIC           = 0x00000004; 
    public static final int SWS_X                 = 0x00000008; 
    public static final int SWS_POINT          = 0x00000010; 
    public static final int SWS_AREA           = 0x00000020; 
    public static final int SWS_BICUBLIN       = 0x00000040; 
    public static final int SWS_GAUSS          = 0x00000080; 
    public static final int SWS_SINC          = 0x00000100; 
    public static final int SWS_LANCZOS       = 0x00000200; 
    public static final int SWS_SPLINE        = 0x00000400; 
    
    public static final int SWS_CS_ITU709         = 0x00000001; 
    public static final int SWS_CS_FCC            = 0x00000004; 
    public static final int SWS_CS_ITU601         = 0x00000005; 
    public static final int SWS_CS_ITU624         = 0x00000005; 
    public static final int SWS_CS_SMPTE170M      = 0x00000005; 
    public static final int SWS_CS_SMPTE240M      = 0x00000007; 
    public static final int SWS_CS_DEFAULT        = 0x00000005; 




    //==============================================================
    // 05_swresample
    //==============================================================

    
    public static final int SWR_DITHER_NONE = 0x00000000; 
    public static final int SWR_DITHER_RECTANGULAR = 0x00000001; 
    public static final int SWR_DITHER_TRIANGULAR = 0x00000002; 
    public static final int SWR_DITHER_TRIANGULAR_HIGHPASS = 0x00000003; 
    
    public static final int SWR_DITHER_NS = 0x00000040; ///< not part of API/ABI
    public static final int SWR_DITHER_NS_LIPSHITZ = 0x00000041; 
    public static final int SWR_DITHER_NS_F_WEIGHTED = 0x00000042; 
    public static final int SWR_DITHER_NS_MODIFIED_E_WEIGHTED = 0x00000043; 
    public static final int SWR_DITHER_NS_IMPROVED_E_WEIGHTED = 0x00000044; 
    public static final int SWR_DITHER_NS_SHIBATA = 0x00000045; 
    public static final int SWR_DITHER_NS_LOW_SHIBATA = 0x00000046; 
    public static final int SWR_DITHER_NS_HIGH_SHIBATA = 0x00000047; 
    public static final int SWR_DITHER_NB = 0x00000048; ///< not part of API/ABI
    
    
    /** Resampling Engines */
    
    public static final int SWR_ENGINE_SWR = 0x00000000; /**< SW Resampler */
    public static final int SWR_ENGINE_SOXR = 0x00000001; /**< SoX Resampler */
    public static final int SWR_ENGINE_NB = 0x00000002; ///< not part of API/ABI
    
    
    /** Resampling Filter Types */
    
    public static final int SWR_FILTER_TYPE_CUBIC = 0x00000000; /**< Cubic */
    public static final int SWR_FILTER_TYPE_BLACKMAN_NUTTALL = 0x00000001; /**< Blackman Nuttall Windowed Sinc */
    public static final int SWR_FILTER_TYPE_KAISER = 0x00000002; /**< Kaiser Windowed Sinc */




    //==============================================================
    // 06_misc
    //==============================================================

    
    
    public static int leTag( int a, int b, int c, int d ) {
        return (   a & 0xFF         ) |
               ( ( b & 0xFF ) <<  8 ) |
               ( ( c & 0xFF ) << 16 ) |
               ( ( d & 0xFF ) << 24 );
    }
    
    
    public static int beTag( int a, int b, int c, int d ) {
        return (   d & 0xFF         ) |
               ( ( c & 0xFF ) <<  8 ) |
               ( ( b & 0xFF ) << 16 ) |
               ( ( a & 0xFF ) << 24 );
    }
    
    
    public static int errTag( int a, int b, int c, int d ) {
        return -leTag( a, b, c, d );
    }
    
    
    
    private Jav() {}
    
    private static boolean sInit = false;
    
    private static native void nInit();





}
