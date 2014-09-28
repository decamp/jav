/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;


public interface NativeObject {
    
    /**
     * @return native pointer to object, or 0 if {@code null}.
     */
    public long pointer();
    
    /**
     * @return the release action required by this object. 
     */
    public ReleaseMethod releaseMethod();
    
}
