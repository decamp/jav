/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import javax.sound.sampled.*;

import java.io.*;
import java.nio.*;

public class WavWriter {
    
    public static void write(float[][] inBufs, int sampleRate, File outputFile) throws IOException{
        final int numBufs = inBufs.length;
        final int numSamples = inBufs[0].length;
        byte[] b = new byte[numBufs * numSamples * 2];
        ByteBuffer buf = ByteBuffer.wrap(b);
        float scale = Short.MAX_VALUE;
        int underSample = 0;
        int overSample = 0;


        for(int i = 0; i < numSamples; i++){
            for(int j = 0; j < numBufs; j++){
                float val = inBufs[j][i];

                if(val < -1f){
                    underSample++;
                    buf.putShort(Short.MIN_VALUE);

                }else if(val > 1f){
                    overSample++;
                    buf.putShort(Short.MAX_VALUE);

                }else{
                    buf.putShort((short)(val * scale));
                }
            }
        }

        System.out.println("Overclipped samples: " + overSample);
        System.out.println("Underclipped samples: " + underSample);
        System.out.println("Writing to: " + outputFile.getPath());

        AudioFormat format = new AudioFormat(sampleRate, 16, numBufs, true, true);  
        ByteArrayInputStream byteStream = new ByteArrayInputStream(b);
        AudioInputStream audioStream = new AudioInputStream(byteStream, format, numSamples * numBufs);
        AudioSystem.write(audioStream, AudioFileFormat.Type.WAVE, outputFile);
    }
    

}
