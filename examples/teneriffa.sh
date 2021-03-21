#!/bin/bash

curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":28.08043,"lon":-16.60749},"status":"🚴🏞️","accuracy":10,"fileName":"teneriffa-1.jpeg"}' \
  -F "file=@/home/stefan/Downloads/teneriffa-1.jpeg" \
  https://xn--rro-pla.de/upload
  
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":28.04755,"lon":-16.57812},"status":"🏊✈️🍦","accuracy":10,"fileName":"teneriffa-2.jpeg"}' \
  -F "file=@/home/stefan/Downloads/teneriffa-2.jpeg" \
  https://xn--rro-pla.de/upload
