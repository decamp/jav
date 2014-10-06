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
import bits.jav.JavException;
import bits.jav.codec.*;
import bits.jav.util.*;
import static bits.jav.Jav.*;


public class TestEncodeMp3 {


    public static final File TEST_VIDEO = TestReadVideo.TEST_VIDEO;


    public static void main( String[] args ) throws Exception {
        Jav.init();
        testFormatEncode();
    }

//    @SuppressWarnings( "resource" )
//    static void testEncode() throws Exception {
//        float[][] audio = readAudio();
//
//        JavCodec codec = JavCodec.findEncoder( AV_CODEC_ID_MP3 );
//        System.out.format( "Got codec: 0x%08X\n", codec.pointer() );
//        JavCodecContext context = JavCodecContext.alloc( null );
//        System.out.format( "Got context: 0x%08X\n", context.pointer() );
//
//        // Sample params
//        context.bitRate( 128000 );
//        context.sampleFormat( AV_SAMPLE_FMT_S16P );
//        context.sampleRate( 48000 );
//        context.channels( audio.length );
//        context.open( codec );
//
//        int frameSize  = context.frameSize();
//        int chans      = context.channels();
//        int sampFormat = context.sampleFormat();
//        JavFrame frame = JavFrame.allocAudio( chans, frameSize, sampFormat, 0, null );
//
//        JavPacket packet   = JavPacket.alloc();
//        File outFile       = new File( "/tmp/audio_test.mp3" );
//        FileChannel out    = new FileOutputStream( outFile ).getChannel();
//
//        int pos = 0;
//        ByteBuffer b    = frame.directBuffer();
//        int[] gotPacket = { 0 };
//
//        while( true ) {
//            JavFrame writeFrame = null;
//
//            if( pos < audio[0].length ) {
//                int lineSize = frame.lineSize( 0 );
//                int len = Math.min( audio[0].length - pos, lineSize / 2 );
//                frame.nbSamples( len );
//
//                b.clear();
//
//                for( int chan = 0; chan < audio.length; chan++ ) {
//                    b.position( chan * lineSize );
//                    for( int i = 0; i < len; i++ ) {
//                        b.putShort( (short)( audio[chan][i+pos] * Short.MAX_VALUE ) );
//                    }
//                }
//
//                b.flip();
//                writeFrame = frame;
//                pos += len;
//            }
//
//            if( writeFrame == null ) {
//                System.out.println( "w null" );
//            } else {
//                System.out.println( "w " + writeFrame.nbSamples() );
//            }
//
//            if( context.encodeAudio( writeFrame, packet, gotPacket ) < 0 ) {
//                throw new IOException( "Encode failed." );
//            }
//
//            if( gotPacket[0] == 0 ) {
//                if( writeFrame == null ) {
//                    // Done
//                    break;
//                } else {
//                    continue;
//                }
//            }
//
//            System.out.println( "ww -> " + packet.size() );
//            b.clear();
//            b.limit( packet.size() );
//            JavMem.copy( packet.dataElem(), b );
//            b.flip();
//
//            out.write( b );
//            packet.freeData();
//            packet.init();
//        }
//
//        out.close();
//    }
//
//

