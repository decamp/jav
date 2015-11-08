#!/bin/bash

cd `dirname "$0"`/..
PROJDIR=`pwd`
CLASSPATH="$PROJDIR/lib/bits_util.jar:$PROJDIR/scratch/main/java"
OUTDIR="$PROJDIR/src/main/c"

names="bits.jav.Jav"
names+=" bits.jav.JavException"
names+=" bits.jav.util.NativeUtil"
names+=" bits.jav.util.JavOption"
names+=" bits.jav.util.JavDict"
names+=" bits.jav.util.JavBufferRef"
names+=" bits.jav.util.JavBufferPool"
names+=" bits.jav.util.JavMem"
names+=" bits.jav.util.JavPixelFormat"
names+=" bits.jav.util.JavSampleFormat"
names+=" bits.jav.util.JavChannelLayout"
names+=" bits.jav.codec.JavCodecContext"
names+=" bits.jav.codec.JavCodec"
names+=" bits.jav.codec.JavFrame"
names+=" bits.jav.codec.JavPacket"
names+=" bits.jav.format.JavFormatContext"
names+=" bits.jav.format.JavStream"
names+=" bits.jav.format.JavOutputFormat"
names+=" bits.jav.format.JavIOContext"
names+=" bits.jav.swscale.SwsContext"
names+=" bits.jav.swresample.SwrContext"


for name in ${names[@]}; do
  javah -classpath $CLASSPATH $name
  # Compute name of generated file.
  f0=$(echo $name|sed 's|\.|_|g').h
  # Determine output file name. The package name get chopped off the front.
  f1=$OUTDIR/$(echo $name|sed 's|.*\.||g').h
  # Replace invalid <jni.h> with <JavaVM/jni.h> and move into place.
  echo "${name} --> $f1"
  cat $f0 | sed "s|^#include <jni\.h>|#include <JavaVM/jni.h>|" > $f1
  rm $f0
  #mv $f0 $f1
done


delNames="bits.jav.util.JavDict.Entry"
for name in ${delNames[@]}; do
  f0=$(echo $name|sed 's|\.|_|g').h
  rm ${f0}
done

