/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package gen;

import java.io.*;
import java.nio.ByteOrder;
import java.util.regex.*;

/**
 * @author decamp
 */
public class GenConsts {

    static final Pattern PREPROC       = Pattern.compile( "^\\s*+\\#" );
    static final Pattern DEFINE        = Pattern.compile( "^\\s*+\\#define\\s++(\\w++\\s++)(.++)" );
    static final Pattern ENUM          = Pattern.compile( "^\\s*+enum\\s++(\\w++)" );
    static final Pattern COMMENT_START = Pattern.compile( "//|/\\*" );
    static final Pattern ENUM_LINE     = Pattern.compile( "^\\s*+(\\w++\\s*+)($|,|=\\s*+(.*+))" );
    static final Pattern BLOCK_END     = Pattern.compile( "^\\s*+\\}" );
    
    static final Pattern VAL_DEC     = Pattern.compile( "^\\s*+(-?)(\\d++)(ULL)?", Pattern.CASE_INSENSITIVE );
    static final Pattern VAL_HEX     = Pattern.compile( "^\\s*+0x([0-9a-f]++)(ULL)?", Pattern.CASE_INSENSITIVE );
    static final Pattern VAL_PIX_FMT = Pattern.compile( "^\\s*+AV_PIX_FMT_NE\\s*+\\(\\s*+(\\w++)\\s*+\\,\\s*+(\\w++)\\s*+\\)" );
    static final Pattern VAL_BE_TAG  = Pattern.compile( "^\\s*+MKBETAG\\s*+\\(([^,]++),([^,]++),([^,]++),([^\\)]++)\\)" );
    static final Pattern VAL_ERR_TAG = Pattern.compile( "^\\s*+FFERRTAG\\s*+\\(([^,]++),([^,]++),([^,]++),([^\\)]++)\\)" );

    
    
    public static void generateConstFile( File snippetDir, File outFile ) throws IOException {
        outFile.getParentFile().mkdirs();
        PrintWriter p = new PrintWriter( outFile );

        p.println( "/* ");
        p.println( "* Copyright (c) 2014. Massachusetts Institute of Technology " );
        p.println( "* Released under the BSD 2-Clause License" );
        p.println( "* http://opensource.org/licenses/BSD-2-Clause ");
        p.println( "* This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later. ");
        p.println( "*/ ");

        p.println( "/* DO NOT MODIFY. This file was generated automatically by " + GenConsts.class.getName() + " */" );
        p.println( "package bits.jav;\n" );
        p.println( "import java.nio.ByteBuffer;");
        p.println( "import java.nio.ByteOrder;");
        p.println( "import bits.jav.util.Rational;\n" );
        p.println( "/**" );
        p.println( " * JAV library initialization and constants." );
        p.println( " * @author " + GenConsts.class.getName() );
        p.println( " */" );

        p.println( "public class Jav { \n" );
        String prefix = "    ";

        File[] snippetDirs = snippetDir.listFiles();
        assert snippetDirs != null;

        for( File dir: snippetDirs ) {
            if( !dir.isDirectory() || dir.isHidden() ) {
                continue;
            }
            
            p.println( prefix + "//==============================================================" );
            p.println( prefix + "// " + dir.getName() );
            p.println( prefix + "//==============================================================\n" );

            File[] dirFiles = dir.listFiles();
            assert dirFiles != null;

            for( File file: dirFiles ) {
                if( !file.isFile() || file.isHidden() ) {
                    continue;
                }
                
                String[] txt = processSnippet( file ).split( "\n" );
                for( String line: txt ) {
                    p.println( prefix + line );
                }
                
                p.println( "\n" );
            }
            
            p.println( "\n" );
        }
        
        p.println( "\n}" );
        p.close();
    }
    
    
    public static String processSnippet( File inputFile ) throws IOException {
        String name = inputFile.getName().toLowerCase();
        
        if( name.endsWith( ".java" ) ) {
            return processJavaSnippet( inputFile );
        } else {
            return processCSnippet( inputFile );
        }
    }
    
    
    public static String processJavaSnippet( File inputFile ) throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader br = new BufferedReader( new FileReader( inputFile ) );
        for( String k = br.readLine(); k != null; k = br.readLine() ) {
            s.append( k );
            s.append( "\n" );
        }
        
