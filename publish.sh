#!/bin/bash

JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64 mvn clean package -Dquarkus.container-image.build=true
docker tag stefan/backend-app:1.0-SNAPSHOT slangenmaier/airrow:latest
docker tag stefan/backend-app:1.0-SNAPSHOT slangenmaier/airrow:donumenta-latest

docker push slangenmaier/airrow:latest
docker push slangenmaier/airrow:donumenta-latest
