/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav;

import java.util.List;

import bits.jav.swscale.SwsContext;
import bits.jav.util.*;

/**
 * @author decamp
 */
public class TestOptions {


    public static void main( String[] args ) throws Exception {
        testOptions();
    }
    
    
    static void testOptions() throws Exception {
        Jav.init();
        
        SwsContext context = SwsContext.alloc();
        List<JavOption> options = JavOption.find( context );
        
        for( JavOption opt: options ) {
            System.out.println( opt.toDescription() );
        }

        JavOption option = JavOption.find( context, "srcw" );
        
        System.out.println( option );
        System.out.println( JavOption.setString( context, "dstw", "65", 0 ) );
        System.out.println( JavOption.getString( context, "dstw", 0 ) );
        
        Rational q = JavOption.getRational( context, "dstw", 0 );
        System.out.println( q.num() + "\t" + q.den() );
    }

}
