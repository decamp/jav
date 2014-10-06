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
import org.junit.*;

import static org.junit.Assert.*;


public class TestJavBufferRef {


    public TestJavBufferRef() {
        Jav.init();
    }


    @Test
    public void testAlloc() throws Exception {
        JavBufferRef ref = JavBufferRef.alloc( 1024, true );
        assertNotNull( ref );
        assertTrue( ref.pointer() != 0 );
        assertEquals( 1, ref.refCount() );
        assertEquals( 1024, ref.size() );
        assertTrue( ref.data() != 0 );
        assertBufferIsCleared( ref );

        ref.unref();
    }

    @Test
    public void testWrap() throws Exception {
        JavBufferRef r0, r1;
        Reference<ByteBuffer> ref;
        {
            ByteBuffer bb = ByteBuffer.allocateDirect( 1024 );
            ref = new WeakReference<ByteBuffer>( bb );
            r0 = JavBufferRef.wrap( bb, 0 );
        }

        assertEquals( 1, r0.refCount() );
        assertNotNull( ref.get() );
        assertTrue( r0.isWritable() );

        r1 = r0.ref();

        assertEquals( 2, r0.refCount() );
        assertEquals( 2, r1.refCount() );
        assertEquals( r0.data(), r1.data() );
        assertEquals( r0.javaBuffer(), r1.javaBuffer() );
        assertTrue( r0.hasJavaBuffer() );
        assertTrue( !r0.isWritable() );

        r0.unref();
        r0 = null;

        assertEquals( 1, r1.refCount() );
        assertTrue( r1.isWritable() );

        // Make sure reference to ByteBuffer has been maintained.
        for( int i = 0; i < 3; i++ ) {
            Thread.sleep( 50L );
            System.gc();
            assertNotNull( ref.get() );
        }

        r1.unref();
        r1 = null;

        // Make sure reference to ByteBuffer has been released.
        for( int i = 0; i < 5; i++ ) {
            Thread.sleep( 50L );
            System.gc();
            if( ref.get() == null ) {
                break;
            }
        }

        assertNull( ref.get() );
    }

    @Test
    public void testRefPointer() throws Exception {
        JavBufferRef r0, r1;
        Reference<ByteBuffer> ref;
        {
            ByteBuffer bb = ByteBuffer.allocateDirect( 1024 );
            ref = new WeakReference<ByteBuffer>( bb );
            r0 = JavBufferRef.wrap( bb, 0 );
        }

        assertEquals( 1, r0.refCount() );
        assertNotNull( ref.get() );
        assertTrue( r0.isWritable() );

        r1 = JavBufferRef.refPointer( r0.pointer() );

        assertEquals( 2, r0.refCount() );
        assertEquals( 2, r1.refCount() );
        assertEquals( r0.data(), r1.data() );
        assertTrue( r0.hasJavaBuffer() );
        assertTrue( r1.hasJavaBuffer() );
        assertTrue( !r0.isWritable() );

        r0.unref();
        r0 = null;

        assertEquals( 1, r1.refCount() );
        assertTrue( r1.isWritable() );

        // Make sure reference to ByteBuffer has been maintained.
        for( int i = 0; i < 3; i++ ) {
            Thread.sleep( 50L );
            System.gc();
            assertNotNull( ref.get() );
        }

        r1.unref();
        r1 = null;

        // Make sure reference to ByteBuffer has been released.
        for( int i = 0; i < 5; i++ ) {
            Thread.sleep( 50L );
            System.gc();
            if( ref.get() == null ) {
                break;
            }
        }

        assertNull( ref.get() );
    }

    @Test
    public void testUnref() throws Exception {
        // Just checking for JVM crashes or error messages here.
        JavBufferRef a = JavBufferRef.alloc( 1024, true );
        JavBufferRef b = a.ref();
        a.unref();
        b.unref();

        a = JavBufferRef.wrap( ByteBuffer.allocateDirect( 1024 ), 0 );
        b = a.ref();
        a.unref();
        b.unref();

        for( int i = 0; i < 4; i++ ) {
            Thread.sleep( 50L );
            System.gc();
        }

        System.out.println( a == b );
    }

    @Test
    public void testRealloc() throws Exception {
        Jav.init();

        // Basic realloc.
        JavBufferRef a = JavBufferRef.alloc( 1024, true );
        assertEquals( 1024, a.size() );
        a = a.realloc( 2048 );
        assertTrue( 2048 <= a.size() );
        a.unref();

        // Realloc with java-allocated byte buffer.
        Reference<ByteBuffer> ref;
        {
            ByteBuffer bb = ByteBuffer.allocateDirect( 1024 );
            ref = new WeakReference<ByteBuffer>( bb );
            a = JavBufferRef.wrap( bb, 0 );
        }
        assertEquals( 1024, a.size() );

        JavBufferRef b = a.realloc( 2048 );

        assertTrue( !a.hasJavaBuffer() );
        assertTrue( !b.hasJavaBuffer() );
        assertTrue( 2048 <= b.size() );

        b.unref();

        for( int i = 0; i < 5; i++ ) {
            Thread.sleep( 50L );
            System.gc();
            if( ref.get() == null ) {
                break;
            }
        }

        assertNull( ref.get() );
    }

    @Test
    public void testRetrieveJavaBuf() throws Exception {
        ByteBuffer a = ByteBuffer.allocateDirect( 1024 );
        JavBufferRef ref = JavBufferRef.wrap( a, 0 );

        ByteBuffer b = ref.javaBuffer();

        assertNotNull( b );
        assertEquals( JavMem.nativeAddress( a ), JavMem.nativeAddress( b ) );

        ref.unref();

        ref = JavBufferRef.alloc( 1024, true );
        b = ref.javaBuffer();
        assertNull( b );
    }


    static void assertBufferIsCleared( JavBufferRef ref ) {
        ByteBuffer bb = copyData( ref );
        while( bb.remaining() > 0 ) {
            assertEquals( 0, bb.get() );
        }
    }


    static ByteBuffer copyData( JavBufferRef ref ) {
        if( ref == null ) {
            return null;
        }
        int len = ref.size();
        ByteBuffer bb = ByteBuffer.allocateDirect( len );
        JavMem.copy( ref.data(), bb );
        bb.flip();

        return bb;
    }

}
