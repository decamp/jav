#!/bin/bash

libname='x264'
libdir='x264'

cd `dirname "$0"`
basedir=`pwd`
workdir=$basedir/$libdir

cd ..
projdir=`pwd`
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

      shortName=$(gen_soname 'x264' '' )
      longName=$(gen_soname 'x264' '.142' )
      
      rm $scratch/lib/$shortName
      mv $scratch/lib/$longName $scratch/lib/$shortName
      
      # Make library paths relative.
      if [[ "$OSTYPE" == "darwin"* ]]; then
          ${projdir}/buildtools/rename_dylib ${scratch}/lib @loader_path true
      fi
      cp $scratch/lib/$shortName $projdir/lib/$shortName
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done
