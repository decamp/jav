/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.nio.ByteBuffer;


/**
 * @author Philip DeCamp
 */
public class JavChannelLayout {

    /**
     * Return a channel layout id that matches name, or 0 if no match is found.
     *
     * <p>name can be one or several of the following notations,
     * separated by '+' or '|': <br>
     * - the name of an usual channel layout (mono, stereo, 4.0, quad, 5.0,
     *   5.0(side), 5.1, 5.1(side), 7.1, 7.1(wide), downmix);<br>
     * - the name of a single channel (FL, FR, FC, LFE, BL, BR, FLC, FRC, BC,
     *   SL, SR, TC, TFL, TFC, TFR, TBL, TBC, TBR, DL, DR);<br>
     * - a number of channels, in decimal, optionally followed by 'c', yielding
     *   the default channel layout for that number of channels (@see
     *   av_get_default_channel_layout);<br>
     * - a channel layout mask, in hexadecimal starting with "0x" (see the
     *   AV_CH_* macros).<br>
     *
     * <p><em>Warning:</em> Starting from the next major bump the trailing character
     * 'c' to specify a number of channels will be required, while a
     * channel layout mask could also be specified as a decimal number
     * (if and only if not followed by "c").
     *
     * <p>Example: "stereo+FC" = "2c+FC" = "2c+1c" = "0x7"
     */
    public static native long get( String name );

    /**
     * @return a description of a channel layout
     * If nb_channels is <= 0, it is guessed from the channel_layout.
     */
    public static native String getString( int numChannels, long channelLayout );

    /**
     * Return the number of channels in the channel layout.
     */
    public static native int getNbChannels( long channelLayout );

    /**
     * Return default channel layout for a given number of channels.
     */
    public static native long getDefault( int numChannels );

    /**
     * Get the index of a channel in channel_layout.
     *
     * @param layout  some channel layout
     * @param channel a channel layout with a single channel that must be present in {@code layout }.
     * @return index of channel in channel_layout on success, a negative AVERROR on error.
     */
    public static native int getChannelIndex( long layout, long channel );

    /**
     * @return the channel with the given index in channel_layout.
     */
    public static native long extractChannel( long layout, int index );

    /**
     * Get the name of a given channel.
     *
     * @return channel name on success, NULL on error.
     */
    public static native String getChannelName( long channel );

    /**
     * Get the description of a given channel.
     *
     * @param channel  a channel layout with a single channel
     * @return  channel description on success, NULL on error
     */
    public static native String getChannelDescription( long channel );

    /**
     * Get the value and name of a standard channel layout.
     *
     * @param index  index in an internal list, starting at 0
     * @return  channelLayout if index in bounds, Long.MIN_VALUE if index is beyond limits.
     */
    public static native long getStandardValue( int index );

    /**
     * Get the value and name of a standard channel layout.
     *
     * @param index   index in an internal list, starting at 0
     * @return name name of layout, or null if index is beyond limits.
     */
    public static native String getStandardName( int index );

}
