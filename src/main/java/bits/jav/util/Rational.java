/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;


public final class Rational {

    public static final int AV_ROUND_ZERO        = 0; ///< Round toward zero.
    public static final int AV_ROUND_INF         = 1; ///< Round away from zero.
    public static final int AV_ROUND_DOWN        = 2; ///< Round toward -infinity.
    public static final int AV_ROUND_UP          = 3; ///< Round toward +infinity.
    public static final int AV_ROUND_NEAR_INF    = 5; ///< Round to nearest and halfway cases away from zero.
    public static final int AV_ROUND_PASS_MINMAX = 8192; ///< Flag to pass INT64_MIN/MAX through instead of rescaling, this avoids special cases for AV_NOPTS_VALUE

    /**
     * Reduces the fraction {@code num / den } to a <i>canonical</i> reduced representation, {@code out}, where:
     * <ul>
     * <li> There will exist no integer that can evenly divide both {@code out.num()} and {@code out.den()}.
     * <li> {@code out.den() >= 0} </li>
     * <li> If {@code den == 0, then out.num == 0 && out.den == 0 }.
     * <li> If {@code den != 0 && num == 0, then out.num = 0 && out.den == 1}.
     * </ul>
     * <p>
     * As a consequnce, if Rationals {@code a} and {@code b} are both
     * reduced, then: <br>
     * {@code a.toDouble() == b.toDouble() => a.equals(b) || Double.isNaN( a.toDouble() ) && Double.isNaN( b.toDouble() )}
     * 
     * @param num Numerator
     * @param den Denominator
     * @return rational in canonical reduced format.
     */
    public static Rational reduce( int num, int den ) {
        if( den == 0 ) {
            num = 0;
        } else if( num == 0 ) {
            den = 1;
        } else {
            int d = gcd( num, den );
            num /= d;
            den /= d;

            if( den < 0 ) {
                num = -num;
                den = -den;
            }
        }

        return new Rational( num, den );
    }

    /**
     * Computes greatest common divisor of two terms using Euclid's method.
     * 
     * @param a Arbitrary number
     * @param b Arbitrary number
     * @return Largest number that evenly divides both a and b.
     */
    public static int gcd( int a, int b ) {
        while( b != 0 ) {
            int c = a % b;
            a = b;
            b = c;
        }
        return a;
    }

    /**
     * Computes greatest common divisor of two terms using Euclid's method.
     * 
     * @param a Arbitrary number
     * @param b Arbitrary number
     * @return Largest number that evenly divides both a and b.
     */
    public static long gcd( long a, long b ) {
        while( b != 0 ) {
            long c = a % b;
            a = b;
            b = c;
        }

        return a;
    }
    
    /**
     * Rescale a 64-bit integer, a * b / c, with protection against
     * overflow. 
     * 
     * @return long value nearest to a * b / c
     */
    public static long rescale( long a, long b, long c ) {
        return rescaleRound( a, b, c, AV_ROUND_NEAR_INF );
    }

