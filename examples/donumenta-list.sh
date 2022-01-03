#!/bin/bash


# list all my points in this area
curl -X POST -H "Content-Type: application/json;charset=UTF-8" \
  -d '{
    "uuid":"289ae107-aa5f-4423-9b5d-487e449ece56",
    "location":{"lat":49.02404,"lon":12.09730}
    }' \
  https://api.vr.donumenta.de/points/list | \
  jq '.points[] | {uuid, objectName}' 
