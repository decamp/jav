/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package gen;

import java.io.*;
import java.util.regex.*;

public class GenCWrapperCode {
    
    
    public static void main( String[] args ) throws Exception {
        genGettersSetters();
    }
    
    
    static void genGettersSetters() throws Exception {
//        File inFile  = new File( "src_c/jav/src/JavCodecContext.h" );
//        File outFile = new File( "src_c/jav/src/JavCodecContextGen.c" );
//        String ctype = "AVCodecContext";
        File inFile  = new File( "src_c/jav/src/JavFrame.h" );
        File outFile = new File( "src_c/jav/src/JavFrameGen.c" );
        String ctype = "AVFrame";
        
        Pattern DEC_PAT = Pattern.compile( "JNIEXPORT (\\w++) JNICALL (\\w+_n([\\w+^_]+)__(J\\w?))\\s*+$" );
        Pattern ARG_PAT = Pattern.compile( "\\w++" );
        
        BufferedReader in = new BufferedReader( new FileReader( inFile ) );
        PrintWriter out   = new PrintWriter( outFile );
        
        out.println( "#include <stdio.h>" );
        out.println( "#include \"libavcodec/avcodec.h\"" );
        out.format( "#include \"%s\"\n\n", inFile.getName() );
        
        
        for( String k = in.readLine(); k != null; k = in.readLine() ) {
            Matcher m = DEC_PAT.matcher( k );
            if( !m.find() ) {
                continue;
            }
            
            String retType  = m.group( 1 ).intern();
            //String funcName = m.group( 2 );
            String varName  = parseVarName( m.group( 3 ) );
            String sig      = m.group( 4 );
            
            if( sig.length() > 2 ) { 
                continue;
            }
            
            //System.out.println( retType + " " + funcName + "  " + parseVarName( varName ) + " " + sig ); 
            k = in.readLine();
            
            String argType = null;
            
            if( sig.length() == 1 ) {
                if( !supportedType( retType ) ) {
                    continue;
                }
            } else if( sig.length() == 2 ) {
                if( retType != "void" ) {
                    continue;
                }
                Matcher m2 = ARG_PAT.matcher( k );
                m2.find();
                m2.find();
                m2.find();
                m2.find();
                argType = m2.group( 0 ).intern();
                if( !supportedType( argType ) ) {
                    continue;
                }
            }
            
            out.println( m.group() );
            out.print( "( JNIEnv *env, jclass clazz, jlong pointer" );
            
            if( argType != null ) {
                out.print( ", " + argType + " val" );
            }
            
            out.println( " )\n{" );
            
            if( argType == null ) {
                out.format( "  return (%s)(**(%s**)&pointer).%s;\n", retType, ctype, varName );
            } else {
                out.format( "  (**(%s**)&pointer).%s = val; \n", ctype, varName );
            }
            
            out.println( "}\n" );
        }
        
        in.close();
        out.close();
    }
    
    
    static String parseVarName( String t ) {
        StringBuilder s = new StringBuilder();
        for( int i = 0; i < t.length(); i++ ) {
            char c = t.charAt( i );
            
            if( c >= 'A' && c <= 'Z' ) {
                if( i > 0 ) {
                    s.append( '_' );
                }
                s.append( (char)( c + 'a' - 'A' ) );
            } else {
                s.append( c );
            }
        }
        
        return s.toString();
    }

    
    static boolean supportedType( String t ) {
        t = t.intern();
        return t == "jint" ||
               t == "jlong" ||
               t == "jbyte" ||
               t == "jshort" ||
               t == "jfloat" ||
               t == "jdouble" ||
               t == "void";
    }
    
}
