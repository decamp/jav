/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import java.io.*;
import java.nio.*;

import bits.jav.Jav;
import bits.jav.codec.*;
import bits.jav.swscale.SwsContext;
import bits.jav.util.*;

import static bits.jav.Jav.*;


public class TestEncodeFormat {

    
    public static void main( String[] args ) throws Exception {
        //printOutputFormats();
        testFormatEncode();
    }

    
    static void testFormatEncode() throws Exception {
        Jav.init();
        
        final String FILE_TYPE = "mp4";
        File file = new File( "/tmp/video_test." + FILE_TYPE );
        if( file.exists() ) {
            file.delete();
        }
        System.out.println( "OUTPUT FILE: " + file.getAbsolutePath() );

        JavFormatContext format = JavFormatContext.openOutput( file, null, null );
        JavOutputFormat  of     = format.outputFormat();
        JavCodec         codec  = JavCodec.findEncoder( Jav.AV_CODEC_ID_H264 );
        
        System.out.println( codec );
        System.out.println( of );
        System.out.println( of.longName() );
        System.out.println( of.mimeType() );
        System.out.println( of.extensions() );
        System.out.format( "Format flags: 0x%08X\n", of.flags() );
        
        JavStream stream   = format.newStream( codec );
        JavCodecContext cc = stream.codecContext();
        final int w = 640;
        final int h = 480;
        final Rational frameRate = new Rational( 1, 30 );
        
        cc.bitRate( 400000 );
        cc.width( w );
        cc.height( h );
        
        cc.timeBase( frameRate );
        System.out.println( "CC : " + cc.timeBase() );
        
        cc.gopSize( 15 );
        cc.maxBFrames( 0 );
        cc.pixelFormat( AV_PIX_FMT_YUV420P );
        
        JavOption.setString( cc, "preset", "slow", 0 );
        if( ( of.flags() & AVFMT_GLOBALHEADER ) != 0 ) {
            cc.flags( cc.flags() | CODEC_FLAG_GLOBAL_HEADER );
        }
        cc.open( codec );
                
        format.writeHeader();
        System.out.println( cc.timeBase() + "\t" + stream.timeBase() );
        Rational timeBase = stream.timeBase();
        
        JavFrame srcFrame = JavFrame.allocVideo( w, h, AV_PIX_FMT_ARGB, null );
        JavFrame dstFrame = JavFrame.allocVideo( w, h, AV_PIX_FMT_YUV420P, null );
        SwsContext sws    = SwsContext.alloc();
        sws.config( 640, 480, AV_PIX_FMT_ARGB,
                    640, 480, AV_PIX_FMT_YUV420P,
                    SWS_POINT );
        assertOkay( sws.init() );
        
        JavPacket packet = JavPacket.alloc();
        packet.streamIndex( 0 );
        packet.allocData( 1024*1024 );
        
        int frameCount    = 0;
        int maxFrameCount = 255;
        int[] gotFrame = { 0 };
        
        while( true ) {
            JavFrame writeFrame = null;
            packet.init();
            packet.size( 1024*1024 );
            
            if( frameCount < maxFrameCount ) {
                ByteBuffer buf = srcFrame.javaBufElem( 0 );
                buf.clear();
                buf.order( ByteOrder.BIG_ENDIAN );
                int v = 0xFFFF0000 + (( frameCount % 256 ) * 0x00000101 ); 
                for( int j = 0; j < w * h; j++ ) {
                    buf.putInt( v );                
                }                    
                buf.flip();
                
                sws.conv( srcFrame, dstFrame );
                writeFrame = dstFrame;
                writeFrame.pts( Rational.rescaleQ( frameCount, frameRate, timeBase ) );
                frameCount++;
            }
            
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
            
            //System.out.println( "Encoded frame: " + frameCount + "\t" + packet.presentTime() + "\t" + packet.decodeTime() + "\t" + packet.size() );
            if( format.writeFrame( packet ) < 0 ) {
                throw new IOException( "Failed to write format." );
            }
        }
        
        while( true ) {
            int err = format.writeFrame( null );
            System.out.println( "Flush : " + err );
            
            if( err < 0 ) {
                throw new IOException( "Failed to flush." );
            }
            if( err > 0 ) {
                break;
            }
        }
        
        format.writeTrailer();
        format.close();
        
        packet.deref();
        srcFrame.deref();
        dstFrame.deref();
        //cc.close();
    }


    static void printOutputFormats() throws Exception {
        Jav.init();

        JavOutputFormat of = JavOutputFormat.nextOutputFormat( null );
        while( of != null ) {
            boolean varFps = ( of.flags() & AVFMT_VARIABLE_FPS ) != 0;

            System.out.println( of.extensions() );
            System.out.println( of.longName() );
            System.out.println( of.mimeType() );
            System.out.format( "Format flags: 0x%08X\n", of.flags() );
            System.out.println( "Supports Viarble FPS: " + varFps );
            System.out.println();
            of = JavOutputFormat.nextOutputFormat( of );
        }
    }
    
}
