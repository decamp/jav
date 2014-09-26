### JAV = Java wrapper for FFMPEG


#### Supported Platforms: OS X 64-bit

JAV stands for "Java Audio Video library". It is a wrapper for FFMPEG. Through FFMPEG, this library can:
- Encode or decode audio/video streams using most major compression formats (h264,mp3,aac,etc.)
- Read/write media files using most major file formats (mp4,mov,flv,etc.)
- Rescale images
- Resample audio

There are many partial FFMPEG wrappers. This one is simply mine that I keep up-to-spec for my own projects. Automatic wrapping for FFMPEG is difficult because of memory management issues. This wrapper includes an attempt to make some of the memory management slightly more doable from Java with minimal overhead. That said, there are a lot of places in here where pointers are passed around as long values, so this is not for JavaBeaners that are feint of hearts.


### Build
1. Third-Party Dynamic Libraries: Pre-compiled binaries are located in the "lib" folder. They can be generated by:
  - cd thirdparty
  - ./build_all.sh
  Note that x264 and lame must be built before FFMPEG.
  
2. C code: Pre-compiled library is located in "lib/libjav.jnilib". This lib can be generated by:
  - cd src/main/c
  - make
  Note that third-party libraries must be built before JAV C code.
3. Java code: "ant"

There is also some light code generate code located in "src/gen/java". There are no instructions, and don't need to be run unless extending the library.


### Runtime:
After build, add all jars, dylibs, and jnilibs in **lib** and **target** folders to your project. Note that the jnilib/dylib files must be kept in the same directory, and that directory must be added to your java.library.path runtime property ("java -Djava.library.path=lib_dir")


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
bits_langx.jar 
- Used for ref counting and object pools.
- License: Same as JAV.


### Gotchas
FFMPEG has updated its memory ownership model using the AVBufferRef class, and this
wrapper does not support this very fully. Be sure to check JavCodecContext.refcountedFrames(),
and set it to TRUE/NON-ZERO if you want to manage your own frame pool, or set it to false
and ALWAYS make safe copies of decoded frames.


---
Author: Philip DeCamp