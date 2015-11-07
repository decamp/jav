#!/bin/bash

libname='lame'
libdir='lame-3.99.5'

cd `dirname "$0"`
basedir=`pwd`
workdir=$basedir/$libdir

cd ..
projdir=`pwd`
scratch=$projdir/scratch/thirdparty/$libname

source buildtools/detect_platform.sh

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
      shortName=$(gen_soname 'mp3lame' '')
      longName=$(gen_soname 'mp3lame' '.0')
      
      if [[ "$OSTYPE" == "darwin"* ]]; then
          $projdir/buildtools/rename_dylib $scratch/lib @loader_path true
      fi
      cp $scratch/lib/$longName $projdir/lib/$shortName
      ;;

    *)
      echo "### Unknown command: $cmd"
      ;;
      
  esac
done