        br.close();
        return s.toString();
    }
    
    
    public static String processCSnippet( File inputFile ) throws IOException {
        BufferedReader br = new BufferedReader( new FileReader( inputFile ) );
        StringBuilder s = new StringBuilder();
        
        boolean inEnum    = false;
        int enumIndex     = 0;
        
        for( String k = br.readLine(); k != null; k = br.readLine() ) {
            // Split line at start of comment, if any.
            k = k.replace( "\t", "    " );
            String trailer = "\n";
            //System.err.println( k );
            
            Matcher m = COMMENT_START.matcher( k );
            if( m.find() ) {
                trailer = k.substring( m.start() ) + "\n";
                k = k.substring( 0, m.start() );
            } 
            
            m = DEFINE.matcher( k );
            if( m.find() ) {
                s.append( formatDefine( m.group( 1 ), parseValue( m.group( 2 ) ) ) );
                s.append( trailer );
                continue;
            }
            
            m = PREPROC.matcher( k );
            if( m.find() ) {
                continue;
            }
            
            m = ENUM.matcher( k );
            if( m.find() ) {
                inEnum    = true;
                enumIndex = 0;
                s.append( trailer );
                continue;
            }
            
            if( !inEnum ) {
                s.append( k );
                s.append( trailer );
                continue;
            }
            
            m = ENUM_LINE.matcher( k );
            if( m.find() ) {
                if( m.group( 2 ).startsWith( "=" ) ) {
                    enumIndex = parseNumber( m.group( 3 ) ).intValue();
                }
                s.append( formatDefine( m.group( 1 ), enumIndex++ ) ); // formatNumber( enumIndex++ ) ) );
                s.append( trailer );
                continue;
            }
            
            m = BLOCK_END.matcher( k );
            if( m.find() ) {
                inEnum = false;
                s.append( trailer );
                continue;
            }
            
            if( k.trim().length() > 0 ) {
                if( !k.trim().startsWith( "*" ) ) {
                    System.err.println( "Warning - Unknown line in enum: " + k );
                }
                s.append( k );
            }
            
            s.append( trailer );
        }
        
        br.close();
        return s.toString();
    }
    
    
    private static Object parseValue( String s ) {
        Matcher m = VAL_PIX_FMT.matcher( s );
        if( m.find() ) {
            return makePixTag( m.group( 1 ), m.group( 2 ) );
        }
        
        Number number = parseNumber( s );
        if( number != null ) {
            return number;
        }
        
        return s;
    }
    
    
    private static Number parseNumber( String s ) {
        Matcher m = VAL_BE_TAG.matcher( s );
        if( m.find() ) {
            int[] vals = parseMakeTagArgs( m, 1, 4 );
            return makeBeTag( vals[0], vals[1], vals[2], vals[3] );
        }
        
        m = VAL_ERR_TAG.matcher( s );
        if( m.find() ) {
            int[] vals = parseMakeTagArgs( m, 1, 4 );
            return makeErrTag( vals[0], vals[1], vals[2], vals[3] );
        }
        
        m = VAL_HEX.matcher( s );
        if( m.find() ) {
            return parseHexMatch( m );
        }        
                
        m = VAL_DEC.matcher( s );
        if( m.find() ) {
            return parseDecMatch( m );
        }
        
        return null;
    }
    
    
    private static Number parseDecMatch( Matcher m ) {
        int sign  = 1;
        if( "-".equals( m.group( 1 ) ) ) {
            sign = -1;
        }
        
        long value = 0;
        String k = m.group( 2 ).toLowerCase();
        
        while( true ) {
            if( k.length() > 8 ) {
                value = value << 32 | Long.parseLong( k.substring( 0, 8 ) );
                k = k.substring( 8 );
            } else if( k.length() > 0 ) {
                value = value << ( k.length() * 4 ) | Long.parseLong( k );
                break;
            } else {
                break;
            }
        }
        
        if( ( m.end( 3 ) - m.start( 3 ) > 0 ) || ( value >>> 32 != 0 ) ) {
            return new Long( sign * value );
        } else {
            return new Integer( (int)( sign * value ) );
        }
    }

    
    private static Number parseHexMatch( Matcher m ) {
        long value = 0;
        String k = m.group( 1 ).toLowerCase();
        
        while( true ) {
            if( k.length() > 8 ) {
                value = value << 32 | Long.parseLong( k.substring( 0, 8 ), 16 );
                k = k.substring( 8 );
            } else if( k.length() > 0 ) {
                value = value << ( k.length() * 4 ) | Long.parseLong( k, 16 );
                break;
            } else {
                break;
            }
        }
        
        if( ( m.end( 2 ) - m.start( 2 ) > 0 ) || ( value >>> 32 != 0 ) ) {
            return new Long( value );
        } else {
            return new Integer( (int)( value ) );
        }
    }
    

    private static int[] parseMakeTagArgs( Matcher m, int off, int len ) {
        int[] ret = new int[len];
        for( int i = 0; i < len; i++ ) {
            String v = m.group( i + off ).trim();
            if( v.length() > 2 && v.charAt( 0 ) == '\'' ) {
                ret[i] = v.charAt( 1 );
                continue;
            }
            
            Number num = parseNumber( v );
            if( num != null ) {
                ret[i] = num.intValue();
                continue;
            }
            
            System.err.println( "Could not parse value: " + m.group( 0 ) );
        }
        
        return ret;
    }
    
    
    private static String formatDefine( String key, Object val ) {
        StringBuilder s = new StringBuilder();
        
        if( val instanceof Long ) {
            s.append( "public static final long " );
        } else {
            s.append( "public static final int " );
        }
        
        s.append( key );
        if( !key.endsWith( " " ) ) {
            s.append( " " );
        }
        s.append( "= " ).append( formatValue( val ) ).append( "; " );
        return s.toString();
    }
    
    
    private static String formatValue( Object val ) {
        if( val instanceof Long || val instanceof Integer ) {
            return formatNumber( (Number)val );
        }
        return val.toString();
    }
    
    
    private static String formatNumber( Number v ) {
        if( !( v instanceof Long || v instanceof Integer ) ) {
            return v.toString();
        }
        
        StringBuilder s = new StringBuilder( "0x" );
        long vv = v.longValue();
        boolean isLong = false;
        
        // Check if input is long.
        if( v instanceof Long ) {
            s.append( String.format( "%08X", vv >>> 32 ) );
            isLong = true;
        }
        s.append( String.format( "%08X", (int)( vv & 0xFFFFFFFFL ) ) );
        if( isLong ) {
            s.append( "L" );
        }
        
        return s.toString();
   }
    
    
    private static int makeLeTag( int a, int b, int c, int d ) {
        return (   a & 0xFF         ) |
               ( ( b & 0xFF ) <<  8 ) |
               ( ( c & 0xFF ) << 16 ) |
               ( ( d & 0xFF ) << 24 );
    }
    

    private static int makeBeTag( int a, int b, int c, int d ) {
        return (   d & 0xFF         ) |
               ( ( c & 0xFF ) <<  8 ) |
               ( ( b & 0xFF ) << 16 ) |
               ( ( a & 0xFF ) << 24 );
    }
    
    
    private static int makeErrTag( int a, int b, int c, int d ) {
        return -makeLeTag( a, b, c, d );
    }
    
    
    private static String makePixTag( String a, String b ) {
        boolean le = ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
        return "AV_PIX_FMT_" + ( le ? a : b );
    }
    
    
    
    public static void main( String[] args ) throws Exception {
        //test1();
        generateConstFile( new File( "resources_build/snippets" ), new File( "src/bits/jav/Jav.java" ) );
    }

    
    public static void test1() throws Exception {
        File dir = new File( "resources_build/snippets" );
        File[] files = dir.listFiles();
        assert files != null;
        for( int i = 4; i < 5; i++ ) {
            File f = files[i];
            System.out.println( f.getAbsolutePath() );
            String s = processSnippet( f );
            System.out.println( s );
        }
        
    }
    
    
    public static void test0() throws Exception {
        //Matcher m = DEFINE.matcher( "#define this that( achey breaky ) /* in with the */" );
        Matcher m = ENUM_LINE.matcher( "          AV_CODEC_ID_Y41P       = MKBETAG('Y','4','1','P')," );
        //Matcher m = VAL_NUM.matcher( "0x445ULL" );
        
        if( m.find() ) {
            for( int i = 0; i <= m.groupCount(); i++ ) {
                System.out.println( m.group( i ) );
            }
        } else {
            System.out.println( "No" );
        }
    }
    
}
