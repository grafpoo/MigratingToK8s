#!/bin/bash
DOCKER_REPO="${DOCKER_REPO-grafpoo}"
usage()
{
   echo "usage: dockerBuild [[-p] [-t version-tag]] | [-h | --help | -?]] <IMAGE_NAME>"
}
push()
{
 if [ "$push" = "true" ]; then
    echo "Pushing $IMAGE_NAME:$1"
    docker push $DOCKER_REPO/$IMAGE_NAME
 fi
}
build()
{
 echo "Building $IMAGE_NAME:$1"
 docker build $HERE -t $DOCKER_REPO/$IMAGE_NAME:$1
# docker build $HERE --add-host artifactory-prod:$artifactory_ip -t $DOCKER_REPO/$IMAGE_NAME:$1
}
push=false
while [ "$1" != "" ]; do
   case $1 in
       -p | --push )           push=true
                               ;;
       -t )               shift
                tag=$1
                               ;;
       -h | --help | -? )      usage
                               exit
                               ;;
       * )                     IMAGE_NAME=$1
       ;;
   esac
   shift
done
HERE="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
build latest
push latest
if [ ! -z $tag ]; then
    build $tag
    push $tag
fi
