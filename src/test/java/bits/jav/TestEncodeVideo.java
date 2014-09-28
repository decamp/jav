/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import bits.jav.codec.*;
import bits.jav.format.*;
import bits.jav.swscale.SwsContext;
import bits.jav.util.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;

import static bits.jav.Jav.*;


public class TestEncodeVideo {

    public static void main( String[] args ) throws Exception {
        testEncode();
    }

    @SuppressWarnings( "resource" )
    static void testEncode() throws Exception {
        Jav.init();
        JavCodec codec = JavCodec.findEncoder( AV_CODEC_ID_MPEG2VIDEO );
        JavCodecContext cc = JavCodecContext.alloc( codec );
        
        final int w = 640;
        final int h = 480;
        cc.bitRate( 400000 );
        cc.width( w );
        cc.height( h );
        cc.timeBase( new Rational( 1, 25 ) );
        cc.gopSize( 15 );
        cc.maxBFrames( 0 );
        cc.pixelFormat( AV_PIX_FMT_YUV420P );
        cc.open( codec );
        
        File file = new File( "/tmp/video_test.mpeg" );
        if( file.exists() ) {
            file.delete();
        }
        FileChannel out = new FileOutputStream( file ).getChannel();
        
        JavFrame srcFrame = JavFrame.allocVideo( w, h, AV_PIX_FMT_ARGB, null );
        JavFrame dstFrame = JavFrame.allocVideo( w, h, AV_PIX_FMT_YUV420P, null );
        SwsContext sws    = SwsContext.alloc();
        sws.configure( 640, 480, AV_PIX_FMT_ARGB, 640, 480, AV_PIX_FMT_YUV420P, SWS_POINT );
        sws.initialize();
        
        JavPacket packet = JavPacket.alloc();
        packet.allocData( 1024*1024 );
        
        ByteBuffer obuf   = ByteBuffer.allocateDirect( 1024 * 1024 );
        int frameCount    = 0;
        int maxFrameCount = 255;
        int[] gotFrame = { 0 };
        
        while( true ) {
            JavFrame writeFrame = null;
            
            if( frameCount < maxFrameCount ) {
                System.out.println( "Frame: " + frameCount );
                
                ByteBuffer buf = srcFrame.directBuffer();
                buf.clear();
                buf.order( ByteOrder.BIG_ENDIAN );
                int v = 0xFF000000 + (( frameCount % 256 ) * 0x00010101 ); 
                for( int j = 0; j < w * h; j++ ) {
                    buf.putInt( v );                
                }                    
                buf.flip();
                
                sws.convert( srcFrame, 0, h, dstFrame );
                writeFrame = dstFrame;
                frameCount++;
            }
            
            packet.size( 1024*1024 );
            if( cc.encodeVideo( writeFrame, packet, gotFrame ) < 0 ) {
                throw new IOException( "Encode failed. ");
            }
            
            if( gotFrame[0] == 0 ) {
                if( frameCount < maxFrameCount ) {
                    continue;
                } else {
                    break;
                }
            }
        
            obuf.clear();
            obuf.limit( packet.size() );
            JavMem.copy( packet.dataPointer(), obuf );
            obuf.flip();

            while( obuf.remaining() > 0 ) {
                if( out.write( obuf ) <= 0 ) {
                    break;
                }
            }
        }
    }

}
