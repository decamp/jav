/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.util.HashSet;
import java.util.Set;

import bits.jav.Jav;
import org.junit.*;
import static org.junit.Assert.*;


public class TestJavBufferPool {

    public TestJavBufferPool() {
        Jav.init();
    }

    @Test
    public void testAlloc() {
        JavBufferPool pool = JavBufferPool.init( 1024 );

        Set<Long> set = new HashSet<Long>();
        JavBufferRef b = pool.get();


        for( int i = 0; i < 1024; i++ ) {
            JavBufferRef a = pool.get();
            set.add( a.buffer() );
            assertEquals( 1024, a.size() );
            assertEquals( 1, a.refCount() );
            a.unref();
        }

        pool.deref();
        b.unref();

        assertTrue( set.size() < 128 );
    }

}
