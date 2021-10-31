# Ärro

Easily meet other people

## Setup

### Elasticsearch

docker-compose up -d

* Elasticsearch available on http://localhost:9200
* Kibana on http://localhost:5601

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