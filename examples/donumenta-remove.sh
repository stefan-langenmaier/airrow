#!/bin/bash


# list all my points in this area
curl -X POST -H "Content-Type: application/json;charset=UTF-8" \
  -d '{
    "uuid":"X"
    }' \
  https://api.dagva.donumenta.de//points/delete
