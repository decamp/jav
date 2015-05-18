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

source buildtools/detect_platform.sh


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
      if [[ "$OSTYPE" == "darwin"* ]]; then
          $projdir/buildtools/rename_dylib $scratch/lib @loader_path true
      fi
            
      # Move libraries into place.
      names[0]=avutil
      names[1]=avcodec
      names[2]=avformat
      names[3]=avdevice
      names[4]=avfilter
      names[5]=swscale
      names[6]=swresample
      names[7]=postproc
      for name in ${names[@]}; do
        cp $scratch/lib/$(gen_soname $name '') $projdir/lib
      done
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done
