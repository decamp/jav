/*
 * Copyright (c) 2014. Massachusetts Institute of Technology
 * Released under the BSD 2-Clause License
 * http://opensource.org/licenses/BSD-2-Clause
 * This file might include comments and code snippets from FFMPEG, released under LGPL 2.1 or later.
 */

package bits.jav.util;


public enum ReleaseMethod {
    SELF,   /** Object will release itself, if necessary, although it may be recommended to release object earlier. */ 
    OWNER,  /** Object is "owned" by another object that will release it. */
    USER    /** User MUST release object manually by calling appropriate method. */
}
