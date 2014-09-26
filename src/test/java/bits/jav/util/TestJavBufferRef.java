/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;

import java.lang.ref.*;
import java.nio.ByteBuffer;

import bits.jav.Jav;

/**
 * @author decamp
 */
public class TestJavBufferRef {
    
    public static void main( String[] args ) throws Exception {
        test2();
    }
    
    
    static void test1() throws Exception {
        Jav.init();
        
        ByteBuffer bb = ByteBuffer.allocateDirect( 1024 );
        JavBufferRef b = JavBufferRef.alloc( 1024, true );
        
        System.out.println( b.pointer() );
        System.out.println( b.size() );
        System.out.println( b.data() );
        System.out.println( b.buffer() );
        
        JavMem.copy( b.data(), bb );
        bb.flip();
            
        b.deref();
    }

    
    static void test2() throws Exception {
        Jav.init();
        
        ReferenceQueue<ByteBuffer> queue = new ReferenceQueue<ByteBuffer>();
        ByteBuffer bb = ByteBuffer.allocate( 1024 );
        JavBufferRef r0 = JavBufferRef.wrap( bb, 0 );
        JavBufferRef r1 = JavBufferRef.wrap( r0.pointer() );
        
        bb = null;
        r0 = null;
        r1.deref();
        
        while( queue.poll() == null ) {
            System.out.println( "not collected" );
            Thread.sleep( 100L );
            System.gc();
        }
    }
    
    
    static Reference<ByteBuffer> context( ReferenceQueue<ByteBuffer> queue ) {
        ByteBuffer c = ByteBuffer.allocate( 1024 );
        return new WeakReference<ByteBuffer>( c, queue );
    }
    
}
