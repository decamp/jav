### JAV (Java Audio Video) library
##### An FFMPEG Wrapper for OS X 10.6 or above.

This library uses FFMPEG to:
- Encode or decode audio/video streams using most major compression formats (h264,mp3,aac,etc.)
- Read/write media files using most major file formats (mp4,mov,flv,etc.)
- Rescale images
- Resample audio

This project includes pre-compiled versions of the FFMPEG, X264, and LAME libraries that are portable and can be included with an application without system install.

This codebase is not particularly stable. There are many partial FFMPEG wrappers. This one is simply the one I maintain for my own projects. Automatic wrapping for FFMPEG is difficult for a number of reasons. This wrapper includes an attempt to make some of the memory management slightly cleaner on the Java side, while keeping overhead minimal. But there are many places where pointers are passed around as long values.

The following FFMPEG components are more-or-less supported:
- libavutil -- Dict, Option, BufferRef, BufferPool, Rational, SampleFormat
- libavcodec -- Codec, CodecContext, Frame, Packet
- libavformat -- FormatContext, IOContext (partial), OutputFormat, Stream
- libswscale -- SwsContext
- libswresample -- SwrContext (not well tested)


### Build
For java code, run "ant".

Pre-compiled native libraries are located in the "lib" folder. They all have install paths of "@loader_path/libname". If necessary, they can be recompiled:

1. Third-Party Libs:
  - cd thirdparty
  - ./build_all.sh
  - Note: x264 and lame must be built before FFMPEG.
  
2. C code:
  - cd src/main/c
  - make
  - Note: third-party libraries must be built before JAV C code.

There is some light code generation going on in "src/gen/java". There are no instructions, but it's things that are mainly run to pull in constants and simple getters/settings from FFMPEG.

### Runtime:
After build, add everything (\*.jar, \*.dylib, \*.jnilib) in **lib** and **target** folders to your project. All jnilib/dylib files must be kept in the same directory (or you must update the install paths with, eg, "install_name_tool"), and that directory must be added to your java.library.path runtime property ("java -Djava.library.path=lib_dir")


### Dependencies:
ffmpeg 
- The main AV library. 
- Located in "thirdparty/ffmpeg" as git submodule.
- License: Most is under LGPL, but some is GPL v2. See "thirdparty/ffmpeg/LICENSE.md"
- Web: <https://www.ffmpeg.org/>

x264
- H264 codec.
- Located in "thirdparty/x264" as git submodule.
- License: GPL v2. See "thirdparty/x264/COPYING"
- Web: <http://www.videolan.org/developers/x264.html>

lame
- MP3 codec
- Located in "thirdparty/lame-3.99.5" as zipped project folder.
- License: LGPL. See "thirdparty/lame-3.99.5.zip/LICENSE"
- Web: <http://lame.sourceforge.net/>

bits_util.jar 
- Used for ref counting and object pools.
- License: Same as this project.
- Web: <https://github.com/decamp/util>


### Gotchas
FFMPEG has updated its memory ownership model using the AVBufferRef class. Support for this
has not been well tested. Be sure to check JavCodecContext.refcountedFrames(),
and set it to **true** if you want to manage your own frame pool, or set it to **false**
and **always** make safe copies of decoded frames.


---
Author: Philip DeCamp