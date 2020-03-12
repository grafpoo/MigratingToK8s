#!/bin/bash
CONTAINER_NAME=abc
IMAGE_NAME=abc
usage()
{
   echo "usage: dockerRun [[-d | --detach (defaults to interactive)]
                           [-o <cmd-to-run-insteadof endpont>]
                           [-n | --network <network-to-run-container-in> ]
                           [ -l | --leave (don't rm the container)]
                    [ -t | --tag <version-tag> (defaults to latest)]    
                [-h | --help | -?]]"
}
tag=latest
mode=-it
network=Test-Net
clean=--rm
entrypoint=
cmd=
while [ "$1" != "" ]; do
   case $1 in
       -d | --detach )         mode=-d
                               ;;
       -n | --network )        shift
                network=$1
                ;;
       -t | --tag )            shift
                tag=$1
                ;;
       -l | --leave )          clean=
                ;;
       -o )                    shift
                               cmd=$1
                entrypoint=--entrypoint=""
                               ;;
       -h | --help | -? )      usage
                               exit
                               ;;
       * )                     usage
                               exit 1
   esac
   shift
done
echo "Running Image: $IMAGE_NAME:$tag as Container: $CONTAINER_NAME"
