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
        JavBufferRef r0, r1;

        Reference<ByteBuffer> ref = context( queue );
        r0 = JavBufferRef.wrap( ref.get(), 0 );

        System.out.println( "REF COUNT: " + r0.refCount() + ", " + r0.nativeRefCount() );
        r1 = JavBufferRef.wrap( r0.pointer() );
        System.out.println( "REF COUNT: " + r0.refCount() + ", " + r0.nativeRefCount() );
        System.out.println( "REF COUNT: " + r1.refCount() + ", " + r1.nativeRefCount() );

        r0 = null;

        while( r1.nativeRefCount() == 2 ) {
            System.out.println( "NOT COLLECTED" );
            Thread.sleep( 100L );
            System.gc();
        }

        System.out.println( "COLLECTED" );
        System.out.println( "REF COUNT: " + r1.refCount() + ", " + r1.nativeRefCount() );
        r1.deref();
        r1 = null;

        while( queue.poll() == null ) {
            System.out.println( "NOT COLLECTED" );
            Thread.sleep( 100L );
            System.gc();
        }

        System.out.println( "COLLECTED" );
    }
    
    
    static Reference<ByteBuffer> context( ReferenceQueue<ByteBuffer> queue ) {
        ByteBuffer c = ByteBuffer.allocate( 1024 );
        return new WeakReference<ByteBuffer>( c, queue );
    }
    
}
