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


public class TestJavSampleFormat {

    public TestJavSampleFormat() {
        Jav.init();
    }

    @Test
    public void testBasics() {
        int fmt = AV_SAMPLE_FMT_S16;
        String name = JavSampleFormat.getName( fmt );
        assertNotNull( name );
        assertEquals( fmt, JavSampleFormat.get( name ) );
        assertEquals( AV_SAMPLE_FMT_S16 , JavSampleFormat.getAlt( AV_SAMPLE_FMT_S16, 0 ) );
        assertEquals( AV_SAMPLE_FMT_S16P, JavSampleFormat.getAlt( AV_SAMPLE_FMT_S16, 1 ) );
        assertEquals( 2, JavSampleFormat.getBytesPerSample( AV_SAMPLE_FMT_S16 ) );
        assertFalse( JavSampleFormat.isPlanar( AV_SAMPLE_FMT_S16 ) );
        assertTrue( JavSampleFormat.isPlanar( AV_SAMPLE_FMT_S16P ) );
    }

}
