/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;


public final class OpaqueJavClass implements JavClass {
    
    final long mPointer;
    
    public OpaqueJavClass( long pointer ) {
        mPointer = pointer;
    }

    
    @Override
    public long pointer() {
        return mPointer;
    }

    @Override
    public ReleaseMethod releaseMethod() {
        return ReleaseMethod.OWNER;
    }

}
