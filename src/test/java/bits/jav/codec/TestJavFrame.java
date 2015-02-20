/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.codec;

import bits.jav.Jav;
import bits.jav.util.JavBufferRef;
import org.junit.Test;
import static bits.jav.Jav.*;
import static junit.framework.Assert.*;

/**
 * @author Philip DeCamp
 */
public class TestJavFrame {

    public TestJavFrame() {
        Jav.init();
    }

    @Test
    public void testBufCounting() {
        JavFrame frame = JavFrame.allocVideo( 1024, 1024, Jav.AV_PIX_FMT_BGR24, null );
        assertEquals( 1, frame.nbAllBufs() );
        assertTrue( frame.useableBufElemSize( 0 ) >= 1024 * 1024 * 3 );
        assertTrue( frame.allBufsMinUseableSize() >= 1024 * 1024 * 3 );
    }

    @Test
    public void testJavaExtendBufElem() {
        JavFrame frame;

        frame = JavFrame.allocAudio( 2, 1024, Jav.AV_SAMPLE_FMT_FLTP, 4, null );
        assertTrue( 0 != frame.data() );
        assertTrue( 0 != frame.dataElem( 0 ) );
        assertTrue( 0 != frame.dataElem( 1 ) );
        assertTrue( 0 == frame.dataElem( 2 ) );
        assertTrue( 0 != frame.extendedData() );

        frame.deref();

        frame = JavFrame.allocAudio( 32, 1024, Jav.AV_SAMPLE_FMT_FLTP, 4, null );
        System.out.println( frame.channels() );
        assertTrue( 0 != frame.data() );
        assertTrue( 0 != frame.dataElem( 0 ) );
        assertTrue( 0 != frame.dataElem( 1 ) );
        assertTrue( 0 != frame.dataElem( 2 ) );
        assertTrue( 0 != frame.extendedData() );
    }

}
