#!/bin/bash

IMAGE_NAME=gateway
TAG=1.0.0
REPO_NAME=middle
DOCKER_FILE_NAME=dockerfile
PRE_TAG=162.168.2.141:1180

# build iamge
docker build --rm=true -f $PWD/${DOCKER_FILE_NAME} -t ${REPO_NAME}/${IMAGE_NAME}:$TAG .

#tag
docker tag ${REPO_NAME}/${IMAGE_NAME}:$TAG ${PRE_TAG}/${REPO_NAME}/${IMAGE_NAME}:$TAG
#rmi
docker rmi ${REPO_NAME}/${IMAGE_NAME}:$TAG
#push
docker push ${PRE_TAG}/${REPO_NAME}/${IMAGE_NAME}:$TAG 
