#!/bin/bash


# list all my points in this area
curl -X POST -H "Content-Type: application/json;charset=UTF-8" \
  -d '{
    "uuid":"XXXX"
    }' \
  https://api.vr.donumenta.de/points/delete
