#!/bin/sh
cd ..
docker run --rm -v "$PWD":/usr/src/project -w /usr/src/project maven:alpine mvn -DskipTests package
docker login --username hectorm15 --password 1995gdo4ev
docker build -t hectorm15/daw .
docker push hectorm15/daw:latest
