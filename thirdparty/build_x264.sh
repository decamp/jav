#!/bin/bash

libname='x264'
libdir='x264'

cd `dirname "$0"`
basedir=`pwd`
workdir=$basedir/$libdir

cd ..
projdir=`pwd`
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
      cd $workdir
      ./configure --prefix=$scratch --enable-shared
      ;;
    
    compile)
      echo "### Compile..."
      cd $workdir
      make lib-shared
      ;;
    
    install)
      echo "### Install..."
      cd $workdir
      make install-lib-shared
      rm ${scratch}/lib/libx264.dylib
      mv ${scratch}/lib/libx264.142.dylib ${scratch}/lib/libx264.dylib
      
      # Make library paths relative.
      ${projdir}/buildtools/rename_dylib ${scratch}/lib @loader_path true
      
      cp ${scratch}/lib/libx264.dylib ${projdir}/lib/libx264.dylib
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done
