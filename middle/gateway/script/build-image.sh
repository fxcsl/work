#!/bin/bash

IMAGE_NAME=gateway
filepath=`pwd`
TAG=${filepath##*-}
REPO_NAME=middle
DOCKER_FILE_NAME=dockerfile
PRE_TAG=10.45.136.151:1180

# build iamge
docker build --rm=true -f $PWD/${DOCKER_FILE_NAME} -t ${REPO_NAME}/${IMAGE_NAME}:$TAG .

#tag
docker tag ${REPO_NAME}/${IMAGE_NAME}:$TAG ${PRE_TAG}/${REPO_NAME}/${IMAGE_NAME}:$TAG

#push
docker push ${PRE_TAG}/${REPO_NAME}/${IMAGE_NAME}:$TAG
#rmi
docker rmi ${REPO_NAME}/${IMAGE_NAME}:$TAG
docker rmi ${PRE_TAG}/${REPO_NAME}/${IMAGE_NAME}:$TAG
