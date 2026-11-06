#!/bin/bash

./mvnw clean package -Dquarkus.container-image.build=true
docker tag stefan/backend-app:1.0-SNAPSHOT slangenmaier/airrow:donumenta-latest

docker push slangenmaier/airrow:donumenta-quarkus3
