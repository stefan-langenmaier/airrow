#!/bin/bash
echo "Creating airrow index"
curl -X PUT "localhost:9200/airrow?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow.json'
echo "Created airrow index"
echo "Creating airrow-trajectories index"
curl -X PUT "localhost:9200/airrow-trajectories?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-trajectories.json'
echo "Created airrow-trajectories index"
echo "Creating airrow-points index"
curl -X PUT "localhost:9200/airrow-points?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-points.json'
echo "Created airrow-points index"
echo "Creating airrow-ratings index"
curl -X PUT "localhost:9200/airrow-ratings?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-ratings.json'
echo "Created airrow-ratings index"
echo "Creating airrow-capabilities index"
curl -X PUT "localhost:9200/airrow-capabilities?pretty" -H 'Content-Type: application/json' -d'@setup/put-airrow-capabilities.json'
echo "Created airrow-capabilties index"
echo "Creating default search script"
curl -X POST "localhost:9200/_scripts/airrow-default-search?pretty" -H 'Content-Type: application/json' -d'@setup/post_scripts-airrow-default-search.json'
echo "Created default search script"
