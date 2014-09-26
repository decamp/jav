/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import java.io.*;

import bits.jav.codec.*;
import bits.jav.format.*;

/**
 * @author decamp
 */
public class TestFrameMem {
    
    
    public static void main( String[] args ) throws Exception {
        test1();
    }
    
    
    static void test1() throws Exception {
        Jav.init();
                
        JavFormatContext fmt = JavFormatContext.openInput( new File( "resources_build/video.mp4" ) );
        JavStream stream     = fmt.streamOfType( Jav.AVMEDIA_TYPE_VIDEO, 0 );
        JavCodecContext cc   = stream.codecContext();
        
        System.out.println( cc.refcountedFrames() );
        cc.refcountedFrames( 1 );
        cc.open( JavCodec.findDecoder( cc.codecId() ) );
        System.out.println( cc.refcountedFrames() );
        
        final int w      = cc.width();
        final int h      = cc.height();
        final int pixFmt = cc.pixelFormat();
        
        System.out.println( w + ", " + h + ", " + pixFmt );

        JavPacket packet   = JavPacket.alloc();
        JavFrame frame     = JavFrame.alloc();
        int[] gotFrame     = { 0 };
        
        for( ;; ) {
            int err;
            
            packet.freeData();
            packet.init();
            err = fmt.readPacket( packet );
            if( err < 0 ) {
                if( err == Jav.AVERROR_EOF ) {
                    break;
                }
                System.out.println( JavException.errStr( err ) );
                throw new IOException( "" + err );
            }
            
            if( packet.streamIndex() != stream.index() ) {
                continue;
            }
            
            long p0 = frame.dataPointer( 0 );
            long p1 = frame.lineSize( 0 );
            
            err = cc.decodeVideo( packet, frame, gotFrame );
            if( err < 0 ) {
                System.out.println( JavException.errStr( err ) );
                throw new IOException( "" + err );
            }
            
            if( gotFrame[0] == 0 ) {
                continue;
            }
            
            System.out.println( p0 + " -> " + frame.dataPointer( 0 ) );
            System.out.println( p1 + " -> " + frame.lineSize( 0 ) );
            
            //frame = JavFrame.alloc();
            //frames.add( frame );
            //frame.deref();
            //frame = JavFrame.alloc();
        }
        
        //frame.deref();
        //fmt.close();
        
    }

}
