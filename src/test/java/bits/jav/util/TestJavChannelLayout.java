/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import bits.jav.Jav;
import org.junit.Test;
import static bits.jav.Jav.*;
import static junit.framework.Assert.*;


/**
 * @author Philip DeCamp
 */
public class TestJavChannelLayout {

    public TestJavChannelLayout() {
        Jav.init();
    }

    @Test
    public void testBasics() {
        long layout = Jav.AV_CH_LAYOUT_MONO;
        String name = JavChannelLayout.getString( 0, layout );
        assertEquals( "mono", name );
        assertEquals( 1, JavChannelLayout.getNbChannels( layout ) );
        assertEquals( layout, JavChannelLayout.getDefault( 1 ) );

        layout = Jav.AV_CH_LAYOUT_STEREO;
        assertEquals( 2, JavChannelLayout.getNbChannels( layout ) );
        assertEquals( 0, JavChannelLayout.getChannelIndex( layout, Jav.AV_CH_FRONT_LEFT ) );
        assertEquals( 1, JavChannelLayout.getChannelIndex( layout, Jav.AV_CH_FRONT_RIGHT ) );
        assertEquals( Jav.AV_CH_FRONT_LEFT, JavChannelLayout.extractChannel( layout, 0 ) );
        assertEquals( Jav.AV_CH_FRONT_RIGHT, JavChannelLayout.extractChannel( layout, 1 ) );
        assertEquals( "FL", JavChannelLayout.getChannelName( Jav.AV_CH_FRONT_LEFT ) );
        assertEquals( "front left", JavChannelLayout.getChannelDescription( Jav.AV_CH_FRONT_LEFT ) );

        assertEquals( Jav.AV_CH_LAYOUT_MONO, JavChannelLayout.getStandardValue( 0 ) );
        assertEquals( "mono", JavChannelLayout.getStandardName( 0 ) );
    }

}
