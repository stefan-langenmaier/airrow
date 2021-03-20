# Ärro

Easily meet other people

## Setup

### Elasticsearch

docker run -d --name elasticsearch -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" elasticsearch-oss:6.8.13

## Kibana

docker run --net host --name kibana -p 5601:5601 --rm -v /home/stefan/git/airrow/kibana.yml:/usr/share/kibana/config/kibana.yml docker.elastic.co/kibana/kibana-oss:6.8.13

## Build this image

mvn clean package -Dquarkus.container-image.build=true

## Usage

On devices that support absolute orientation the real north can still be off, what seems to help is do a figure eight to calibrate it.

## Place custom points

```
curl -X POST \
  -F 'meta={"creator":"<uuid>","location":{"lat":47.62791,"lon":10.27410},"status":"✨😎✨🏀🔥","accuracy":10,"fileName":"agile-cycle.png"}' \
  -F "file=@/home/stefan/Downloads/cool.jpeg" \
  https://xn--rro-pla.de/upload
```