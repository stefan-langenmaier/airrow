# Ärro

Easily meet other people

## Setup

### Elasticsearch

docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch:6.8.13
docker run -d --link elasticsearch:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:6.8.13

## Usage

On devices that support absolute orientation the real north can still be off, what seems to help is do a figure eight to calibrate it.
