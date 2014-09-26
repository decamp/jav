/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.swresample;

import java.io.*;
import java.nio.*;

import bits.jav.*;
import bits.jav.codec.*;
import bits.jav.format.*;
import bits.jav.util.*;

import static bits.jav.Jav.*;


/**
 * @author Philip DeCamp
 */
public class TestSwresample {

    public static void main( String[] args ) throws Exception {
        Jav.init();

        testSwresample( 22050, 22050 );
        //testSample( 22050, 22050 );
        //readAudio( 22050, 22050 );
    }


    static void testSwresample( int sampleRate, int dstBufSize ) throws Exception {
        float[] audio = readAudio( sampleRate, dstBufSize )[0];
        System.out.println( "Done reading audio. ");

        File outFile = new File( "/tmp/audio_format.mp3" );
        if( outFile.exists() ) {
            outFile.delete();
        }

        JavFormatContext format = JavFormatContext.openOutput( outFile, null, null );
        JavOutputFormat ofmt    = format.outputFormat();
        JavCodec codec          = JavCodec.findEncoder( AV_CODEC_ID_MP3 );

        System.out.println( ofmt.name() + "\t "+ ofmt.longName() + "\t" + ofmt.mimeType() );
        JavStream stream   = format.newStream( codec );
        JavCodecContext cc = stream.codecContext();

        int bitRate = (int)Rational.rescale( 128000, sampleRate, 44100 );
        System.out.println( bitRate / 1024 );

        cc.bitRate( bitRate );
        cc.sampleRate( sampleRate );
        cc.sampleFormat( AV_SAMPLE_FMT_S16P );
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

            //System.out.println( packet.dataPointer() + "\t" + packet.size() );
            format.writeFrame( packet );
            packet.size( 1024*1024 );
            packet.init();
        }

        while( true ) {
            if( cc.encodeAudio( null, packet, gotPacket ) < 0 ) {
                throw new IOException( "Encode audio failed." );
            }
            if( gotPacket[0] == 0 ) {
                break;
            }

            //System.out.println( packet.dataPointer() + "\t" + packet.size() );
            format.writeFrame( packet );
            packet.size( 1024*1024 );
            packet.init();
        }

        format.writeTrailer();
        format.close();

    }


    static void testSample( int sampleRate, int resampBufSize ) throws Exception {
        float[][] audio = readAudio( sampleRate, resampBufSize );
        File outFile = new File( "/tmp/audio_format.wav" );
        if( outFile.exists() ) {
            outFile.delete();
        }

        WavWriter.write( audio, sampleRate, outFile );
    }


    static float[][] readAudio( int sampleRate, int resampBufSize ) throws Exception {
        File file = new File( "resources_ext/video.ts" );
        JavFormatContext format = JavFormatContext.openInput( file );
        JavStream stream = null;

        for(int i = 0; i < format.streamCount(); i++) {
            JavStream s = format.stream( i );
            if( s.codecContext().codecType() == AVMEDIA_TYPE_AUDIO ) {
                stream = s;
                break;
            }
        }

        if( stream == null ) {
            System.out.println( "No audio stream found." );
            return null;
        }

        final int streamIndex = stream.index();
        JavPacket packet      = JavPacket.alloc();
        JavCodecContext cc    = stream.codecContext();

        System.out.format( "Codec: %d channels, %d layout, %d sampleRate, %s sampleFormat\n" ,
                           cc.channels(),
                           cc.channelLayout(),
                           cc.sampleRate(),
                           JavSampleFormat.getSampleFormatName( cc.sampleFormat() ) );



        JavFrame frame = JavFrame.alloc();

        //Open decodec.
        if( !cc.isOpen() ) {
            JavCodec cod = JavCodec.findDecoder( cc.codecId() );
            if( cod == null ) {
                throw new JavException( AVERROR_DECODER_NOT_FOUND, "Decoder not found.");
            }
            cc.open( cod );
        }

        int chans          = 1; //cc.channels();
        float[][] audio    = new float[chans][sampleRate*16];
        int samplePos      = 0;
        int packetPos      = 0;
        int[] gotFrame     = { 0 };
        boolean endOfInput = false;

        SwrContext rc = SwrContext.allocAndInit( cc.channelLayout(),
                                                 cc.sampleFormat(),
                                                 cc.sampleRate(),
                                                 Jav.AV_CH_LAYOUT_MONO,
                                                 Jav.AV_SAMPLE_FMT_FLT,
                                                 sampleRate );
        JavFrame res   = JavFrame.allocAudio( chans, resampBufSize, Jav.AV_SAMPLE_FMT_FLT, 0, null );
        ByteBuffer buf = res.directBuffer();

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

            assertOkay( cc.decodeAudio( packet, frame, gotFrame ) );

            if( gotFrame[0] == 0 ) {
                if( endOfInput ) {
                    break;
                } else {
                    continue;
                }
            }

            int samps    = frame.nbSamples();
            int resSamps = rc.convert( frame, samps, res, resampBufSize );
            assertOkay( resSamps );
            buf.clear();


            {   // Copy from frame buffer to array.
                final int end = Math.min( audio[0].length, samplePos + resSamps );
                for( int i = samplePos; i < end; i++ ) {
                    for( int j = 0; j < chans; j++ ) {
                        float f = buf.getFloat();
                        audio[j][i] = f;
                    }
                }

                samplePos = end;
                if( samplePos == audio.length ) {
                    break;
                }
            }
        }

        System.out.format( "Read: %d packets,  %d samples\n", packetPos, samplePos );
        return audio;
    }

}
