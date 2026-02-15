#!/bin/bash

CONTAINER_NAME="mongodb"
IMAGE_NAME="mongo:latest"

if [ ! "$(docker images -q ${IMAGE_NAME})" ]; then
  docker pull ${IMAGE_NAME}
fi

if [ ! "$(docker ps -q -f name=${CONTAINER_NAME})" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=${CONTAINER_NAME})" ]; then
        docker rm ${CONTAINER_NAME}
    fi
    docker run -d -p 27017:27017 --name ${CONTAINER_NAME} ${IMAGE_NAME}
fi
