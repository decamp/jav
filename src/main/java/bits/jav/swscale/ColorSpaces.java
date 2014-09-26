/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.swscale;

/**
 * @author decamp
 */
final class ColorSpaces {
    static final int[][] FF_YUV2RGB_COEFFS = {
        { 117504, 138453, 13954, 34903 }, /* no sequence_display_extension */
        { 117504, 138453, 13954, 34903 }, /* ITU-R Rec. 709 (1990) */
        { 104597, 132201, 25675, 53279 }, /* unspecified */
        { 104597, 132201, 25675, 53279 }, /* reserved */
        { 104448, 132798, 24759, 53109 }, /* FCC */
        { 104597, 132201, 25675, 53279 }, /* ITU-R Rec. 624-4 System B, G */
        { 104597, 132201, 25675, 53279 }, /* SMPTE 170M */
        { 117579, 136230, 16907, 35559 }  /* SMPTE 240M (1987) */
    };
}
