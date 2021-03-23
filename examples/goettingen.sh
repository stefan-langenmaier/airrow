#!/bin/bash

# wilhelmsplatz
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.53375,"lon":9.93808},"status":"👑🤴🎓","accuracy":10,"fileName":"wilhelmsplatz-link"}' \
  -F "file=https://www.youtube.com/embed/gO1K-GSogoA" \
  https://xn--rro-pla.de/upload && echo "ok wilhelmsplatz"

# cafe central
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.54204,"lon":9.93541},"status":"☕🍰","accuracy":10,"fileName":"cafe-central"}' \
  -F "file=https://www.youtube.com/embed/Trs-isdu4eE" \
  https://xn--rro-pla.de/upload && echo "ok cafe central"

# gaenseliesel
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.5327103,"lon":9.9329767},"status":"🦆👩🦆","accuracy":10,"fileName":"gaenseliesel"}' \
  -F "file=https://craigwritescode.medium.com/user-engagement-is-code-for-addiction-a2f50d36d7ac" \
  https://xn--rro-pla.de/upload && echo "ok gaenseliesel"

# der tanz
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.5344295,"lon":9.9346896},"status":"💃🕺","accuracy":10,"fileName":"tanz"}' \
  -F "file=https://slatestarcodex.com/2018/10/30/sort-by-controversial/" \
  https://xn--rro-pla.de/upload && echo "ok tanz"

# wall
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.537949,"lon":9.936944},"status":"🛡️🧱","accuracy":10,"fileName":"wall"}' \
  -F "file=https://medium.com/@philippbloch/5-beispiele-für-gelungene-gamification-92204515345" \
  https://xn--rro-pla.de/upload && echo "ok wall"

# kiessee
curl -X POST \
  -F 'meta={"creator":"1eb21401-267a-4887-9e27-55b2780eee46","location":{"lat":51.5189275,"lon":9.923548},"status":"🌊🏊","accuracy":10,"fileName":"kiessee"}' \
  -F "file=https://www.best-practice-business.de/blog/marketing-preis/2013/10/24/precision-moments-targeting-will-konsumenten-belohnen-wenn-sie-ein-erfolgsmoment-erleben/" \
  https://xn--rro-pla.de/upload && echo "ok kiessee"











