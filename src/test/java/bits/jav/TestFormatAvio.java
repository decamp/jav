/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import bits.jav.codec.JavPacket;
import bits.jav.format.JavFormatContext;
import bits.jav.format.JavIOContext;

import java.io.File;


/**
 * @author Philip DeCamp
 */
public class TestFormatAvio {

    public static void main( String[] args ) throws Exception {
        test1();
    }

    static void test1() throws Exception {
        Jav.init();
        File file = new File( "resources_build/video.mp4" );
        JavFormatContext f = JavFormatContext.openInput( file );
        JavIOContext io = f.io();
        JavPacket packet = JavPacket.alloc();

        long bufferPos = io.bufferPtr() - io.buffer();
        System.out.println( io.pos() + "\t" + io.filePos() );

        f.seek( 0, 1000000L, 0 );
        System.out.println( io.pos() + "\t" + io.filePos() );

        f.seek( 0, 10000000L, 0 );
        System.out.println( io.pos() + "\t" + io.filePos() );

        f.seek( 0, 1000000L, 0 );
        System.out.println( io.pos() + "\t" + io.filePos() );

        f.seek( 0, 5000000L, 0 );
        System.out.println( io.pos() + "\t" + io.filePos() );


    }


}
