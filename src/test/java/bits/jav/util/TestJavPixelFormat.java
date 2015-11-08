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


public class TestJavPixelFormat {

    public TestJavPixelFormat() {
        Jav.init();
    }

    @Test
    public void testBasics() {
        int fmt = AV_PIX_FMT_BGRA;
        String name = JavPixelFormat.getName( fmt );
        System.out.println( name );

        assertEquals( "bgra", name );
        assertEquals( fmt, JavPixelFormat.get( name ) );

        System.out.println( "%%% " + JavPixelFormat.getString( -1 ) );
        System.out.println( "%%% " + JavPixelFormat.getString( fmt ) );
    }

}

