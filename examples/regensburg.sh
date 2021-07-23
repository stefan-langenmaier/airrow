#!/bin/bash

# link
curl -X POST \
  -F 'meta={"creator":"289ae107-aa5f-4423-9b5d-487e449ece56","location":{"lat":49.01324,"lon":12.10696},"status":"ℹ️📯🎨🎭","accuracy":10,"fileName":"link.txt"}' \
  -F "file=@regensburg/link.txt" \
  https://research.xn--rro-pla.de/upload && echo "ok link"

# foto
curl -X POST \
  -F 'meta={"creator":"289ae107-aa5f-4423-9b5d-487e449ece56","location":{"lat":49.01465,"lon":12.10646},"status":"ℹ️📯🤳👀","accuracy":10,"fileName":"foto.jpeg"}' \
  -F "file=@regensburg/foto.jpeg" \
  https://research.xn--rro-pla.de/upload && echo "ok foto"

# brunnen
curl -X POST \
  -F 'meta={"creator":"289ae107-aa5f-4423-9b5d-487e449ece56","location":{"lat":49.01595,"lon":12.10699},"status":"ℹ️📯⛲🏺","accuracy":10,"fileName":"brunnen.glb"}' \
  -F "file=@regensburg/brunnen.glb" \
  https://research.xn--rro-pla.de/upload && echo "ok brunnen"

# glocken
curl -X POST \
  -F 'meta={"creator":"289ae107-aa5f-4423-9b5d-487e449ece56","location":{"lat":49.01699,"lon":12.10786},"status":"ℹ️📯🔔🚇🔔","accuracy":10,"fileName":"glocken.mp3"}' \
  -F "file=@regensburg/glocken.mp3" \
  https://research.xn--rro-pla.de/upload && echo "ok glocken"

# ufer
curl -X POST \
  -F 'meta={"creator":"289ae107-aa5f-4423-9b5d-487e449ece56","location":{"lat":49.01936,"lon":12.10878},"status":"ℹ️📯🌊⛵🌊","accuracy":10,"fileName":"ufer.txt"}' \
  -F "file=@regensburg/ufer.txt" \
  https://research.xn--rro-pla.de/upload && echo "ok ufer"