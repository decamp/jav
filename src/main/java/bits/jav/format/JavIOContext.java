/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.format;

import bits.jav.util.JavClass;
import bits.jav.util.ReleaseMethod;


/**
 * @author Philip DeCamp
 */
public class JavIOContext implements JavClass {

    private long mPointer;


    JavIOContext( long pointer ) {
        mPointer = pointer;
    }


    /**
     * @return start of the buffer.
     */
    public long buffer() {
        return nBuffer( mPointer );
    }

    /**
     * Maximum buffer size
     * */
    public int bufferSize() {
        return nBufferSize( mPointer );
    }

    /**
     * @return Current position in the buffer
     */
    public long bufferPtr() {
        return nBufferPtr( mPointer );
    }

    /**
     * @return End of the data, may be less than buffer+buffer_size if the read function returned
     *         less data than requested, e.g. for streams where no more data has been received yet.
     */
    public long bufferEnd() {
        return nBufferEnd( mPointer );
    }

    /**
     * @return position in the file of the current buffer
     */
    public long pos() {
        return nPos( mPointer );
    }

    /**
     * @return position in the file that corresponds to current buffer position.
     */
    public long filePos() {
        return nPosPtr( mPointer );
    }

    /**
     * @return true if the next seek should flush
     */
    public boolean mustFlush() {
        return nMustFlush( mPointer ) != 0;
    }

    /**
     * @return true if eof reached
     */
    public boolean eofReached() {
        return nEofReached( mPointer ) != 0;
    }

    /**
     * @return true if open for writing
     */
    public boolean writeFlag() {
        return nWriteFlag( mPointer ) != 0;
    }


    public int maxPacketSize() {
        return nMaxPacketSize( mPointer );
    }

    /**
     * @return contains the error code or 0 if no error happened
     */
    public int error() {
        return nError( mPointer );
    }

    /**
     * A combination of AVIO_SEEKABLE_ flags or 0 when the stream is not seekable.
     */
    public int seekable() {
        return nSeekable( mPointer);
    }

    /**
     * max filesize, used to limit allocations
     * This field is internal to libavformat and access from outside is not allowed.
     */
    public long maxSize() {
        return nMaxSize( mPointer );
    }

    /**
     * avio_read and avio_write should if possible be satisfied directly
     * instead of going through a buffer, and avio_seek will always
     * call the underlying seek function directly.
     */
    public int direct() {
        return nDirect( mPointer );
    }


    public long pointer() {
        return mPointer;
    }


    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }


    private static native long nBuffer( long ptr );
    private static native int  nBufferSize( long ptr );
    private static native long nBufferPtr( long ptr );
    private static native long nBufferEnd( long ptr );
    private static native long nPos( long ptr );
    private static native long nPosPtr( long ptr );
    private static native int  nMustFlush( long ptr );
    private static native int  nEofReached( long ptr );
    private static native int  nWriteFlag( long ptr );
    private static native int  nMaxPacketSize( long ptr );
    private static native int  nError( long ptr );
    private static native int  nSeekable( long ptr );
    private static native int  nMaxSize( long ptr );
    private static native int  nDirect( long ptr );

}