    static void testFormatEncode() throws Exception {
        Jav.init();

        float[] audio = readAudio()[0];
        System.out.println( "Done reading audio. " );

        File outFile = new File( "/tmp/audio_format.mp3" );
        if( outFile.exists() ) {
            outFile.delete();
        }
        System.out.println( "OUTPUT FILE: " + outFile.getAbsolutePath() );

        JavFormatContext format = JavFormatContext.openOutput( outFile, null, null );
        JavOutputFormat ofmt    = format.outputFormat();
        JavCodec codec          = JavCodec.findEncoder( AV_CODEC_ID_MP3 );
        
        System.out.println( ofmt.name() + "\t "+ ofmt.longName() + "\t" + ofmt.mimeType() );
        
        JavStream stream   = format.newStream( codec );
        JavCodecContext cc = stream.codecContext();
        
        cc.bitRate( 64000 );
        cc.sampleRate( 48000 );
        cc.sampleFormat( AV_SAMPLE_FMT_S16 );
        cc.channels( 1 );
        cc.channelLayout( AV_CH_LAYOUT_MONO );
        cc.open( codec );
        
        int frameSize  = cc.frameSize();
        int chans      = cc.channels();
        int sampFormat = cc.sampleFormat();
        
        { //Setup metadata
            JavDict dict = new JavDict();
            dict.set( "artist", "Artist", 0 );
            dict.set( "album", "Album", 0 );
            dict.set( "title", "Title", 0 );
            
            System.out.println( "DictSize: " + dict.count() );
            format.metadata( dict );
        }
        
        System.out.println( "Writing header..." );
        format.writeHeader();
        System.out.println( "Header written... ");

        
        JavFrame frame     = JavFrame.allocAudio( chans, frameSize, sampFormat, 0, null );
        JavPacket packet   = JavPacket.alloc();
        //packet.allocData( 1024 * 1024 );
        
        int pos = 0;
        ByteBuffer b = frame.directBuffer();
        int[] gotPacket = { 0 };
        
        while( pos < audio.length ) {
            b.clear();
            int len = Math.min( audio.length - pos, b.remaining() / 2 );

            for( int i = 0; i < len; i++ ) {
                b.putShort( (short)( audio[i+pos] * Short.MAX_VALUE ) );
            }
            
            pos += len;
            b.flip();
            frame.nbSamples( b.remaining() / 2 );

            if( cc.encodeAudio( frame, packet, gotPacket ) < 0 ) {
                throw new IOException( "Encode audio failed." );
            }
            if( gotPacket[0] == 0 ) {
                continue;
            }
            
            //System.out.println( packet.dataElem() + "\t" + packet.size() );
            format.writeFrame( packet );
            //packet.size( 1024*1024 );
            //packet.init();
        }
        
        while( true ) {
            if( cc.encodeAudio( null, packet, gotPacket ) < 0 ) {
                throw new IOException( "Encode audio failed." );
            }
            if( gotPacket[0] == 0 ) {
                break;
            }
            
            //System.out.println( packet.dataElem() + "\t" + packet.size() );
            format.writeFrame( packet );
            packet.size( 1024*1024 );
            packet.init();
        }

        format.writeTrailer();
        format.close();
        
    }
    
    
    static float[][] readAudio() throws Exception {
        JavFormatContext format = JavFormatContext.openInput( TEST_VIDEO );
        JavStream stream = format.streamOfType( AVMEDIA_TYPE_AUDIO, 0 );
        if(stream == null) {
            System.out.println( "No audio stream found." );
            return null;
        }

        final int streamIndex = stream.index();
        JavPacket packet      = JavPacket.alloc();
        JavCodecContext cc    = stream.codecContext();
        
        System.out.format( "Codec: channels %d, layout %d, sampleRate %d, sampleFormat %s\n" ,
                           cc.channels(),
                           cc.channelLayout(),
                           cc.sampleRate(),
                           JavSampleFormat.getName( cc.sampleFormat() ) );

        JavFrame frame = JavFrame.alloc();

        //Open decodec.
        if( !cc.isOpen() ) {
            JavCodec cod = JavCodec.findDecoder( cc.codecId() );
            if( cod == null ) {
                throw new JavException( AVERROR_DECODER_NOT_FOUND, "Decoder not found.");
            }
            cc.open( cod );
        }

        ByteBuffer buf     = null;
        int chans          = cc.channels();
        float[][] audio    = new float[chans][44100*16];
        
        int samplePos      = 0;
        int packetPos      = 0;
        int[] gotFrame     = { 0 };
        boolean endOfInput = false;
                
        while( true ) {
            if( !endOfInput ) {
                int err = format.readPacket( packet );
                if( err != 0 ) {
                    endOfInput = true;
                    packet.freeData();
                    continue;
                }
                if( packet.streamIndex() != streamIndex ) {
                    continue;
                }
                
                packetPos++;
            }
            
            if( cc.decodeAudio( packet, frame, gotFrame ) < 0 ) {
                throw new IOException( "Decode failed." );
            }
            
            if( gotFrame[0] == 0 ) {
                if( endOfInput ) {
                    break; 
                } else {
                    continue;
                }
            }
            
            int samps = frame.nbSamples();
            if( buf == null || buf.capacity() < 4 * samps ) {
                buf = Jav.allocBuffer( 4 * samps );
            }

            int len = Math.min( audio[0].length - samplePos, samps );
            for( int i = 0; i < chans; i++ ) {
                buf.clear().limit( samps * 4 );
                JavMem.copy( frame.extendedDataPointer( i ), buf );
                buf.flip();
                float[] dst = audio[i];
                
                for( int j = 0; j < len; j++ ) {
                    dst[samplePos+j] = buf.getFloat();
                }
            }
            
            samplePos += len;
            if( samplePos == audio.length ) { 
                break;
            }
        }
        
        System.out.format( "Read: %d packets,  %d samples\n", packetPos, samplePos ); 
        return audio;
    }
    
    
    static void testPlanar() throws Exception {
        Jav.init();
        int channels = 2;
        JavFrame frame = JavFrame.allocAudio( channels, 100, Jav.AV_SAMPLE_FMT_S16P, 0, null );
                
        for( int i = 0; i < 4; i++ ) {
            System.out.println( frame.lineSize( i ) + "\t" + frame.extendedDataPointer( i ) );
        }
    }

}
