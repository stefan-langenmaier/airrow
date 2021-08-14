#!/bin/bash

curl -X PUT "localhost:9200/airrow?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow.json'

curl -X PUT "localhost:9200/airrow-trajectories?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-trajectories.json'

curl -X PUT "localhost:9200/airrow-points?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-points.json'

curl -X PUT "localhost:9200/airrow-ratings?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-ratings.json'

curl -X PUT "localhost:9200/airrow-capabilities?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-capabilities.json'

curl -X POST "localhost:9200/_scripts/airrow-default-search?pretty" -H 'Content-Type: application/json' -d'@setup/post_scripts-airrow-default-search.json'
