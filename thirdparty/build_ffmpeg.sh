#!/bin/bash

libname='ffmpeg'
libdir='ffmpeg'

cd `dirname "$0"`
basedir=`pwd`
workdir=$basedir/$libdir
cd ..
projdir=`pwd`
incdir=$projdir/scratch/thirdparty
scratch=$projdir/scratch/thirdparty/$libname

if [ ! -d $workdir ]; then
  >&2 echo "No directory: $workdir"
  exit 1
fi

if [ $# -lt 1 ]; then
  cmds=("clean" "configure" "compile" "install")
else
  cmds=$@
fi

for cmd in ${cmds[@]}; do
  case $cmd in 
    
    clean)
      echo "### Clean..."
      cd $workdir
      git reset --hard
      git clean -d -x -f
      ;;
    
    configure)
      echo "### Configure..."
      echo "PREFIX: ${scratch}"
      cd $workdir
      ./configure \
        --enable-shared \
        --prefix=${scratch} \
        --disable-debug \
        --disable-doc \
        --disable-programs \
        --disable-sdl \
        --enable-gpl \
        --enable-libmp3lame \
        --enable-libx264 \
        --disable-ffplay \
        --extra-cflags="-I$incdir/lame/include -I$incdir/x264/include" \
        --extra-ldflags="-L$incdir/lame/lib -L$incdir/x264/lib"
      
      # Apply patches.
      patch -p1 < ../ffmpeg_id3_comment.patch
      ;;

    compile)
      echo "### Compile..."
      cd $workdir
      make
      ;;
    
    install)
      echo "### Install..."
      cd $workdir
      make install
      
      # Make library paths relative.
      $projdir/buildtools/rename_dylib $scratch/lib @loader_path
            
      # Move libraries into place.
      names[0]=libavutil
      names[1]=libavcodec
      names[2]=libavformat
      names[3]=libavdevice
      names[4]=libavfilter
      names[5]=libswscale
      names[6]=libswresample
      names[7]=libpostproc
      for name in ${names[@]}; do
        cp $scratch/lib/$name.dylib $projdir/lib
      done
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done