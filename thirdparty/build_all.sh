#!/bin/bash

cd `dirname "$0"`
./build_lame.sh
./build_x264.sh
./build_ffmpeg.sh