    /**
     * Rescale a 64-bit integer, a * b / c, with specified rounding 
     * and protection against overflow.
     * 
     * @return rescaled value a, or if AV_ROUND_PASS_MINMAX is set and a is
     *         INT64_MIN or INT64_MAX then a is passed through unchanged.
     */
    public static long rescaleRound( long a, long b, long c, int rnd ) {
        long r = 0;
        assert( c > 0 );
        assert( b >= 0 );
        assert( ( rnd & ~AV_ROUND_PASS_MINMAX ) <= 5 && ( rnd & ~AV_ROUND_PASS_MINMAX ) != 4 );
        
        if( ( rnd & AV_ROUND_PASS_MINMAX ) != 0) {
            if( a == Long.MIN_VALUE || a == Long.MAX_VALUE ) {
                return a;
            }
            rnd -= AV_ROUND_PASS_MINMAX;
        }
        
        if( a < 0 && a != Long.MIN_VALUE ) {
            return -rescaleRound( -a, b, c, rnd ^ ((rnd >> 1 ) & 1 ) );
        }
        if( rnd == AV_ROUND_NEAR_INF ) {
            r = c / 2;
        } else if( ( rnd & 1 ) != 0 ) {
            r = c -1;
        }
        
        if( b <= Integer.MAX_VALUE && c <= Integer.MAX_VALUE ) {
            if( a <= Integer.MAX_VALUE ) {
                return ( a * b + r ) / c;
            } else {
                return a / c * b + ( a % c * b + r ) / c;
            }
        } else {
            long a0 = a & 0xFFFFFFFFL;
            long a1 = a >> 32;
            long b0 = b & 0xFFFFFFFFL;
            long b1 = b >> 32;
            long t1 = a0 * b1 + a1 * b0;
            long t1a = t1 << 32;
            
            a0 = a0 * b0 + t1a;
            a1 = a1 * b1 + ( t1 >> 32 ) + ( a0 < t1a ? 1 : 0 );
            a0 += r;
            a1 += ( a0 < r ? 1 : 0 );
            
            for( int i = 63; i >= 0; i-- ) {
                a1 += a1 + (( a0 >> i ) & 1 );
                t1 += t1;
                if( c <= a1 ) {
                    a1 -= c;
                    t1++;
                }
            }

            return t1;
        }
    }

    /**
     * Rescale a 64-bit integer by 2 rational numbers.
     */
    public static long rescaleQ( long a, Rational bq, Rational cq ) {
        return rescaleQRound( a, bq, cq, AV_ROUND_NEAR_INF );
    }
    
    /**
     * Rescale a 64-bit integer by 2 rational numbers, a * bq / cq, 
     * with specified rounding.
     *
     * @return rescaled value a, or if AV_ROUND_PASS_MINMAX is set and a is
     *         INT64_MIN or INT64_MAX then a is passed through unchanged.
     */
    public static long rescaleQRound( long a, Rational bq, Rational cq, int rnd ) {
        long b = bq.num() * (long)cq.den();
        long c = cq.num() * (long)bq.den();
        return rescaleRound( a, b, c, rnd );
    }
    
    /**
     * Compare 2 timestamps each in its own timebases.
     * The result of the function is undefined if one of the timestamps
     * is outside the int64_t range when represented in the others timebase.
     * @return -1 if ts_a is before ts_b, 1 if ts_a is after ts_b or 0 if they represent the same position
     */
    public static int compareTimestamps( long ts_a, Rational tb_a, long ts_b, Rational tb_b ) {
        long a = tb_a.num() * (long)tb_b.den();
        long b = tb_b.num() * (long)tb_a.den();
        
        if( ( Math.abs( ts_a ) | a | Math.abs( ts_b ) | b ) <= Integer.MAX_VALUE ) {
            return ( ts_a * a > ts_b * b ? 1 : 0 ) - ( ts_a * a < ts_b * b ? 1 : 0 );
        }
        if( rescaleRound( ts_a, a, b, AV_ROUND_DOWN ) < ts_b ) {
            return -1;
        }
        if( rescaleRound( ts_b, b, a, AV_ROUND_DOWN ) < ts_a ) {
            return 1;
        }
        
        return 0;
    }

    /**
     * Don't use this unless you're dealing with the native code that returns
     * a correctly constructed Rational.
     *
     * @param val
     * @return
     */
    public static Rational fromNativeLong( long val ) {
        return new Rational( (int)(val >>> 32), (int)val );
    }



    private final int mNum;
    private final int mDen;


    public Rational( int num, int den ) {
        mNum = num;
        mDen = den;
    }


    public int num() {
        return mNum;
    }


    public int den() {
        return mDen;
    }


    public double toDouble() {
        return (double)mNum / mDen;
    }


    public Rational reduce() {
        return reduce( mNum, mDen );
    }


    @Override
    public String toString() {
        return String.format( "%d/%d", mNum, mDen );
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Rational) ) {
            return false;
        }
        Rational r = (Rational)obj;
        return mNum == r.mNum && mDen == r.mDen;
    }

    @Override
    public int hashCode() {
        return mNum ^ mDen;
    }


    @Deprecated public double asDouble() {
        return (double)mNum / mDen;
    }

}
