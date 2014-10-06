/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import bits.jav.Jav;
import bits.jav.codec.*;
import bits.jav.format.*;
import bits.jav.swscale.SwsContext;
import bits.jav.util.*;
import bits.util.gui.ImagePanel;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.*;

import static bits.jav.Jav.*;
import static bits.jav.JavException.assertNoErr;


public class TestReadVideo {


    public static final File TEST_VIDEO = new File( "../test/resources/test.mp4" );


    public static void main( String[] args ) throws Exception {
        testReadVideo();
    }

    static void testReadVideo() throws Exception {
        Jav.init();
        File file = TEST_VIDEO;

        JavFormatContext format = JavFormatContext.openInput( file );
        JavStream stream        = format.stream(0);
        JavCodecContext cc      = stream.codecContext();

        JavCodec codec = JavCodec.findDecoder( cc.codecId() );
        
        System.out.println( " Got codec: " );
        System.out.println( codec );
        System.out.println( cc.codecId() );
        System.out.println( codec.pointer() + "\t" + cc.pointer() );
        
        cc.open( codec );
        
        JavPacket packet = JavPacket.alloc();
        JavFrame frame   = JavFrame.alloc();
        Rational aspect  = cc.sampleAspectRatio();
        
        int srcw = cc.width();
        int srch = cc.height();
        int srcf = cc.pixelFormat();
        int dstw = srcw * aspect.num() / aspect.den();
        int dsth = srch;
        int dstf = AV_PIX_FMT_BGRA;
        
        SwsContext sws = SwsContext.alloc();
        assertNoErr( sws.config( srcw, srch, srcf, dstw, dsth, dstf, SWS_FAST_BILINEAR ) );
        assertNoErr( sws.init() );
        
        //printOptions(sws);
        //System.out.println(JavOption.getOptionLong( sws, "srcw", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "srch", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "src_format", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "src_range", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "dstw", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "dsth", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "dst_format", 0 ) );
        //System.out.println(JavOption.getOptionLong( sws, "dst_range", 0 ) );
        //System.out.printf( "0x%08X\n", JavOption.getOptionLong( sws, "sws_flags", 0 ) );
        
        BufferedImage dispIm = new BufferedImage( dstw, dsth, BufferedImage.TYPE_INT_ARGB );
        int[] dispPix = new int[ dstw * dsth ];
        
        ImagePanel panel = new ImagePanel( dispIm );
        ImagePanel.framePanel( panel ).setVisible( true );
        JavFrame outFrame = JavFrame.allocVideo( dstw, dsth, dstf, null );

        int count = 0;
        long startMillis = System.currentTimeMillis();
        boolean endOfInput  = false;
        boolean decodeReady = false;
        int[] gotFrame = { 0 };
        
        while( true ) {
            if( !decodeReady ) {
                if( format.readPacket( packet ) != 0 ) {
                    endOfInput  = true;
                    decodeReady = true;
                    packet.freeData();
                    continue;
                }
                if( packet.streamIndex() != stream.index() ) {
                    continue;
                }
                System.out.println( "Read packet." );
                decodeReady = true;
            }
            
            int size = packet.size();
            int err  = cc.decodeVideo( packet, frame, gotFrame );
            if( err < 0 ) {
                throw new IOException( "Decode failed." );
            }
            
            if( err < size ) {
                packet.size( size - err );
                packet.dataPointer( packet.dataPointer() + err );
            } else if( !endOfInput ) {
                decodeReady = false;
            }
            if( gotFrame[0] == 0 ) {
                continue;
            }

            System.out.println( "Decoded frame" );
            assertNoErr( sws.conv( frame, 0, srch, outFrame ) );

            IntBuffer ib = outFrame.directBuffer().asIntBuffer();
            ib.get( dispPix );
            dispIm.setRGB( 0, 0, dstw, dsth, dispPix, 0, dstw );
            panel.repaint();

            if( ++count % 100 == 0 ) {
                long now    = System.currentTimeMillis();
                double secs = ( now - startMillis ) / 1000.0;
                System.out.println( "FPS: " + ( count / secs ) );
                startMillis = now;
                count       = 0;
            }

            try {
                Thread.sleep( 50L );
            } catch( InterruptedException ex ) {}
        }
    }

}
