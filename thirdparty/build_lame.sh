#!/bin/bash

libname='lame'
libdir='lame-3.99.5'

cd `dirname "$0"`
basedir=`pwd`
workdir=$basedir/$libdir
cd `dirname "$0"`/..
projdir=`pwd`
scratch=$projdir/scratch/thirdparty/$libname


if [ $# -lt 1 ]; then
  cmds=("clean" "configure" "compile" "install")
else
  cmds=$@
fi

for cmd in ${cmds[@]}; do
  case $cmd in 
    
    clean)
      echo "### Clean..."
      if [ -d $workdir ]; then
        rm -r $workdir
      fi
      cd $basedir
      unzip -o ${libdir}.zip
      ;;
    
    configure)
      echo "### Configure..."
      if [ ! -d $workdir ]; then
        >&2 echo "No directory: $workdir"
        exit 1
      fi

      cd $workdir
      ./configure --prefix=$scratch
      ;;
    
    compile)
      echo "### Compile..."
      if [ ! -d $workdir ]; then
        >&2 echo "No directory: $workdir"
        exit 1
      fi

      cd $workdir
      make
      ;;
    
    install)
      echo "### Install..."
      if [ ! -d $workdir ]; then
        >&2 echo "No directory: $workdir"
        exit 1
      fi

      cd $workdir
      make install
      # Make library paths relative.
      $projdir/buildtools/rename_dylib $scratch/lib @loader_path
      cp $scratch/lib/libmp3lame.0.dylib $projdir/lib/libmp3lame.dylib
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done