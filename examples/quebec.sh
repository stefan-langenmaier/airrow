#!/bin/bash

curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":46.82517,"lon":-71.24309},"status":"🚴","accuracy":10,"fileName":"augsburg-1.jpeg"}' \
  -F "file=@/home/stefan/Downloads/augsburg-1.jpeg" \
  https://xn--rro-pla.de/upload && echo "ok 1"

curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":46.82538,"lon":-71.23889},"status":"🃏","accuracy":10,"fileName":"quebec-1.jpeg"}' \
  -F "file=@/home/stefan/Downloads/quebec-1.jpeg" \
  https://xn--rro-pla.de/upload && echo "ok 2"

# snow
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":46.82235,"lon":-71.23529},"status":"⛸️❄️","accuracy":10,"fileName":"quebec-4.jpeg"}' \
  -F "file=@/home/stefan/Downloads/quebec-4.jpeg" \
  https://xn--rro-pla.de/upload && echo "ok 3"

# bagel
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":46.82250,"lon":-71.22598},"status":"🥯","accuracy":10,"fileName":"quebec-2.jpeg"}' \
  -F "file=@/home/stefan/Downloads/quebec-2.jpeg" \
  https://xn--rro-pla.de/upload && echo "ok 4"

# airplane
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee45","location":{"lat":46.80665,"lon":-71.24581},"status":"✈️","accuracy":10,"fileName":"quebec-3.jpeg"}' \
  -F "file=@/home/stefan/Downloads/quebec-3.jpeg" \
  https://xn--rro-pla.de/upload && echo "ok 5"
